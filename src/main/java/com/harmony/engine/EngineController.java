package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.documentation.Documentation;
import com.harmony.engine.io.Editor;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import com.harmony.engine.utils.gameObjects.GameObjectUtils;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

public class EngineController {

    // Misc.
    public TabPane tabBar;

    public static Label staticStatusLabel;
    public static Label staticMousePositionLabel;
    public Label statusLabel;
    public Label mousePositionLabel;

    public Button saveProjectButton;
    public Button runProjectButton;
    public Button documentationButton;
    public Button globalPreferencesButton;

    // Project Tab
    public Tab projectTab;
    public TextField projectName;
    public TextField author;
    public TextField version;
    public Button openDocumentationButton;

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
    public TextField gameObjectNameField;
    public TextField gameObjectTextureField;
    public TextField gameObjectPosX;
    public TextField gameObjectPosY;
    public Button chooseGameObjectTexture;
    public Button saveGameObjectButton;

    // Editor Tab
    public Tab editorTab;
    public Canvas editorCanvas;
    public AnchorPane editorPane;
    public GridPane objectsPane;
    public TreeView<String> hierarchy;

    @FXML
    public void initialize() {
        initMiscMethods();

        initProjectTab();
        initTexturesTab();
        initGameObjectsTab();
        initEditorTab();

        Status.setCurrentStatus(Status.Type.READY);
    }

    // Misc Methods
    private void initMiscMethods() {
        staticStatusLabel = statusLabel;
        staticMousePositionLabel = mousePositionLabel;

        Status.setCurrentStatus(Status.Type.STAND_BY);

        saveProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/save-icon.png"), 32, 32, true, true)));
        runProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/run-icon.png"), 32, 32, true, true)));
        documentationButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/info-icon.png"), 32, 32, true, true)));
        globalPreferencesButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/settings-icon.png"), 20, 20, true, true)));

        saveProjectButton.setOnMouseClicked(mouse -> ProjectData.save(Harmony.directory));
        documentationButton.setOnMouseClicked(mouse -> Documentation.showDocumentation(this, tabBar.getSelectionModel().getSelectedItem()));
        globalPreferencesButton.setOnMouseClicked(mouse -> GlobalData.launchGlobalPreferences());
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

        openDocumentationButton.setOnMouseClicked(mouseEvent -> {
            try {
                System.out.println("Hey");
                Desktop.getDesktop().browse(URI.create("https://github.com/HarmonyEngines/HarmonyDocumentation/tree/" + Launcher.GITHUB_VERSION_STRING));
            } catch (Exception e) {
                System.err.println("Could not load the documentation at branch: " + Launcher.GITHUB_VERSION_STRING);
            }
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

            if (index < 0) {
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

            if (index < 0) return;

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
            if (index < 0) return;

            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(Harmony.directory.getPath() + "/Resources/Textures"));
            chooser.setTitle("Choose Texture");
            File selectedFile = chooser.showOpenDialog(Harmony.staticStage);

            if (selectedFile == null) return;

            textureLocationField.setText(Harmony.getResourceString(selectedFile.getPath()));
            setTexturesImage(Harmony.getResourceString(selectedFile.getPath()));
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
    public static Image loadTexturesImage(String path) {
        try {
            return new Image(new FileInputStream(new File(Harmony.directory.getPath() + path)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void synchronizeTextures() {
        staticTexturesList.getItems().clear();

        for (int i = 0; i < ProjectData.textures.size(); i++) {
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

        staticGameObjectsList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            int index = staticGameObjectsList.getSelectionModel().getSelectedIndex();

            if (index < 0) {
                gameObjectsInteractables.setVisible(false);
                return;
            } else {
                gameObjectsInteractables.setVisible(true);
            }

            GameObject object = ProjectData.gameObjects.get(index);

            gameObjectNameField.setText(object.name);
            gameObjectPosX.setText(Float.toString(object.position.x));
            gameObjectPosY.setText(Float.toString(object.position.y));

            if (object.texture != null) gameObjectTextureField.setText(object.texture.name);
            else gameObjectTextureField.setText("");
        });

        saveGameObjectButton.setOnMouseClicked(mouseEvent -> {
            int index = staticGameObjectsList.getSelectionModel().getSelectedIndex();

            if (index < 0) return;

            // Put The New Data In The Data Buffer
            ProjectData.gameObjects.get(index).name = gameObjectNameField.getText().trim();

            // Update The Static Game Objects List
            staticGameObjectsList.getItems().set(index, gameObjectNameField.getText().trim());
            staticGameObjectsList.getSelectionModel().select(index);
            staticGameObjectsList.scrollTo(index);
        });

        chooseGameObjectTexture.setOnMouseClicked(mouseEvent -> {
            TextureUtils.chooseTextureForGameObject(ProjectData.gameObjects.get(staticGameObjectsList.getSelectionModel().getSelectedIndex()));
        });
    }
    public static void synchronizeGameObjects() {
        int index = staticGameObjectsList.getSelectionModel().getSelectedIndex();
        staticGameObjectsList.getItems().clear();

        for (int i = 0; i < ProjectData.gameObjects.size(); i++) {
            staticGameObjectsList.getItems().add(i, ProjectData.gameObjects.get(i).name);
        }

        if (index >= 0) staticGameObjectsList.getSelectionModel().select(index);
    }

    // Editor Methods
    private void initEditorTab() {
        new Editor(editorCanvas, editorPane, objectsPane, hierarchy);

        tabBar.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if(t1 == editorTab) Editor.update();
        });
    }
}