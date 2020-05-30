package com.harmony.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static final int[] VERSION_ID = new int[] {
            1, 0, 0
    };

    public static Stage staticStage;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        configureSystemProperties();
        staticStage = stage;

        Parent root = FXMLLoader.load(Harmony.class.getResource("/launcher.fxml"));
        Scene scene = new Scene(root, 640, 400);

        stage.setTitle("Harmony Engine v1.0");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void configureSystemProperties() {
        System.setProperty("prism.lcdtext", "false");
    }
}
