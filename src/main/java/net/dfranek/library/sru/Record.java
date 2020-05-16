package net.dfranek.library.sru;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="record", namespace = "http://www.loc.gov/zing/srw/")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

    @XmlAnyElement(InnerXmlHandler.class)
    private String recordData;

    public String getRecordData() {
        return recordData;
    }


}
