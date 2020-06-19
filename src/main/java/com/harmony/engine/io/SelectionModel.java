package com.harmony.engine.io;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionModel {

    public ArrayList<TreeItem<String>> model = new ArrayList<>();

    @SafeVarargs
    public final void setSelection(TreeItem<String>... selections) {
        this.clear();
        this.addToSelection(selections);
    }

    @SafeVarargs
    public final void addToSelection(TreeItem<String>... selections) { model.addAll(Arrays.asList(selections)); }

    @SafeVarargs
    public final void removeFromSelection(TreeItem<String>... selections) {
        for(int i = 0; i < selections.length; i++) { model.remove(selections[i]); }
    }

    public boolean contains(TreeItem<String> item) { return model.contains(item); }

    public void clear() { model.clear(); }

    @Override
    public String toString() {
        return "Selection Model: { " + model.toString() + " }";
    }
}
