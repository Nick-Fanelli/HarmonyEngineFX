package com.harmony.engine.utils;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.documentation.Documentation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    public ComboBox<String> jdk;

    // Editor
    public TextField panMultiplier;
    public TextField editorBGColor;
    public TextField editorOutlineColor;
    public CheckBox editorDrawFromTop;
    public CheckBox editorShowGuideLines;
    public TextField editorGuideLineDist;

    private boolean isThemeChange = false;
    private File[] jdkList;

    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Runnable fields = this::setFields;
        fields.run();

        cancelButton.setOnMouseClicked(event -> GlobalData.staticStage.close());

        applyButton.setOnMouseClicked(event -> {
            // Set Values
            for(int i = 0; i < GlobalData.Theme.values().length; i++) {
                if(GlobalData.Theme.values()[i].toString().equals(theme.getSelectionModel().getSelectedItem()))
                    GlobalData.setTheme(GlobalData.Theme.values()[i]);
            }

            GlobalData.setJDKLocation(DataUtils.OperatingSystem.getCurrentOS().jdkLocation
                    + File.separator + jdk.getSelectionModel().getSelectedItem() + ".jdk");
            GlobalData.setAutoSave(autoSave.isSelected());
            GlobalData.setPanMultiplier(Double.parseDouble(panMultiplier.getText()));
            GlobalData.setEditorBackgroundColor(editorBGColor.getText().replaceAll("#", ""));
            GlobalData.setEditorOutlineColor(editorOutlineColor.getText().replaceAll("#", ""));
            GlobalData.setEditorDrawFromTop(editorDrawFromTop.isSelected());
            GlobalData.setEditorShowGuideLines(editorShowGuideLines.isSelected());
            GlobalData.setEditorGuideLineDist(Double.parseDouble(editorGuideLineDist.getText()));

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

        handleChanges();
    }

    private void setFields() {
        this.setTheme();
        this.setJDK();

        autoSave.setSelected(GlobalData.getAutoSave());
        panMultiplier.setText(Double.toString(GlobalData.getPanMultipler()));
        editorBGColor.setText(GlobalData.getEditorBackgroundColor());
        editorOutlineColor.setText(GlobalData.getEditorOutlineColor());
        editorDrawFromTop.setSelected(GlobalData.getEditorDrawFromTop());
        editorShowGuideLines.setSelected(GlobalData.getEditorShowGuideLines());
        editorGuideLineDist.setText(Double.toString(Math.round(GlobalData.getEditorGuideLineDist())));
    }

    private void handleChanges() {
        theme.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> isThemeChange = true);
    }

    private void setTheme() {
        int index = 0;

        for(int i = 0; i < GlobalData.Theme.values().length; i++) {
            theme.getItems().add(GlobalData.Theme.values()[i].toString());
            if(GlobalData.Theme.values()[i] == GlobalData.getTheme()) index = i;
        }

        theme.getSelectionModel().select(index);
    }

    private void setJDK() {
        int selectedIndex = 0;

        File jdkDir = new File(DataUtils.OperatingSystem.getCurrentOS().jdkLocation);
        jdkList = jdkDir.listFiles();
        assert jdkList != null;

        for(int i = 0; i < jdkList.length; i++) {
            this.jdk.getItems().add(jdkList[i].getName().replaceAll("\\.jdk", ""));
            if(jdkList[i].getPath().equals(GlobalData.getJDKLocation())) selectedIndex = i;
        }

        this.jdk.getSelectionModel().select(selectedIndex);
    }
}
