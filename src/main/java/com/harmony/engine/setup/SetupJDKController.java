/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.setup;

import com.harmony.engine.data.DataUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SetupJDKController {

    private ArrayList<JDK> jdkChoices = new ArrayList<>();

    public Button finishButton;
    public Button backButton;

    public AnchorPane step1;
    public AnchorPane step2;

    public Label step1Label;
    public Label step2Label;
    public Label statusLabel;

    public Button goThereButton;
    public Button verifyButton;
    public Button chooseAgain;

    public ImageView checkStep1;
    public ImageView checkStep2;

    public ProgressBar progressBar;

    public ComboBox<JDK> versionDropdown;

    private boolean firstJDKCheck = true;

    @FXML
    public void initialize() {
        this.verifyJDK();
        this.firstJDKCheck = false;

        backButton.setOnMouseClicked(mouseEvent -> SetupController.setScene(SetupController.themeScene));

        goThereButton.setOnMouseClicked(mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.oracle.com/java/technologies/javase-jdk14-downloads.html"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        versionDropdown.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            if(versionDropdown.getSelectionModel().getSelectedItem() != null) {
                SetupController.jdkLocation = versionDropdown.getSelectionModel().getSelectedItem().location;
                checkStep2.setVisible(true);
                step2.setDisable(true);
                chooseAgain.setVisible(true);
                finishButton.setDisable(false);
            }
        });

        chooseAgain.setOnMouseClicked(mouseEvent -> {
            finishButton.setDisable(true);
            checkStep2.setVisible(false);
            step2.setDisable(false);
            chooseAgain.setVisible(false);
            this.verifyJDK();
        });

        finishButton.setOnMouseClicked(mouseEvent -> SetupController.finish());
    }

    private void verifyJDK() {
        DataUtils.OperatingSystem os = DataUtils.OperatingSystem.getCurrentOS();

        if(os == DataUtils.OperatingSystem.UNDEFINED) {
            statusLabel.setText("Could not identify OS!");
            statusLabel.setStyle("-fx-text-fill: red");
            statusLabel.setVisible(true);
            return;
        }

        File jdkLocation = new File(os.jdkLocation);
        if(!jdkLocation.isDirectory() || !jdkLocation.exists()) {
            statusLabel.setText("JDK Not Installed");
            statusLabel.setStyle("-fx-text-fill: red");
            statusLabel.setVisible(true);
            return;
        }

        File[] jdkList = jdkLocation.listFiles();
        if(jdkList == null || jdkList.length <= 0) {
            statusLabel.setText("JDK Not Installed");
            statusLabel.setStyle("-fx-text-fill: red");
            statusLabel.setVisible(true);
            return;
        }

        jdkChoices.clear();
        for(File jdk : jdkList) jdkChoices.add(new JDK(jdk.getName().replaceAll("\\.jdk", ""), jdk));

        statusLabel.setText(firstJDKCheck ? "Success - Pre-Installed!" : "Success...");
        statusLabel.setStyle("-fx-text-fill: green");
        statusLabel.setVisible(true);
        checkStep1.setVisible(true);
        step1.setDisable(true);
        step2.setDisable(false);
        versionDropdown.getItems().clear();
        versionDropdown.getItems().addAll(jdkChoices);
    }

    public static class JDK {
        public String name;
        public File location;

        public JDK(String name, File location) {
            this.name = name;
            this.location = location;
        }

        @Override public String toString() { return name; }
    }

}
