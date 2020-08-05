/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils.math;

public class MathUtils {

    public static byte replicateNumberSign(double value) {
        if(value > 0) return 1;
        else return -1;
    }

}
