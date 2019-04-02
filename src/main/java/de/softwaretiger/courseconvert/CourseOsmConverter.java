package de.softwaretiger.courseconvert;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.softwaretiger.courseconvert.IdGenerator.nextId;
import static java.util.stream.Collectors.toMap;

public class CourseOsmConverter {

    private static final String TAG_FILE_NAME = "fileName";
    private static final String TAG_USED = "used";
    private static final String TAG_PATH = "name";
    private static final String TAG_TYPE = "type";
    private static final String TYPE_COURSE_WAY = "courseWay";
    private static final String COURSE_PREFIX = "course.";
    private static final double METER_PER_DEGREE = 1_000_000d / 9d;

    public static void main(String[] args) throws JDOMException, IOException {
        final Path dir = Paths.get("/mnt/diskstation/tiger/YankeeeCourseDrawer/LS19_MIG_Map_MadeInGermany_Celle.MiG_Map");
        convertManager(
            dir.resolve("courseManager.xml"),
            dir.resolve("course.osm"),
            4096
        );

        convertOsm(dir.resolve("course.osm"), dir.resolve("out"));
    }

    public static void convertManager(final Path managerPath, final Path path, final int mapSize) throws JDOMException, IOException {
        final CourseManager courseManager = CourseManager.fromElement(XmlUtil.load(managerPath).getRootElement());
        final OsmMap osm = new OsmMap();
        for (final CourseManager.Save save : courseManager.getSaves()) {
            final Course course = parseCourse(managerPath.resolveSibling(save.getFileName()));
            final List<Node> courseNodes = course
                .getWaypoints()
                .stream()
                .map(CourseOsmConverter::toOsmNode)
                .collect(Collectors.toList());
            osm.getNodes().addAll(courseNodes);
            final Way courseWay = new Way(nextId(), courseNodes);
            courseWay.getTags().put(TAG_TYPE, TYPE_COURSE_WAY);
            courseWay.getTags().put(TAG_FILE_NAME, save.getFileName());
            courseWay.getTags().put(TAG_USED, String.valueOf(save.isUsed()));
            courseWay.getTags().put(TAG_PATH, save.getPath());
            course.getProperties().forEach((k, v) -> courseWay.getTags().put(COURSE_PREFIX + k, v));
            osm.getWays().add(courseWay);
        }
        addMapCalibrationWay(osm, mapSize / 2);
        XmlUtil.save(osm.toJdom(), path);
    }

    public static void convertOsm(final Path osmPath, final Path outputPath) throws JDOMException, IOException {
        final OsmMap osm = OsmMap.fromJdom(XmlUtil.load(osmPath).getRootElement());

        for (final Way way : osm.getWays()) {
            if (TYPE_COURSE_WAY.equals(way.getTags().get(TAG_TYPE))) {
                saveCourseXml(way, outputPath);
            }
        }
    }

    private static void saveCourseXml(final Way way, final Path dir) throws IOException {
        final Course course = new Course(
            way.getNodes().stream().map(CourseOsmConverter::toCourseWaypoint).collect(Collectors.toList()),
            way.getTags().entrySet().stream()
                .filter(e -> e.getKey().startsWith(COURSE_PREFIX))
                .collect(toMap(e -> e.getKey().substring(COURSE_PREFIX.length()), Map.Entry::getValue))
        );
        XmlUtil.save(course.toJdom().getRootElement(), dir.resolve(way.getTags().get(TAG_FILE_NAME)));
    }

    private static void addMapCalibrationWay(final OsmMap osm, final int halfMapSize) {
        final List<Node> rectNodes = Arrays.asList(
            createNodeFromMapCoordinates(-halfMapSize, halfMapSize, nextId()),
            createNodeFromMapCoordinates(halfMapSize, halfMapSize, nextId()),
            createNodeFromMapCoordinates(halfMapSize, -halfMapSize, nextId()),
            createNodeFromMapCoordinates(-halfMapSize, -halfMapSize, nextId())
        );
        final Way mapCalibrationWay = new Way(nextId(), rectNodes);
        mapCalibrationWay.getTags().put("type", "calibration rectangle");
        mapCalibrationWay.getNodes().add(rectNodes.get(0));
        osm.getWays().add(mapCalibrationWay);
        osm.getNodes().addAll(rectNodes);
    }

    private static Node toOsmNode(final CourseWaypoint waypoint) {
        final Node node = new Node(
            nextId(),
            meterToDegree(waypoint.getX()),
            meterToDegree(waypoint.getY() * -1),
            waypoint.getProperties().entrySet().stream().collect(
                toMap(entry -> COURSE_PREFIX + entry.getKey(), Map.Entry::getValue)
            )
        );
        node.getTags().put("height", String.valueOf(waypoint.getHeight()));
        return node;
    }

    public static CourseWaypoint toCourseWaypoint(final Node node) {
        return new CourseWaypoint(
            degreeToMeter(node.getLon()),
            degreeToMeter(node.getLat() * -1),
            Double.parseDouble(node.getTags().get("height")),
            node.getTags().entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(COURSE_PREFIX))
                .collect(toMap(entry -> entry.getKey().substring(COURSE_PREFIX.length()), Map.Entry::getValue))
        );
    }

    private static Course parseCourse(final Path in) throws JDOMException, IOException {
        final Element root = XmlUtil.load(in).getRootElement();

        return new Course(
            root.getChildren().stream()
                .map(CourseWaypoint::fromElement)
                .collect(Collectors.toList()),
            root.getAttributes().stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue))
        );
    }

    private static double meterToDegree(final double meter) {
        return meter / METER_PER_DEGREE;
    }

    private static double degreeToMeter(final double degree) {
        return degree * METER_PER_DEGREE;
    }

    private static Node createNodeFromMapCoordinates(final double metersX, final double metersY, final long id) {
        return new Node(id, meterToDegree(metersX), meterToDegree(metersY));
    }
}
