package com.harmony.engine.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheData {

    public static final String RECENT_PROJECTS = "RecentProjects";

    private static final String TAG_NAME = "cache-data";
    private static final HashMap<String, String> dataContext = new HashMap<>();

    public static void push(String key, String value) {
        dataContext.put(key, value);
    }

    public static String get(String key) {
        return dataContext.get(key);
    }

    public static void save() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = dbFactory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("CacheData");
            document.appendChild(rootElement);

            for(Map.Entry<String, String> entry : dataContext.entrySet()) {
                Element node = document.createElement(TAG_NAME);

                node.setAttribute("key", entry.getKey());
                node.setAttribute("value", entry.getValue());

                rootElement.appendChild(node);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(GlobalData.CACHE_DATA_LOCATION);

            transformer.transform(source, file);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            File inputFile = new File(GlobalData.CACHE_DATA_LOCATION);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(TAG_NAME);

            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if(element.hasAttribute("key") && element.hasAttribute("value")) {
                        dataContext.put(element.getAttribute("key"), element.getAttribute("value"));
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
