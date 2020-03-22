package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Author findByName(String name);
}
