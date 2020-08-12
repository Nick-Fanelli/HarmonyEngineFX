/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.setup.SetupController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Launcher extends Application {

    // TODO - Deploy: Update The Version Data
    public static final int[] VERSION_ID = new int[] { 1, 0, 0 };
    public static final LaunchType LAUNCH_TYPE = LaunchType.VERSION;

    public static final String GITHUB_VERSION_STRING = "version-" + VERSION_ID[0] + "." + VERSION_ID[1] + "." + VERSION_ID[2];
    public enum LaunchType { VERSION, SNAPSHOT }

    public static Stage staticStage;
    public static Scene staticScene;

    public enum LauncherMethod { DEFAULT, OPEN, NEW }
    public static LauncherMethod launcherMethod;

    public static void main(String[] args) {
        if(args.length > 0) {
            if(args[0].equals("H_OPEN")) launcherMethod = LauncherMethod.OPEN;
            else if(args[0].equals("H_NEW")) launcherMethod = LauncherMethod.NEW;
            else launcherMethod = LauncherMethod.DEFAULT;
        } else launcherMethod = LauncherMethod.DEFAULT;

        configureSystemProperties();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> fontFamilies = Font.getFamilies();
        for (String fontFamily: fontFamilies) {
            Font.font(fontFamily);
        }

        if(!validateJDK()) System.exit(-1);

        staticStage = stage;

        Parent root = FXMLLoader.load(Harmony.class.getResource("/launcher.fxml"));
        Scene scene = new Scene(root, 800, 600);
        staticScene = scene;

        // Handle Theme
        scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

        stage.setTitle(String.format("%sHarmony Engine", LAUNCH_TYPE == LaunchType.SNAPSHOT ? "PRE-RELEASE - " : ""));
        stage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(windowEvent -> {
            GlobalData.save();
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
        GlobalData.load();
    }

    public static void changeTheme() {
        staticScene.getStylesheets().remove(staticScene.getStylesheets().size() - 1);

        // Handle Theme
        staticScene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());
    }

    private boolean validateJDK() {
        if(GlobalData.getJDKLocation() != null) {
            File jdk = new File(GlobalData.getJDKLocation());
            if(jdk.exists() && jdk.isDirectory()) return true;
        }

        SetupController.runSetup();
        return true;
    }
}
