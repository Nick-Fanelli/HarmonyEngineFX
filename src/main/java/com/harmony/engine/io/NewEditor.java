package com.harmony.engine.io;

import com.harmony.engine.EngineController;
import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.hierarchy.HierarchyItemContext;
import com.harmony.engine.math.Vector2f;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;

public class NewEditor implements Runnable {

    public static final int CANVAS_POSITION_OFFSET = 111;

    private static Thread editorThread;

    private static Canvas canvas;
    private static AnchorPane editorPane;
    private static GridPane objectsPane;
    private static TreeView<String> hierarchy;

    private final static Vector2f editorCamera = new Vector2f();
    private final static Vector2f mousePosition = new Vector2f();

    private static HashMap<TreeItem<String>, GameObject> gameObjects = new HashMap<>();
    private static HashMap<GameObject, ImageView> images = new HashMap<>();

    private static GraphicsContext g;
    private static GameObject copiedGameObject;

    private static final SelectionModel selectionModel = new SelectionModel();
    private static TreeItem<String> root;

    // Preferences

    public NewEditor(Canvas canvas, AnchorPane editorPane, GridPane objectsPane, TreeView<String> hierarchy) {
        NewEditor.canvas = canvas;
        NewEditor.editorPane = editorPane;
        NewEditor.objectsPane = objectsPane;
        NewEditor.hierarchy = hierarchy;

        if(editorThread != null) return;

        editorThread = new Thread(this, "Harmony:EditorThread");
        editorThread.start();
    }

    @Override
    public void run() {
        // Handle Sub-Threads
        Runnable editorManager = this::handleInput;
        Runnable hierarchyManager = this::initializeHierarchy;

        canvas.widthProperty().bind(editorPane.widthProperty());
        canvas.heightProperty().bind(editorPane.heightProperty());

        canvas.widthProperty().addListener(event -> update());
        canvas.heightProperty().addListener(event -> update());

        g = canvas.getGraphicsContext2D();

        objectsPane.setHgap(5);
        objectsPane.setVgap(5);

        editorManager.run();
        hierarchyManager.run();
    }

    private void initializeHierarchy() {
        hierarchy.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        hierarchy.setEditable(true);

        root = new TreeItem<>(Harmony.directory.getName());
        root.setExpanded(true);

        hierarchy.setRoot(root);

        hierarchy.setContextMenu(new HierarchyItemContext());

        for (GameObject object : ProjectData.hierarchy) addGameObject(object);
    }

    private void handleInput() {
        this.handleHierarchyInput();
        this.handleCanvasInput();
    }

