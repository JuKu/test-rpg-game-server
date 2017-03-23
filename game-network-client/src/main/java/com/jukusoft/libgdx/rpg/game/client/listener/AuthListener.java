package com.jukusoft.libgdx.rpg.game.client.listener;

/**
 * Created by Justin on 23.03.2017.
 */
@FunctionalInterface
public interface AuthListener {

    public void onAuth (boolean success, int errorCode, String message);

}
