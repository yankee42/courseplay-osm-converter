package de.softwaretiger.courseconvert;

public class IdGenerator {
    private static long id = -1;

    public static long nextId() {
        return id--;
    }
}
