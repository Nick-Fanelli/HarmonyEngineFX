/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.core.State;
import com.harmony.engine.utils.Status;
import com.harmony.core.GameObject;
import com.harmony.core.io.Texture;
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
import java.util.ArrayList;

public class ProjectData {

    public static String projectName;
    public static String harmonyVersionID;
    public static String author;
    public static String versionID;
    public static String launcherState;

    public static ArrayList<Texture> textures = new ArrayList<>();
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static ArrayList<State> states = new ArrayList<>();

    public static void reset() {
        projectName = "";
        harmonyVersionID = "";
        author = "";
        versionID = "";
        launcherState = "";
        textures.clear();
        gameObjects.clear();
        states.clear();
    }

    public static void save(File directory) {
        if(Harmony.staticStage != null)
            Status.setCurrentStatus(Status.Type.SAVING);

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
            transformer.setOutputProperty(OutputKeys.INDENT, GlobalData.getCompressProject() ? "no" : "yes");

            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(new File(directory.getPath() + "/" + directory.getName() + ".hyproj"));

            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Harmony.staticStage != null)
            Status.setCurrentStatus(Status.Type.READY);
    }

    public static void addAttributes(Element rootElement, Document document) {
        rootElement.appendChild(createElement(document, VALUE, "ProjectName", projectName));
        rootElement.appendChild(createElement(document, VALUE, "HarmonyVersion", Launcher.GITHUB_VERSION_STRING));
        rootElement.appendChild(createElement(document, VALUE, "Author", author));
        rootElement.appendChild(createElement(document, VALUE, "VersionID", versionID));
        rootElement.appendChild(createElement(document, VALUE, "LauncherState", launcherState));

        ProjectData.addTextureAttributes(rootElement, document);
        ProjectData.addGameObjectAttributes(rootElement, document);
        ProjectData.addStateAttributes(rootElement, document);
    }

    private static void addTextureAttributes(Element rootElement, Document document) {
        Element texturesElement = createContainerElement(document, "Textures");

        for(Texture texture : textures) {
            texturesElement.appendChild(createTextureElement(document, texture.getPath(), texture.name, texture.id));
        }

        rootElement.appendChild(texturesElement);
    }

    private static void addGameObjectAttributes(Element rootElement, Document document) {
        Element gameObjectsElement = createContainerElement(document, "GameObjects");

        for(GameObject gameObject : gameObjects) {
            gameObjectsElement.appendChild(createGameObjectElement(document, gameObject));
        }

        rootElement.appendChild(gameObjectsElement);
    }

    private static void addStateAttributes(Element rootElement, Document document) {
        Element stateElement = createContainerElement(document, "States");

        for(State state : states) {
            stateElement.appendChild(createStateElement(document, state));
        }

        rootElement.appendChild(stateElement);
    }

    public static final String VALUE = "value";
    public static final String TEXTURE = "texture";
    public static final String GAME_OBJECT = "gameObject";
    public static final String STATE = "state";

    public static Element createElement(Document document, String type, String name, String value) {
        Element node = document.createElement(type);

        node.setAttribute("name", name);
        node.setAttribute("value", value);

        return node;
    }

    public static Element createTextureElement(Document document, String path, String name, int id) {
        Element node = document.createElement(TEXTURE);

        node.setAttribute("path", path);
        node.setAttribute("name", name);
        node.setAttribute("id", Integer.toString(id));

        return node;
    }

    public static Element createGameObjectElement(Document document, GameObject gameObject) {
        Element node = document.createElement(GAME_OBJECT);

        Element[] children = DataUtils.saveGameObject(document, gameObject);

        for(Element child : children) node.appendChild(child);

        return node;
    }

    public static Element createStateElement(Document document, State state) {
        Element node = document.createElement(STATE);

        Element[] children = DataUtils.saveState(document, state);

        for(Element child : children) node.appendChild(child);

        return node;
    }

    public static Element createContainerElement(Document document, String name) { return document.createElement(name); }

    public static void load(File directory) {
        ProjectData.reset();

        File inputFile = new File(directory.getPath() + "/" + directory.getName() + ".hyproj");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            // Load in all of the nodes
            loadValueNodes(document.getElementsByTagName(VALUE));
            loadTextureNodes(document.getElementsByTagName(TEXTURE));
            loadGameObjectNodes(document.getElementsByTagName(GAME_OBJECT));
            loadStateNodes(document.getElementsByTagName(STATE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadValueNodes(NodeList nList) {
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                switch (eElement.getAttribute("name")) {
                    case "ProjectName":
                        projectName = eElement.getAttribute("value");
                        break;
                    case "HarmonyVersion":
                        harmonyVersionID = eElement.getAttribute("value");
                        break;
                    case "Author":
                        author = eElement.getAttribute("value");
                        break;
                    case "VersionID":
                        versionID = eElement.getAttribute("value");
                        break;
                    case "LauncherState":
                        launcherState = eElement.getAttribute("value");
                        break;
                }
            }
        }

    }

    private static void loadTextureNodes(NodeList nList) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                Texture texture = new Texture(eElement.getAttribute("path").trim(),
                        eElement.getAttribute("name").trim(),
                        Integer.parseInt(eElement.getAttribute("id").trim()));
                textures.add(texture);
            }
        }
    }

    private static void loadGameObjectNodes(NodeList nList) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                if(eElement.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
                    Element pElement = (Element) eElement.getParentNode();

                    if(pElement.getTagName().equals("GameObjects"))
                        gameObjects.add(DataUtils.loadGameObject(eElement));
                }

            }
        }
    }

    private static void loadStateNodes(NodeList nList) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                if(eElement.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
                    Element pElement = (Element) eElement.getParentNode();

                    if(pElement.getTagName().equals("States"))
                        states.add(DataUtils.loadState(eElement));
                }
            }
        }
    }
}