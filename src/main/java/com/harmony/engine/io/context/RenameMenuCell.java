/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.context;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.converter.DefaultStringConverter;

public class RenameMenuCell extends TextFieldTreeCell<String> {

    private final ContextMenu menu = new ContextMenu();

    public RenameMenuCell() {
        super(new DefaultStringConverter());

        MenuItem renameItem = new MenuItem("Rename");
        menu.getItems().add(renameItem);
        renameItem.setOnAction(actionEvent -> {
            super.startEdit();
        });
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEditable()) setContextMenu(menu);
    }

}
