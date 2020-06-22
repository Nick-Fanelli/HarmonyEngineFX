package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.Image;
import java.net.URL;
import java.util.List;

public class Launcher extends Application {

    // TODO - Deploy: Update The Version Data
    public static final int[] VERSION_ID = new int[] { 1, 0 };
    public static final LaunchType LAUNCH_TYPE = LaunchType.SNAPSHOT;

    public static final String GITHUB_VERSION_STRING = "version-" + VERSION_ID[0] + "." + VERSION_ID[1];
    public enum LaunchType { Version, SNAPSHOT }

    public static Stage staticStage;
    public static Scene staticScene;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> fontFamilies = Font.getFamilies();
        for (String fontFamily: fontFamilies) {
            Font.font(fontFamily);
        }

        configureSystemProperties();
        staticStage = stage;

        GlobalData.load();

        Parent root = FXMLLoader.load(Harmony.class.getResource("/launcher.fxml"));
        Scene scene = new Scene(root, 640, 400);
        staticScene = scene;

        // Handle Theme
        scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

        stage.setTitle("Harmony Engine v1.0");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(windowEvent -> {
            GlobalData.save();
            Platform.exit();
            System.exit(0);
        });

        stage.show();

    }

    public static void configureSystemProperties() {
        System.setProperty("prism.lcdtext", "false");
    }

    public static void changeTheme() {
        staticScene.getStylesheets().remove(staticScene.getStylesheets().size() - 1);

        // Handle Theme
        staticScene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());
    }
}
