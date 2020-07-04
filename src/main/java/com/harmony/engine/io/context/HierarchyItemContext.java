package com.harmony.engine.io.context;

import com.harmony.engine.io.editor.StateEditor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class HierarchyItemContext extends ContextMenu {

    private final MenuItem moveUp;
    private final MenuItem moveDown;

    private final MenuItem find;

    private final MenuItem deleteItem;

    public HierarchyItemContext() {
        boolean onMac = System.getProperty("os.name").startsWith("Mac");

        // Create Objects
        moveUp = new MenuItem("Move Up");
        moveDown = new MenuItem("Move Down");

        find = new MenuItem("Find");

        deleteItem = new MenuItem("Delete Game Object");

        // Handle Multi-Platform KeyStrokes
        KeyCombination.Modifier controlModifier = onMac ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN;

        // Handle Accelerators
        moveUp.setAccelerator(new KeyCodeCombination(KeyCode.OPEN_BRACKET, controlModifier));
        moveDown.setAccelerator(new KeyCodeCombination(KeyCode.CLOSE_BRACKET, controlModifier));

        deleteItem.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, controlModifier));

        find.setAccelerator(new KeyCodeCombination(KeyCode.F));

        super.getItems().addAll(moveUp, moveDown, new SeparatorMenuItem(),
                new SeparatorMenuItem(), find, new SeparatorMenuItem(), deleteItem);

        handleInput();
    }

    public void handleInput() {
        deleteItem.setOnAction(actionEvent -> StateEditor.deleteSelectedGameObjects());

        moveUp.setOnAction(actionEvent -> StateEditor.moveSelectedGameObjectUp());
        moveDown.setOnAction(actionEvent -> StateEditor.moveSelectedGameObjectDown());

        find.setOnAction(actionEvent -> StateEditor.findSelectedGameObject());
    }

}
