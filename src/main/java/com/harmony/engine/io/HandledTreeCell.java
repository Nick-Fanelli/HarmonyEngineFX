package com.harmony.engine.io;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class HandledTreeCell extends TreeCell<String> {

    private TextField textField;
    private ContextMenu deleteMenu = new ContextMenu();

    public HandledTreeCell() {
        MenuItem deleteMenuItem = new MenuItem("Delete Game Object");
        deleteMenu.getItems().add(deleteMenuItem);
        deleteMenuItem.setOnAction(actionEvent -> {
            System.out.println("Delete " + actionEvent.getSource());
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
                if (!getTreeItem().isLeaf()&&getTreeItem().getParent()!= null) {
                    setContextMenu(deleteMenu);
                }
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}
