package com.github.yankee42.courseconvert;

import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager {
    private List<Save> saves;

    public CourseManager(final List<Save> saves) {
        this.saves = new ArrayList<>(saves);
    }

    public Document toJdom() throws DataConversionException {

        final Element saveSlot = new Element("saveSlot");
        final Map<String, Element> folders = new HashMap<>();
        for (final Save save : saves) {
            final String[] path = save.path.split("/");
            final StringBuilder current = new StringBuilder();
            int parent = 0;
            for(int i = 0; i < path.length - 1; i++) {
                if (i > 0) {
                    current.append('/');
                }
                current.append(path[i]);
                final int finalI = i;
                final int finalParent = parent;
                parent = folders.computeIfAbsent(current.toString(), s -> {
                    final Element folder = new Element("folder");
                    folder.setAttribute("id", String.valueOf(folders.size() + 1));
                    folder.setAttribute("parent", String.valueOf(finalParent));
                    folder.setName(path[finalI]);
                    return folder;
                }).getAttribute("id").getIntValue();
            }
            final Element slot = new Element("slot");
            slot.setAttribute("fileName", path[path.length - 1]);
            slot.setAttribute("id", String.valueOf(saveSlot.getChildren().size() + 1));
            slot.setAttribute("parent", String.valueOf(parent));
            slot.setAttribute("isUsed", String.valueOf(save.isUsed));
            slot.setName(save.fileName);
        }
        final Element root = new Element("courseManager");
        root.addContent(saveSlot);
        final Element foldersElement = new Element("folders");
        foldersElement.addContent(folders.values());
        root.addContent(foldersElement);
        return new Document(root);
    }

    public static CourseManager fromElement(final Element element) throws DataConversionException {
        final Map<Integer, Element> folders = new HashMap<>();
        final Element foldersElement = element.getChild("folders");
        if (foldersElement != null) {
            for (final Element folder : foldersElement.getChildren()) {
                folders.put(folder.getAttribute("id").getIntValue(), folder);
            }
        }
        final List<Save> saves = new ArrayList<>();
        for (final Element saveSlot : element.getChild("saveSlot").getChildren()) {
            final Attribute isUsedAttribute = saveSlot.getAttribute("isUsed");
            final boolean isUsed = isUsedAttribute != null && isUsedAttribute.getBooleanValue();
            if (isUsed) {
                saves.add(new Save(
                    saveSlot.getAttribute("id").getIntValue(),
                    isUsed,
                    buildPath(saveSlot, folders),
                    saveSlot.getAttributeValue("fileName")
                ));
            }
        }
        return new CourseManager(saves);
    }
   
    private static String buildPath(final Element saveSlot, final Map<Integer, Element> folders) throws DataConversionException {
        int parentId = saveSlot.getAttribute("parent").getIntValue();
        final StringBuilder result = new StringBuilder(saveSlot.getAttributeValue("name"));
        while(parentId != 0) {
            final Element folder = folders.get(parentId);
            result.insert(0, folder.getAttributeValue("name") + "/");
            parentId = folder.getAttribute("parent").getIntValue();
        }
        return result.toString();
    }

    public List<Save> getSaves() {
        return saves;
    }

    public static class Save {
        private int id;
        private boolean isUsed;
        private String path;
        private String fileName;

        public Save(final int id, final boolean isUsed, final String path, final String fileName) {
            this.id = id;
            this.isUsed = isUsed;
            this.path = path;
            this.fileName = fileName;
        }

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void setUsed(final boolean used) {
            isUsed = used;
        }

        public String getPath() {
            return path;
        }

        public void setPath(final String path) {
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(final String fileName) {
            this.fileName = fileName;
        }
    }
}
