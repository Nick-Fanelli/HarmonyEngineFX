package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.textures.Texture;

import java.io.Serializable;

public class GameObject implements Serializable {

    // Reminder if this is updated update the DataUtils class

    public String name;
    public Texture texture = null;
    public Vector2f position = new Vector2f();

    public GameObject(String name) {
        this.name = name;
    }

    public GameObject copy() {
        GameObject gameObject = new GameObject(name);

        gameObject.texture = texture;
        gameObject.position = position;

        return gameObject;
    }

}
