package com.harmony.engine.data;

import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.gameObjects.GameObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
}
