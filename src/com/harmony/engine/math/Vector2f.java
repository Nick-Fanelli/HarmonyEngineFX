package com.harmony.engine.math;

import java.io.Serializable;

/**
 * The type Vector 2 f.
 */
public class Vector2f implements Serializable {

    /**
     * The X.
     */
    public float x, /**
     * The Y.
     */
    y;

    /**
     * Instantiates a new Vector 2 f.
     *
     * @param x the x
     * @param y the y
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a new Vector 2 f.
     */
    public Vector2f() { this(0, 0); }

    /**
     * Instantiates a new Vector 2 f.
     *
     * @param r the r
     */
    public Vector2f(Vector2f r) { this(r.x, r.y); }

    /**
     * Add.
     *
     * @param x the x
     * @param y the y
     */
    public void add(float x, float y) { this.x += x; this.y += y;}

    /**
     * Sub.
     *
     * @param x the x
     * @param y the y
     */
    public void sub(float x, float y) { this.x -= x; this.y -= y;}

    /**
     * Mul.
     *
     * @param x the x
     * @param y the y
     */
    public void mul(float x, float y) { this.x *= x; this.y *= y;}

    /**
     * Div.
     *
     * @param x the x
     * @param y the y
     */
    public void div(float x, float y) { this.x /= x; this.y /= y;}

    /**
     * Add.
     *
     * @param r the r
     */
    public void add(Vector2f r) { this.x += r.x; this.y += r.y;}

    /**
     * Sub.
     *
     * @param r the r
     */
    public void sub(Vector2f r) { this.x -= r.x; this.y -= r.y;}

    /**
     * Mul.
     *
     * @param r the r
     */
    public void mul(Vector2f r) { this.x *= r.x; this.y *= r.y;}

    /**
     * Div.
     *
     * @param r the r
     */
    public void div(Vector2f r) { this.x /= r.x; this.y /= r.y;}

    /**
     * Add context vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the vector 2 f
     */
    public Vector2f addContext(float x, float y) { return new Vector2f(this.x + x, this.y + y); }

    /**
     * Sub context vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the vector 2 f
     */
    public Vector2f subContext(float x, float y) { return new Vector2f(this.x - x, this.y - y); }

    /**
     * Mul context vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the vector 2 f
     */
    public Vector2f mulContext(float x, float y) { return new Vector2f(this.x * x, this.y * y); }

    /**
     * Div context vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the vector 2 f
     */
    public Vector2f divContext(float x, float y) { return new Vector2f(this.x / x, this.y / y); }

    /**
     * Add context vector 2 f.
     *
     * @param r the r
     * @return the vector 2 f
     */
    public Vector2f addContext(Vector2f r) { return new Vector2f(this.x + r.x, this.y + r.y); }

    /**
     * Sub context vector 2 f.
     *
     * @param r the r
     * @return the vector 2 f
     */
    public Vector2f subContext(Vector2f r) { return new Vector2f(this.x - r.x, this.y - r.y); }

    /**
     * Mul context vector 2 f.
     *
     * @param r the r
     * @return the vector 2 f
     */
    public Vector2f mulContext(Vector2f r) { return new Vector2f(this.x * r.x, this.y * r.y); }

    /**
     * Div context vector 2 f.
     *
     * @param r the r
     * @return the vector 2 f
     */
    public Vector2f divContext(Vector2f r) { return new Vector2f(this.x / r.x, this.y / r.y); }

    /**
     * Gets copy.
     *
     * @return the copy
     */
    public Vector2f getCopy() { return new Vector2f(this.x, this.y); }

    /**
     * Set.
     *
     * @param x the x
     * @param y the y
     */
    public void set(float x, float y) { this.x = x; this.y = y; }

    /**
     * Reset.
     */
    public void reset() { this.x = 0; this.y = 0; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2f)) {
            System.err.println("Can not compare Vector2f class with a type of " + obj.getClass().getSimpleName());
            return false;
        }

        return ((Vector2f) obj).x == this.x && ((Vector2f) obj).y == this.y;
    }

    @Override public String toString() { return String.format("X: %s, Y: %s", x, y); }
}
