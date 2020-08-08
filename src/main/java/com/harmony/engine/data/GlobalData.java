/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.utils.Status;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GlobalData implements Serializable {

    public static final String GLOBAL_PREFERENCES_FILENAME = "globalPreferences.xml";
    public static final String IMPORTED_PREFERENCES_DIRECTORY_NAME = "imported";

    public static final String GLOBAL_DATA_LOCATION =  System.getProperty("user.home") + File.separator + ".harmony" + File.separator +
            "v" + Launcher.VERSION_ID[0];

    public static final String GLOBAL_PREFERENCES_LOCATION = GLOBAL_DATA_LOCATION + File.separator + GLOBAL_PREFERENCES_FILENAME;
    public static final String IMPORTED_THEMES_LOCATION = GLOBAL_PREFERENCES_LOCATION + File.separator + IMPORTED_PREFERENCES_DIRECTORY_NAME +
            File.separator + "themes";

    public static HashMap<String, String> dataContext = new HashMap<>();

    public static void setDefaults() {
        GlobalData.setTheme(Theme.LIGHT);
        GlobalData.setCompressProject(false);
        GlobalData.setAutoSave(true);
        GlobalData.setPanMultiplier(1f);
        GlobalData.setEditorBackgroundColor("35406c");
        GlobalData.setEditorOutlineColor("ff0000");
        GlobalData.setEditorDrawFromTop(true);
        GlobalData.setEditorShowGuideLines(true);
        GlobalData.setEditorGuideLineDist(10);
    }

    /********************** GENERAL **********************/

    // Theme
    public static final String THEME_LOCATION = "theme";
    public enum Theme {
        LIGHT("Default Light"),
        DARK("Default Dark");

        private final String name;
        Theme(String name) { this.name = name; }

        @Override public String toString() { return name; }
    }
    public static void setTheme(Theme theme) { dataContext.put(THEME_LOCATION, theme.name()); }
    public static Theme getTheme() { return Theme.valueOf(dataContext.get(THEME_LOCATION)); }

    // Compress Project
    public static final String COMPRESS_PROJECT = "compressProject";
    public static void setCompressProject(boolean value) { dataContext.put(COMPRESS_PROJECT, Boolean.toString(value)); }
    public static boolean getCompressProject() { return Boolean.parseBoolean(dataContext.get(COMPRESS_PROJECT)); }

    // Auto-Save
    public static final String AUTO_SAVE_LOCATION = "autosave";
    public static void setAutoSave(boolean value) { dataContext.put(AUTO_SAVE_LOCATION, Boolean.toString(value)); }
    public static boolean getAutoSave() { return Boolean.parseBoolean(dataContext.get(AUTO_SAVE_LOCATION)); }

    // JDK Location - Does not get default value
    public static final String JDK_LOCATION = "jdkLocation";
    public static void setJDKLocation(String location) { dataContext.put(JDK_LOCATION, location); }
    public static String getJDKLocation() { return dataContext.get(JDK_LOCATION); }

    /********************** EDITOR **********************/

    // Pan Multipler
    public static final String PAN_MULTIPLIER = "panMultiplier";
    public static void setPanMultiplier(double value) { dataContext.put(PAN_MULTIPLIER, Double.toString(value)); }
    public static double getPanMultipler() { return Double.parseDouble(dataContext.get(PAN_MULTIPLIER)); }

    // Editor Background Color
    public static final String EDITOR_BACKGROUND_COLOR = "editorBackgroundColor";
    public static void setEditorBackgroundColor(String value) { dataContext.put(EDITOR_BACKGROUND_COLOR, value); }
    public static String getEditorBackgroundColor() { return dataContext.get(EDITOR_BACKGROUND_COLOR); }

    // Editor Background Color
    public static final String EDITOR_OUTLINE_COLOR = "editorOutlineColor";
    public static void setEditorOutlineColor(String value) { dataContext.put(EDITOR_OUTLINE_COLOR, value); }
    public static String getEditorOutlineColor() { return dataContext.get(EDITOR_OUTLINE_COLOR); }

    // Editor Draw From Top
    public static final String EDITOR_DRAW_FROM_TOP = "editorDrawFromTop";
    public static void setEditorDrawFromTop(boolean value) { dataContext.put(EDITOR_DRAW_FROM_TOP, Boolean.toString(value)); }
    public static boolean getEditorDrawFromTop() { return Boolean.parseBoolean(dataContext.get(EDITOR_DRAW_FROM_TOP)); }

    // Editor Show Guide Lines
    public static final String EDITOR_SHOW_GUIDE_LINES = "editorShowGuideLines";
    public static void setEditorShowGuideLines(boolean value) { dataContext.put(EDITOR_SHOW_GUIDE_LINES, Boolean.toString(value)); }
    public static boolean getEditorShowGuideLines() { return Boolean.parseBoolean(dataContext.get(EDITOR_SHOW_GUIDE_LINES)); }

    // Editor Guide Distance
    public static final String EDITOR_GUIDE_LINE_DIST = "editorGuideLineDist";
    public static void setEditorGuideLineDist(double value) { dataContext.put(EDITOR_GUIDE_LINE_DIST, Double.toString(value)); }
    public static double getEditorGuideLineDist() { return Double.parseDouble(dataContext.get(EDITOR_GUIDE_LINE_DIST)); }

    public static void save() {
        if(Harmony.staticStage != null)
            Status.setCurrentStatus(Status.Type.SAVING);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = dbFactory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("GlobalData");
            document.appendChild(rootElement);

            GlobalData.addAttributes(rootElement, document); // Add in the elements

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(GLOBAL_PREFERENCES_LOCATION);

            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Harmony.staticStage != null)
            Status.setCurrentStatus(Status.Type.READY);
    }

    private static void addAttributes(Element root, Document document) {
        for(Map.Entry<String, String> entry : dataContext.entrySet()) {
            root.appendChild(createElement(document, entry.getKey(), entry.getValue()));
        }
    }

    public static final String PREFERENCE_TAG_NAME = "preference";

    private static Element createElement(Document document, String key, String value) {
        Element node = document.createElement(PREFERENCE_TAG_NAME);

        node.setAttribute("key", key);
        node.setAttribute("value", value);

        return node;
    }


    public static HashMap<String, String> load() {

        try {
            File inputFile = new File(GLOBAL_PREFERENCES_LOCATION);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            // Load in all of the nodes
            NodeList nList = document.getElementsByTagName(PREFERENCE_TAG_NAME);

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;

                    if(eElement.hasAttribute("key") && eElement.hasAttribute("value")) {
                        dataContext.put(eElement.getAttribute("key"), eElement.getAttribute("value"));
                    }
                }
            }

            return dataContext;
        } catch (Exception ignored) {
        }

        try {
            new File(GLOBAL_DATA_LOCATION).mkdirs();
            System.out.println("Harmony -> Created the global data location");
            new File(GLOBAL_PREFERENCES_LOCATION).createNewFile();
            System.out.println("Harmony -> Created the global data preferences file");
        } catch (Exception e) {
            System.err.println("Harmony -> Could not create the location or the preferences file");
            e.printStackTrace();
        }

        GlobalData.setDefaults();
        return dataContext;
    }

    // Utils
    public static String getThemeCSSLocation() { return "/css/themes/" + getTheme().name().toLowerCase() + "Theme.css"; }
    public static String getThemeCSSLocation(Theme theme) { return "/css/themes/" + theme.name().toLowerCase() + "Theme.css"; }

    public static Stage staticStage;

    public static void launchGlobalPreferences() {
        try {
            if(staticStage != null && staticStage.isShowing()) staticStage.close();

            FXMLLoader loader = new FXMLLoader(GlobalData.class.getResource("/fxml/globalPreferences.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            Stage stage = new Stage();
            GlobalData.staticStage = stage;
            stage.setTitle("Global Preferences");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveTheme(String themeName, String themeData) {
        File themes = new File(IMPORTED_THEMES_LOCATION);

        if(!themes.exists()) themes.mkdirs();

        File themeFile = new File(IMPORTED_THEMES_LOCATION + File.separator + themeName + ".css");
        if(!themeFile.exists()) {
            try {
                themeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(themeFile));
            writer.write(themeData);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
