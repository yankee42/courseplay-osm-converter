package com.github.yankee42.courseconvert.gui;

import com.github.yankee42.courseconvert.CourseOsmConverter;
import org.jdom2.JDOMException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainGui {
    private JFrame frame;

    private MainGui() {
        SwingUtilities.invokeLater(this::createFrame);
    }

    public static void main(String[] args) {
        new MainGui();
    }

    private void createFrame() {
        frame = new JFrame("Course<->OSM");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(createOsmToCourseButton());
        frame.add(createCourseToOsmButton());
        frame.setSize(300, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
    }

    private Component createOsmToCourseButton() {
        final JButton result = new JButton("Course -> OSM");
        result.addActionListener(this::courseToOsm);
        return result;
    }

    private Component createCourseToOsmButton() {
        final JButton result = new JButton("OSM -> Course");
        result.addActionListener(this::osmToCourse);
        return result;
    }

    private void osmToCourse(final ActionEvent actionEvent) {
        try {
            tryOsmToCourse();
        } catch (Exception e) {
            exceptionMessage(e);
        }
    }

    private void tryOsmToCourse() throws JDOMException, IOException {
        final JFileChooser inputFileChooser = new JFileChooser();
        inputFileChooser.setFileFilter(new FileNameExtensionFilter("OSM", "osm"));
        inputFileChooser.setDialogTitle("select OSM-File");
        if (inputFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            final File inputFile = inputFileChooser.getSelectedFile();
            final JFileChooser outputFileChooser = new JFileChooser();
            outputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            outputFileChooser.setCurrentDirectory(
                LsPathUtils.COURSE_PATH == null ? inputFile.getParentFile() : LsPathUtils.COURSE_PATH.toFile()
            );
            outputFileChooser.setDialogTitle("Select output directory");

            if (outputFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                final File outputFile = outputFileChooser.getSelectedFile();
                CourseOsmConverter.convertOsm(inputFile.toPath(), outputFile.toPath());
                JOptionPane.showMessageDialog(frame, "Done");
            }
        }
    }

    private void courseToOsm(ActionEvent event) {
        try {
            tryCourseToOsm();
        } catch (Exception e) {
            exceptionMessage(e);
        }
    }

    private void exceptionMessage(final Exception e) {
        final StringWriter buffer = new StringWriter();
        e.printStackTrace(new PrintWriter(buffer));
        final String strackTrace = buffer.toString();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(strackTrace), null);
        JOptionPane.showMessageDialog(
            frame, "An error occured (also copied to clipboard):\n\n" + strackTrace
        );
    }

    private void tryCourseToOsm() throws JDOMException, IOException {
        final JFileChooser inputFileChooser = new JFileChooser();
        inputFileChooser.setDialogTitle("Open courseManager.xml");
        inputFileChooser.setFileFilter(new FileNameExtensionFilter("Course XML", "xml"));
        if (LsPathUtils.COURSE_PATH != null) {
            inputFileChooser.setCurrentDirectory(LsPathUtils.COURSE_PATH.toFile());
        }
        if (inputFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            final File inputFile = inputFileChooser.getSelectedFile();
            final JFileChooser outputFileChooser = new JFileChooser();
            outputFileChooser.setDialogTitle("Select output file");
            outputFileChooser.setFileFilter(new FileNameExtensionFilter("OSM", "osm"));
            outputFileChooser.setCurrentDirectory(inputFile.getParentFile());

            final int mapSize = Integer.parseInt(JOptionPane.showInputDialog(frame, "Map size", 4096));

            if (outputFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File outputFile = outputFileChooser.getSelectedFile();
                if (!outputFile.getName().contains(".")) {
                    outputFile = new File(outputFile.getParentFile(), outputFile.getName() + ".osm");
                }
                CourseOsmConverter.convertManager(inputFile.toPath(), outputFile.toPath(), mapSize);
                JOptionPane.showMessageDialog(frame, "File created: " + outputFile);
            }
        }
    }
}
