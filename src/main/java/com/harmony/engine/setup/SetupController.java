/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.setup;

import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {

    private static Stage stage;
    private static Scene themeScene;

    // Value
    private GlobalData.Theme theme;

    // Theme
    public AnchorPane lightThemePane;
    public AnchorPane darkThemePane;
    public AnchorPane lightThemeBox;
    public AnchorPane darkThemeBox;

    public Button themeNextButton;

    public static void runSetup() {
        try {
            Parent themeParent = FXMLLoader.load(SetupController.class.getResource("/fxml/setup/theme.fxml"));

            stage = new Stage();
            stage.setTitle("Harmony Engine Setup");
            stage.setResizable(false);

            stage.setOnCloseRequest(windowEvent -> System.exit(0));

            themeScene = new Scene(themeParent);

            stage.setScene(themeScene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setScene(Scene scene) {
        if(stage == null || scene == null) return;

        stage.setScene(scene);
    }

    @FXML
    public void initialize() {
        initializeTheme();
    }

    // Theme
    private void initializeTheme() {
        lightThemePane.setOnMouseClicked (mouseEvent -> setTheme(GlobalData.Theme.LIGHT)) ;
        darkThemePane.setOnMouseClicked  (mouseEvent -> setTheme(GlobalData.Theme.DARK))  ;
    }

    private String themeBoxCSS = "-fx-background-color:transparent;-fx-border-radius:25px;-fx-border-width:12.5px;";

    private void setTheme(GlobalData.Theme theme) {
        this.theme = theme;

        if(theme == GlobalData.Theme.LIGHT) {
            lightThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-extra-light");
            darkThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-light");

            lightThemeBox.setStyle(themeBoxCSS + "-fx-border-color: #365880");
            darkThemeBox.setStyle(themeBoxCSS + "-fx-border-color: -fx-default-dark");
        } else {
            lightThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-light");
            darkThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-extra-light");

            lightThemeBox.setStyle(themeBoxCSS + "-fx-border-color: -fx-default-dark");
            darkThemeBox.setStyle(themeBoxCSS + "-fx-border-color: #365880");
        }
    }

}
