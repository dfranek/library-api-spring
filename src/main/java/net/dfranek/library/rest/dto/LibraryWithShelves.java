package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibraryWithShelves {

    private LibraryDto library;

    private final List<ShelfDto> shelves = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryWithShelves that = (LibraryWithShelves) o;
        return Objects.equals(library, that.library) &&
                Objects.equals(shelves, that.shelves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(library, shelves);
    }
}
