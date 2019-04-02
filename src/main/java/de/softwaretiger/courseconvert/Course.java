package de.softwaretiger.courseconvert;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private final List<CourseWaypoint> waypoints;
    private final Map<String, String> properties;

    public Course(final List<CourseWaypoint> waypoints, final Map<String, String> properties) {
        this.waypoints = new ArrayList<>(waypoints);
        this.properties = new HashMap<>(properties);
    }

    public List<CourseWaypoint> getWaypoints() {
        return waypoints;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Document toJdom() {
        final Element root = new Element("course");
        properties.forEach(root::setAttribute);
        long i = 1;
        for (final CourseWaypoint waypoint : waypoints) {
            root.addContent(waypoint.toJdom(i++));
        }
        return new Document(root);
    }
}
