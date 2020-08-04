/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.GlobalData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

        DataUtils.OperatingSystem os = DataUtils.OperatingSystem.getCurrentOS();

        if(os == DataUtils.OperatingSystem.UNDEFINED) {
            StringBuilder osNameBuilder = new StringBuilder();

            for(DataUtils.OperatingSystem operatingSystem : DataUtils.OperatingSystem.values()) {
                if(operatingSystem != DataUtils.OperatingSystem.UNDEFINED) osNameBuilder.append(operatingSystem.name()).append(", ");
            }

            String osNameList = osNameBuilder.substring(0, osNameBuilder.toString().length() - 2);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Undefined Operating System");
            alert.setHeaderText("Could not identity current operating system.");
            alert.setContentText("The current operating system that is running could not be identified by Harmony Engine. " +
                    "Currently Harmony Engine can only support the following operating systems: " + osNameList);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            alert.showAndWait();

            return false;
        }

        File jdkLocation = new File(os.jdkLocation);
        if (!jdkLocation.isDirectory() || !jdkLocation.exists()) return false;

        File[] jdkList = jdkLocation.listFiles();
        if(jdkList == null || jdkList.length <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing JDK");
            alert.setHeaderText("Could Not Find A Valid JDK");
            alert.setContentText("Harmony Engine uses the JDK to compile and build all Java scripts that are written, please make sure you download the JDK to the default directory." +
                    "\n\nhttps://www.oracle.com/java/technologies/javase-downloads.html");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            alert.showAndWait();

            return false;
        }

        ArrayList<String> jdkChoices = new ArrayList<>();
        for(File jdk : jdkList) jdkChoices.add(jdk.getName().replaceAll("\\.jdk", ""));

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose JDK", jdkChoices);
        dialog.setTitle("Choose JDK");
        dialog.setHeaderText("Choose Target JDK");
        dialog.setContentText("Please choose target JDK:");

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
        dialog.getDialogPane().getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

        Optional<String> result = dialog.showAndWait();
        AtomicReference<File> selectedJDK = new AtomicReference<>();
        result.ifPresent(jdk -> selectedJDK.set(new File(os.jdkLocation + File.separator + result.get() + (os == DataUtils.OperatingSystem.WINDOWS ? "" : ".jdk"))));
        if(selectedJDK.get() == null || !selectedJDK.get().exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error With JDK Location");
            alert.setHeaderText("Could not validate the JDK Location");
            alert.setHeaderText("Could not validate the JDK Location");
            alert.setContentText("Something went terribly wrong :(");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            alert.showAndWait();
            return false;
        }

        GlobalData.setJDKLocation(selectedJDK.get().getPath());

        return true;
    }
}
