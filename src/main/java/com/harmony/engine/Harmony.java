package com.harmony.engine;

import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.editor.CodeEditor;
import com.harmony.engine.io.editor.StateEditor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Harmony extends Application {

    public static File directory;

    public static Stage staticStage;
    public static Scene staticScene;

    private static Runnable inputThread;

    public static boolean controlDown = false;
    public static boolean sDown = false;
    public static boolean altDown = false;
    public static boolean shiftDown = false;

    public static boolean saving = false;
    public static boolean loaded = false;

    public static void main(String[] args) throws Exception { open(new File("/Users/nick227889/Dev/Game")); }

    public static void open(File directory) throws Exception {
        Harmony.directory = directory;
        ProjectData.load(directory);

        if(!ProjectData.harmonyVersionID.equals(Launcher.GITHUB_VERSION_STRING)) {
            System.err.println("Harmony Warning -> The current version of the project does not match the current version of the engine\n"
                    + "\tProject Version: " + ProjectData.harmonyVersionID + "\n"
                    + "\tEngine Version: " + Launcher.GITHUB_VERSION_STRING);
        }

        // TODO - Deploy: Comment Out
        if(Launcher.staticStage != null) {
            new Harmony().start(new Stage());
        } else {
            Launcher.configureSystemProperties();
            launch();
        }
    }

    public static void show() {
        System.out.println(Thread.currentThread());
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> fontFamilies = Font.getFamilies();
        for (String fontFamily: fontFamilies) {
            Font.font(fontFamily);
        }

        Parent root = FXMLLoader.load(Harmony.class.getResource("/engine.fxml"));

        Scene scene = new Scene(root, 1280, 720);

        Harmony.staticStage = stage;
        Harmony.staticScene = scene;

        GlobalData.load();

        // Handle Theme
        scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

        stage.setTitle("Harmony Engine v1.0");
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();

        stage.setOnCloseRequest(event -> closeApplication());

        Platform.runLater(() -> loaded = true);

        inputThread = this::handleInput;
        inputThread.run();
    }

    private void handleInput() {
        staticScene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case CONTROL:
                case COMMAND:
                    controlDown = true;         break;
                case S: sDown = true;           break;
                case ALT:
                    altDown = true;
                    triggerHand(true);
                    break;
                case SHIFT: shiftDown = true;   break;
            }

            if(controlDown && sDown && !saving) {
                saving = true;
                save();
                saving = false;
            }
        });

        staticScene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case CONTROL:
                case COMMAND:
                    controlDown = false;        break;
                case S: sDown = false;          break;
                case ALT:
                    altDown = false;
                    triggerHand(false);
                    break;
                case SHIFT: shiftDown = false;  break;
            }
        });
    }

    public static void save() {
        System.out.println("Harmony -> Saving...");
        ProjectData.save(Harmony.directory);
        CodeEditor.saveSelectedScript();
    }

    public static void triggerHand(boolean value) {
        if(value && StateEditor.interactingWithCanvas) staticScene.setCursor(Cursor.CLOSED_HAND);
        else staticScene.setCursor(Cursor.DEFAULT);
    }

    private void closeApplication() {
        ProjectData.save(Harmony.directory);
        GlobalData.save();

        Platform.exit();
        System.exit(0);
    }

    public static void changeTheme() {
        staticScene.getStylesheets().remove(staticScene.getStylesheets().size() - 1);

        // Handle Theme
        staticScene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());
    }

    public static String getResourceString(String path) {
        return path.trim().replaceAll(Harmony.directory.getPath(), "").trim();
    }

    public static String getScriptsLocationString() { return Harmony.directory + File.separator + "Resources" + File.separator + "Scripts"; }
    public static File getScriptsLocation() { return new File(getScriptsLocationString()); }
}
