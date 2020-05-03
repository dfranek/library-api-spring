package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Shelf;
import net.dfranek.library.rest.entity.ShelfEntry;
import org.springframework.data.repository.CrudRepository;

public interface ShelfEntryRepository extends CrudRepository<ShelfEntry, Integer> {

}
