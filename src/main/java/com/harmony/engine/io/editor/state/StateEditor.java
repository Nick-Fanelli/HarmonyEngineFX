/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.editor.state;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.SelectionModel;
import com.harmony.engine.io.context.HierarchyItemContext;
import com.harmony.engine.io.tabs.GameObjectsTab;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import com.harmony.engine.utils.math.Vector2f;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StateEditor implements Runnable {

    private static Thread editorThread;

    public static Runnable load;
    public static Runnable choose;

    private static Canvas canvas;
    private static AnchorPane editorPane;
    private static GridPane objectsPane;
    private static TreeView<String> hierarchy;

    private static AnchorPane statePane;
    private static AnchorPane interactablePane;
    private static ListView<String> statesList;
    private final static HashMap<String, State> statesHashMap = new HashMap<>();

    private static Button openStateButton;
    private static Button newStateButton;
    private static Button deleteStateButton;
    private static Button backButton;

    private static Button zoom50;
    private static Button zoom100;
    private static Button zoom150;
    private static Button zoom200;

    private final static Vector2f editorCamera = new Vector2f();
    private final static Vector2f mousePosition = new Vector2f();

    private static HashMap<TreeItem<String>, GameObject> gameObjects = new HashMap<>();
    private static HashMap<GameObject, ImageView> images = new HashMap<>();

    private static GraphicsContext g;
    private static GameObject copiedGameObject;

    private static State activeState;
    private static final SelectionModel<TreeItem<String>> selectionModel = new SelectionModel();
    private static TreeItem<String> root;

    private static float globalScale = 1f;

    public static boolean interactingWithCanvas = false;

    // Preferences

    public StateEditor(Canvas canvas, AnchorPane editorPane, GridPane objectsPane, TreeView<String> hierarchy,
                       ListView<String> statesList, Button openStateButton, Button newStateButton, Button deleteStateButton,
                       AnchorPane statePane, AnchorPane interactablePane, Button backButton, Button zoom50, Button zoom100,
                       Button zoom150, Button zoom200) {
        StateEditor.canvas = canvas;
        StateEditor.editorPane = editorPane;
        StateEditor.objectsPane = objectsPane;
        StateEditor.hierarchy = hierarchy;
        StateEditor.statesList = statesList;
        StateEditor.openStateButton = openStateButton;
        StateEditor.newStateButton = newStateButton;
        StateEditor.deleteStateButton = deleteStateButton;
        StateEditor.statePane = statePane;
        StateEditor.interactablePane = interactablePane;
        StateEditor.backButton = backButton;
        StateEditor.zoom50 = zoom50;
        StateEditor.zoom100 = zoom100;
        StateEditor.zoom150 = zoom150;
        StateEditor.zoom200 = zoom200;

        if(editorThread != null) return;

        editorThread = new Thread(this, "Harmony:StateEditor");
        editorThread.start();
    }


    @Override
    public void run() {
       choose = this::chooseActiveState;
       load = this::loadActiveState;

       choose.run();
    }

    private void chooseActiveState() {
        StateEditor.save();
        activeState = null;

        interactablePane.setVisible(false);
        statePane.setVisible(true);

        statesList.getItems().clear();
        statesHashMap.clear();

        for(State state : ProjectData.states) {
            statesList.getItems().add(state.name);
            statesHashMap.put(state.name, state);
        }

        openStateButton.setOnMouseClicked(mouseEvent -> {
            activeState = statesHashMap.get(statesList.getSelectionModel().getSelectedItem());
            load.run();
        });

        newStateButton.setOnMouseClicked(mouseEvent ->  StateUtils.createState());

        deleteStateButton.setOnMouseClicked(mouseEvent -> {
            State selectedState = statesHashMap.get(statesList.getSelectionModel().getSelectedItem());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Confirm State Deletion");
            alert.setContentText("Are you sure you want to delete the \"" + selectedState.name + "\" state?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                ProjectData.states.remove(selectedState);

                statesHashMap.remove(statesList.getSelectionModel().getSelectedItem());
                statesList.getItems().remove(statesList.getSelectionModel().getSelectedItem());

                if (GlobalData.getAutoSave()) Harmony.save();
            }
        });
    }

    private void loadActiveState() {
        statePane.setVisible(false);
        interactablePane.setVisible(true);

        if(activeState == null) {
            choose.run();
            return;
        }

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

        gameObjects.clear();

        root = new TreeItem<>(activeState.name);

        StateEditor.update();
        editorManager.run();
        hierarchyManager.run();
    }

    private void initializeHierarchy() {
        hierarchy.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        hierarchy.setEditable(true);
        hierarchy.setFocusTraversable(false);

        root.setExpanded(true);

        hierarchy.setRoot(root);

        hierarchy.setContextMenu(new HierarchyItemContext());

        for (GameObject object : activeState.gameObjects) addGameObject(object);
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

            StateEditor.draw();
        });

        backButton.setOnMouseClicked(mouseEvent -> choose.run());
    }

    private TreeItem<String> clearItem = null;
    private boolean shouldClear = false;

    private static GameObject draggedObject = null;
    private static boolean draggingSelected = false;

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
                Image image = copiedGameObject.texture.getImage();
                copiedGameObject.position = editorCamera.copy().inverse().add((float) dragEvent.getX() - (float) image.getWidth() / 2,
                        (float) dragEvent.getY() - (float) image.getHeight() / 2);

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

        canvas.setOnMouseEntered(mouseEvent -> interactingWithCanvas = true);

        canvas.setOnMouseExited(mouseEvent -> {
            Status.setMousePosition(null);
            interactingWithCanvas = false;
        });

        canvas.setOnMousePressed(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                for(int i = 0; i < root.getChildren().size(); i++) {
                    GameObject object = gameObjects.get(root.getChildren().get(i));

                    Image image = images.get(object).getImage();
                    if(image == null) continue;

                    // Translate Position
                    Vector2f position = worldToScreen(object.position.copy());

                    // Check to make sure the game object is in bounds.
                    if(position.x > canvas.getWidth() || position.y > canvas.getHeight() ||
                            position.x + image.getWidth() * globalScale < 0 || position.y + image.getHeight() * globalScale < 0)
                        return;

                    if(position.x <= mousePosition.x &&
                            position.y <= mousePosition.y &&
                            position.x + image.getWidth() * globalScale >= mousePosition.x &&
                            position.y + image.getHeight() * globalScale >= mousePosition.y) {

                        if(!Harmony.shiftDown) {
                            shouldClear = true;
                            clearItem = root.getChildren().get(i);
                        } else {
                            shouldClear = false;
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

                        StateEditor.draw();

                        return;
                    }
                }

                if(!Harmony.shiftDown) {
                    selectionModel.clear();
                    hierarchy.getSelectionModel().clearSelection();
                    shouldClear = false;
                }

                StateEditor.draw();
            } else if(mouseEvent.getButton() == MouseButton.MIDDLE)
                Harmony.triggerHand(true);
        });

        canvas.setOnMouseReleased(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.MIDDLE) Harmony.triggerHand(false);
            else if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if(shouldClear) {
                    hierarchy.getSelectionModel().clearSelection();
                    hierarchy.getSelectionModel().select(clearItem);
                    selectionModel.setSelection(clearItem);
                }
            }

            if(draggedObject != null) {
                draggedObject = null;
                StateEditor.draw();
            }

            draggingSelected = false;
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            shouldClear = false;
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if(Harmony.altDown) {
                    editorCamera.add((float) (mouseEvent.getX() - mousePosition.x) * (float) GlobalData.getPanMultipler(),
                            (float) (mouseEvent.getY() - mousePosition.y) * (float) GlobalData.getPanMultipler());
                } else {
                    GameObject selectedObject = null;

                    for (int i = 0; i < root.getChildren().size(); i++) {
                        GameObject object = gameObjects.get(root.getChildren().get(i));
                        if (object == null) continue;

                        if(draggedObject != null && object != draggedObject) continue;

                        Image image = images.get(object).getImage();
                        if (image == null) continue;

                        // Translate Position
                        Vector2f position = worldToScreen(object.position.copy());

                        // Check to make sure the game object is in bounds.
                        if(position.x > canvas.getWidth() || position.y > canvas.getHeight() ||
                                position.x + image.getWidth() * globalScale < 0 || position.y + image.getHeight() * globalScale < 0)
                            return;

                        if(position.x <= mousePosition.x &&
                                position.y <= mousePosition.y &&
                                position.x + image.getWidth() * globalScale >= mousePosition.x &&
                                position.y + image.getHeight() * globalScale >= mousePosition.y) {
                            selectedObject = object;

                            boolean shouldContinue = false;

                            for(TreeItem<String> selection : selectionModel.model) {
                                if(gameObjects.get(selection) == selectedObject) {
                                    draggingSelected = true;
                                    break;
                                } else if(draggingSelected && gameObjects.get(selection) != selectedObject) {
                                    shouldContinue = true;
                                }
                            }

                            if(shouldContinue) continue;
                            break;
                        }
                    }

                    if(draggingSelected) {
                        for(TreeItem<String> item : selectionModel.model) {
                            GameObject object = gameObjects.get(item);

                            Vector2f mouseBefore = screenToWorld(mousePosition.copy());
                            Vector2f mouseAfter  = screenToWorld(new Vector2f((float) mouseEvent.getX(), (float) mouseEvent.getY()));

                            object.position.x = mouseAfter.x - (mouseBefore.x - object.position.x);
                            object.position.y = mouseAfter.y - (mouseBefore.y - object.position.y);
                        }
                    } else {
                        if(selectedObject != null && (draggedObject == null || draggedObject == selectedObject)) {

                            Vector2f mouseBefore = screenToWorld(mousePosition.copy());
                            Vector2f mouseAfter  = screenToWorld(new Vector2f((float) mouseEvent.getX(), (float) mouseEvent.getY()));

                            selectedObject.position.x = mouseAfter.x - (mouseBefore.x - selectedObject.position.x);
                            selectedObject.position.y = mouseAfter.y - (mouseBefore.y - selectedObject.position.y);

                            draggedObject = selectedObject;
                        }
                    }

                    StateEditor.draw();
                }
            } else if(mouseEvent.getButton() == MouseButton.MIDDLE) {

                Vector2f mouseBefore = screenToWorld(mousePosition.copy());
                Vector2f mouseAfter  = screenToWorld(new Vector2f((float) mouseEvent.getX(), (float) mouseEvent.getY()));

                editorCamera.add((mouseAfter.x - mouseBefore.x) * (float) GlobalData.getPanMultipler(),
                        (mouseAfter.y - mouseBefore.y) * (float) GlobalData.getPanMultipler());
            }

            mousePosition.set((float) mouseEvent.getX(), (float) mouseEvent.getY());
            Status.setMousePosition(mousePosition);
            StateEditor.draw();
        });

        canvas.setOnScroll(scrollEvent -> {
            Vector2f mouseBeforeZoom = screenToWorld(mousePosition.copy());

            if(scrollEvent.getDeltaY() > 0) {
                globalScale *= 1.1f;
            } else if(scrollEvent.getDeltaY() < 0) {
                globalScale *= 0.9f;
            }

            Vector2f mouseAfterZoom = screenToWorld(mousePosition.copy());
            editorCamera.add(mouseAfterZoom.sub(mouseBeforeZoom));

            StateEditor.draw();

            if(globalScale == 0.5) zoomButton(zoom50, 0.5, false);
            if(globalScale == 1.0) zoomButton(zoom50, 1.0, false);
            if(globalScale == 1.5) zoomButton(zoom50, 1.5, false);
            if(globalScale == 2.0) zoomButton(zoom50, 2.0, false);
            else {
                zoom50.getStyleClass().remove("selected");
                zoom100.getStyleClass().remove("selected");
                zoom150.getStyleClass().remove("selected");
                zoom200.getStyleClass().remove("selected");
            }
        });

        zoom50.setOnMouseClicked (mouseEvent -> zoomButton(zoom50,  0.5, true));
        zoom100.setOnMouseClicked(mouseEvent -> zoomButton(zoom100, 1.0, true));
        zoom150.setOnMouseClicked(mouseEvent -> zoomButton(zoom150, 1.5, true));
        zoom200.setOnMouseClicked(mouseEvent -> zoomButton(zoom200, 2.0, true));
    }

    private void zoomButton(Button button, double percent, boolean completeZoom) {
        zoom50.getStyleClass().remove("selected");
        zoom100.getStyleClass().remove("selected");
        zoom150.getStyleClass().remove("selected");
        zoom200.getStyleClass().remove("selected");

        button.getStyleClass().add("selected");

        if(completeZoom) globalScale = (float) percent;

        StateEditor.draw();
    }

    public static void update() {
        objectsPane.getChildren().removeAll(objectsPane.getChildren());

        for(int i = 0; i < ProjectData.gameObjects.size(); i++) {
            ImageView texture = new ImageView(ProjectData.gameObjects.get(i).texture.getImage());

            texture.setFitHeight(50);
            texture.setFitWidth(50);

            objectsPane.add(texture, i, 0);

            int finalI = i;
            texture.setOnDragDetected(event -> {
                Dragboard db = texture.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                copiedGameObject = ProjectData.gameObjects.get(finalI).copy();
                content.putString("GameObject:" + ProjectData.gameObjects.get(finalI));

                content.putImage(copiedGameObject.texture.getImage());
                db.setContent(content);

                event.consume();
            });
        }

        draw();
    }

    public static void draw() {
        if(canvas == null || root == null) return;

        if(globalScale < 0) globalScale = 0; // TODO: Add a minimum zoom distance.

        double width  = canvas.getWidth();
        double height = canvas.getHeight();

        g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        try { g.setFill(Color.web(GlobalData.getEditorBackgroundColor())); }
        catch (Exception ignored) {}
        g.fillRect(0, 0, width, height);

        // Draw Guide Lines
//        drawGuides((int) width, (int) height);

        // Draw the game objects
        if(GlobalData.getEditorDrawFromTop())
            for(int i = 0; i < root.getChildren().size(); i++) drawGameObject(gameObjects.get(root.getChildren().get(i)));
        else
            for(int i = root.getChildren().size() - 1; i >= 0; i--) drawGameObject(gameObjects.get(root.getChildren().get(i)));

        // Draw the selection boxes
        for(int i = 0; i < selectionModel.model.size(); i++) {
            drawBounds(gameObjects.get(selectionModel.model.get(i)), width, height);
        }

        if(draggedObject != null) drawBounds(draggedObject, width, height);
    }

    private static void drawBounds(GameObject object, double width, double height) {
        try {
            if (object == null) return;

            Image image = images.get(object).getImage();
            if (image == null) return;

            // Translate Position
            Vector2f position = worldToScreen(object.position.copy());

            // Check to make sure the game object is in bounds.
            if(position.x > canvas.getWidth() || position.y > canvas.getHeight() ||
                    position.x + image.getWidth() * globalScale < 0 || position.y + image.getHeight() * globalScale < 0)
                return;

            try {
                g.setStroke(Color.web(GlobalData.getEditorOutlineColor()));
            } catch (Exception ignored) {}
            g.strokeRect(position.x, position.y, image.getWidth() * globalScale, image.getHeight() * globalScale);
        } catch (Exception ignored) {}
    }

    private static void drawGameObject(GameObject object) {
        Image image = images.get(object).getImage();
        if(image == null) return;

        // Translate Position
        Vector2f position = worldToScreen(object.position.copy());

        // Check to make sure the game object is in bounds.
        if(position.x > canvas.getWidth() || position.y > canvas.getHeight() ||
                position.x + image.getWidth() * globalScale < 0 || position.y + image.getHeight() * globalScale < 0)
            return;

        g.drawImage(image, position.x, position.y, image.getWidth() * globalScale, image.getHeight() * globalScale);
    }

    // Screen To World and World to Screen Translations
    public static Vector2f worldToScreen(Vector2f position) {
        Vector2f vector2f = new Vector2f();

        vector2f.x = (position.x + editorCamera.x) * globalScale;
        vector2f.y = (position.y + editorCamera.y) * globalScale;

        return vector2f;
    }

    public static Vector2f screenToWorld(Vector2f screen) {
        Vector2f vector2f = new Vector2f();

        vector2f.x = (screen.x) / globalScale - editorCamera.x;
        vector2f.y = (screen.y) / globalScale - editorCamera.y;

        return vector2f;
    }

    // Game Object Methods
    public static void addGameObject(GameObject gameObject) {
        TreeItem<String> key = new TreeItem<>(gameObject.name);
        gameObjects.put(key, gameObject);
        root.getChildren().add(key);

        if(gameObject.texture != null) {
            Image image = gameObject.texture.getImage();
            if(image != null) images.put(gameObject, new ImageView(image));
        }

        selectionModel.setSelection(key);
        hierarchy.getSelectionModel().clearSelection();
        hierarchy.getSelectionModel().select(key);

        StateEditor.draw();
    }

    public static void removeGameObject(TreeItem<String> pointer) {
        GameObject object = gameObjects.get(pointer);

        root.getChildren().remove(pointer);
        gameObjects.remove(pointer);

        images.remove(object);
        activeState.gameObjects.remove(object);

        StateEditor.draw();
    }

    public static void save() {
        if(activeState == null) return;

        activeState.gameObjects.clear();

        for(Map.Entry<TreeItem<String>, GameObject> entry : gameObjects.entrySet()) {
            activeState.gameObjects.add(entry.getValue());
        }
    }

    // Utility Methods
    public static void deleteSelectedGameObjects() {
        TreeItem<String>[] array = selectionModel.model.toArray(new TreeItem[0]);
        selectionModel.clear();

        for(TreeItem<String> item : array) removeGameObject(item);

        selectionModel.setSelection(hierarchy.getSelectionModel().getSelectedItem());
    }

    public static void findSelectedGameObject() {
        if(selectionModel.model.size() <= 0) return;
        TreeItem<String> key = selectionModel.model.get(0);

        hierarchy.getSelectionModel().select(key);

        GameObject object = gameObjects.get(key);
        editorCamera.set(object.position.copy().inverse());

        StateEditor.draw();
    }

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

        StateEditor.draw();
    }

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

        StateEditor.draw();
    }

    public enum Position { UP, DOWN, LEFT, RIGHT }

    public static HashMap<TreeItem<String>, GameObject> getHierarchy() { return gameObjects; }

    public static void openSelectedGameObject() {
        GameObject object = gameObjects.get(hierarchy.getSelectionModel().getSelectedItem());
        if(object != null) GameObjectsTab.editGameObject(object);
    }

    public static void updateObject(GameObject gameObject) {
        for(Map.Entry<TreeItem<String>, GameObject> entry : gameObjects.entrySet()) {
            if(entry.getValue() == gameObject) {
                entry.getKey().setValue(gameObject.name);
                return;
            }
        }
    }
}