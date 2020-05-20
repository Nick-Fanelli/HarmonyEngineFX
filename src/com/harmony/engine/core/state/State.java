package com.harmony.engine.core.state;

import com.harmony.engine.physics.gameobject.GameObject;

import java.awt.*;
import java.util.ArrayList;

public abstract class State {

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void onCreate() {}

    public void update() {
        for(GameObject gameObject : gameObjects) { gameObject.update(); }
    }

    public void render(Graphics2D g) {
        for(GameObject gameObject : gameObjects) { gameObject.render(g); }
    }

    public void onDestroy() {}

    public void instantiateGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.onCreate();
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        gameObject.onDestroy();
    }
}
