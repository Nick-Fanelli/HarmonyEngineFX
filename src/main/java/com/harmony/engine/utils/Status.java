package com.harmony.engine.utils;

import com.harmony.engine.EngineController;
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
}
