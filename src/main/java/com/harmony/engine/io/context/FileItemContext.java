/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.context;

import com.harmony.engine.Harmony;
import com.harmony.engine.io.MenuManager;
import com.harmony.engine.io.editor.CodeEditor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class FileItemContext extends ContextMenu {

    public final MenuItem expand;
    public final MenuItem collapse;

    public FileItemContext() {
        // Create Object
        expand = new MenuItem("Expand");
        collapse = new MenuItem("Collapse");

        // Handle Multi-Platform KeyStrokes
        expand.setAccelerator(new KeyCodeCombination(KeyCode.DOWN, MenuManager.controlModifier));
        collapse.setAccelerator(new KeyCodeCombination(KeyCode.UP, MenuManager.controlModifier));

        // Add Item
        super.getItems().addAll(expand, collapse);

        // Handle Input
        handleInput();
    }

    private void handleInput() {
        expand.setOnAction(actionEvent -> CodeEditor.codeFileList.getSelectionModel().getSelectedItem().setExpanded(true));
        collapse.setOnAction(actionEvent -> CodeEditor.codeFileList.getSelectionModel().getSelectedItem().setExpanded(false));
    }

}
