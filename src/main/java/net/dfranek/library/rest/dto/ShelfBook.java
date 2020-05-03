package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfBook extends BasicBook {

    private String row;
    private ShelfDto shelf;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public ShelfDto getShelf() {
        return shelf;
    }

    public void setShelf(ShelfDto shelf) {
        this.shelf = shelf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShelfBook shelfBook = (ShelfBook) o;
        return Objects.equals(row, shelfBook.row) &&
                Objects.equals(shelf, shelfBook.shelf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), row, shelf);
    }
}
