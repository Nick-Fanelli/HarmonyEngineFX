package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static final int[] VERSION_ID = new int[] {
            1, 0, 0
    };

    public static final String GITHUB_VERSION_STRING = "version-1.0";

    public static Stage staticStage;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        configureSystemProperties();
        staticStage = stage;

        Parent root = FXMLLoader.load(Harmony.class.getResource("/launcher.fxml"));
        Scene scene = new Scene(root, 640, 400);

        // Handle Theme
        scene.getStylesheets().add(Harmony.class.getResource("/cssThemes/"
                + GlobalData.dataContext.theme.name().toLowerCase() + "Theme.css").toExternalForm());

        stage.setTitle("Harmony Engine v1.0");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void configureSystemProperties() {
        System.setProperty("prism.lcdtext", "false");
    }
}
