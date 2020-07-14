/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.editor.state;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class StateUtils {

    private static Stage stage;

    public TextField name;

    public Button createButton;
    public Button cancelButton;

    @FXML
    public void initialize() {
        createButton.setOnMouseClicked(mouseEvent -> create());
        cancelButton.setOnMouseClicked(mouseEvent -> stage.close());
    }

    public static void createState() {
        try {
            FXMLLoader loader = new FXMLLoader(StateUtils.class.getResource("/fxml/createState.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            stage = new Stage(StageStyle.UNDECORATED);
            stage.setResizable(false);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create() {
        if(name.getText().isEmpty()) return;

        for(State state : ProjectData.states) {
            if(state.name.equals(name.getText().trim())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Name");
                alert.setHeaderText("Choose a Different Name");
                alert.setContentText("The name " + name.getText() +  " has been taken, please use a different name.");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(Harmony.class.getResource("/css/harmony.css").toExternalForm());
                dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

                alert.show();
                return;
            }
        }

        ProjectData.states.add(new State(name.getText().trim(), new ArrayList<>()));
        StateEditor.load.run();
        stage.close();
    }

}
