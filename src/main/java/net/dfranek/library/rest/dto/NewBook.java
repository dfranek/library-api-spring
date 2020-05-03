package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewBook implements BookWithLocation {

    private String isbn13;
    private String isbn10;
    private String author;
    private String title;

    private List<LibraryDto> libraries = new ArrayList<>();
    private List<ShelfEntryDto> shelves = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<StateDto> states = new ArrayList<>();

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<LibraryDto> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<LibraryDto> libraries) {
        this.libraries = libraries;
    }

    public List<ShelfEntryDto> getShelves() {
        return shelves;
    }

    public void setShelves(List<ShelfEntryDto> shelves) {
        this.shelves = shelves;
    }

    public List<StateDto> getStates() {
        return states;
    }

    public void setStates(List<StateDto> states) {
        this.states = states;
    }

    @Override
    public List<ShelfInterface> getSimpleShelves() {
        return shelves.stream().map(shelf -> (ShelfInterface) shelf).collect(Collectors.toList());
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewBook newBook = (NewBook) o;
        return Objects.equals(isbn13, newBook.isbn13) &&
                Objects.equals(isbn10, newBook.isbn10) &&
                Objects.equals(author, newBook.author) &&
                Objects.equals(title, newBook.title) &&
                Objects.equals(libraries, newBook.libraries) &&
                Objects.equals(shelves, newBook.shelves) &&
                Objects.equals(tags, newBook.tags) &&
                Objects.equals(states, newBook.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn13, isbn10, author, title, libraries, shelves, tags, states);
    }
}
