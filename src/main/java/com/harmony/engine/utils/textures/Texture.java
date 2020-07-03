package com.harmony.engine.utils.textures;

import com.harmony.engine.EngineController;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Texture implements Serializable {

    private String path;
    public String name;
    public int id;

    private Image image = null;

    public Texture(String path, String name, int id) {
        this.path = path;
        this.name = name;
        this.id = id;
    }

    public Image getImage() {
        if(image == null) image = EngineController.loadTexturesImage(path);
        return image;
    }

    public Image getImage(int width, int height) {
        return EngineController.loadTexturesImage(path, width, height);
    }

    public void setPath(String path) {
        image = EngineController.loadTexturesImage(path);
        this.path = path;
    }

    public String getPath() { return path; }

}
