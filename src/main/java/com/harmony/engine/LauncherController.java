/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine;

import com.harmony.engine.data.CacheData;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.data.networking.resource.NetResource;
import com.harmony.engine.utils.NewProjectUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class LauncherController {

    public AnchorPane anchorPane;
    public Button newProjectButton;
    public Button openProjectButton;
    public Button globalPreferencesButton;
    public Label versionLabel;
    public Label noRecentLabel;
    public Label recentProjects;
    public ProgressBar pb;
    public static ProgressBar progressBar;

    public Label p1;
    public Label p2;
    public Label p3;
    public Label p4;
    public Label p5;

    @FXML
    public void initialize() {
        progressBar = pb;
        progressBar.setProgress(0);
        progressBar.setVisible(false);

        versionLabel.setText(Launcher.LAUNCH_TYPE == Launcher.LaunchType.VERSION ? String.format("v%s.%s.%s", Launcher.VERSION_ID[0], Launcher.VERSION_ID[1], Launcher.VERSION_ID[2])
                : String.format("SNAPSHOT: %s.%s.%s", Launcher.VERSION_ID[0], Launcher.VERSION_ID[1], Launcher.VERSION_ID[2]));

        globalPreferencesButton.setGraphic(new ImageView(new Image(EngineController.class.getResourceAsStream("/images/icons/settings-icon.png"), 20, 20, true, true)));
        globalPreferencesButton.setOnMouseClicked(mouse -> GlobalData.launchGlobalPreferences());

        File[] recent = CacheData.getRecentProjects();
        boolean hasRecent = false;

        Label[] labels = new Label[] { p1, p2, p3, p4, p5 };

        for(File file : recent) {
            if(file == null) continue;
            else hasRecent = true;

            for(Label label : labels) {
                if(label.getText().isEmpty()) {
                    label.setText(file.getName());
                    label.getStyleClass().add("active");

                    label.setOnMouseClicked(mouseEvent -> {
                        try {
                            showProgressAndOpen(file, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    break;
                }
            }
        }

        if(hasRecent) noRecentLabel.setVisible(false);
        else recentProjects.setVisible(false);

        newProjectButton.setOnMouseClicked(mouseEvent -> NewProjectUtils.createNewProject());

        openProjectButton.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Project");

            File selectedFile = directoryChooser.showDialog(Launcher.staticStage);

            if(selectedFile != null) {
                open(selectedFile);
            }
        });

        if(Launcher.launcherMethod == Launcher.LauncherMethod.OPEN) openProjectButton.fire();
        else if(Launcher.launcherMethod == Launcher.LauncherMethod.NEW) newProjectButton.fire();
    }

    private void open(File directory) {
        if(!directory.exists()) return;
        if(!directory.isDirectory()) return;

        GlobalData.save();

        String[] children = directory.list();
        assert children != null;

        boolean projCheck = false;

        for(String child : children) {
            if(child.endsWith("hyproj")) projCheck = true;
        }

        if(!projCheck) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Opening Project Directory");
            alert.setContentText("The selected directory is either not a Harmony Project directory or is corrupted.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            alert.showAndWait();

            return;
        }

        try {
            ProjectData.reset();
            showProgressAndOpen(directory, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String listFiles(String[] children) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String str : children) {
            stringBuilder.append("\t").append(str).append("\n");
        }

        return stringBuilder.toString();
    }

    public static void showProgressAndOpen(File directory, NetResource[] netResources) throws Exception {
        CacheData.setRecentProject(directory);
        CacheData.save();

        Runnable progress = LauncherController::handleProgressBar;

        progressBar.setVisible(true);
        new Thread(progress, "Harmony:Progress").start();

        if(netResources != null) {
            for(NetResource resource : netResources) {
                resource.downloadTo(new File(directory.getPath() + File.separator + "Resources"));
            }
        }

        Harmony.open(directory);
    }

    private static void handleProgressBar() {
        int delay = 25;

        for(int i = 0; i < 100; i++) {

            if(Harmony.loaded) delay = 10;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progressBar.setProgress(progressBar.getProgress() + 0.01);
        }

        while(!Harmony.loaded) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Platform.runLater(() -> {
            Launcher.staticStage.close();
            Harmony.staticStage.show();
        });
    }
}