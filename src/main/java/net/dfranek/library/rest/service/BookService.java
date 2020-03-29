package net.dfranek.library.rest.service;

import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.dto.NewBook;
import net.dfranek.library.rest.webservice.BookWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private List<BookWebService> webServices;

    public List<DetailBook> getBooksByDto(NewBook newBook) {
        return Collections.emptyList();
    }

}
