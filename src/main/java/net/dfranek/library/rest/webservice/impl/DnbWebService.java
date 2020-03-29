package net.dfranek.library.rest.webservice.impl;

import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.webservice.BookWebService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DnbWebService implements BookWebService {
    @Override
    public List<DetailBook> getBookByIsbn(String isbn) {
        return null;
    }

    @Override
    public List<DetailBook> getBookByTitleAndAuthor(String title, String author) {
        return null;
    }
}
