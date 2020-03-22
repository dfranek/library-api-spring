package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.DetailBook;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Book implements EntityInterface<DetailBook> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;

    private String isbn13;

    private String isbn10;

    private String description;

    private String publisher;

    private ZonedDateTime datePublished;

    private String placeOfPublication;

    private int pages;

    private String tagline;

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<State> states;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "tag_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<ShelfEntry> shelfEntries;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private DatabaseFile image;

    private ZonedDateTime dateAdded;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ZonedDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(ZonedDateTime datePublished) {
        this.datePublished = datePublished;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<ShelfEntry> getShelfEntries() {
        return shelfEntries;
    }

    public void setShelfEntries(Set<ShelfEntry> shelfEntries) {
        this.shelfEntries = shelfEntries;
    }

    public DatabaseFile getImage() {
        return image;
    }

    public void setImage(DatabaseFile image) {
        this.image = image;
    }

    public ZonedDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(ZonedDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public DetailBook toDto() {
        DetailBook detailBook = new DetailBook();
        detailBook.setTitle(title);
        if(image != null) {
            detailBook.setImage(image.toDto());
        }
        detailBook.setDescription(description);
        detailBook.setTagline(tagline);
        detailBook.setDatePublished(datePublished);
        detailBook.setPlaceOfPublication(placeOfPublication);
        detailBook.setPublisher(publisher);
        detailBook.setIsbn10(isbn10);
        detailBook.setIsbn13(isbn13);
        detailBook.setPages(pages);
        detailBook.setId(id);
        detailBook.setAuthors(Optional.ofNullable(authors)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(Author::getName)
                .collect(Collectors.toList())
        );
        detailBook.setTags(Optional.ofNullable(tags)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(Tag::getName)
                .collect(Collectors.toList())
        );

        return detailBook;
    }
}
