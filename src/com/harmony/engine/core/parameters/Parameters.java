package com.harmony.engine.core.parameters;

/**
 * The type Parameters is used to set the parameters that the engine will follow.
 */
public class Parameters {

    /**
     * The constant DEFAULT_PARAMETERS is a set of default parameters.
     */
    public static final Parameters DEFAULT_PARAMETERS = new Parameters();

    /**
     * The Display parameters of the program.
     */
    public DisplayParameters displayParameters = DisplayParameters.DEFAULT_DISPLAY_PARAMETERS;

    /**
     * The Target fps of the program.
     */
    public int targetFps = 60;

}
