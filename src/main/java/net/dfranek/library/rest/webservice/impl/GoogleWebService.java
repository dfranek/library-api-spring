package net.dfranek.library.rest.webservice.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.jwt.JwtAuthenticationFilter;
import net.dfranek.library.rest.webservice.BookWebService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoogleWebService implements BookWebService {

    private static final String API_URL = "https://www.googleapis.com/books/v1/";

    private Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    public List<DetailBook> getBookByIsbn(String isbn) {
        return doQuery("isbn:" + isbn);
    }

    @Override
    public List<DetailBook> getBookByTitleAndAuthor(String title, String author) {
        final String authorQuery = StringUtils.isNotBlank(author) ? " inauthor:" + author : "";

        return doQuery("\"" + title + "\"" + authorQuery);
    }

    private List<DetailBook> doQuery(String query) {
        final Books books;
        try {
            books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                    .build();
            final Books.Volumes.List list = books.volumes().list(query);
            Volumes volumes = list.execute();
            if (volumes.getTotalItems() > 0 && volumes.getItems() != null) {
                return volumes.getItems().stream()
                        .map(Volume::getVolumeInfo)
                        .map(volumeInfo -> {
                            final DetailBook book = new DetailBook();
                            book.setTitle(volumeInfo.getTitle());
                            book.getAuthors().addAll(volumeInfo.getAuthors());
                            book.setPages(volumeInfo.getPageCount());
                            book.setDatePublished(ZonedDateTime.parse(volumeInfo.getPublishedDate()));
                            book.setDescription(volumeInfo.getDescription());
                            book.setPublisher(volumeInfo.getPublisher());
                            book.setIsbn13(volumeInfo.getIndustryIdentifiers().stream()
                                    .filter(industryIdentifiers -> industryIdentifiers.getType().equals("ISBN_13"))
                                    .map(Volume.VolumeInfo.IndustryIdentifiers::getIdentifier)
                                    .findFirst()
                                    .orElse(null)
                            );
                            book.setIsbn13(volumeInfo.getIndustryIdentifiers().stream()
                                    .filter(industryIdentifiers -> industryIdentifiers.getType().equals("ISBN_10"))
                                    .map(Volume.VolumeInfo.IndustryIdentifiers::getIdentifier)
                                    .findFirst()
                                    .orElse(null)
                            );
                            final Volume.VolumeInfo.ImageLinks imageLinks = volumeInfo.getImageLinks();
                            final String imageUrl = StringUtils.firstNonBlank(imageLinks.getExtraLarge(), imageLinks.getLarge(), imageLinks.getMedium(), imageLinks.getSmall(), imageLinks.getThumbnail(), imageLinks.getSmallThumbnail());
                            if (StringUtils.isNotBlank(imageUrl)) {
                                try {
                                    URL imageUrlObj = new URL(imageUrl);
                                    imageUrlObj.openStream();
                                    // TBD: fetch image
                                } catch (IOException e) {
                                    LOG.warn("error fetching image", e);
                                }
                            }

                            return book;
                        })
                        .collect(Collectors.toList());
            }
        } catch (GeneralSecurityException | IOException e ) {
            LOG.warn("exception making request to google api", e);
        }


        return Collections.emptyList();
    }
}
