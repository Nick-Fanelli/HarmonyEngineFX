/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.setup;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {

    private static Stage stage;
    public static Scene themeScene;
    public static Scene jdkScene;

    // Value
    public static GlobalData.Theme theme;

    // Theme
    public AnchorPane lightThemePane;
    public AnchorPane darkThemePane;
    public AnchorPane lightThemeBox;
    public AnchorPane darkThemeBox;

    public Button themeNextButton;

    public static void runSetup() {
        try {
            Parent themeParent = FXMLLoader.load(SetupController.class.getResource("/fxml/setup/theme.fxml"));
            Parent jdkParent = FXMLLoader.load(SetupController.class.getResource("/fxml/setup/jdk.fxml"));

            stage = new Stage();
            stage.setTitle("Harmony Engine Setup");
            stage.getIcons().add(new Image(Harmony.class.getResourceAsStream("/images/logo.png")));
            stage.setResizable(false);

            stage.setOnCloseRequest(windowEvent -> System.exit(0));

            themeScene = new Scene(themeParent);
            jdkScene = new Scene(jdkParent);

            themeScene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation(GlobalData.Theme.LIGHT)).toExternalForm());

            stage.setScene(themeScene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setScene(Scene scene) {
        if(stage == null || scene == null) return;
        scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation(theme != null ? theme : GlobalData.Theme.LIGHT)).toExternalForm());
        stage.setScene(scene);
    }

    private static void updateTheme() {
        if(theme == null) return;
        themeScene.getStylesheets().remove(0);
        themeScene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation(theme)).toExternalForm());
    }

    public static void finish() {
        GlobalData.setTheme(theme);

        stage.close();
    }

    @FXML
    public void initialize() {
        initializeTheme();
    }

    // Theme
    private void initializeTheme() {
        lightThemePane.setOnMouseClicked (mouseEvent -> setTheme(GlobalData.Theme.LIGHT)) ;
        darkThemePane.setOnMouseClicked  (mouseEvent -> setTheme(GlobalData.Theme.DARK))  ;

        themeNextButton.setOnMouseClicked(mouseEvent -> setScene(jdkScene));

        SetupController.theme = GlobalData.Theme.LIGHT;
        lightThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-extra-light");
        darkThemePane.setStyle("-fx-cursor:hand;-fx-background-color: -fx-default-light");

        lightThemeBox.setStyle(themeBoxCSS + "-fx-border-color: #365880");
        darkThemeBox.setStyle(themeBoxCSS + "-fx-border-color: -fx-default-dark");
    }

    private final String themeBoxCSS = "-fx-background-color:transparent;-fx-border-radius:25px;-fx-border-width:12.5px;";

    private void setTheme(GlobalData.Theme theme) {
        SetupController.theme = theme;

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

        updateTheme();
    }

}
