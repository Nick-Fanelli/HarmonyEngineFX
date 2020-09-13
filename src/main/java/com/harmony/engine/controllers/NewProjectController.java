package com.harmony.engine.controllers;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class NewProjectController {

    private static Stage stage;

    public TextField projectName;
    public TextField locationField;

    public AnchorPane blankPane;
    public AnchorPane starterPane;

    public AnchorPane blankRadioPane;
    public AnchorPane starterRadioPane;

    public Button chooseLocation;
    public Button createButton;
    public Button cancelButton;

    private File parentDirectory;

    public static void createNewProject() {
        try {
            Parent parent = FXMLLoader.load(NewProjectController.class.getResource("/fxml/newProject.fxml"));
            stage = new Stage(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);

            Scene scene = new Scene(parent);
            stage.setResizable(false);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        chooseLocation.setOnAction(actionEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Parent Directory");
            parentDirectory = directoryChooser.showDialog(stage);
            if(parentDirectory == null) return;

            if(!parentDirectory.exists()) {
                boolean isSuccess = parentDirectory.mkdirs();
                if(!isSuccess) {
                    Log.error("Could not create parent directory at: " + parentDirectory.getPath());
                    return;
                }
            }

            locationField.setText(parentDirectory.getPath());
        });

        locationField.textProperty().addListener(observable -> {
            File file = new File(locationField.getText());
            if(file.exists()) {
                createButton.setDisable(false);
                locationField.setStyle("-fx-text-fill: -fx-default-text-fill;");
            } else {
                createButton.setDisable(true);
                locationField.setStyle("-fx-text-fill: #fc3838");
            }
        });

        cancelButton.setOnAction(actionEvent -> stage.close());

        createButton.setOnAction(actionEvent -> {
            if(parentDirectory == null || !parentDirectory.exists()) {
                Log.error("Parent directory could not be validated.");
                return;
            }

            ProjectData.setDirectory(new File(parentDirectory + File.separator
                    + (projectName.getText().trim().isEmpty() ? "Untitled Project" : projectName.getText().trim())));

            NewProjectController.stage.close();

            LauncherController.open(ProjectData.getDirectory());
        });
    }
}
