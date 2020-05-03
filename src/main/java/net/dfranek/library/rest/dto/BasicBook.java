package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Author;
import net.dfranek.library.rest.entity.Book;
import net.dfranek.library.rest.entity.Tag;
import net.dfranek.library.rest.repository.AuthorRepository;
import net.dfranek.library.rest.repository.TagRepository;
import net.dfranek.library.rest.utils.SpringContext;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicBook implements DtoInterface<Book> {

    private Integer id;
    private String title;
    private String tagline;
    private FileObject image;
    private List<String> authors = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private int pages;
    private LocalDate datePublished;
    private List<StateDto> states = new ArrayList<>();

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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public FileObject getImage() {
        return image;
    }

    public void setImage(FileObject image) {
        this.image = image;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDate datePublished) {
        this.datePublished = datePublished;
    }

    public List<StateDto> getStates() {
        return states;
    }

    public void setStates(List<StateDto> states) {
        this.states = states;
    }

    @Override
    public Book toEntity() {
        Book book = new Book();

        if(StringUtils.isNotBlank(title)) {
            book.setTitle(title);
        }

        if(StringUtils.isNotBlank(tagline)) {
            book.setTitle(tagline);
        }

        book.setPages(pages);

        if(book.getDatePublished() != null) {
            book.setDatePublished(datePublished);
        }

        if(tags != null) {
            TagRepository tagRepository = SpringContext.getBean(TagRepository.class);
            book.setTags(tags.stream()
                    .map(tag -> Optional.ofNullable(tagRepository.findByName(tag))
                            .orElseGet(() -> {
                                Tag tagObj = new Tag();
                                tagObj.setName(tag);
                                tagObj.setLibraries(new HashSet<>());
                                return tagObj;
                            })
                    )
                    .collect(Collectors.toSet()));
        }

        if(authors != null) {
            AuthorRepository authorRepository = SpringContext.getBean(AuthorRepository.class);

            book.setAuthors(authors.stream()
                    .map(author -> Optional.ofNullable(authorRepository.findByName(author))
                            .orElseGet(() -> {
                                Author authorObj = new Author();
                                authorObj.setName(author);
                                return authorObj;
                            })
                    )
                    .collect(Collectors.toSet())
            );

        }

        if(states != null) {
            book.setStates(states.stream()
                    .map(StateDto::toEntity)
                    .collect(Collectors.toSet())
            );
        }

        if(image != null) {
            book.setImage(image.toEntity());
        }


        return book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicBook basicBook = (BasicBook) o;
        return pages == basicBook.pages &&
                Objects.equals(id, basicBook.id) &&
                Objects.equals(title, basicBook.title) &&
                Objects.equals(tagline, basicBook.tagline) &&
                Objects.equals(image, basicBook.image) &&
                Objects.equals(authors, basicBook.authors) &&
                Objects.equals(tags, basicBook.tags) &&
                Objects.equals(datePublished, basicBook.datePublished) &&
                Objects.equals(states, basicBook.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, tagline, image, authors, tags, pages, datePublished, states);
    }
}
