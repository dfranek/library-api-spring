package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Shelf;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfDto implements DtoInterface<Shelf> {
    private Integer id;
    private String name;
    private int numItems;

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

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    @Override
    public Shelf toEntity() {
        Shelf shelf = new Shelf();
        if(id != null) {
            shelf.setId(id);
        }
        shelf.setName(name);

        return shelf;
    }
}
