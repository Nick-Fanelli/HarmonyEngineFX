/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.Documentation;
import com.harmony.engine.io.MenuManager;
import com.harmony.engine.io.editor.CodeEditor;
import com.harmony.engine.io.editor.state.State;
import com.harmony.engine.io.editor.state.StateEditor;
import com.harmony.engine.io.tabs.GameObjectsTab;
import com.harmony.engine.io.tabs.TexturesTab;
import com.harmony.engine.utils.Status;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class EngineController implements Runnable {

    private static Thread engineThread;

    // Misc.
    public static TabPane staticTabBar;
    public TabPane tabBar;
    public AnchorPane contentAnchorPane;
    public MenuBar menuBar;

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
    public TextField launcherState;

    public Button openDocumentationButton;
    public Button chooseStateButton; // TODO: Make Work

    // Textures Tab
    public Tab texturesTab;
    public ScrollPane texturesScrollPane;
    public GridPane texturesArray;
    public Button newTextureButton;
    public TextField texturesSearch;

    public AnchorPane texturesInteractables;
    public TextField textureName;
    public TextField textureLocation;
    public Button chooseTextureButton;
    public Button deleteTextureButton;

    // Game Objects Tab
    public Tab gameObjectsTab;
    public GridPane objectsArray;
    public Button newObjectButton;
    public Button openObjectButton;

    // State Editor Tab
    public static Tab staticEditorTab;
    public Tab editorTab;
    public AnchorPane editorState;
    public AnchorPane editorInteractable;

    public ListView<String> editorStatesList;
    public Button editorOpenStateButton;
    public Button editorNewStateButton;
    public Button deleteStateButton;

    public Canvas editorCanvas;
    public AnchorPane editorPane;
    public GridPane objectsPane;
    public TreeView<String> hierarchy;
    public Button backStateButton;

    public Button zoom50;
    public Button zoom100;
    public Button zoom150;
    public Button zoom200;

    // Code Editor Tab
    public TreeView<String> codeFileList;
    public WebView codeView;
    public AnchorPane codeNoFilePane;
    public AnchorPane codeTabEditorPane;
    public TabPane codeTabBar;

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
        staticTabBar = tabBar;

        new MenuManager(menuBar, contentAnchorPane).initialize();

        Status.setCurrentStatus(Status.Type.STAND_BY);

        saveProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream(
                String.format("/images/icons/save-icon-%s.png", GlobalData.getTheme() == GlobalData.Theme.LIGHT ? "light" : "dark")), 24, 24, true, true)));
        runProjectButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/run-icon.png"), 24, 24, true, true)));
        documentationButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/info-icon.png"), 28, 28, true, true)));
        globalPreferencesButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/settings-icon.png"), 20, 20, true, true)));

        saveProjectButton.setOnMouseClicked(mouse -> Harmony.save());
        documentationButton.setOnMouseClicked(mouse -> Documentation.showDocumentation(this, tabBar.getSelectionModel().getSelectedItem()));
        globalPreferencesButton.setOnMouseClicked(mouse -> GlobalData.launchGlobalPreferences());

        runProjectButton.setOnMouseClicked(mouse -> {
        });

        tabBar.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            Platform.runLater(() -> {
                if(tabBar.getSelectionModel().getSelectedItem() == editorTab) {
                    StateEditor.update();
                }
            });
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
        launcherState.setText(ProjectData.launcherState);

        Runnable elements = this::handleProjectElements;
        elements.run();
    }
    private void handleProjectElements() {
        // Project Name
        projectName.textProperty().addListener((observableValue, s, t1) -> ProjectData.projectName = t1);

        // Author
        author.textProperty().addListener((observableValue, s, t1) -> ProjectData.author = t1);

        // VersionID
        version.textProperty().addListener((observableValue, s, t1) -> ProjectData.versionID = t1);

        // Launcher State
        launcherState.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.isEmpty()) return;

            boolean match = false;
            for(State state : ProjectData.states) {
                if(t1.equals(state.name)) {
                    match = true;
                    break;
                }
            }

            if(!match) {
                launcherState.setStyle("-fx-text-fill: red;");
                return;
            }

            launcherState.setStyle("-fx-text-fill: -fx-default-text-fill;");
            ProjectData.launcherState = launcherState.getText();
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
    private void initTexturesTab() { new TexturesTab(texturesArray, newTextureButton, texturesInteractables, textureName,
            textureLocation, chooseTextureButton, deleteTextureButton, texturesSearch); }

    public static Image loadTexturesImage(String path) {
        try {
            return new Image(new FileInputStream(new File(Harmony.directory.getPath() + path)));
        } catch (Exception e) {
            System.err.println("Harmony -> Could not find an image at \"" + path + "\"");
        }

        return null;
    }
    public static Image loadTexturesImage(String path, int width, int height) {
        try {
            return new Image(new FileInputStream(new File(Harmony.directory.getPath() + path)), width, height, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // GameObject Methods
    private void initGameObjectsTab() {
        new GameObjectsTab(objectsArray, newObjectButton, openObjectButton);
    }

    // Editor Methods
    private void initEditorTab() {
        staticEditorTab = editorTab;
        new StateEditor(editorCanvas, editorPane, objectsPane, hierarchy, editorStatesList, editorOpenStateButton,
                editorNewStateButton, deleteStateButton, editorState, editorInteractable, backStateButton, zoom50, zoom100, zoom150, zoom200);
    }

    // Code Methods
    private void initCodeTab() { new CodeEditor(codeFileList, codeView, codeNoFilePane, codeTabEditorPane, codeTabBar); }

    // Util Methods
    public static boolean isStatusReady() { return staticStatusLabel != null; }
}