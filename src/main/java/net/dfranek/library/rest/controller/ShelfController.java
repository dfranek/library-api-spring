package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.dto.InformationalResponse;
import net.dfranek.library.rest.dto.ShelfDto;
import net.dfranek.library.rest.dto.ShelfWithBooks;
import net.dfranek.library.rest.entity.Library;
import net.dfranek.library.rest.entity.Shelf;
import net.dfranek.library.rest.entity.ShelfEntry;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.LibraryRepository;
import net.dfranek.library.rest.repository.ShelfRepository;
import net.dfranek.library.rest.repository.UserRepository;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@CrossOrigin
@RequestMapping(path = "/library/{libraryId}/shelves")
public class ShelfController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> getShelves(@PathVariable int libraryId, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                return new ResponseEntity<>(library.getShelves().stream().map(Shelf::toDto).collect(Collectors.toList()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> createShelf(@PathVariable int libraryId, @RequestBody ShelfDto shelfToCreate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                Shelf shelf = shelfToCreate.toEntity();
                shelf.setLibrary(library);
                shelf.setDateAdded(ZonedDateTime.now());
                shelfRepository.save(shelf);

                shelfToCreate.setId(shelf.getId());

                return new ResponseEntity<>(shelfToCreate, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PutMapping("{shelfId}")
    public @ResponseBody
    ResponseEntity<?> editShelf(@PathVariable int libraryId, @PathVariable int shelfId, @RequestBody ShelfDto shelfToUpdate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if (library.getUsers().contains(user)) {
                Optional<Shelf> shelfOptional = shelfRepository.findById(shelfId);
                if(shelfOptional.isPresent()) {
                    Shelf shelf = shelfOptional.get();
                    shelf.setName(shelfToUpdate.getName());
                    shelf.setDateModified(ZonedDateTime.now());

                    shelfRepository.save(shelf);

                    return new ResponseEntity<>(shelf.toDto(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no shelf found"), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DeleteMapping("{shelfId}")
    public @ResponseBody
    ResponseEntity<?> deleteShelf(@PathVariable int libraryId, @PathVariable int shelfId, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if (library.getUsers().contains(user)) {
                Optional<Shelf> shelfOptional = shelfRepository.findById(shelfId);
                if (shelfOptional.isPresent()) {
                    Shelf shelf = shelfOptional.get();
                    shelfRepository.delete(shelf);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no shelf found"), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("{shelfId}")
    public @ResponseBody
    ResponseEntity<?> getShelf(@PathVariable int libraryId, @PathVariable int shelfId, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if (!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if (library.getUsers().contains(user)) {
                Optional<Shelf> shelfOptional = shelfRepository.findById(shelfId);
                if (shelfOptional.isPresent()) {
                    Shelf shelf = shelfOptional.get();
                    ShelfWithBooks shelfWithBooks = new ShelfWithBooks();
                    shelfWithBooks.setShelf(shelf.toDto());
                    shelfWithBooks.setBooks(Optional.ofNullable(shelf.getShelfEntries())
                            .map(Collection::stream)
                            .orElse(Stream.empty())
                            .map(ShelfEntry::toDto)
                            .collect(Collectors.toList())
                    );
                    return new ResponseEntity<>(shelfWithBooks, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no shelf found"), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
