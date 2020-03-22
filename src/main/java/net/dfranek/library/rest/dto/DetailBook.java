package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Book;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailBook extends BasicBook {

    private String description;
    private String isbn13;
    private String isbn10;
    private String publisher;
    private String placeOfPublication;
    private List<ShelfEntryDto> shelves;
    private List<LibraryDto> libraries;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public List<ShelfEntryDto> getShelves() {
        return shelves;
    }

    public void setShelves(List<ShelfEntryDto> shelves) {
        this.shelves = shelves;
    }

    public List<LibraryDto> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<LibraryDto> libraries) {
        this.libraries = libraries;
    }

    @Override
    public Book toEntity() {
        Book book = super.toEntity();
        if(StringUtils.isNotBlank(description)) {
            book.setDescription(description);
        }

        if(StringUtils.isNotBlank(isbn10)) {
            book.setDescription(isbn13);
        }

        if(StringUtils.isNotBlank(publisher)) {
            book.setPublisher(publisher);
        }

        if(StringUtils.isNotBlank(placeOfPublication)) {
            book.setPublisher(placeOfPublication);
        }

        return book;
    }
}
