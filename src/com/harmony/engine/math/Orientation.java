package com.harmony.engine.math;

import java.io.Serializable;

/**
 * The type Orientation.
 */
public class Orientation implements Serializable {

    /**
     * The Degrees.
     */
    public float degrees;

    /**
     * Instantiates a new Orientation.
     *
     * @param degrees the degrees
     */
    public Orientation(float degrees) {
        this.degrees = degrees;
    }

    /**
     * Instantiates a new Orientation.
     */
    public Orientation() { this(0); }

    /**
     * Gets degrees as radians.
     *
     * @return the degrees as radians
     */
    public double getDegreesAsRadians() { return (Math.PI / 180) * degrees; }
}
