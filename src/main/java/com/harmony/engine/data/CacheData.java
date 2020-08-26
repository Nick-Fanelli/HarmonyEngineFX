/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data;

import com.harmony.engine.Harmony;
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
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CacheData {

    public static final String CACHE_LOCATION = GlobalData.GLOBAL_DATA_LOCATION + File.separator + "cache.dat";
    public static final String CACHE_TAG_NAME = "cache-data";

    public static HashMap<String, String> dataContext = new HashMap<>();

    public static void setRecentProject(File directory) {
        String location = directory.getPath();

        if(location.equals(dataContext.get("p1")) || location.equals(dataContext.get("p2")) ||
                location.equals(dataContext.get("p3")) || location.equals(dataContext.get("p4"))
                ||location.equals(dataContext.get("p5"))) return;

        dataContext.put("p5", dataContext.get("p4"));
        dataContext.put("p4", dataContext.get("p3"));
        dataContext.put("p3", dataContext.get("p2"));
        dataContext.put("p2", dataContext.get("p1"));
        dataContext.put("p1", location);
    }

    public static File[] getRecentProjects() {
        String[] locations = new String[] {
                dataContext.get("p1"),
                dataContext.get("p2"),
                dataContext.get("p3"),
                dataContext.get("p4"),
                dataContext.get("p5"),
        };

        ArrayList<File> files = new ArrayList<>();

        for(String location : locations) {
            if(location == null || location.isEmpty()) continue;
            File file = new File(location);
            if(file.exists()) files.add(file);
            else dataContext.remove(location);
        }

        return files.toArray(new File[0]);
    }

    private static void addAttributes(Element root, Document document) {
        for(Map.Entry<String, String> entry : dataContext.entrySet()) {
            root.appendChild(createElement(document, entry.getKey(), entry.getValue()));
        }
    }

    private static Element createElement(Document document, String key, String value) {
        Element node = document.createElement(CACHE_TAG_NAME);

        node.setAttribute("key", key);
        node.setAttribute("value", value);

        return node;
    }

    public static void save() {

        CacheData.setValues();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = dbFactory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("CacheData");
            document.appendChild(rootElement);

            CacheData.addAttributes(rootElement, document); // Add in the elements

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(CACHE_LOCATION);

            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> load() {
        try {
            File inputFile = new File(CACHE_LOCATION);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            // Load in all of the nodes
            NodeList nList = document.getElementsByTagName(CACHE_TAG_NAME);

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
        } catch (Exception ignored) { }

        return new HashMap<>();
    }

    private static void setValues() {
        // Set the harmony window size.
        if(Harmony.staticStage != null) {
            setWindowSize(new Dimension((int) Harmony.staticStage.getWidth(), (int) Harmony.staticStage.getHeight()));
        }
    }

    // Elements
    public static void setWindowSize(Dimension dimension) {
        dataContext.put("windowWidth", Integer.toString(dimension.width));
        dataContext.put("windowHeight", Integer.toString(dimension.height));
    }

    public static Dimension getWindowSize() {
        if(!dataContext.containsKey("windowWidth") || !dataContext.containsKey("windowHeight")) return null;

        return new Dimension(Integer.parseInt(dataContext.get("windowWidth")), Integer.parseInt(dataContext.get("windowHeight")));
    }

}
