package com.harmony.engine.data;

import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.gameObjects.GameObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;

public class DataUtils {

    // Game Object Utils
    public static Element[] saveGameObject(Document document, GameObject gameObject) {
        // Save Name Attribute
        Element name = document.createElement("data");
        name.setAttribute("name", gameObject.name);

        // Save Texture ID
        Element texture = document.createElement("data");
        if(gameObject.texture != null) {
            texture.setAttribute("textureID", Integer.toString(gameObject.texture.id));
        } else {
            texture.setAttribute("textureID", "null");
        }

        // Save Position
        Element position = saveVector2f(document, gameObject.position);

        return new Element[] {
                name, texture, position
        };
    }

    public static GameObject loadGameObject(Element data) {
        GameObject object = new GameObject("");

        NodeList nList = data.getChildNodes();

        for(int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if(element.hasAttribute("name")) object.name = element.getAttribute("name");

                else if(element.hasAttribute("textureID")) {
                    object.texture = ProjectData.textures.get(Integer.parseInt(element.getAttribute("textureID")));
                }

                else if(element.getTagName().equals("vector2f")) {
                    object.position = loadVector2f(element);
                }
            }
        }

        return object;
    }

    // Vector Utils

    public static Element saveVector2f(Document document, Vector2f vector2f) {
        Element data = document.createElement("vector2f");

        data.setAttribute("x", Float.toString(vector2f.x));
        data.setAttribute("y", Float.toString(vector2f.y));

        return data;
    }

    public static Vector2f loadVector2f(Element data) {
        float x = Float.parseFloat(data.getAttribute("x"));
        float y = Float.parseFloat(data.getAttribute("y"));

        return new Vector2f(x, y);
    }

    // File Utils
    public enum FileType {
        PYTHON("python"),
        JAVA("java"),
        TEXT("plain_text");

        public String aceKey;
        FileType(String aceKey) { this.aceKey = aceKey; }

        public static FileType fromExtension(String extension) {
            switch (extension) {
                case "py":      return PYTHON;
                case "txt":     return TEXT;
                case "java":    return JAVA;
            }

            return TEXT;
        }

        public static FileType fromFile(File file) {
            String[] split = file.getName().split("\\.");
            if(split.length > 1) return fromExtension(split[split.length - 1]);
            return TEXT;
        }
    }

    public static String readFile(File file) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while((line = reader.readLine()) != null) { builder.append(line).append("\n"); }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static String readFileForAce(File file) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while((line = reader.readLine()) != null) { builder.append(line).append("\\n"); }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    // OS Utils
    public enum OperatingSystem {
        MAC("/Library/Java/JavaVirtualMachines"),
        WINDOWS("C:\\Program Files\\Java");

        public final String jdkLocation;

        OperatingSystem(String jdkLocation) {
            this.jdkLocation = jdkLocation;
        }

        public static OperatingSystem getCurrentOS() {
            String osName = System.getProperty("os.name");

            if(osName.startsWith("Windows")) return WINDOWS;
            else return MAC;
        }
    }
}
