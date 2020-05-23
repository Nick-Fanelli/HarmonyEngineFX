package com.harmony.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Harmony extends Application {

    public static File directory;

    public static void open(File directory) throws Exception {
        Harmony.directory = directory;
        new Harmony().start(Launcher.staticStage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Harmony.class.getResource("/engine.fxml"));
        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Harmony Engine v1.0");
        stage.setScene(scene);
        stage.show();
    }
}
