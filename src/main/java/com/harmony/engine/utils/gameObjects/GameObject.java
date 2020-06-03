package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.textures.Texture;

import java.io.Serializable;

public class GameObject implements Serializable {

    public String name;
    public Texture texture = null;
    public Vector2f position = new Vector2f();

    public GameObject(String name) {
        this.name = name;
    }

}
