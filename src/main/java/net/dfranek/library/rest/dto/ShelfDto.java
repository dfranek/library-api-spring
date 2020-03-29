package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Shelf;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfDto implements DtoInterface<Shelf>, ShelfInterface {
    private Integer id;
    private String name;
    private int numItems;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getNumItems() {
        return numItems;
    }

    @Override
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
