package de.softwaretiger.courseconvert;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlUtil {
    static Document load(final Path path) throws JDOMException, IOException {
        return new SAXBuilder().build(path.toFile());
    }

    static void save(final Document document, final Path path) throws IOException {
        final XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, Files.newBufferedWriter(path));
    }

    static void save(final Element element, final Path path) throws IOException {
        final XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(element, Files.newBufferedWriter(path));
    }
}
