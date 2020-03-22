package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibraryWithShelves {

    private LibraryDto library;

    private List<ShelfDto> shelves = new ArrayList<>();

    public LibraryDto getLibrary() {
        return library;
    }

    public void setLibrary(LibraryDto library) {
        this.library = library;
    }

    public List<ShelfDto> getShelves() {
        return shelves;
    }

    public void addShelf(ShelfDto shelf) {
        this.shelves.add(shelf);
    }

    public void removeShelf(ShelfDto shelf) {
        this.shelves.remove(shelf);
    }
}
