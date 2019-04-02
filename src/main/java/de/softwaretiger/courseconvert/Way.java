package de.softwaretiger.courseconvert;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static de.softwaretiger.courseconvert.Tag.addTagsToElement;
import static java.util.stream.Collectors.toList;

public class Way {
    private final long id;
    private final List<Node> nodes;
    private final Map<String, String> tags = new HashMap<>();

    public Way(final long id, final List<Node> nodes) {
        this.id = id;
        this.nodes = new ArrayList<>(nodes);
    }

    public Way(final long id, final List<Node> nodes, final Map<String, String> tags) {
        this.id = id;
        this.nodes = new ArrayList<>(nodes);
        this.tags.putAll(tags);
    }

    public static Way fromJdom(final Element element, final Function<Long, Node> nodeResolver) throws DataConversionException {
        return new Way(
            element.getAttribute("id").getLongValue(),
            element.getChildren("nd")
                .stream()
                .map(elem -> nodeResolver.apply(Long.parseLong(elem.getAttributeValue("ref")))).collect(toList()),
            Tag.getTags(element)
        );
    }

    public Element toJdom() {
        final Element element = new Element("way");
        element.setAttribute("id", String.valueOf(id));
        addTagsToElement(element, tags);
        for (final Node node : nodes) {
            final Element ref = new Element("nd");
            ref.setAttribute("ref", String.valueOf(node.getId()));
            element.addContent(ref);
        }
        return element;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
