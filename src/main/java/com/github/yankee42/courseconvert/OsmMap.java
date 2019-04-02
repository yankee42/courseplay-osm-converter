package com.github.yankee42.courseconvert;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class OsmMap {
    private List<Node> nodes = new ArrayList<>();
    private List<Way> ways = new ArrayList<>();

    public static OsmMap fromJdom(final Element root) throws DataConversionException {
        final OsmMap result = new OsmMap();
        for (final Element node : root.getChildren("node")) {
            result.getNodes().add(Node.fromJdom(node));
        }
        final Map<Long, Node> nodeMap = result.getNodes().stream().collect(toMap(Node::getId, Function.identity()));
        for (final Element way : root.getChildren("way")) {
            result.getWays().add(Way.fromJdom(way, nodeMap::get));
        }
        return result;
    }

    public Document toJdom() {
        final Element root = new Element("osm");
        root.setAttribute("version", "0.6");
        for (final Node node : nodes) {
            root.addContent(node.toJdom());
        }
        for (final Way way : ways) {
            root.addContent(way.toJdom());
        }
        return new Document(root);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Way> getWays() {
        return ways;
    }

    public void setWays(final List<Way> ways) {
        this.ways = ways;
    }
}
