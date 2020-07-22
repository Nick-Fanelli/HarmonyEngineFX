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
import javafx.stage.Stage;

import java.io.IOException;

public class NewProjectUtils {

    private static Stage staticStage;
    private static Scene staticScene;

    public static void createNewProject() {
        try {
            Parent parent = FXMLLoader.load(NewProjectUtils.class.getResource("/fxml/newProject.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(parent, 800, 600);

            staticStage = stage;
            staticScene = scene;

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        
    }

}
