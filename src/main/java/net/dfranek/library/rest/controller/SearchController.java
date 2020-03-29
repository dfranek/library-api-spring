package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.entity.Book;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.BookRepository;
import net.dfranek.library.rest.repository.UserRepository;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/search")
public class SearchController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<DetailBook>> search(@RequestParam("q") String query, @RequestParam(required = false) String fields, @RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        List<String> fieldsToUse = null;
        if(StringUtils.isNotBlank(fields)) {
            fieldsToUse = Arrays.asList(fields.split(","));
        }

        return new ResponseEntity<>(
                bookRepository
                    .searchForBook(query, user, fieldsToUse)
                    .stream()
                    .map(Book::toDto)
                    .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}
