package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Library;
import org.springframework.data.repository.CrudRepository;

public interface LibraryRepository extends CrudRepository<Library, Integer>, LibraryRepositoryAdditional {

}
