package com.harmony.engine.math;

import java.io.Serializable;

/**
 * The type Scale.
 */
public class Scale implements Serializable {

    /**
     * The Width.
     */
    public int width, /**
     * The Height.
     */
    height;

    /**
     * Instantiates a new Scale.
     *
     * @param width  the width
     * @param height the height
     */
    public Scale(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Instantiates a new Scale.
     */
    public Scale() { this(0, 0); }

    @Override public String toString() { return String.format("Width: %s, Height: %s", width, height); }
}
