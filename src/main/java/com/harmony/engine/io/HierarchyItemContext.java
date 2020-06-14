package com.harmony.engine.io;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class HierarchyItemContext extends ContextMenu {

    private MenuItem deleteItem;

    public HierarchyItemContext() {
        deleteItem = new MenuItem("Delete Game Object");

        super.getItems().add(deleteItem);

        // Eg. context.getItems().addAll(item1, item2, new SperatorMenuItem(), item3);

        handleInput();
    }

    public void handleInput() {
        deleteItem.setOnAction(actionEvent -> Editor.deleteSelectedGameObject());
    }

}
