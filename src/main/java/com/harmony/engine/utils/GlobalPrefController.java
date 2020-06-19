package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GlobalPrefController {

    public Button cancelButton;
    public Button applyButton;
    public Button resetToDefaultsButton;

    public ComboBox<String> theme;
    public CheckBox autoSave;

    public TextField panMultiplier;
    public TextField editorBGColor;
    public TextField editorOutlineColor;

    public ScrollPane scrollPane;

    private boolean isThemeChange = false;

    @FXML
    public void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // never show a vertical ScrollBar
        scrollPane.setFitToWidth(true); // set content width to viewport width
        scrollPane.setPannable(true); // allow scrolling via mouse dragging

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

            if(isThemeChange) {
                if(Harmony.staticStage != null) {
                    Harmony.changeTheme();
                    System.out.println("Hey");
                }
                else if(Launcher.staticStage != null)
                    Launcher.changeTheme();
            }

            GlobalData.staticStage.close();
        });

        resetToDefaultsButton.setOnMouseClicked(mouseEvent -> {
            GlobalData.setDefaults();
            this.initialize();
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
    }

    private void handleChanges() {
        theme.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> isThemeChange = true);
    }

}
