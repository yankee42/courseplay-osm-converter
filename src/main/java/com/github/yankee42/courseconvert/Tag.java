package com.github.yankee42.courseconvert;

import org.jdom2.Element;

import java.util.Map;
import java.util.stream.Collectors;

public class Tag {
    static Element createTag(final String key, final String value) {
        final Element tag = new Element("tag");
        tag.setAttribute("k", key);
        tag.setAttribute("v", value);
        return tag;
    }

    static void addTagsToElement(final Element element, final Map<String, String> tags) {
        for (final Map.Entry<String, String> entry : tags.entrySet()) {
            element.addContent(createTag(entry.getKey(), entry.getValue()));
        }
    }

    static Map<String, String> getTags(final Element element) {
        return element.getChildren("tag").stream().collect(
            Collectors.toMap(elem -> elem.getAttributeValue("k"), elem -> elem.getAttributeValue("v"))
        );
    }
}
