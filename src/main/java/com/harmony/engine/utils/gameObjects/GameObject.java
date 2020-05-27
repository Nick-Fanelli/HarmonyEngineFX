package com.harmony.engine.utils.gameObjects;

import java.io.Serializable;

public class GameObject implements Serializable {

    public String name;

    public GameObject(String name) {
        this.name = name;
    }

}
