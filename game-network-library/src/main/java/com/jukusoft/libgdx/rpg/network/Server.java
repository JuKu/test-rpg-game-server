package com.jukusoft.libgdx.rpg.network;

/**
 * Created by Justin on 22.03.2017.
 */
public interface Server {

    public void start (String interfaceName, int port);

    public void start (int port);

    public void startAsync (String interfaceName, int port);

    public void startAsync (int port);

    public void shutdownServer ();

}
