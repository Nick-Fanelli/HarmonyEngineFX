package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

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

    public AnchorPane textureInteractables;
    public TextField textureField;
    public TextField textureLocationField;
    public ImageView textureImageView;
    public Button saveTextureButton;
    public Button chooseTextureButton;

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
        textureInteractables.setVisible(false);

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

            if(index < 0) {
                textureInteractables.setVisible(false);
                return;
            } else {
                textureInteractables.setVisible(true);
            }

            Texture texture = ProjectData.textures.get(index);

            textureField.setText(texture.name);
            textureLocationField.setText(texture.path);

            setTexturesImage(texture.path);
        });

        saveTextureButton.setOnMouseClicked(mouseEvent -> {
            int index = staticTexturesList.getSelectionModel().getSelectedIndex();

            if(index < 0) return;

            // Put The New Data In The Data Buffer
            ProjectData.textures.get(index).name = textureField.getText().trim();
            ProjectData.textures.get(index).path = textureLocationField.getText().trim();

            // Update The Static Textures List
            staticTexturesList.getItems().set(index, textureField.getText().trim());
            staticTexturesList.getSelectionModel().select(index);
            staticTexturesList.scrollTo(index);
        });

        chooseTextureButton.setOnMouseClicked(mouseEvent -> {
            int index = staticTexturesList.getSelectionModel().getSelectedIndex();
            if(index < 0) return;

            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(Harmony.directory.getPath() + "/Resources/Textures"));
            chooser.setTitle("Choose Texture");
            File selectedFile = chooser.showOpenDialog(Harmony.staticStage);

            if(selectedFile == null) return;

            textureLocationField.setText(selectedFile.getPath());
            setTexturesImage(selectedFile.getPath());
        });
    }
    private void setTexturesImage(String path) {
        try {
            textureImageView.setImage(new Image(new FileInputStream(new File(path))));
            textureLocationField.setStyle("-fx-text-fill: #eeeeee;");
        } catch (FileNotFoundException e) {
            // TODO: Error Pop-Up on the screen
            System.err.println("Harmony -> Could not load resource(s): " + path);
            textureImageView.setImage(new Image(EngineController.class.getResourceAsStream("/images/image-not-found.png")));
            textureLocationField.setStyle("-fx-text-fill: red;");
        }
    }
    public static void synchronizeTextures() {
        staticTexturesList.getItems().clear();

        for(int i = 0; i < ProjectData.textures.size(); i++) {
            staticTexturesList.getItems().add(i, ProjectData.textures.get(i).name);
            ProjectData.textures.get(i).id = i;
        }
    }
}
