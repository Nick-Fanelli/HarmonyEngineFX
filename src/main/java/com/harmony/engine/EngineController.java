package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.documentation.Documentation;
import com.harmony.engine.io.editor.CodeEditor;
import com.harmony.engine.io.editor.StateEditor;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import com.harmony.engine.utils.gameObjects.GameObjectUtils;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

public class EngineController implements Runnable {

    private static Thread engineThread;

    // Misc.
    public TabPane tabBar;

    public static Label staticStatusLabel;
    public static Label staticUtilityLabel;
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
    public Tab texturesTab;
//    public ListView<String> texturesList;
//    public Button newTextureButton;
//    public Button deleteTextureButton;
//
//    public AnchorPane textureInteractables;
//    public TextField textureField;
//    public TextField textureLocationField;
//    public ImageView textureImageView;
//    public Button saveTextureButton;
//    public Button chooseTextureButton;

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

    // Code Tab
    public TreeView<String> codeFileList;
    public WebView codeView;

    @FXML
    public void initialize() {
        initMiscMethods();

        // Start Engine Thread
        if(engineThread != null) return;
        engineThread = new Thread(this, "Harmony:EngineThread");
        engineThread.start();
    }

    @Override
    public void run() {
        initProjectTab();
        initTexturesTab();
        initGameObjectsTab();
        initEditorTab();
        initCodeTab();

        Status.setCurrentStatus(Status.Type.READY);
    }

    // Misc Methods
    private void initMiscMethods() {
        staticStatusLabel = statusLabel;
        staticUtilityLabel = mousePositionLabel;

        Status.setCurrentStatus(Status.Type.STAND_BY);

        saveProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/save-icon.png"), 32, 32, true, true)));
        runProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/run-icon.png"), 32, 32, true, true)));
        documentationButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/info-icon.png"), 32, 32, true, true)));
        globalPreferencesButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/settings-icon.png"), 20, 20, true, true)));

        saveProjectButton.setOnMouseClicked(mouse -> ProjectData.save(Harmony.directory));
        documentationButton.setOnMouseClicked(mouse -> Documentation.showDocumentation(this, tabBar.getSelectionModel().getSelectedItem()));
        globalPreferencesButton.setOnMouseClicked(mouse -> GlobalData.launchGlobalPreferences());

        tabBar.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if(t1 == editorTab) StateEditor.update();
            else if(t1 == gameObjectsTab) synchronizeGameObjects();
//            else if(t1 == texturesTab) synchronizeTextures();
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

        openDocumentationButton.setOnMouseClicked(mouseEvent -> {
            try {
                System.out.println("Harmony -> Opening Full Documentation in Default Web Browser...");
                Desktop.getDesktop().browse(URI.create("https://github.com/HarmonyEngines/HarmonyDocumentation/tree/" + Launcher.GITHUB_VERSION_STRING));
            } catch (Exception e) {
                System.err.println("Could not load the documentation at branch: " + Launcher.GITHUB_VERSION_STRING);
                System.err.println("If the documentation branch doesn't work navigate to: " + Documentation.GITHUB_LOCATION);
            }
        });
    }

    // Textures Methods
    private void initTexturesTab() {

    }

    public static Image loadTexturesImage(String path) {
        try {
            return new Image(new FileInputStream(new File(Harmony.directory.getPath() + path)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
            GameObject bufferObject = ProjectData.gameObjects.get(index);

            bufferObject.name = gameObjectNameField.getText().trim();
            bufferObject.position.set(Float.parseFloat(gameObjectPosX.getText()), Float.parseFloat(gameObjectPosY.getText()));

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
        new StateEditor(editorCanvas, editorPane, objectsPane, hierarchy);
    }

    // Code Methods
    private void initCodeTab() { new CodeEditor(codeFileList, codeView); }
}