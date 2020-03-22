package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Library;
import net.dfranek.library.rest.entity.User;

import java.util.List;

public interface LibraryRepositoryAdditional {

    List<Library> findByUser(User user);
}
