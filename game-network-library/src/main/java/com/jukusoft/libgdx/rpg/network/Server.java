package com.jukusoft.libgdx.rpg.network;

import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import io.netty.buffer.ByteBufHolder;

/**
 * Created by Justin on 22.03.2017.
 */
public interface Server<T extends ByteBufHolder> {

    public void start (String interfaceName, int port);

    public void start (int port);

    public void startAsync (String interfaceName, int port);

    public void startAsync (int port);

    public void shutdownServer ();

    public void sendBroadcastMessage (T message);

}
