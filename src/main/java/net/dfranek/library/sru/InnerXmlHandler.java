package net.dfranek.library.sru;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class InnerXmlHandler implements DomHandler<String, StreamResult> {

    private static final String START_TAG = "<recordData>";
    private static final String END_TAG = "</recordData>";

    private StringWriter xmlWriter = new StringWriter();

    public StreamResult createUnmarshaller(ValidationEventHandler errorHandler) {
        return new StreamResult(xmlWriter);
    }

    public String getElement(StreamResult rt) {
        String xml = rt.getWriter().toString();
        int beginIndex = xml.indexOf(START_TAG) + START_TAG.length();
        int endIndex = xml.lastIndexOf(END_TAG);
        return xml.substring(beginIndex, endIndex);
    }

    public Source marshal(String n, ValidationEventHandler errorHandler) {
        try {
            String xml = START_TAG + n.trim() + END_TAG;
            StringReader xmlReader = new StringReader(xml);
            return new StreamSource(xmlReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}