package net.dfranek.library.rest.webservice;

import net.dfranek.library.rest.dto.DetailBook;

import java.util.List;

public interface BookWebService {

    List<DetailBook> getBookByIsbn(String isbn);

    List<DetailBook> getBookByTitleAndAuthor(String title, String author);
}
