package com.harmony.engine.utils.textures;

import java.io.Serializable;

public class Texture implements Serializable {

    public String path;
    public String name;
    public int id;

    public Texture(String path, String name, int id) {
        this.path = path;
        this.name = name;
        this.id = id;
    }

}
