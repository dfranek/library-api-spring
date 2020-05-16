package net.dfranek.library.rest.webservice.impl;

import net.dfranek.library.rest.config.DnbConfig;
import net.dfranek.library.rest.dto.DetailBook;
import net.dfranek.library.rest.utils.ImageDownloadHelper;
import net.dfranek.library.rest.webservice.BookWebService;
import net.dfranek.library.sru.SearchRetrieveResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.marc4j.MarcReader;
import org.marc4j.MarcXmlReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DnbWebService implements BookWebService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageDownloadHelper.class);

    private static final String BASE_URL = "https://services.dnb.de/sru/dnb";

    @Autowired
    private DnbConfig dnbConfig;


    @Override
    public List<DetailBook> getBookByIsbn(String isbn) {
        return queryWebService("isbn=" + isbn);
    }

    @Override
    public List<DetailBook> getBookByTitleAndAuthor(String title, String author) {
        StringBuilder query = new StringBuilder("tit=\"");
        query.append(title);
        query.append("\"  and mat=books");
        if(StringUtils.isNotBlank(author)) {
            query.append("and per=\"");
            query.append(author);
            query.append("\"");
        }

        return queryWebService(query.toString());
    }

    private List<DetailBook> queryWebService(final String query) {
        try {
            URIBuilder uriBuilder = new URIBuilder(BASE_URL);
            uriBuilder.addParameter("operation", "searchRetrieve");
            uriBuilder.addParameter("version", "1.1");
            uriBuilder.addParameter("accessToken", dnbConfig.getAccessToken());
            uriBuilder.addParameter("query", query);
            uriBuilder.addParameter("recordSchema", "MARC21-xml");
            final URI uri = uriBuilder.build();
            final URL url = uri.toURL();

            JAXBContext jaxbContext = JAXBContext.newInstance(SearchRetrieveResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            List<DetailBook> books = new ArrayList<>();
            final SearchRetrieveResponse response = (SearchRetrieveResponse) unmarshaller.unmarshal(url.openStream());
            response.getRecords()
                    .forEach(record -> {
                        InputStream targetStream = new ByteArrayInputStream(record.getRecordData().getBytes());
                        MarcReader reader = new MarcXmlReader(targetStream);
                        while (reader.hasNext()) {
                            Optional.ofNullable(createDetailBookFromRecord(reader.next()))
                                .ifPresent(books::add);
                        }
                    });

            return books;
        } catch (URISyntaxException | IOException | JAXBException e) {
            LOG.error("error opening connection to DNB", e);
        }

        return Collections.emptyList();
    }

    private DetailBook createDetailBookFromRecord(Record record) {
        final DetailBook book = new DetailBook();
        book.setTitle(getFieldData(record, "245", "a"));
        Optional.ofNullable(parsePages(getFieldData(record, "300", "a")))
                .ifPresent(book::setPages);
        book.setPublisher(getFieldData(record, "264", "b"));
        book.setPlaceOfPublication(getFieldData(record, "264", "a"));
        Optional.ofNullable(getFieldData(record, "245", "bnp"))
                .ifPresent(book::setTagline);

        getFieldDataAsList(record, "020", "a")
                .forEach(isbn -> {
                    if(isbn.length() == 10) {
                        book.setIsbn10(isbn);
                    } else {
                        book.setIsbn13(isbn);
                    }
                });

        return null;
    }

    private Integer parsePages(final String value) {
        final Pattern pattern = Pattern.compile("\\d+", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            final String pagesAsString = matcher.group(0);
            try {
                return Integer.parseInt(pagesAsString);
            } catch (NumberFormatException exception){
                return null;
            }
        }
        return null;
    }

    private String getFieldData(final Record record, final String fieldSpecifier, final String subFieldSpecifier) {
        return Optional.ofNullable((DataField) record.getVariableField(fieldSpecifier))
                .map(field -> field.getSubfieldsAsString(subFieldSpecifier))
                .orElse(null);
    }

    private List<String> getFieldDataAsList(final Record record, final String fieldSpecifier, final String subFieldSpecifier) {
        return record.getVariableFields(fieldSpecifier).stream()
                .map(field -> (DataField) field)
                .map(field -> field.getSubfieldsAsString(subFieldSpecifier))
                .collect(Collectors.toList());
    }

}
