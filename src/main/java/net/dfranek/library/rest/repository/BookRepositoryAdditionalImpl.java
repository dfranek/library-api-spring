package net.dfranek.library.rest.repository;

import net.dfranek.library.rest.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepositoryAdditionalImpl implements BookRepositoryAdditional {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> searchForBook(String queryString, User user, List<String> fieldsToUse) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);
        Join<Object, Tag> tags = book.join("tags");
        Join<Object, Author> authors = book.join("authors");
        Join<Object, ShelfEntry> shelfEntries = book.join("shelfEntries");
        Join<Object, Shelf> shelves = shelfEntries.join("shelf");
        Join<Object, Library> library = shelves.join("library");


        List<String> searchFields = Arrays.asList("title", "isbn", "description", "publisher", "tagline", "tag", "author", "placeOfPublication");
        if (fieldsToUse != null) {
            searchFields = fieldsToUse.stream()
                    .distinct()
                    .filter(searchFields::contains)
                    .collect(Collectors.toList());
        }


        List<Predicate> predicates = new ArrayList<>();
        for (String field : searchFields) {
            switch (field) {
                case "isbn":
                    predicates.add(cb.like(book.get("isbn10"), "%" + queryString + "%"));
                    predicates.add(cb.like(book.get("isbn13"), "%" + queryString + "%"));
                    break;
                case "tag":
                    predicates.add(cb.like(tags.get("name"), "%" + queryString + "%"));
                    break;
                case "author":
                    predicates.add(cb.like(authors.get("name"), "%" + queryString + "%"));
                    break;
                default:
                    predicates.add(cb.like(book.get(field), "%" + queryString + "%"));
                    break;
            }
        }

        query.select(book)
                .distinct(true)
                .where(cb.and(cb.isMember(user, library.get("users")),
                        cb.or(predicates.toArray(new Predicate[0]))));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
