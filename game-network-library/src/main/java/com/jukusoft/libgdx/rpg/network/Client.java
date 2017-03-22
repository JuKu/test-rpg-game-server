package com.jukusoft.libgdx.rpg.network;

import io.netty.channel.Channel;

/**
 * Created by Justin on 22.03.2017.
 */
public interface Client {

    public void connect (String ip, int port);

    public void connectAsync (String ip, int port);

    public void close ();

    public void shutdown ();

    public boolean isConnected ();

    public Channel getChannel ();

}
