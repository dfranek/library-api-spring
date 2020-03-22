package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.Library;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibraryDto implements DtoInterface<Library> {

    private Integer id;
    private String name;

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

    @Override
    public Library toEntity() {

        Library library = new Library();
        if(id != null) {
            library.setId(id);
        }
        library.setName(name);

        return library;
    }
}