    private void handleHierarchyInput() {
        hierarchy.setOnDragOver(event -> {
            if(event.getGestureSource() != hierarchy && copiedGameObject != null) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        hierarchy.setOnDragDropped(dragEvent -> {
            boolean success = false;

            if(copiedGameObject != null) {
                addGameObject(copiedGameObject);

                copiedGameObject = null;
                success = true;
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });

        hierarchy.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {
            ObservableList<TreeItem<String>> list = hierarchy.getSelectionModel().getSelectedItems();

            selectionModel.clear();
            for(TreeItem<String> item : list) { if(item != root) selectionModel.addToSelection(item); }

            NewEditor.draw();
        });
    }

    private void handleCanvasInput() {
        canvas.setOnDragOver(dragEvent -> {
            if(dragEvent.getGestureSource() != canvas && copiedGameObject != null) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            dragEvent.consume();
        });

        canvas.setOnDragDropped(dragEvent -> {
            boolean success = false;

            if(copiedGameObject != null) {
                copiedGameObject.position = editorCamera.copy().add((float) dragEvent.getX() - CANVAS_POSITION_OFFSET,
                        (float) dragEvent.getY() - CANVAS_POSITION_OFFSET);

                addGameObject(copiedGameObject);

                copiedGameObject = null;
                success = true;
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });

        canvas.setOnMouseMoved(mouseEvent -> {
            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);
        });

        canvas.setOnMouseExited(mouseEvent -> Status.setMousePosition(null));

        canvas.setOnMousePressed(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                for(int i = 0; i < root.getChildren().size(); i++) {
                    GameObject object = gameObjects.get(root.getChildren().get(i));

                    Image image = images.get(object).getImage();
                    if(image == null) continue;

                    // Check to make sure the game object is in bounds.
                    if(object.position.x + editorCamera.x > canvas.getWidth() || object.position.y + editorCamera.y > canvas.getHeight() ||
                            object.position.x + image.getWidth() + editorCamera.x < 0 || object.position.y + image.getHeight() + editorCamera.y < 0)
                        continue;

                    if(object.position.x + editorCamera.x <= mousePosition.x &&
                       object.position.y + editorCamera.y <= mousePosition.y &&
                       object.position.x + editorCamera.x + image.getWidth() >= mousePosition.x &&
                       object.position.y + editorCamera.y + image.getHeight() >= mousePosition.y) {

                        if(!Harmony.shiftDown) {
                            hierarchy.getSelectionModel().clearSelection();
                            hierarchy.getSelectionModel().select(root.getChildren().get(i));
                            selectionModel.setSelection(root.getChildren().get(i));
                        } else {
                            if(!selectionModel.contains(root.getChildren().get(i))) {
                                hierarchy.getSelectionModel().select(root.getChildren().get(i));
                            } else {
                                ArrayList<TreeItem<String>> list = new ArrayList<>();

                                for(TreeItem<String> item : hierarchy.getSelectionModel().getSelectedItems()) {
                                    if(item == root.getChildren().get(i)) continue;
                                    list.add(item);
                                }

                                hierarchy.getSelectionModel().clearSelection();

                                for(TreeItem<String> item : list) { hierarchy.getSelectionModel().select(item); }
                            }
                        }

                        NewEditor.draw();

                        return;
                    }
                }

                if(!Harmony.shiftDown) {
                    selectionModel.clear();
                    hierarchy.getSelectionModel().clearSelection();
                }

                NewEditor.draw();
            }
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                // Fill-In
            } else if(mouseEvent.getButton() == MouseButton.MIDDLE) {
                editorCamera.add((float) (mouseEvent.getX() - mousePosition.x) * (float) GlobalData.getPanMultipler(),
                                 (float) (mouseEvent.getY() - mousePosition.y) * (float) GlobalData.getPanMultipler());
                NewEditor.draw();
            }

            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
        });
    }

    public static void update() {
        objectsPane.getChildren().removeAll(objectsPane.getChildren());

        for(int i = 0; i < ProjectData.gameObjects.size(); i++) {
            ImageView texture = new ImageView(EngineController.loadTexturesImage(ProjectData.gameObjects.get(i).texture.path));

            texture.setFitHeight(50);
            texture.setFitWidth(50);

            objectsPane.add(texture, i, 0);

            int finalI = i;
            texture.setOnDragDetected(event -> {
                Dragboard db = texture.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                copiedGameObject = ProjectData.gameObjects.get(finalI).copy();
                content.putString("GameObject:" + ProjectData.gameObjects.get(finalI));
                content.putImage(EngineController.loadTexturesImage(copiedGameObject.texture.path));
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

        g.setFill(Color.web(GlobalData.getEditorBackgroundColor()));
        g.fillRect(0, 0, width, height);

        // Draw the game objects
        if(GlobalData.getEditorDrawFromTop())
            for(int i = 0; i < root.getChildren().size(); i++) drawGameObject(gameObjects.get(root.getChildren().get(i)));
        else
            for(int i = root.getChildren().size() - 1; i >= 0; i--) drawGameObject(gameObjects.get(root.getChildren().get(i)));

        // Draw the selection boxes
        for(TreeItem<String> selection : selectionModel.model) {
            GameObject object = gameObjects.get(selection);

            if(object == null) continue;

            Image image = images.get(object).getImage();
            if(image == null) continue;

            // Check to make sure the game object is in bounds.
            if(object.position.x + editorCamera.x > width || object.position.y + editorCamera.y > height ||
                    object.position.x + image.getWidth() + editorCamera.x < 0 || object.position.y + image.getHeight() + editorCamera.y < 0)
                continue;

            g.setStroke(Color.web(GlobalData.getEditorOutlineColor()));
            g.strokeRect(object.position.x + editorCamera.x, object.position.y + editorCamera.y,
                    image.getWidth(), image.getHeight());
        }
    }

    private static void drawGameObject(GameObject object) {
        Image image = images.get(object).getImage();
        if(image == null) return;

        // Check to make sure the game object is in bounds.
        if(object.position.x + editorCamera.x > canvas.getWidth() || object.position.y + editorCamera.y > canvas.getHeight() ||
                object.position.x + image.getWidth() + editorCamera.x < 0 || object.position.y + image.getHeight() + editorCamera.y < 0)
            return;

        g.drawImage(image, object.position.x + editorCamera.x, object.position.y + editorCamera.y,
                image.getWidth(), image.getHeight());
    }

    // Game Object Methods
    public static void addGameObject(GameObject gameObject) {
        TreeItem<String> key = new TreeItem<>(gameObject.name);
        gameObjects.put(key, gameObject);
        root.getChildren().add(key);

        if(gameObject.texture != null) {
            Image image = EngineController.loadTexturesImage(gameObject.texture.path);
            if(image != null) images.put(gameObject, new ImageView(image));
        }

        selectionModel.setSelection(key);
        hierarchy.getSelectionModel().clearSelection();
        hierarchy.getSelectionModel().select(key);

        NewEditor.draw();
    }

    public static void removeGameObject(TreeItem<String> pointer) {
        GameObject object = gameObjects.get(pointer);
        images.remove(object);
        gameObjects.remove(pointer);
        root.getChildren().remove(pointer);
        NewEditor.draw();
    }

    // Utility Methods
    public static void deleteSelectedGameObjects() {
        TreeItem<String>[] array = selectionModel.model.toArray(new TreeItem[0]);
        selectionModel.clear();

        for(TreeItem<String> item : array) removeGameObject(item);
    }

    public static void findSelectedGameObject() {
        if(selectionModel.model.size() <= 0) return;
        TreeItem<String> key = selectionModel.model.get(0);

        hierarchy.getSelectionModel().select(key);

        GameObject object = gameObjects.get(key);
        editorCamera.set(object.position.copy().inverse());

        NewEditor.draw();
    }

    // TODO: update to use parent of game object
    public static void moveSelectedGameObjectDown() {
        TreeItem<String> key = hierarchy.getSelectionModel().getSelectedItem();
        if(key == null) return;

        int keyIndex = Integer.MIN_VALUE;

        for(int i = 0; i < root.getChildren().size(); i++) {
            if(root.getChildren().get(i) == key) {
                keyIndex = i;
                break;
            }
        }

        if(keyIndex == Integer.MIN_VALUE) return;
        if(root.getChildren().size() - 1 < keyIndex + 1) return;

        TreeItem<String> target = root.getChildren().get(keyIndex + 1);

        root.getChildren().set(keyIndex + 1, key);
        root.getChildren().set(keyIndex, target);

        hierarchy.getSelectionModel().clearSelection();
        hierarchy.getSelectionModel().select(key);

        NewEditor.draw();
    }

    //TODO: update to use parent of game object
    public static void moveSelectedGameObjectUp() {
        TreeItem<String> key = hierarchy.getSelectionModel().getSelectedItem();
        if(key == null) return;

        int keyIndex = Integer.MIN_VALUE;

        for(int i = 0; i < root.getChildren().size(); i++) {
            if(root.getChildren().get(i) == key) {
                keyIndex = i;
                break;
            }
        }

        if(keyIndex == Integer.MIN_VALUE) return;
        if(keyIndex - 1 < 0) return;

        TreeItem<String> target = root.getChildren().get(keyIndex - 1);

        root.getChildren().set(keyIndex - 1, key);
        root.getChildren().set(keyIndex, target);

        hierarchy.getSelectionModel().clearSelection();
        hierarchy.getSelectionModel().select(key);

        NewEditor.draw();
    }

    public static HashMap<TreeItem<String>, GameObject> getHierarchy() { return gameObjects; }
}
