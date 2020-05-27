package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LauncherController {

    public AnchorPane anchorPane;
    public Button newProjectButton;
    public Button openProjectButton;
    public Label versionLabel;

    @FXML
    public void initialize() {
        versionLabel.setText(String.format("Version: %s.%s.%s", Launcher.VERSION_ID[0], Launcher.VERSION_ID[1], Launcher.VERSION_ID[2]));

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

        String[] children = directory.list();
        assert children != null;

        if(children.length > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("The Directory You Choose Was Not Empty");
            alert.setContentText("All files in the directory: " + directory.getPath() + ":\n" + listFiles(children)
                    + "\nWill be overridden!");

            ButtonType buttonTypeOverride = new ButtonType("Override");
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
            new File(directory.getPath() + "/" + directory.getName() + ".hypref").createNewFile();
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

        String[] children = directory.list();
        assert children != null;

        boolean prefCheck = false;
        boolean projCheck = false;

        for(String child : children) {
            if(child.endsWith("hypref")) prefCheck = true;
            if(child.endsWith("hyproj")) projCheck = true;
        }

        if(!(prefCheck && projCheck)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Opening Project Directory");
            alert.setContentText("The selected directory is either not a Harmony Project directory or is corrupted.");

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