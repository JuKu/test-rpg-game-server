package com.jukusoft.libgdx.rpg.game.server;

/**
 * Created by Justin on 22.03.2017.
 */
public interface GameServer {

    public void start ();

    public void shutdown ();

    public int getPort ();

    public int countOpenConnections ();

}
