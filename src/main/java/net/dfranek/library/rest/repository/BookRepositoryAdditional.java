package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Book;
import net.dfranek.library.rest.entity.User;

import java.util.List;

public interface BookRepositoryAdditional {

    List<Book> searchForBook(String query, User user, List<String> fieldsToUse);

}
