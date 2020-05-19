package com.harmony.engine.core.parameters;

import com.harmony.engine.core.io.Display;

import java.awt.*;

/**
 * The type Display Parameters is used to set the parameters that the display will follow.
 */
public class DisplayParameters {

    /**
     * The constant DEFAULT_DISPLAY_PARAMETERS is a set of default display parameters.
     */
    public static final DisplayParameters DEFAULT_DISPLAY_PARAMETERS = new DisplayParameters();

    /**
     * The Title of the display.
     */
    public String title = "Harmony Engine v1.0";

    /**
     * The Starting width of the display.
     */
    public int startingWidth = 1280;
    /**
     * The Starting height of the display.
     */
    public int startingHeight = 720;

    /**
     * The Starting Background color of the frame.
     */
    public Color backgroundColor = Color.BLACK;

}
