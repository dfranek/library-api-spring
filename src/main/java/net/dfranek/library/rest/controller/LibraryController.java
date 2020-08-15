package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.dto.InformationalResponse;
import net.dfranek.library.rest.dto.LibraryDto;
import net.dfranek.library.rest.entity.Library;
import net.dfranek.library.rest.entity.Shelf;
import net.dfranek.library.rest.entity.ShelfEntry;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.LibraryRepository;
import net.dfranek.library.rest.repository.UserRepository;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@CrossOrigin
@RequestMapping(path = "/library")
public class LibraryController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @PostMapping
    public @ResponseBody
    ResponseEntity<LibraryDto> createLibrary(@RequestBody LibraryDto libraryToCreate, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Library library = libraryToCreate.toEntity();
        library.setUsers(Collections.singleton(user));
        library.setDateAdded(ZonedDateTime.now());

        libraryRepository.save(library);
        libraryToCreate.setId(library.getId());

        return new ResponseEntity<>(libraryToCreate, HttpStatus.CREATED);
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<LibraryDto>> getLibraries(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        List<Library> libraries = libraryRepository.findByUser(user);

        return new ResponseEntity<>(
                Optional.ofNullable(libraries)
                    .map(Collection::stream)
                    .orElse(Stream.empty())
                    .map(Library::toDto)
                    .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @PutMapping("{libraryId}")
    public @ResponseBody
    ResponseEntity<?> editLibrary(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int libraryId, @RequestBody LibraryDto updatedLibrary) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                library.setName(updatedLibrary.getName());
                library.setDateModified(ZonedDateTime.now());
                libraryRepository.save(library);
                return new ResponseEntity<>(library.toDto(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("{libraryId}")
    public @ResponseBody
    ResponseEntity<?> getLibrary(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int libraryId) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                return new ResponseEntity<>(library.toDto(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DeleteMapping("{libraryId}")
    public @ResponseBody
    ResponseEntity<?> deleteLibrary(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int libraryId) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                libraryRepository.delete(library);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("{libraryId}/books")
    public @ResponseBody
    ResponseEntity<?> getBooks(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader, @PathVariable int libraryId) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        if(!libraryOptional.isPresent()) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.NOT_FOUND.value(), "no library found"), HttpStatus.NOT_FOUND);
        } else {
            Library library = libraryOptional.get();
            if(library.getUsers().contains(user)) {
                return new ResponseEntity<>(
                        library.getShelves()
                                .stream()
                                .map(Shelf::getShelfEntries)
                                .flatMap(Collection::stream)
                                .map(ShelfEntry::toDto)
                                .collect(Collectors.toSet()),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "Library does not belong to user!"), HttpStatus.UNAUTHORIZED);
            }
        }


    }

}
