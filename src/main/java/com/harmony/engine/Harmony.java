package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import com.harmony.engine.data.ProjectData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Harmony extends Application {

    public static void main(String[] args) { open(new File("/Users/nick227889/Dev/Game"), args); }

    public static void open(File directory, String[] args) {
        ProjectData.setDirectory(directory);
        // TODO: Load Project Data

        if(Launcher.staticStage != null) {
            try {
                Launcher.staticStage.close();
                new Harmony().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Launcher.isDebugMode = true;
            Launcher.configureSystemProperties();
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = new FXMLLoader(Harmony.class.getResource("/fxml/engine.fxml")).load();

        Scene scene = new Scene(root, 1280, 720);
        stage.setMinWidth(1024);
        stage.setMinWidth(600);
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> {
            CacheData.save();

            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }
}
