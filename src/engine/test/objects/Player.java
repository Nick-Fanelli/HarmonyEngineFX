package engine.test.objects;

import com.harmony.engine.math.Scale;
import com.harmony.engine.math.Vector2f;
import com.harmony.engine.physics.gameobject.GameObject;

import java.awt.*;

public class Player extends GameObject {

    public Player(Vector2f position) {
        super(position, new Scale(64, 64));
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {

    }

    @Override
    public void onDestroy() {

    }
}
