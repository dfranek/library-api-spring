package net.dfranek.library.rest.jwt;

import net.dfranek.library.rest.entity.Shelf;
import org.springframework.data.repository.CrudRepository;

public interface ShelfRepository extends CrudRepository<Shelf, Integer> {
}
