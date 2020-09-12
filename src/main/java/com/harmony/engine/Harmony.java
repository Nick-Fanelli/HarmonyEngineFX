package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Harmony extends Application {

    public static void main(String[] args) {
        Launcher.configureSystemProperties();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = new FXMLLoader(Harmony.class.getResource("/fxml/engine.fxml")).load();

        Scene scene = new Scene(root, 1024, 600);
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> {
            CacheData.save();

            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }
}
