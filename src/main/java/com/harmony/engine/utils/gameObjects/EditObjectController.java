/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.EngineController;
import com.harmony.engine.io.editor.state.StateEditor;
import com.harmony.engine.io.tabs.GameObjectsTab;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class EditObjectController {

    private GameObject gameObject;

    // General
    public TextField name;
    public TextField xPos, yPos;
    public TextField texture;

    public Button textureButton;

    @FXML
    public void initialize() {
        if(GameObjectUtils.staticGameObject != null) this.gameObject = GameObjectUtils.staticGameObject;
        else {
            if(GameObjectUtils.staticStage != null) GameObjectUtils.staticStage.close();
            return;
        }

        Runnable input = this::handleInput;

        assignFields();

        input.run();
    }

    private void assignFields() {
        name.setText(gameObject.name);
        xPos.setText(Float.toString(gameObject.position.x));
        yPos.setText(Float.toString(gameObject.position.y));
        texture.setText(gameObject.texture.getPath());
    }

    private void handleInput() {
        name.textProperty().addListener(observable -> {
            if(!name.getText().isEmpty()){
                gameObject.name = name.getText();
                GameObjectsTab.synchronize.run();
                StateEditor.updateObject(gameObject);
            }
        });

        xPos.textProperty().addListener(observable -> {
            try {
                gameObject.position.x = Float.parseFloat(xPos.getText());
                if(EngineController.staticTabBar.getSelectionModel().getSelectedItem() == EngineController.staticEditorTab)
                    StateEditor.draw();
            }
            catch (Exception ignored) {}
        });

        yPos.textProperty().addListener(observable -> {
            try {
                gameObject.position.y = Float.parseFloat(yPos.getText());
                if(EngineController.staticTabBar.getSelectionModel().getSelectedItem() == EngineController.staticEditorTab)
                    StateEditor.draw();
            }
            catch (Exception ignored) {}
        });

        texture.textProperty().addListener(observable -> {
            Image image = EngineController.loadTexturesImage(texture.getText());

            if(image != null) {
                texture.setStyle("-fx-text-fill: -fx-default-text-fill");
                gameObject.texture.setPath(texture.getText(), image);
            } else {
                texture.setStyle("-fx-text-fill: #ff0000");
            }
        });

        textureButton.setOnMouseClicked(mouseEvent -> TextureUtils.chooseTextureForGameObject(gameObject, this));
    }
}
