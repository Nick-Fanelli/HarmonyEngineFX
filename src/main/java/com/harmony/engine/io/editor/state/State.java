package com.harmony.engine.io.editor.state;

import com.harmony.engine.utils.gameObjects.GameObject;

import java.util.ArrayList;

public class State {

    public String name;
    public ArrayList<GameObject> gameObjects;

    public State(String name, ArrayList<GameObject> gameObjects) {
        this.name = name;
        this.gameObjects = gameObjects;
    }

}
