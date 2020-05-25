package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EngineController {

    // Project Tab
    public TextField projectName;
    public TextField author;
    public TextField version;

    // Textures Tab
    public static ListView<String> staticTexturesList;

    public ListView<String> texturesList;
    public Button newTextureButton;
    public Button deleteTextureButton;

    public TextField textureField;
    public ImageView textureImageView;

    @FXML
    public void initialize() {
        initProjectTab();
        initTexturesTab();
    }

    // Project Methods
    private void initProjectTab() {
        projectName.setText(ProjectData.projectName);
        author.setText(ProjectData.author);
        version.setText(ProjectData.versionID);

        handleProjectElements();
    }

    private void handleProjectElements() {
        // Project Name
        projectName.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.projectName = t1;
        });

        // Author
        author.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.author = t1;
        });

        // VersionID
        version.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.versionID = t1;
        });
    }

    // Textures Methods
    private void initTexturesTab() {
        EngineController.staticTexturesList = texturesList;

        EngineController.synchronizeTextures();
        handleTextureElements();
    }

    private void handleTextureElements() {
        newTextureButton.setOnMouseClicked(mouseEvent -> {
            TextureUtils.createTexture();
        });

        deleteTextureButton.setOnMouseClicked(mouseEvent -> {
            int id = staticTexturesList.getSelectionModel().getSelectedIndex();
            staticTexturesList.getItems().remove(id);
            ProjectData.textures.remove(id);
            EngineController.synchronizeTextures();
        });

        staticTexturesList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            int index = staticTexturesList.getSelectionModel().getSelectedIndex();

            if(index < 0) return;

            Texture texture = ProjectData.textures.get(index);
            textureField.setText(texture.name);

            try {
                textureImageView.setImage(new Image(new FileInputStream(new File(texture.path))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        textureField.textProperty().addListener((observableValue, s, t1) -> {
            int index = staticTexturesList.getSelectionModel().getSelectedIndex();
            if (index < 0) return;

            ProjectData.textures.get(index).name = t1;

            staticTexturesList.getItems().set(index, t1);
        });

    }

    public static void synchronizeTextures() {
        staticTexturesList.getItems().clear();

        for(int i = 0; i < ProjectData.textures.size(); i++) {
            staticTexturesList.getItems().add(i, ProjectData.textures.get(i).name);
        }
    }
}
