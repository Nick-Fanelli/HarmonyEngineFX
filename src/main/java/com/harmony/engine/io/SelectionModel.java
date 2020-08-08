/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io;

import com.harmony.engine.event.AbstractEventHandler;
import com.harmony.engine.event.SelectionEventHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionModel<T> {

    public ArrayList<T> model = new ArrayList<>();
    public AbstractEventHandler<SelectionEventHandler> handler = new AbstractEventHandler<>();

    @SafeVarargs
    public final void setSelection(T... selections) {
        this.clear();
        this.addToSelection(selections);
    }

    @SafeVarargs
    public final void addToSelection(T... selections) {
        model.addAll(Arrays.asList(selections));

        for(SelectionEventHandler handler : handler.getEventHandlers()) handler.onSelectionChange();
    }

    @SafeVarargs
    public final void removeFromSelection(T... selections) {
        for (T selection : selections) { model.remove(selection); }

        for(SelectionEventHandler handler : handler.getEventHandlers()) handler.onSelectionChange();
    }

    public boolean contains(T item) { return model.contains(item); }

    public void clear() {
        model.clear();

        for(SelectionEventHandler handler : handler.getEventHandlers()) handler.onSelectionChange();
    }

    @Override
    public String toString() {
        return "Selection Model: { " + model.toString() + " }";
    }
}
