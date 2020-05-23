package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LauncherController {

    public AnchorPane anchorPane;
    public Button newProjectButton;

    @FXML
    public void initialize() {
        newProjectButton.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Project Directory");

            File selectedFile = directoryChooser.showDialog(Launcher.staticStage);

            if (selectedFile != null) {
                create(selectedFile);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ProjectData.projectName = directory.getName();
            ProjectData.save(directory);

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