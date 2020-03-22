package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Author;
import net.dfranek.library.rest.entity.Book;
import net.dfranek.library.rest.entity.Tag;
import net.dfranek.library.rest.repository.AuthorRepository;
import net.dfranek.library.rest.repository.TagRepository;
import net.dfranek.library.rest.utils.SpringContext;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicBook implements DtoInterface<Book> {

    private Integer id;
    private String title;
    private String tagline;
    private FileObject image;
    private List<String> authors;
    private List<String> tags;
    private int pages;
    private ZonedDateTime datePublished;
    private List<StateDto> states;

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

    public ZonedDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(ZonedDateTime datePublished) {
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

        if(image != null) {
            book.setImage(image.toEntity());
        }


        return book;
    }
}
