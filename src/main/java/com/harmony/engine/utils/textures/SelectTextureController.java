package com.harmony.engine.utils.textures;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.gameObjects.GameObjectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Map;

public class SelectTextureController {

    public ListView<String> texturesList;
    public Button cancelButton;
    public Button chooseButton;

    public Label textureName;
    public ImageView texturePreview;

    private Texture selectedTexture = null;

    @FXML
    public void initialize() {
        // Handle the Cancel Button
        cancelButton.setOnMouseClicked(mouseEvent -> TextureUtils.close());

        // Handle the Choose Button
        chooseButton.setOnMouseClicked(mouseEvent -> {
            if(selectedTexture == null) return;

            if(TextureUtils.staticGameObject != null)
                TextureUtils.staticGameObject.texture = selectedTexture;
            else
                GameObjectController.setTexture(selectedTexture);

            EngineController.synchronizeGameObjects();
            TextureUtils.close();
        });

        // Fill out the textures list
        ArrayList<String> textureNames = new ArrayList<>();
        for(Texture texture : ProjectData.textures) { textureNames.add(texture.name); }
        ObservableList<String> textureData = FXCollections.observableList(textureNames);
        texturesList.setItems(textureData);

        // Handle a selection change in the textures list
        texturesList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            int index = texturesList.getSelectionModel().getSelectedIndex();

            selectedTexture = ProjectData.textures.get(index);

            textureName.setText(selectedTexture.name);
            texturePreview.setImage(EngineController.loadTexturesImage(selectedTexture.path));
        });
    }

}
