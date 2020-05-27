package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GameObjectController {

    public TextField nameField;
    public Button chooseButton;
    public Button cancelButton;

    @FXML
    public void initialize() {
        cancelButton.setOnMouseClicked(mouseEvent -> {
            Status.setCurrentStatus(Status.Type.READY);
            GameObjectUtils.staticStage.close();
        });

        chooseButton.setOnMouseClicked(mouseEvent -> {
            if(!(nameField.getText().isEmpty())) {
                ProjectData.gameObjects.add(new GameObject(nameField.getText().trim()));
                EngineController.synchronizeGameObjects();
                Status.setCurrentStatus(Status.Type.READY);
                GameObjectUtils.staticStage.close();
            }
        });

    }

}
