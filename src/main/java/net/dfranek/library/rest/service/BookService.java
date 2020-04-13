package net.dfranek.library.rest.service;

import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.dto.NewBook;
import net.dfranek.library.rest.webservice.BookWebService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private List<BookWebService> webServices;

    public List<DetailBook> getBooksByDto(NewBook newBook) {
        List<DetailBook> books = Collections.emptyList();
        for (BookWebService service : webServices) {
            if (StringUtils.isNotBlank(newBook.getIsbn13()) || StringUtils.isNotBlank(newBook.getIsbn10())) {
                books = service.getBookByIsbn(newBook.getIsbn13().trim().isEmpty() ? newBook.getIsbn10() : newBook.getIsbn13());
            } else if(StringUtils.isNotBlank(newBook.getTitle())) {
                books = service.getBookByTitleAndAuthor(newBook.getTitle(), newBook.getAuthor());
            }

            if(books != null) {
                books.forEach(book -> {
                    book.getLibraries().addAll(newBook.getLibraries());
                    book.getShelves().addAll(newBook.getShelves());
                    book.getTags().addAll(newBook.getTags());
                });
            }

        }

        return books;
    }

}
