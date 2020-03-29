package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.dto.BookWithLocation;
import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.dto.InformationalResponse;
import net.dfranek.library.rest.dto.NewBook;
import net.dfranek.library.rest.entity.Library;
import net.dfranek.library.rest.entity.Shelf;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.BookRepository;
import net.dfranek.library.rest.repository.UserRepository;
import net.dfranek.library.rest.service.BookService;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/book")
public class BookController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

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

        final DetailBook savedBook = saveBook(possibleBooks.get(0));

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

    private DetailBook saveBook(DetailBook bookToCreate) {
        return bookToCreate;
    }


    @PostMapping(params = "type=MANUAL")
    public @ResponseBody
    ResponseEntity<?> createBook(@RequestBody DetailBook bookToCreate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));
        final ResponseEntity<InformationalResponse> error = checkPreRequisites(user, bookToCreate);
        if(error != null) {
            return error;
        }

        final DetailBook savedBook = saveBook(bookToCreate);

        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

}
