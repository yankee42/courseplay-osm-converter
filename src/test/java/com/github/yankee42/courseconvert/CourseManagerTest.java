package com.github.yankee42.courseconvert;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CourseManagerTest {
    @Test
    public void testName() throws Exception {
        // setup
        final Document document = new SAXBuilder().build(
            CourseManagerTest.class.getResourceAsStream("CourseManagerTest_simpleCourseManager.xml")
        );

        // execution
        final CourseManager actual = CourseManager.fromElement(document.getRootElement());

        // evaluation
        assertEquals(actual.getSaves().size(), 1);
        assertEquals(actual.getSaves().get(0).getFileName(), "courseStorage0001.xml");
        assertEquals(actual.getSaves().get(0).getPath(), "someName");
        assertEquals(actual.getSaves().get(0).getId(), 30);
        assertTrue(actual.getSaves().get(0).isUsed());
    }
}
