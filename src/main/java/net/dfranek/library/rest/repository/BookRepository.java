package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer>, BookRepositoryAdditional {

    Book findByIsbn10(String isbn10);

    Book findByIsbn13(String isbn13);

}
