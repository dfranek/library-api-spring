package net.dfranek.library.sru;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="searchRetrieveResponse", namespace = "http://www.loc.gov/zing/srw/")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRetrieveResponse {

    @XmlElement(namespace = "http://www.loc.gov/zing/srw/")
    private String version;

    @XmlElement(namespace = "http://www.loc.gov/zing/srw/")
    private Integer numberOfRecords;

    @XmlElement(namespace = "http://www.loc.gov/zing/srw/")
    private List<Record> records;

    public String getVersion() {
        return version;
    }

    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }

    public List<Record> getRecords() {
        return records;
    }
}
