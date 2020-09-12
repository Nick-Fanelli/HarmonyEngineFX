package com.harmony.engine.controllers;

import com.harmony.engine.Launcher;
import com.harmony.engine.utils.Log;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LauncherController {

    public Label versionID;

    public Button newProjectButton;
    public Button openProjectButton;

    public AnchorPane recentProjectsPane;
    public AnchorPane[] recentProjects;

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

        Log.debug("Sample Debug Message");
        Log.error("Sample Error Message");

        newProjectButton.setOnAction(actionEvent -> NewProjectController.createNewProject());
    }

}
