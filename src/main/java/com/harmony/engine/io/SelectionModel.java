/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionModel<T> {

    public ArrayList<T> model = new ArrayList<>();

    @SafeVarargs
    public final void setSelection(T... selections) {
        this.clear();
        this.addToSelection(selections);
    }

    @SafeVarargs
    public final void addToSelection(T... selections) { model.addAll(Arrays.asList(selections)); }

    @SafeVarargs
    public final void removeFromSelection(T... selections) {
        for (T selection : selections) { model.remove(selection); }
    }

    public boolean contains(T item) { return model.contains(item); }

    public void clear() { model.clear(); }

    @Override
    public String toString() {
        return "Selection Model: { " + model.toString() + " }";
    }
}
