package com.harmony.engine;

import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EngineController {

    public TextField projectName;
    public TextField author;
    public TextField version;

    @FXML
    public void initialize() {
        projectName.setText(ProjectData.projectName);
        author.setText(ProjectData.author);
        version.setText(ProjectData.versionID);

        handleChanges();
    }

    private void handleChanges() {
        // Project Name
        projectName.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.projectName = t1;
            ProjectData.save(Harmony.directory);
        });

        // Author
        author.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.author = t1;
            ProjectData.save(Harmony.directory);
        });

        // VersionID
        version.textProperty().addListener((observableValue, s, t1) -> {
            ProjectData.versionID = t1;
            ProjectData.save(Harmony.directory);
        });
    }

}
