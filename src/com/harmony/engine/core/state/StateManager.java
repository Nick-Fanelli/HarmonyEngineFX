package com.harmony.engine.core.state;

import java.awt.*;

/**
 * The type State manager is used to set the current state.
 */
public class StateManager {

    private static State currentState = null;

    public static void setCurrentState(State state) {
        if(currentState != null) currentState.onDestroy();
        state.onCreate();
        currentState = state;
    }

    public void update() {
        if(currentState == null) return;
        currentState.update();
    }

    public void render(Graphics2D g) {
        if(currentState == null) return;
        currentState.render();
    }

}
