/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils.math;

public class Vector2f {

    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this(0f, 0f);
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f inverse() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    public Vector2f abs() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }

    public Vector2f set(Vector2f r) {
        this.x = r.x;
        this.y = r.y;
        return this;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f copy() {
        return new Vector2f(this.x, this.y);
    }

    @Override public String toString() {
        return String.format("xValue: %s, yValue: %s", this.x, this.y);
    }

    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2f mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2f div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vector2f add(Vector2f r) {
        this.x += r.x;
        this.y += r.y;
        return this;
    }

    public Vector2f sub(Vector2f r) {
        this.x -= r.x;
        this.y -= r.y;
        return this;
    }

    public Vector2f mul(Vector2f r) {
        this.x *= r.x;
        this.y *= r.y;
        return this;
    }

    public Vector2f div(Vector2f r) {
        this.x /= r.x;
        this.y /= r.y;
        return this;
    }
}
