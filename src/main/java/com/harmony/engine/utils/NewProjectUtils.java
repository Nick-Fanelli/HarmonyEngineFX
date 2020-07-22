/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewProjectUtils {

    private static Stage stage;

    // Misc.
    public Label projectNameLabel;
    public Button createButton;
    public Button cancelButton;

    // Fields
    public TextField projectName;

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
        projectName.textProperty().addListener((observableValue, s, t1) -> {
            if(projectName.getText().isEmpty()) {
                createButton.setDisable(true);
                return;
            }

            projectNameLabel.setText(projectName.getText());
            stage.setTitle("New Project - " + projectName.getText());
            createButton.setDisable(false);
        });

        cancelButton.setOnMouseClicked(mouseEvent -> stage.close());
    }

}
