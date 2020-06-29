package com.harmony.engine.io.context;

import com.harmony.engine.Harmony;
import com.harmony.engine.io.editor.CodeEditor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class FileItemContext extends ContextMenu {

    public final MenuItem save;

    public final MenuItem expand;
    public final MenuItem collapse;

    public FileItemContext() {
        boolean onMac = System.getProperty("os.name").startsWith("Mac");

        // Create Object
        save = new MenuItem("Save");

        expand = new MenuItem("Expand");
        collapse = new MenuItem("Collapse");

        // Handle Multi-Platform KeyStrokes
        KeyCombination.Modifier controlModifier = onMac ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN;

        save.setAccelerator(new KeyCodeCombination(KeyCode.S, controlModifier));

        expand.setAccelerator(new KeyCodeCombination(KeyCode.DOWN, controlModifier));
        collapse.setAccelerator(new KeyCodeCombination(KeyCode.UP, controlModifier));

        // Add Item
        super.getItems().addAll(save, new SeparatorMenuItem(), expand, collapse);

        // Handle Input
        handleInput();
    }

    private void handleInput() {
        save.setOnAction(actionEvent -> Harmony.save());

        expand.setOnAction(actionEvent -> CodeEditor.codeFileList.getSelectionModel().getSelectedItem().setExpanded(true));
        collapse.setOnAction(actionEvent -> CodeEditor.codeFileList.getSelectionModel().getSelectedItem().setExpanded(false));
    }

}
