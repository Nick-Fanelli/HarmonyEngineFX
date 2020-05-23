package com.harmony.engine.data;

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
import java.io.File;

public class ProjectData {

    public static String projectName;
    public static String author;
    public static String versionID;

    public static void reset() {
        projectName = "";
        author = "";
        versionID = "";
    }

    public static void save(File directory) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = dbFactory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("Project");
            document.appendChild(rootElement);

            ProjectData.addAttributes(rootElement, document); // Add in the elements

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(new File(directory.getPath() + "/" + directory.getName() + ".hyproj"));

            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAttributes(Element rootElement, Document document) {
        rootElement.appendChild(createElement(document, VALUE, "ProjectName", projectName));
        rootElement.appendChild(createElement(document, VALUE, "Author", author));
        rootElement.appendChild(createElement(document, VALUE, "VersionID", versionID));
    }

    public static final String VALUE = "value";

    public static Element createElement(Document document, String type, String name, String value) {
        Element node = document.createElement(type);

        node.setAttribute("name", name);
        node.setAttribute("value", value);

        return node;
    }

    public static void load(File directory) {
        File inputFile = new File(directory.getPath() + "/" + directory.getName() + ".hyproj");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName(VALUE);

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;

                    switch (eElement.getAttribute("name")) {
                        case "ProjectName":
                            projectName = eElement.getAttribute("value");
                            break;
                        case "Author":
                            author = eElement.getAttribute("value");
                            break;
                        case "VersionID":
                            versionID = eElement.getAttribute("value");
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}