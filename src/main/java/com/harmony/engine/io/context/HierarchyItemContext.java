/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.context;

import com.harmony.engine.io.MenuManager;
import com.harmony.engine.io.editor.state.StateEditor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class HierarchyItemContext extends ContextMenu {

    private final MenuItem moveUp;
    private final MenuItem moveDown;

    private final MenuItem open;
    private final MenuItem find;

    private final MenuItem deleteItem;

    public HierarchyItemContext() {
        // Create Objects
        moveUp = new MenuItem("Move Up");
        moveDown = new MenuItem("Move Down");

        open = new MenuItem("Open");
        find = new MenuItem("Find");

        deleteItem = new MenuItem("Delete Game Object");

        // Handle Accelerators
        moveUp.setAccelerator(new KeyCodeCombination(KeyCode.OPEN_BRACKET, MenuManager.controlModifier));
        moveDown.setAccelerator(new KeyCodeCombination(KeyCode.CLOSE_BRACKET, MenuManager.controlModifier));

        deleteItem.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, MenuManager.controlModifier));

        open.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, MenuManager.controlModifier));
        find.setAccelerator(new KeyCodeCombination(KeyCode.F));

        super.getItems().addAll(moveUp, moveDown, new SeparatorMenuItem(),
                new SeparatorMenuItem(), open, find, new SeparatorMenuItem(), deleteItem);

        handleInput();
    }

    public void handleInput() {
        deleteItem.setOnAction(actionEvent -> StateEditor.deleteSelectedGameObjects());

        moveUp.setOnAction(actionEvent -> StateEditor.moveSelectedGameObjectUp());
        moveDown.setOnAction(actionEvent -> StateEditor.moveSelectedGameObjectDown());

        open.setOnAction(actionEvent -> StateEditor.openSelectedGameObject());
        find.setOnAction(actionEvent -> StateEditor.findSelectedGameObject());
    }

}
