/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Launcher extends Application {

    // TODO - Deploy: Update The Version Data
    public static final int[] VERSION_ID = new int[] { 1, 0, 0 };
    public static final LaunchType LAUNCH_TYPE = LaunchType.VERSION;
    public static boolean isDebugMode = false;

    public enum LaunchType { VERSION, SNAPSHOT }

    public static Stage staticStage;
    public static Scene staticScene;

    public static void main(String[] args) {
        configureSystemProperties();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> fontFamilies = Font.getFamilies();
        for (String fontFamily: fontFamilies) {
            Font.font(fontFamily);
        }

        staticStage = stage;

        Parent root = FXMLLoader.load(Launcher.class.getResource("/fxml/launcher.fxml"));
        Scene scene = new Scene(root, 800, 600);
        staticScene = scene;

        stage.setTitle(String.format("%sHarmony Engine", LAUNCH_TYPE == LaunchType.SNAPSHOT ? "PRE-RELEASE - " : ""));
        stage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(windowEvent -> {
            CacheData.save();

            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }

    public static void configureSystemProperties() {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.subpixeltext", "false");

        CacheData.load();
    }
}
