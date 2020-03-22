package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.ShelfDto;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class Shelf implements EntityInterface<ShelfDto> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    private Library library;

    @OneToMany(mappedBy = "shelf", orphanRemoval = true)
    private Set<ShelfEntry> shelfEntries;

    private ZonedDateTime dateAdded;

    private ZonedDateTime dateModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Set<ShelfEntry> getShelfEntries() {
        return shelfEntries;
    }

    public void setShelfEntries(Set<ShelfEntry> shelfEntries) {
        this.shelfEntries = shelfEntries;
    }

    public ZonedDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(ZonedDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public ZonedDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public ShelfDto toDto() {
        ShelfDto shelfDto = new ShelfDto();
        shelfDto.setId(id);
        shelfDto.setName(name);
        shelfDto.setNumItems(shelfEntries.size());
        return shelfDto;
    }
}
