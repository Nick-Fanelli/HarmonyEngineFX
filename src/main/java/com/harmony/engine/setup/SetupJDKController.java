/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.setup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SetupJDKController {

    public Button nextButton;
    public Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(mouseEvent -> SetupController.setScene(SetupController.themeScene));
    }

}
