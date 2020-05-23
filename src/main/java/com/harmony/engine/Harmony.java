package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Harmony extends Application {

    public static File directory;

    public static void main(String[] args) throws Exception { open(new File("/Users/nick227889/Dev/Game")); }

    public static void open(File directory) throws Exception {
        Harmony.directory = directory;
        ProjectData.load(directory);

        if(Launcher.staticStage != null) {
            new Harmony().start(Launcher.staticStage);
        } else {
            launch();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Harmony.class.getResource("/engine.fxml"));
        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Harmony Engine v1.0");
        stage.getIcons().add(new Image(Harmony.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            ProjectData.save(Harmony.directory);
            Platform.exit();
            System.exit(0);
        });
    }
}
