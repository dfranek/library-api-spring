package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.ShelfEntry;
import net.dfranek.library.rest.repository.ShelfRepository;
import net.dfranek.library.rest.utils.SpringContext;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfEntryDto implements DtoInterface<ShelfEntry>, ShelfInterface {

    private Integer id;

    private String name;

    private int numItems;

    private String row;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumItems() {
        return numItems;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    @Override
    public ShelfEntry toEntity() {
        ShelfEntry entry = new ShelfEntry();
        entry.setRow(row);
        ShelfRepository shelfRepository = SpringContext.getBean(ShelfRepository.class);
        entry.setShelf(shelfRepository.findById(id).orElse(null));

        return entry;
    }
}
