package com.harmony.engine.utils;

import com.harmony.engine.EngineController;
import com.harmony.engine.math.Vector2f;
import javafx.scene.paint.Color;

public class Status {

    public enum Type {
        READY("Ready", Color.color(0, 1, 0)),
        STAND_BY("Stand-By", Color.color(1, 1, 0)),
        SAVING("Saving", Color.color(0, 1, 1));

        private String tag;
        public Color color;

        Type(String tag, Color color) {
            this.tag = tag;
            this.color = color;
        }

        @Override public String toString() { return tag; }
    }

    public static void setCurrentStatus(Type type) {
        EngineController.setStatusLabel(type.toString(), type.color);
    }

    public static void setUtilityText(String content) {
        EngineController.staticUtilityLabel.setText(content == null ? "" : content);
    }

    public static void setMousePosition(Vector2f position) {
        if(position == null) {
            setUtilityText(null);
            return;
        }

        setUtilityText(String.format("(%s)", position.toString()));
    }
}
