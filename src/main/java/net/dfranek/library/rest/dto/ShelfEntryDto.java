package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.ShelfEntry;
import net.dfranek.library.rest.repository.ShelfRepository;
import net.dfranek.library.rest.utils.SpringContext;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShelfEntryDto implements DtoInterface<ShelfEntry> {

    private String row;
    private ShelfDto shelf;


    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public ShelfDto getShelf() {
        return shelf;
    }

    public void setShelf(ShelfDto shelf) {
        this.shelf = shelf;
    }

    public Integer getId() {
        return shelf.getId();
    }

    public String getName() {
        return shelf.getName();
    }

    public int getNumItems() {
        return shelf.getNumItems();
    }

    @Override
    public ShelfEntry toEntity() {
        ShelfEntry entry = new ShelfEntry();
        entry.setRow(row);
        ShelfRepository shelfRepository = SpringContext.getBean(ShelfRepository.class);
        entry.setShelf(shelfRepository.findById(shelf.getId()).orElse(null));

        return entry;
    }
}
