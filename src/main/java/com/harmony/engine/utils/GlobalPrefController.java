package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class GlobalPrefController {

    public GlobalData data;

    public ComboBox<String> theme;

    public Button cancelButton;
    public Button applyButton;

    private boolean isThemeChange = false;

    @FXML
    public void initialize() {
        data = GlobalData.dataContext.copy();
        setFields();

        cancelButton.setOnMouseClicked(event -> GlobalData.staticStage.close());

        applyButton.setOnMouseClicked(event -> {
            GlobalData.dataContext = data;
            if(isThemeChange) Harmony.changeTheme();

            GlobalData.staticStage.close();
        });

        handleChanges();
    }

    private void setFields() {
        int selectedTheme = 0;

        for(int i = 0; i < GlobalData.Theme.values().length; i++) {
            theme.getItems().add(GlobalData.Theme.values()[i].toString());
            if(GlobalData.Theme.values()[i] == GlobalData.dataContext.theme) selectedTheme = i;
        }

        theme.getSelectionModel().select(selectedTheme);
    }

    private void handleChanges() {
        theme.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            data.theme = GlobalData.Theme.values()[t1.intValue()];
            isThemeChange = true;
        });
    }

}
