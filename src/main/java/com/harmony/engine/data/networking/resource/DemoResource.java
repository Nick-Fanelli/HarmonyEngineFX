/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data.networking.resource;

public class DemoResource extends NetResource {

    public static final DemoResource RESOURCE_CONTEXT = new DemoResource();

    public DemoResource() {
        super("https://harmonyengines.com/resources/demo", new String[] {
                "Tileset.png"
        });
    }
}
