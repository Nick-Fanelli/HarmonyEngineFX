/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.tabs;

import com.harmony.engine.data.ProjectData;
import com.harmony.core.GameObject;
import com.harmony.engine.utils.gameObjects.GameObjectUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;

public class GameObjectsTab {

    private static final HashMap<String, HBox> searchMap = new HashMap<>();

    public static Runnable synchronize;
    private static Runnable input;

    private static GridPane array;
    private static Button newButton;
    private static Button openButton;

    private static HBox hBoxSelected;
    private static GameObject selectedObject;

    public GameObjectsTab(GridPane array, Button newButton, Button openButton) {
        GameObjectsTab.array = array;
        GameObjectsTab.newButton = newButton;
        GameObjectsTab.openButton = openButton;

        synchronize = this::synchronize;
        input = this::handleInput;

        this.initialize();
    }

    private void initialize() {
        array.setHgap(20);
        array.setVgap(20);
        array.setPadding(new Insets(10));

        input.run();
        synchronize.run();
    }

    private void synchronize() {
        int x = 0;
        int y = 0;

        array.getChildren().clear();

        for(GameObject object : ProjectData.gameObjects) {
            Image image = object.texture.getImage(TexturesTab.IMAGE_SIZE, TexturesTab.IMAGE_SIZE);
            if(image == null) continue;

            ImageView imageView = new ImageView(image);
            Label label = new Label(object.name);
            label.getStyleClass().add("default-label");
            label.setWrapText(true);
            HBox.setMargin(label, new Insets(0, 0, 0, 10));

            HBox hBox = new HBox();
            hBox.getStyleClass().add("item-array");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(0, 0, 0, 20));

            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            hBox.setOnMouseClicked(mouseEvent -> {
                select(hBox, object);

                if(mouseEvent.getClickCount() == 2 && selectedObject != null) editGameObject(selectedObject);
            });

            searchMap.put(object.name.toLowerCase(), hBox);

            if(x > 2) {
                x = 0;
                y++;
            }

            array.add(hBox, x, y);

            x++;
        }
    }

    private void handleInput() {
        newButton.setOnMouseClicked(mouseEvent -> GameObjectUtils.createGameObject());

        openButton.setOnMouseClicked(mouseEvent -> {
            if(selectedObject != null) editGameObject(selectedObject);
        });
    }

    public static void select(HBox hBox, GameObject object) {
        if(hBoxSelected != null) hBoxSelected.getStyleClass().remove(1);
        (hBoxSelected = hBox).getStyleClass().add(1, "selected");
        selectedObject = object;
        openButton.setDisable(hBoxSelected == null || selectedObject == null);
    }

    public static void editGameObject(GameObject gameObject) {
        GameObjectUtils.editGameObject(gameObject);
    }
}
