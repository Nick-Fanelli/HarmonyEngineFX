package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class GlobalPrefController {

    public ComboBox<String> theme;

    public Button cancelButton;
    public Button applyButton;

    private boolean isThemeChange = false;

    @FXML
    public void initialize() {
        setFields();

        cancelButton.setOnMouseClicked(event -> {
            Status.setCurrentStatus(Status.Type.READY);
            GlobalData.staticStage.close();
        });

        applyButton.setOnMouseClicked(event -> {
            // Set Values
            for(int i = 0; i < GlobalData.Theme.values().length; i++) {
                if(GlobalData.Theme.values()[i].toString().equals(theme.getSelectionModel().getSelectedItem()))
                    GlobalData.setTheme(GlobalData.Theme.values()[i]);
            }

            if(isThemeChange) Harmony.changeTheme();

            Status.setCurrentStatus(Status.Type.READY);
            GlobalData.staticStage.close();
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
    }

    private void handleChanges() {
        theme.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> isThemeChange = true);
    }

}
