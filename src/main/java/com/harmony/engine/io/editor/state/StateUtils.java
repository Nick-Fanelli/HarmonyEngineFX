package com.harmony.engine.io.editor.state;

import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

            stage = new Stage(StageStyle.UNDECORATED);
            stage.setResizable(false);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create() {
        ProjectData.states.add(new State(name.getText().isEmpty() ? "Untitled State" : name.getText().trim(), new ArrayList<>()));
        StateEditor.load.run();
        stage.close();
    }

}
