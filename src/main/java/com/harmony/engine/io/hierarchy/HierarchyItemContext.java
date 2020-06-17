package com.harmony.engine.io.hierarchy;

import com.harmony.engine.io.Editor;
import com.harmony.engine.io.NewEditor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class HierarchyItemContext extends ContextMenu {

    private final MenuItem moveUp;
    private final MenuItem moveDown;

    private final MenuItem find;

    private final MenuItem deleteItem;

    public HierarchyItemContext() {
        moveUp = new MenuItem("Move Up");
        moveDown = new MenuItem("Move Down");

        find = new MenuItem("Find");

        deleteItem = new MenuItem("Delete Game Object");

        super.getItems().addAll(moveUp, moveDown, new SeparatorMenuItem(), find,
                new SeparatorMenuItem(), deleteItem);

        handleInput();
    }

    public void handleInput() {
        deleteItem.setOnAction(actionEvent -> NewEditor.deleteSelectedGameObjects());

        moveUp.setOnAction(actionEvent -> Editor.moveSelectedObjectUp());

        find.setOnAction(actionEvent -> NewEditor.findSelectedGameObject());
    }

}
