package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

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
}
