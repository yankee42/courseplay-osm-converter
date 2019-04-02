package de.softwaretiger.courseconvert;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CourseWaypoint {
    private static final DecimalFormat POS_FORMAT = new DecimalFormat("#.##");
    static {
        POS_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }
    private double x;
    private double y;
    private double height;
    private Map<String, String> properties = new HashMap<>();

    public CourseWaypoint(final double x, final double y, final double height, final Map<String, String> properties) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.properties = properties;
    }

    public static CourseWaypoint fromElement(final Element element) {
        double x = 0, y = 0, height = 0;
        final Map<String, String> properties = new HashMap<>();
        for (final Attribute attribute : element.getAttributes()) {
            if (attribute.getName().equals("pos")) {
                final String[] values = attribute.getValue().split(" ");
                x = Double.parseDouble(values[0]);
                y = Double.parseDouble(values[2]);
                height = Double.parseDouble(values[1]);
            } else {
                properties.put(attribute.getName(), attribute.getValue());
            }
        }
        return new CourseWaypoint(x, y, height, properties);
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(final double height) {
        this.height = height;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public Element toJdom(final long number) {
        final Element element = new Element("waypoint" + number);
        element.setAttribute("pos", String.format(Locale.US, "%1$.2f %2$.2f %3$.2f", x, height, y));
        /*element.setAttribute(
            "pos", POS_FORMAT.format(x) + ' ' + POS_FORMAT.format(height) + ' ' + POS_FORMAT.format(y)
        );*/
        properties.forEach(element::setAttribute);
        return element;
    }
}
