package com.harmony.engine.io;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.util.event.MouseControlUtil;

import java.util.HashMap;
import java.util.Map;

public class Editor {

    public static Vector2f editorCamera = new Vector2f();

    private static Canvas canvas;
    private static AnchorPane editorPane;
    private static GridPane objectsPane;

    private static HashMap<Integer, GameObject> gameObjects = new HashMap<>();

    private static Vector2f mousePosition = new Vector2f();

    private static double deltaScale = 1;
    private static int selectedObject = -1;

    public Editor(Canvas canvas, AnchorPane editorPane, GridPane objectsPane) {
        Editor.canvas = canvas;
        Editor.editorPane = editorPane;
        Editor.objectsPane = objectsPane;

        canvas.widthProperty().bind(editorPane.widthProperty());
        canvas.heightProperty().bind(editorPane.heightProperty());

        canvas.widthProperty().addListener(event -> update());
        canvas.heightProperty().addListener(event -> update());

        objectsPane.setHgap(5);
        objectsPane.setVgap(5);

        handleInput();

    }

    public static void update() {
        for(int i = 0; i < ProjectData.gameObjects.size(); i++) {
            ImageView texture = new ImageView(EngineController.loadTexturesImage(ProjectData.gameObjects.get(i).texture.path));

            texture.setFitHeight(50);
            texture.setFitWidth(50);

            objectsPane.add(texture, i, 0);

            int finalI = i;
            texture.setOnDragDetected(event -> {
                Dragboard db = texture.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(ProjectData.gameObjects.get(finalI).name);
                content.putImage(EngineController.loadTexturesImage(ProjectData.gameObjects.get(finalI).texture.path));
                db.setContent(content);

                event.consume();
            });
        }

        draw();
    }

    public static void draw() {
        if(canvas == null) return;

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        if(deltaScale != 1) {
            g.moveTo((int) mousePosition.x, (int) mousePosition.y);
            g.scale(deltaScale, deltaScale);
        }

        for (Map.Entry<Integer, GameObject> integerGameObjectEntry : gameObjects.entrySet()) {
            GameObject object = integerGameObjectEntry.getValue();

            if (object.texture == null) continue;

            g.drawImage(EngineController.loadTexturesImage(object.texture.path),
                    editorCamera.x + object.position.x, editorCamera.y + object.position.y);
        }

        deltaScale = 1;
    }

    private void handleInput() {
        canvas.setOnScroll(scrollEvent -> {
            deltaScale += scrollEvent.getDeltaY() * 0.002;
            Editor.draw();
        });

        canvas.setOnMouseMoved(mouseEvent -> {
            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);
        });
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
