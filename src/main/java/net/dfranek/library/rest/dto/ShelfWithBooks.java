package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfWithBooks {

    private ShelfDto shelf;
    private List<ShelfBook> books;

    public ShelfDto getShelf() {
        return shelf;
    }

    public void setShelf(ShelfDto shelf) {
        this.shelf = shelf;
    }

    public List<ShelfBook> getBooks() {
        return books;
    }

    public void setBooks(List<ShelfBook> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShelfWithBooks that = (ShelfWithBooks) o;
        return Objects.equals(shelf, that.shelf) &&
                Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelf, books);
    }
}
