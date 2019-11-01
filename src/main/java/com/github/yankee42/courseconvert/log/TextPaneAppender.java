package com.github.yankee42.courseconvert.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

/*
Credits: https://stackoverflow.com/questions/9395358/how-link-log-from-logback-in-swing-application
 */
public class TextPaneAppender extends AppenderBase<ILoggingEvent> {
    private static SimpleAttributeSet ERROR_ATT, WARN_ATT, INFO_ATT, DEBUG_ATT, TRACE_ATT, RESTO_ATT;

    private PatternLayout patternLayout;
    private static JTextPane textPane = new JTextPane();

    static {
        // ERROR
        ERROR_ATT = new SimpleAttributeSet();
        ERROR_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
        ERROR_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.FALSE);
        ERROR_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(153, 0, 0));

        // WARN
        WARN_ATT = new SimpleAttributeSet();
        WARN_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        WARN_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.FALSE);
        WARN_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(153, 76, 0));

        // INFO
        INFO_ATT = new SimpleAttributeSet();
        INFO_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        INFO_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.FALSE);
        INFO_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(0, 0, 153));

        // DEBUG
        DEBUG_ATT = new SimpleAttributeSet();
        DEBUG_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        DEBUG_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
        DEBUG_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(64, 64, 64));

        // TRACE
        TRACE_ATT = new SimpleAttributeSet();
        TRACE_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        TRACE_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
        TRACE_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(153, 0, 76));

        // RESTO
        RESTO_ATT = new SimpleAttributeSet();
        RESTO_ATT.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        RESTO_ATT.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
        RESTO_ATT.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(0, 0, 0));
    }

    @Override
    public void start() {
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        patternLayout.start();

        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (SwingUtilities.isEventDispatchThread()) {
            appendInternal(event);
        } else {
            SwingUtilities.invokeLater(() -> appendInternal(event));
        }
    }

    private void appendInternal(final ILoggingEvent event) {
        final String formattedMsg = patternLayout.doLayout(event);
        try {
            int limite = 1000;
            int apaga = 200;
            if (textPane.getDocument().getDefaultRootElement().getElementCount() > limite) {
                int end = getLineEndOffset(textPane, apaga);
                replaceRange(textPane, null, 0, end);
            }

            if (event.getLevel() == Level.ERROR)
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, ERROR_ATT);
            else if (event.getLevel() == Level.WARN)
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, WARN_ATT);
            else if (event.getLevel() == Level.INFO)
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, INFO_ATT);
            else if (event.getLevel() == Level.DEBUG)
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, DEBUG_ATT);
            else if (event.getLevel() == Level.TRACE)
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, TRACE_ATT);
            else
                textPane.getDocument().insertString(textPane.getDocument().getLength(), formattedMsg, RESTO_ATT);

        } catch (BadLocationException e) {
            // Faz nada
        }
        textPane.setCaretPosition(textPane.getDocument().getLength());
        textPane.update(textPane.getGraphics());
    }

    private int getLineCount(JTextPane textPane) {
        return textPane.getDocument().getDefaultRootElement().getElementCount();
    }

    private int getLineEndOffset(JTextPane textPane, int line) throws BadLocationException {
        int lineCount = getLineCount(textPane);
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", textPane.getDocument().getLength()+1);
        } else {
            Element map = textPane.getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            int endOffset = lineElem.getEndOffset();
            // hide the implicit break at the end of the document
            return ((line == lineCount - 1) ? (endOffset - 1) : endOffset);
        }
    }

    private void replaceRange(JTextPane textPane, String str, int start, int end) throws IllegalArgumentException {
        if (end < start) {
            throw new IllegalArgumentException("end before start");
        }
        Document doc = textPane.getDocument();
        if (doc != null) {
            try {
                if (doc instanceof AbstractDocument) {
                    ((AbstractDocument)doc).replace(start, end - start, str, null);
                }
                else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public static JTextPane getTextPane() {
        return textPane;
    }
}
