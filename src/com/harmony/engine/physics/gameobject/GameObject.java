package com.harmony.engine.physics.gameobject;

import com.harmony.engine.math.Orientation;
import com.harmony.engine.math.Scale;
import com.harmony.engine.math.Vector2f;

import java.awt.*;

/**
 * The type Game object.
 */
public abstract class GameObject {

    /**
     * The Position.
     */
    public Vector2f position;
    /**
     * The Scale.
     */
    public Scale scale;
    /**
     * The Orientation.
     */
    public Orientation orientation;

    /**
     * Instantiates a new Game object.
     *
     * @param position    the position
     * @param scale       the scale
     * @param orientation the orientation
     */
    public GameObject(Vector2f position, Scale scale, Orientation orientation) {
        this.position = position;
        this.scale = scale;
        this.orientation = orientation;
    }

    /**
     * Instantiates a new Game object.
     *
     * @param position the position
     * @param scale    the scale
     */
    public GameObject(Vector2f position, Scale scale) {
        this(position, scale, new Orientation());
    }

    /**
     * Instantiates a new Game object.
     *
     * @param position the position
     */
    public GameObject(Vector2f position) {
        this(position, new Scale(), new Orientation());
    }

    /**
     * On create.
     */
    public abstract void onCreate();

    /**
     * Update.
     */
    public abstract void update();

    /**
     * Render.
     *
     * @param g the g
     */
    public abstract void render(Graphics2D g);

    /**
     * On destroy.
     */
    public abstract void onDestroy();

}
