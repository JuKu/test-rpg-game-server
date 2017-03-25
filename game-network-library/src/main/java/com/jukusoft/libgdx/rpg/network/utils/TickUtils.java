package com.jukusoft.libgdx.rpg.network.utils;

/**
 * Created by Justin on 23.03.2017.
 */
public class TickUtils {

    public static final long TICK_LENGTH = 20;

    public static long getTickLength () {
        return TICK_LENGTH;
    }

    public static long getTick (long tickLength) {
        return System.currentTimeMillis() / tickLength;
    }

}
