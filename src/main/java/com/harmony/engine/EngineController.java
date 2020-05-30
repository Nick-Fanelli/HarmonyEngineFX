package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.documentation.Documentation;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObjectUtils;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EngineController {

    // Misc.
    public TabPane tabBar;

    public static Label staticStatusLabel;
    public Label statusLabel;

    public Button saveProjectButton;
    public Button runProjectButton;
    public Button documentationButton;

    // Project Tab
    public Tab projectTab;
    public TextField projectName;
    public TextField author;
    public TextField version;

    // Textures Tab
    public static ListView<String> staticTexturesList;

    public Tab texturesTab;
    public ListView<String> texturesList;
    public Button newTextureButton;
    public Button deleteTextureButton;

    public AnchorPane textureInteractables;
    public TextField textureField;
    public TextField textureLocationField;
    public ImageView textureImageView;
    public Button saveTextureButton;
    public Button chooseTextureButton;

    // GameObjects Tab
    public static ListView<String> staticGameObjectsList;

    public Tab gameObjectsTab;
    public ListView<String> gameObjectsList;
    public Button newGameObjectButton;
    public Button deleteGameObjectButton;

    public AnchorPane gameObjectsInteractables;

    @FXML
    public void initialize() {
        initMiscMethods();

        initProjectTab();
        initTexturesTab();
        initGameObjectsTab();

        Status.setCurrentStatus(Status.Type.READY);
    }

    // Misc Methods
    private void initMiscMethods() {
        staticStatusLabel = statusLabel;
        Status.setCurrentStatus(Status.Type.STAND_BY);

        saveProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/save-icon.png"), 32, 32, true, true)));
        runProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/run-icon.png"), 32, 32, true, true)));
        documentationButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/info-icon.png"), 32, 32, true, true)));

        saveProjectButton.setOnMouseClicked(mouse -> ProjectData.save(Harmony.directory));
        documentationButton.setOnMouseClicked(mouse -> {
            if(tabBar.getSelectionModel().getSelectedItem() == projectTab)
                Documentation.showDocumentation(Documentation.Location.PROJECT_TAB);
            else if(tabBar.getSelectionModel().getSelectedItem() == texturesTab)
                Documentation.showDocumentation(Documentation.Location.TEXTURES_TAB);
        });
    }
    public static void setStatusLabel(String status, Color color) {
        staticStatusLabel.setTextFill(color);
        staticStatusLabel.setText("Status: " + status);
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
            ProjectData.textures.get(index).path = textureLocationField.getText().trim().replaceAll(Harmony.directory.getPath(), "");

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
            textureImageView.setImage(new Image(new FileInputStream(new File(Harmony.directory.getPath() + path))));
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

    // GameObject Methods
    private void initGameObjectsTab() {
        EngineController.staticGameObjectsList = gameObjectsList;
        gameObjectsInteractables.setVisible(false);

        EngineController.synchronizeGameObjects();
        handleGameObjectElements();
    }
    private void handleGameObjectElements() {
        newGameObjectButton.setOnMouseClicked(mouseEvent -> GameObjectUtils.createGameObject());

        deleteGameObjectButton.setOnMouseClicked(mouseEvent -> {
            int id = staticGameObjectsList.getSelectionModel().getSelectedIndex();
            staticGameObjectsList.getItems().remove(id);
            ProjectData.gameObjects.remove(id);
            EngineController.synchronizeGameObjects();
        });
    }
    public static void synchronizeGameObjects() {
        staticGameObjectsList.getItems().clear();

        for(int i = 0; i < ProjectData.gameObjects.size(); i++) {
            staticGameObjectsList.getItems().add(i, ProjectData.gameObjects.get(i).name);
        }
    }

}
