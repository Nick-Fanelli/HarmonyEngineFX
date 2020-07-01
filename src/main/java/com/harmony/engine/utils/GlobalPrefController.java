package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.documentation.Documentation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class GlobalPrefController {

    public Button cancelButton;
    public Button applyButton;
    public Button resetToDefaultsButton;
    public Button documentationButton;
    public ScrollPane scrollPane;

    // General
    public ComboBox<String> theme;
    public CheckBox autoSave;
    public Button importTheme;

    // Editor
    public TextField panMultiplier;
    public TextField editorBGColor;
    public TextField editorOutlineColor;
    public CheckBox editorDrawFromTop;

    private boolean isThemeChange = false;

    @FXML
    public void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        setFields();

        cancelButton.setOnMouseClicked(event -> {
            GlobalData.staticStage.close();
        });

        applyButton.setOnMouseClicked(event -> {
            // Set Values
            for(int i = 0; i < GlobalData.Theme.values().length; i++) {
                if(GlobalData.Theme.values()[i].toString().equals(theme.getSelectionModel().getSelectedItem()))
                    GlobalData.setTheme(GlobalData.Theme.values()[i]);
            }

            GlobalData.setAutoSave(autoSave.isSelected());
            GlobalData.setPanMultiplier(Double.parseDouble(panMultiplier.getText()));
            GlobalData.setEditorBackgroundColor(editorBGColor.getText().replaceAll("#", ""));
            GlobalData.setEditorOutlineColor(editorOutlineColor.getText().replaceAll("#", ""));
            GlobalData.setEditorDrawFromTop(editorDrawFromTop.isSelected());

            if(isThemeChange) {
                if(Harmony.staticStage != null)
                    Harmony.changeTheme();
                else if(Launcher.staticStage != null)
                    Launcher.changeTheme();
            }

            GlobalData.staticStage.close();
        });

        resetToDefaultsButton.setOnMouseClicked(mouseEvent -> {
            GlobalData.setDefaults();
            this.initialize();
        });

        documentationButton.setOnMouseClicked(mouseEvent -> Documentation.showDocumentation(Documentation.Location.GLOBAL_PREFERENCES));

        importTheme.setOnMouseClicked(mouseEvent -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Theme");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("css"));

        });

        handleChanges();
    }

    private void setFields() {
        int selectedTheme = 0;

        for(int i = 0; i < GlobalData.Theme.values().length; i++) {
            theme.getItems().add(GlobalData.Theme.values()[i].toString());
            if(GlobalData.Theme.values()[i] == GlobalData.getTheme()) selectedTheme = i;
        }

        theme.getSelectionModel().select(selectedTheme);
        autoSave.setSelected(GlobalData.getAutoSave());
        panMultiplier.setText(Double.toString(GlobalData.getPanMultipler()));
        editorBGColor.setText(GlobalData.getEditorBackgroundColor());
        editorOutlineColor.setText(GlobalData.getEditorOutlineColor());
        editorDrawFromTop.setSelected(GlobalData.getEditorDrawFromTop());
    }

    private void handleChanges() {
        theme.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> isThemeChange = true);
    }

}
