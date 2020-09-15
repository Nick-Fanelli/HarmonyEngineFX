package com.harmony.engine.controllers;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.data.CacheData;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.utils.Log;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.HashMap;

public class LauncherController {

    public Label versionID;

    public Button newProjectButton;
    public Button openProjectButton;

    public AnchorPane recentProjectsPane;
    public AnchorPane[] recentProjects;

    public HashMap<AnchorPane, String> recentProjectsHasMap = new HashMap<>();

    @FXML
    public void initialize() {
        versionID.setText(String.format("v%s.%s.%s", Launcher.VERSION_ID[0], Launcher.VERSION_ID[1], Launcher.VERSION_ID[2]));

        recentProjects = new AnchorPane[15];

        for(int i = 0; i < 15; i++) {
            AnchorPane pane = new AnchorPane();
            pane.setMinHeight(40);
            pane.setMaxHeight(40);
            pane.setPrefHeight(40);

            pane.getStyleClass().add("recent-button");

            recentProjectsPane.getChildren().add(pane);

            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setTopAnchor(pane, 40.0 * i);

            recentProjects[i] = pane;
        }

        String[] recentProjectPaths = CacheData.get(CacheData.RECENT_PROJECTS) != null ? DataUtils.stringToStringArray(CacheData.get(CacheData.RECENT_PROJECTS)) : null;

        if(recentProjectPaths != null) {
            for(int i = 0; i < recentProjectPaths.length; i++) {
                File file = new File(recentProjectPaths[i]);

                if(!file.exists())  recentProjectPaths[i] = null;
            }

            String[] buffer = new String[recentProjectPaths.length];

            // TODO: Fix Project Creation.

            int diff = 0;
            for(int i = 0; i < buffer.length; i++) {
                if(recentProjectPaths[i] != null) {
                    buffer[i - diff] = recentProjectPaths[i];
                } else diff--;
            }

            recentProjectPaths = new String[buffer.length - diff];
            System.arraycopy(buffer, 0, recentProjectPaths, 0, buffer.length);

            CacheData.push(CacheData.RECENT_PROJECTS, DataUtils.stringArrayToString(recentProjectPaths));

            for (String recentProjectPath : recentProjectPaths) {
                for (AnchorPane pane : recentProjects) {
                    if (pane.getChildren().size() != 0) continue;
                    Label label = new Label(new File(recentProjectPath).getName());
                    label.getStyleClass().add("default-label");
                    AnchorPane.setTopAnchor(label, 0.0);
                    AnchorPane.setBottomAnchor(label, 0.0);
                    AnchorPane.setLeftAnchor(label, 15.0);

                    pane.getStyleClass().add("active");
                    pane.getChildren().add(label);
                    recentProjectsHasMap.put(pane, recentProjectPath);
                    break;
                }
            }
        }

        newProjectButton.setOnAction(actionEvent -> NewProjectController.createNewProject());

        openProjectButton.setOnAction(actionEvent -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose Project Directory");
            File projectDirectory = chooser.showDialog(Launcher.staticStage);

            if(projectDirectory == null) return;
            if(!projectDirectory.exists() || !projectDirectory.isDirectory()) {
                Log.error("Could not validate open directory at: " + projectDirectory.getPath());
                return;
            }

            File[] children = projectDirectory.listFiles();
            if(children == null) {
                Log.error("Could not locate any child files under the directory at: " + projectDirectory.getPath());
                return;
            }

            boolean containsProjectXML = false;
            boolean containsResources = false;

            for(File file : children) {
                if(file.getName().equals("project.xml")) {
                    containsProjectXML = true;
                } else if(file.getName().equals("Resources") && file.isDirectory()) {
                    containsResources = true;
                }
            }

            if(!containsProjectXML || !containsResources) {
                Log.error("Could not validate the project directory at: " + projectDirectory.getPath()
                        + "\n\tContains Project XML File: " + containsProjectXML + "\n\tContains Resources Directory: "
                        + containsResources + "\n\n\tQuick Fix: Create inside the project directory a \"project.xml\" file and a \"Resources\" directory");
                return;
            }

            open(projectDirectory);
        });
    }

    public static void open(File directory) {
        String[] recentProjects = CacheData.get(CacheData.RECENT_PROJECTS) != null ?
                DataUtils.stringToStringArray(CacheData.get(CacheData.RECENT_PROJECTS)) : new String[0];
        boolean shouldAdd = true;

        for(String recentProject : recentProjects) {
            if(recentProject == null || recentProject.equals(directory.getPath())) {
                shouldAdd = false;
                break;
            }
        }

        if(shouldAdd) {
            String[] newRecentProjects = new String[recentProjects.length + 1];

            System.arraycopy(recentProjects, 0, newRecentProjects, 1, recentProjects.length);
            newRecentProjects[0] = directory.getPath();

            CacheData.push(CacheData.RECENT_PROJECTS, DataUtils.stringArrayToString(newRecentProjects));
        }

        Harmony.open(directory, null);
        Launcher.staticStage.close();
    }

}
