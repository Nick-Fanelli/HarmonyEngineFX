package com.harmony.engine.io;

import com.harmony.engine.EngineController;
import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class Editor {

    public static Vector2f editorCamera = new Vector2f();

    private static Canvas canvas;
    private static HashMap<Integer, GameObject> gameObjects = new HashMap<>();

    public Editor(Canvas canvas, AnchorPane pane) {
        Editor.canvas = canvas;

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        canvas.widthProperty().addListener(event -> draw());
        canvas.heightProperty().addListener(event -> draw());
    }

    public static void draw() {
        if(canvas == null) return;

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        for (Map.Entry<Integer, GameObject> integerGameObjectEntry : gameObjects.entrySet()) {
            GameObject object = integerGameObjectEntry.getValue();

            if (object.texture == null) continue;

            g.drawImage(EngineController.loadTexturesImage(object.texture.path),
                    editorCamera.x + object.position.x, editorCamera.y + object.position.y);
        }
    }

    public static void addGameObject(int index, GameObject gameObject) {
        gameObjects.put(index, gameObject);
        Editor.draw();
    }

    public static void removeGameObject(int index) {
        gameObjects.remove(index);
    }

    public static GameObject getGameObject(int index) {
        return gameObjects.get(index);
    }
}
