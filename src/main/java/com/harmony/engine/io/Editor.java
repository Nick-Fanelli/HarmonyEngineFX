package com.harmony.engine.io;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Editor implements Runnable {

    public static final float MINIMUM_ZOOM_DISTANCE = 0.01f;

    public static Vector2f editorCamera = new Vector2f();

    private static Thread editorThread;
    private static Runnable editorManager;
    private static Runnable hierarchyManager;

    private static Canvas canvas;
    private static AnchorPane editorPane;
    private static GridPane objectsPane;
    private static TreeView<String> hierarchy;

    private static TreeItem<String> root;

    private final static HashMap<TreeItem<String>, GameObject> gameObjects = new HashMap<>();

    private final static Vector2f mousePosition = new Vector2f();

    private static GameObject selectedObject = null;

    private static GraphicsContext g;

    public Editor(Canvas canvas, AnchorPane editorPane, GridPane objectsPane, TreeView<String> hierarchy) {
        Editor.canvas = canvas;
        Editor.editorPane = editorPane;
        Editor.objectsPane = objectsPane;
        Editor.hierarchy = hierarchy;

        editorManager = this::handleInput;
        hierarchyManager = this::initializeHierarchy;

        if(editorThread != null) return;

        editorThread = new Thread(this, "Harmony:EditorThread");
        editorThread.start();
    }

    @Override
    public void run() {
        canvas.widthProperty().bind(editorPane.widthProperty());
        canvas.heightProperty().bind(editorPane.heightProperty());

        canvas.widthProperty().addListener(event -> update());
        canvas.heightProperty().addListener(event -> update());

        g = canvas.getGraphicsContext2D();

        objectsPane.setHgap(5);
        objectsPane.setVgap(5);

        editorManager.run();
        hierarchyManager.run();

        hierarchy.setOnDragOver(event -> {
            if(event.getGestureSource() != hierarchy && copiedGameObject != null) {
                if(hierarchy.getSelectionModel().getSelectedIndex() >= 0) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                } else {
                    event.acceptTransferModes(TransferMode.NONE);
                }

            }
            event.consume();
        });

        canvas.setOnDragOver(event -> {
            if(event.getGestureSource() != canvas && copiedGameObject != null) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        });

        hierarchy.setOnDragDropped(event -> {
            if(hierarchy.getSelectionModel().getSelectedIndex() < 0) return;
            boolean success = false;

            if (copiedGameObject != null) {
                addObjectToSelectedIndex(copiedGameObject);
                copiedGameObject = null;
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });

        canvas.setOnDragDropped(event -> {
            boolean success = false;

            if(copiedGameObject != null) {
                copiedGameObject.position.set(mousePosition.x + editorCamera.x, mousePosition.y + editorCamera.y);
                addObjectToSelectedIndex(copiedGameObject);
                copiedGameObject = null;
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private static GameObject copiedGameObject = null;

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
                copiedGameObject = ProjectData.gameObjects.get(finalI);
                content.putString("GameObject:" + ProjectData.gameObjects.get(finalI));
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

        g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        for(Map.Entry<TreeItem<String>, GameObject> item : gameObjects.entrySet()) {
            if(item.getValue().texture == null) continue;

            Image image = EngineController.loadTexturesImage(item.getValue().texture.path);
            if(image == null) continue;

            g.drawImage(image, editorCamera.x + item.getValue().position.x, editorCamera.y +
                            item.getValue().position.y, image.getWidth(), image.getHeight());
        }

        if(selectedObject != null) {
            Image image = EngineController.loadTexturesImage(selectedObject.texture.path);

            if(image == null) return;

            g.setStroke(Color.BLUE);
            g.strokeRect(selectedObject.position.x + editorCamera.x, selectedObject.position.y + editorCamera.y,
                    image.getWidth(), image.getHeight());
        }
    }

    private float xDiff = 0;
    private float yDiff = 0;

    private void handleInput() {
        canvas.setOnScroll(scrollEvent -> {
            // TODO: Add in the zoom again
//            scale = Math.max(scale + scrollEvent.getDeltaY() * 0.002, MINIMUM_ZOOM_DISTANCE);
//            Editor.draw();
        });

        canvas.setOnMouseMoved(mouseEvent -> {
            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.MIDDLE) {
                editorCamera.add((float) mouseEvent.getX() - mousePosition.x, (float) mouseEvent.getY() - mousePosition.y);
                Editor.draw();
            }

            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);

           if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (selectedObject == null) return;

                Image image = EngineController.loadTexturesImage(selectedObject.texture.path);
                if(image == null) return;

                if(mousePosition.x >= selectedObject.position.x + editorCamera.x)
                    if(mousePosition.y >= selectedObject.position.y + editorCamera.y)
                        if(mousePosition.x <= selectedObject.position.x + editorCamera.x + image.getWidth())
                            if(mousePosition.y <= selectedObject.position.y + editorCamera.y + image.getHeight()) {
                                selectedObject.position.x = mousePosition.x - xDiff;
                                selectedObject.position.y = mousePosition.y - yDiff;
                                Editor.draw();
                            }

            }
        });

        canvas.setOnMouseExited(mouseEvent -> Status.setMousePosition(null));

        canvas.setOnMousePressed(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                for(Map.Entry<TreeItem<String>, GameObject> item : gameObjects.entrySet()) {
                    if(item.getValue().texture == null) continue;

                    Image image = EngineController.loadTexturesImage(item.getValue().texture.path);
                    if(image == null) continue;

                    if(item.getValue().position.x + editorCamera.x > canvas.getWidth() ||
                       item.getValue().position.y + editorCamera.y > canvas.getHeight()) continue;

                    if(mousePosition.x >= item.getValue().position.x + editorCamera.x)
                        if(mousePosition.y >= item.getValue().position.y + editorCamera.y)
                            if(mousePosition.x <= item.getValue().position.x + editorCamera.x + image.getWidth())
                                if(mousePosition.y <= item.getValue().position.y + editorCamera.y + image.getHeight()) {
                                    selectedObject = item.getValue();

                                    xDiff = mousePosition.x - selectedObject.position.x;
                                    yDiff = mousePosition.y - selectedObject.position.y;

                                    Editor.draw();
                                    return;
                                }

                }

                selectedObject = null;
                Editor.draw();
            }
        });

        hierarchy.getSelectionModel().selectedItemProperty().addListener((observableValue, selectionMode, t1) -> {
            if(t1 == root) selectedObject = null;
            else selectedObject = gameObjects.get(t1);

            Editor.draw();
        });
    }

    private void initializeHierarchy() {
        root = new TreeItem<>();
        root.setValue(ProjectData.projectName);

        hierarchy.setShowRoot(true);
        hierarchy.setRoot(root);
    }

    public static void addObjectToSelectedIndex(GameObject gameObject) {
        gameObject.parent = getGameObject(hierarchy.getSelectionModel().getSelectedItem());
        addGameObject(hierarchy.getSelectionModel().getSelectedItem(), new TreeItem<>(gameObject.name), gameObject);
    }

    public static void addGameObject(TreeItem<String> parent, TreeItem<String> pointer, GameObject gameObject) {
        if(parent != null)
            parent.getChildren().add(pointer);
        else
            root.getChildren().add(pointer);

        gameObjects.put(pointer, gameObject);

        if (gameObject.texture != null) {
            g.drawImage(EngineController.loadTexturesImage(gameObject.texture.path), editorCamera.x
                    + gameObject.position.x, editorCamera.y + gameObject.position.y);
        }

        Editor.draw();
    }

    public static void removeGameObject(TreeItem<String> pointer) {
        gameObjects.remove(pointer);
    }

    public static GameObject getGameObject(TreeItem<String> pointer) {
        return gameObjects.get(pointer);
    }
}
