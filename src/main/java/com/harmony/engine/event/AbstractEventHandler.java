/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.event;

import java.util.ArrayList;

public class AbstractEventHandler<T extends EventHandler> {

    private final ArrayList<T> handlers = new ArrayList<>();

    public void addEventListener(T handler) {
        this.handlers.add(handler);
    }

    public ArrayList<T> getEventHandlers() { return handlers; }

}