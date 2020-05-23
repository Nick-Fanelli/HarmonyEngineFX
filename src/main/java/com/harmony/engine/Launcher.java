package com.harmony.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static Stage staticStage;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        staticStage = stage;

        Parent root = FXMLLoader.load(Harmony.class.getResource("/launcher.fxml"));
        Scene scene = new Scene(root, 640, 400);

        stage.setTitle("Harmony Engine v1.0");
        stage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }
}
