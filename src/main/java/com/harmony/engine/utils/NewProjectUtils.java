/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.LauncherController;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.data.networking.resource.DemoResource;
import com.harmony.engine.data.networking.resource.NetResource;
import com.harmony.core.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewProjectUtils {

    private static Stage stage;

    // Misc.
    public Label projectNameLabel;
    public Button createButton;
    public Button cancelButton;

    public Button chooseLocationButton;

    // Fields
    public TextField projectName;
    public TextField locationField;
    public TextField version;
    public CheckBox demoResources;

    public static void createNewProject() {
        try {
            if(NewProjectUtils.stage != null && NewProjectUtils.stage.isShowing()) NewProjectUtils.stage.close();

            FXMLLoader loader = new FXMLLoader(GlobalData.class.getResource("/fxml/newProject.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            Stage stage = new Stage();
            NewProjectUtils.stage = stage;
            stage.setTitle("New Project");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        projectName.textProperty().addListener(observable -> {
            if(projectName.getText().isEmpty()) {
                setCreateButton(false);
                return;
            }

            projectNameLabel.setText(projectName.getText());
            stage.setTitle("New Project - " + projectName.getText());
            setCreateButton(true);
        });

        locationField.textProperty().addListener(observable -> {
            if(locationField.getText() == null || locationField.getText().isEmpty()) {
                setCreateButton(false);
                return;
            }

            File file = new File(locationField.getText());

            if(!file.exists() || !file.isDirectory()) {
                locationField.setStyle("-fx-text-fill: #ff0000");
                setCreateButton(false);
            } else {
                locationField.setStyle("-fx-text-fill: -fx-default-text-fill");
                setCreateButton(true);
            }
        });

        version.textProperty().addListener(observable -> {
            if(version.getText().isEmpty()) setCreateButton(false);
        });

        chooseLocationButton.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Parent Directory");

            File selectedFile = directoryChooser.showDialog(NewProjectUtils.stage);

            if(selectedFile != null) locationField.setText(selectedFile.getPath());
        });

        cancelButton.setOnMouseClicked(mouseEvent -> stage.close());

        createButton.setOnMouseClicked(mouseEvent -> create());
    }

    private void create() {
        File parentDirectory = new File(locationField.getText());

        if(!parentDirectory.exists() || !parentDirectory.isDirectory()) return;

        File directory = new File(parentDirectory.getPath() + File.separator + projectName.getText());
        boolean status = directory.mkdirs();

        if(!status) return;

        GlobalData.save();

        try {
            new File(directory.getPath() + "/" + directory.getName() + ".hyproj").createNewFile();
            new File(directory.getPath() + "/Resources").mkdir();
            new File(directory.getPath() + "/Resources/Textures").mkdir();
            new File(directory.getPath() + "/Resources/Scripts").mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ProjectData.reset();

            // Create Project Things...
            ProjectData.projectName = projectName.getText();
            ProjectData.versionID = version.getText();

            ProjectData.states.add(new State("Main State", new ArrayList<>()));
            ProjectData.launcherState = "Main State";

            ProjectData.save(directory);

            stage.close();
            LauncherController.showProgressAndOpen(directory,
                    demoResources.isSelected() ? new NetResource[] { DemoResource.RESOURCE_CONTEXT } : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCreateButton(boolean value) {
        if(!value) createButton.setDisable(true);
        else {
            if(!locationField.getText().isEmpty() && !projectName.getText().isEmpty() && !version.getText().isEmpty() &&
                    ((new File(locationField.getText())).exists())) {
                createButton.setDisable(false);
            }
        }
    }

}
