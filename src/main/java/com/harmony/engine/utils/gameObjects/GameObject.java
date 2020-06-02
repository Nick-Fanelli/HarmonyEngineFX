package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.utils.textures.Texture;

import java.io.Serializable;

public class GameObject implements Serializable {

    public String name;
    public Texture texture = null;

    public GameObject(String name) {
        this.name = name;
    }

}
