package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findByName(String name);

}
