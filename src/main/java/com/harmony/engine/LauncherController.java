package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LauncherController {

    public AnchorPane anchorPane;
    public Button newProjectButton;
    public Button openProjectButton;
    public Button globalPreferencesButton;
    public Label versionLabel;

    @FXML
    public void initialize() {
        versionLabel.setText(String.format("%s: %s.%s", Launcher.LAUNCH_TYPE.name(), Launcher.VERSION_ID[0], Launcher.VERSION_ID[1]));

        globalPreferencesButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/settings-icon.png"), 20, 20, true, true)));
        globalPreferencesButton.setOnMouseClicked(mouse -> GlobalData.launchGlobalPreferences());

        newProjectButton.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Create Project");

            File selectedFile = directoryChooser.showDialog(Launcher.staticStage);

            if (selectedFile != null) {
                create(selectedFile);
            }
        });

        openProjectButton.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Project");

            File selectedFile = directoryChooser.showDialog(Launcher.staticStage);

            if(selectedFile != null) {
                open(selectedFile);
            }
        });
    }

    private void create(File directory) {
        if(!directory.isDirectory()) return;
        GlobalData.save();

        String[] children = directory.list();
        assert children != null;

        if(children.length > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("The Directory You Choose Was Not Empty");
            alert.setContentText("All files in the directory: " + directory.getPath() + ":\n" + listFiles(children)
                    + "\nWill be overridden!");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/engine.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            ButtonType buttonTypeOverride = new ButtonType("Override", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOverride, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == buttonTypeOverride) {
                cleanDirectory(directory);
            } else {
                return;
            }
        }

        try {
            new File(directory.getPath() + "/" + directory.getName() + ".hyproj").createNewFile();
            new File(directory.getPath() + "/Resources").mkdir();
            new File(directory.getPath() + "/Resources/Textures").mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ProjectData.reset();

            ProjectData.projectName = directory.getName();
            ProjectData.versionID = "0.0.1";

            ProjectData.save(directory);

            Harmony.open(directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void open(File directory) {
        if(!directory.exists()) return;
        if(!directory.isDirectory()) return;

        GlobalData.save();

        String[] children = directory.list();
        assert children != null;

        boolean projCheck = false;

        for(String child : children) {
            if(child.endsWith("hyproj")) projCheck = true;
        }

        if(!projCheck) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Opening Project Directory");
            alert.setContentText("The selected directory is either not a Harmony Project directory or is corrupted.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/engine.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            alert.showAndWait();

            return;
        }

        try {
            ProjectData.reset();
            Harmony.open(directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String listFiles(String[] children) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String str : children) {
            stringBuilder.append("\t").append(str).append("\n");
        }

        return stringBuilder.toString();
    }

    private void cleanDirectory(File directory) {
        File[] children = directory.listFiles();
        if(children == null) return;

        for(File file : children) {
            if(file.isDirectory()) cleanDirectory(file);
            file.delete();
        }
    }
}