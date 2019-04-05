package com.github.yankee42.courseconvert;

import java.nio.file.Path;

public class ParseCourseException extends RuntimeException {
    private final Path file;

    public ParseCourseException(final Path file, final Exception e) {
        super(e);
        this.file = file;
    }

    @Override
    public String getMessage() {
        return "Could not parse course from file <" + file + ">: " + super.getMessage();
    }
}
