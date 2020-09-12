package com.harmony.engine.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NewProjectController {

    public static void createNewProject() {
        try {
            Parent parent = FXMLLoader.load(NewProjectController.class.getResource("/fxml/newProject.fxml"));
            Stage stage = new Stage();

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

    }

}
