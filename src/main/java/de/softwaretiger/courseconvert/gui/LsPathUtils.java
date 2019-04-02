package de.softwaretiger.courseconvert.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class LsPathUtils {

    static final Path COURSE_PATH = findCoursesDirectory();

    private static Path findCoursesDirectory() {
        final Path home = Paths.get(System.getProperty("user.home"));
        return Stream
            .of(
                Paths.get("Documents", "My Games", "FarmingSimulator2019", "CoursePlay_Courses"), // windows
                Paths.get("Library", "Application Support", "FarmingSimulator2019", "CoursePlay_Courses") // mac
            )
            .map(home::resolve)
            .filter(Files::exists)
            .findFirst()
            .orElse(null);
    }

}
