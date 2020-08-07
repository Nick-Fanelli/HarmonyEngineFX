/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils.math;

import java.awt.*;

public class Transform {

    public Vector2f position;
    public Dimension scale;

    public Transform(Vector2f position, Dimension scale) {
        this.position = position;
        this.scale = scale;
    }

    @Override
    public String toString() {
        return String.format("Transform:[ Position:Vector2f=[ %s ], Scale:Dimension=[ width=%s, height=%s ] ]", position.toString(),
                scale.width, scale.height);
    }
}
