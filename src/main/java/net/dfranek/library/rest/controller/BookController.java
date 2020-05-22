package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.config.FileConfig;
import net.dfranek.library.rest.dto.*;
import net.dfranek.library.rest.entity.*;
import net.dfranek.library.rest.repository.*;
import net.dfranek.library.rest.service.BookService;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/book")
public class BookController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ShelfEntryRepository shelfEntryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping(params = "type=AUTOMATIC")
    public @ResponseBody
    ResponseEntity<?> createBook(@RequestBody NewBook bookToCreate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));
        final ResponseEntity<InformationalResponse> error = checkPreRequisites(user, bookToCreate);
        if(error != null) {
            return error;
        }

        final List<DetailBook> possibleBooks = bookService.getBooksByDto(bookToCreate);
        if(possibleBooks.isEmpty()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "No book found!"), HttpStatus.NOT_FOUND);
        }

        if(possibleBooks.size() > 1) {
            return new ResponseEntity<>(possibleBooks, HttpStatus.MULTIPLE_CHOICES);
        }

        final DetailBook savedBook = saveBook(possibleBooks.get(0), user);

        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    private ResponseEntity<InformationalResponse> checkPreRequisites(User user, BookWithLocation book) {
        if (book.getLibraries() == null || book.getLibraries().isEmpty()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.BAD_REQUEST.value(), "At least one library is required"), HttpStatus.BAD_REQUEST);
        }

        final Set<Integer> userLibraries = user.getLibraries().stream()
                .map(Library::getId)
                .collect(Collectors.toSet());

        final Set<Integer> userShelves = user.getLibraries().stream()
                .map(Library::getShelves)
                .flatMap(Collection::stream)
                .map(Shelf::getId)
                .collect(Collectors.toSet());

        if (book.getLibraries().stream()
                .anyMatch(libraryDto -> !userLibraries.contains(libraryDto.getId()))) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "A user can only add to his libraries"), HttpStatus.UNAUTHORIZED);
        }

        if (book.getSimpleShelves().stream()
                .anyMatch(shelfDto -> !userShelves.contains(shelfDto.getId()))) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "A user can only add to his shelves"), HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    private DetailBook saveBook(final DetailBook bookToCreate, final User user) {
        Optional.ofNullable(bookToCreate.getImage())
            .ifPresent(fileObject -> {
                try {
                    fileObject.save(fileConfig.getBookStoragePath());
                } catch (IOException e) {
                    LOG.warn("error writing file", e);
                }
            });

        Book book = checkIfBookExists(bookToCreate);
        if(book == null) {
            book = bookToCreate.toEntity();
            book.setDateAdded(ZonedDateTime.now());
        } else {
            book.updateFromDto(bookToCreate);
        }

        doDbSave(book, bookToCreate, user);

        return book.toDto();
    }

    private Book checkIfBookExists(final DetailBook bookToCreate) {
        return Optional.ofNullable(bookRepository.findByIsbn13(bookToCreate.getIsbn13()))
            .orElse(Optional.ofNullable(bookRepository.findByIsbn10(bookToCreate.getIsbn10()))
                .orElse(Optional.ofNullable(bookRepository.findByTitleAndAuthorsIn(
                        bookToCreate.getTitle(),
                        bookToCreate.getAuthors()
                    ))
                    .orElse(null)
                )
            );
    }


    @PostMapping(params = "type=MANUAL")
    public @ResponseBody
    ResponseEntity<?> createBook(@RequestBody DetailBook bookToCreate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));
        final ResponseEntity<InformationalResponse> error = checkPreRequisites(user, bookToCreate);
        if(error != null) {
            return error;
        }

        final DetailBook savedBook = saveBook(bookToCreate, user);

        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }


    @GetMapping("{bookId}")
    public @ResponseBody
    ResponseEntity<?> getBook(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int bookId) {
        final Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(!bookOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no book found"), HttpStatus.NOT_FOUND);
        } else {
            Book book = bookOptional.get();
            return new ResponseEntity<>(book.toDto(), HttpStatus.OK);
        }
    }

    @DeleteMapping("{bookId}")
    public @ResponseBody
    ResponseEntity<?> deleteBook(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(!bookOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no book found"), HttpStatus.NOT_FOUND);
        } else {
            Book book = bookOptional.get();
            bookRepository.delete(book);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("{bookId}")
    public @ResponseBody
    ResponseEntity<?> editBook(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int bookId, @RequestBody DetailBook bookToUpdate) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(!bookOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no book found"), HttpStatus.NOT_FOUND);
        } else {
            Book book = bookOptional.get();

            book.updateFromDto(bookToUpdate);
            doDbSave(book, bookToUpdate, user);

            return new ResponseEntity<>(book.toDto(), HttpStatus.CREATED);
        }
    }

    private void doDbSave(final Book book, final DetailBook bookToSave, final User user) {
        book.getTags()
                .stream()
                .filter(tag -> tag.getId() == null)
                .forEach(tag -> tagRepository.save(tag));

        book.getStates()
                .stream()
                .filter(state -> state.getId() == null)
                .forEach(state -> {
                    state.setUser(user);
                    state.setBook(book);
                    stateRepository.save(state);
                });

        book.getAuthors()
                .stream()
                .filter(author -> author.getId() == null)
                .forEach(author -> authorRepository.save(author));

        bookRepository.save(book);

        bookToSave.getShelves()
                .stream()
                .map(ShelfEntryDto::toEntity)
                .forEach(shelfEntry -> {
                    shelfEntry.setBook(book);
                    shelfEntryRepository.save(shelfEntry);
                    book.getShelfEntries().add(shelfEntry);
                });
        book.getAuthors()
                .stream()
                .filter(author -> author.getId() == null)
                .forEach(author -> {
                    author.getBooks().add(book);
                    book.getAuthors().add(author);
                    authorRepository.save(author);
                });
    }

}
