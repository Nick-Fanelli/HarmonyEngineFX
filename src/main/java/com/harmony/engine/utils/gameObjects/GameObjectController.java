package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.tabs.GameObjectsTab;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class GameObjectController {

    public TextField nameField;
    public TextField textureField;
    public static TextField staticTextureField;

    public Button chooseTextureButton;

    public Button chooseButton;
    public Button cancelButton;

    private static Texture texture;

    private boolean control = false;
    private boolean enter = false;

    @FXML
    public void initialize() {
        staticTextureField = textureField;

        cancelButton.setOnMouseClicked(mouseEvent -> {
            Status.setCurrentStatus(Status.Type.READY);
            GameObjectUtils.staticStage.close();
        });

        chooseButton.setOnMouseClicked(mouseEvent -> {
            if(!(nameField.getText().isEmpty()) && texture != null) {
                GameObject bufferObject = new GameObject(nameField.getText().trim());
                bufferObject.texture = texture;

                ProjectData.gameObjects.add(bufferObject);

                GameObjectsTab.synchronize.run();
                Status.setCurrentStatus(Status.Type.READY);
                GameObjectUtils.staticStage.close();
            }
        });

        chooseTextureButton.setOnMouseClicked(mouseEvent -> TextureUtils.chooseTextureForGameObjectController());
    }

    public static void setTexture(Texture texture) {
        if(texture == null || staticTextureField == null) return;
        staticTextureField.setText(texture.name);
        GameObjectController.texture = texture;
    }

}
