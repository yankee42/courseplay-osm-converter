package com.github.yankee42.courseconvert;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

import static com.github.yankee42.courseconvert.Tag.addTagsToElement;

public class Node {
    private long id;
    private double lon;
    private double lat;
    private Map<String, String> tags = new HashMap<>();

    public Node(final long id, final double lon, final double lat, final Map<String, String> tags) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.tags = new HashMap<>(tags);
    }

    public Node(final long id, final double lon, final double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public static Node fromJdom(final Element element) throws DataConversionException {
        return new Node(
            element.getAttribute("id").getLongValue(),
            element.getAttribute("lon").getDoubleValue(),
            element.getAttribute("lat").getDoubleValue(),
            Tag.getTags(element)
        );
    }

    public Element toJdom() {
        final Element node = new Element("node");
        node.setAttribute("id", String.valueOf(id));
        node.setAttribute("lat", String.valueOf(lat));
        node.setAttribute("lon", String.valueOf(lon));
        addTagsToElement(node, tags);
        return node;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(final double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(final Map<String, String> tags) {
        this.tags = tags;
    }
}
