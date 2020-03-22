package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

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
}
