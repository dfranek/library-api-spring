package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewBook implements BookWithLocation {

    private String isbn13;
    private String isbn10;
    private String author;
    private String title;

    private List<LibraryDto> libraries;
    private List<ShelfEntryDto> shelves;
    private List<String> tags;

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
}
