package com.harmony.engine.controllers;

import com.harmony.engine.managers.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class EngineController {

    // File Manager
    public TreeView<String> fileManager;
    public VBox newFilePane;
    public Button createNewFileButton;
    public TextField newFileField;

    @FXML
    public void initialize() {
        new FileManager(fileManager, newFilePane, createNewFileButton, newFileField);
    }

}
