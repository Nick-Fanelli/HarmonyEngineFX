package engine.test;

import com.harmony.engine.core.Harmony;
import com.harmony.engine.core.parameters.Parameters;
import com.harmony.engine.core.state.StateManager;
import engine.test.states.GameState;

public class Launcher {

    public static void main(String[] args) {
        Harmony harmony = new Harmony(Parameters.DEFAULT_PARAMETERS);
        harmony.run();

        StateManager.setCurrentState(new GameState());
    }

}
