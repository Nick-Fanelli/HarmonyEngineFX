package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.Status;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;

public class Harmony extends Application {

    public static File directory;
    public static Stage staticStage;

    private boolean controlDown = false;
    private boolean sDown = false;
    public static boolean saving = false;

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
        Harmony.staticStage = stage;

        Parent root = FXMLLoader.load(Harmony.class.getResource("/engine.fxml"));
        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Harmony Engine v1.0");
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        stage.setOnCloseRequest(event -> {
            ProjectData.save(Harmony.directory);
            Platform.exit();
            System.exit(0);
        });

        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.CONTROL || keyEvent.getCode() == KeyCode.COMMAND) {
                controlDown = true;
            } else if(keyEvent.getCode() == KeyCode.S) {
                sDown = true;
            }

            if(controlDown && sDown && !saving) {
                saving = true;
                Status.setCurrentStatus(Status.Type.SAVING);
                ProjectData.save(Harmony.directory);
                Status.setCurrentStatus(Status.Type.READY);
                saving = false;
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.CONTROL || keyEvent.getCode() == KeyCode.COMMAND) {
                controlDown = false;
            } else if(keyEvent.getCode() == KeyCode.S) {
                sDown = false;
            }
        });

    }
}
