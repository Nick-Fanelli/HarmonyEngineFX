package com.harmony.engine.io;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class HierarchyItemContext extends ContextMenu {

    private final MenuItem deleteItem;
    private final MenuItem moveUp;

    public HierarchyItemContext() {
        deleteItem = new MenuItem("Delete Game Object");
        moveUp = new MenuItem("Move Up");

        super.getItems().addAll(deleteItem, new SeparatorMenuItem(), moveUp);

        handleInput();
    }

    public void handleInput() {
        deleteItem.setOnAction(actionEvent -> Editor.deleteSelectedGameObject());

        moveUp.setOnAction(actionEvent -> Editor.moveSelectedObjectUp());
    }

}
