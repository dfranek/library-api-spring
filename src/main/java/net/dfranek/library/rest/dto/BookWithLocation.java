package net.dfranek.library.rest.dto;

import java.util.List;

public interface BookWithLocation {

    List<LibraryDto> getLibraries();

    List<ShelfInterface> getSimpleShelves();
}
