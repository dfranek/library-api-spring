package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.ShelfBook;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class ShelfEntry implements EntityInterface<ShelfBook> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Shelf shelf;

    @ManyToOne
    private Book book;

    private String row;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    @Override
    public ShelfBook toDto() {
        ShelfBook shelfBook = new ShelfBook();
        shelfBook.setTitle(book.getTitle());
        shelfBook.setAuthors(Optional.ofNullable(book.getAuthors())
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(Author::getName)
                .collect(Collectors.toList())
        );
        shelfBook.setImage(Optional.ofNullable(book.getImage())
                .map(DatabaseFile::toDto)
                .orElse(null)
        );
        shelfBook.setTagline(book.getTagline());
        shelfBook.setPages(book.getPages());
        shelfBook.setDatePublished(book.getDatePublished());
        shelfBook.setId(book.getId());
        shelfBook.setRow(row);
        shelfBook.setShelf(shelf.toDto());

        return shelfBook;
    }
}
