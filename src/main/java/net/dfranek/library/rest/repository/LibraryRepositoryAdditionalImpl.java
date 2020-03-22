package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.Library;
import net.dfranek.library.rest.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LibraryRepositoryAdditionalImpl implements LibraryRepositoryAdditional {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Library> findByUser(User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Library> query = cb.createQuery(Library.class);
        Root<Library> library = query.from(Library.class);

        Path<Set<User>> userPath = library.get("users");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isMember(user, userPath));

        query.select(library)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
