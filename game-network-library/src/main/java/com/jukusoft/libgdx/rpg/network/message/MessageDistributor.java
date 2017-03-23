package com.jukusoft.libgdx.rpg.network.message;

import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelInboundHandler;

/**
 * Created by Justin on 23.03.2017.
 */
public interface MessageDistributor<T extends ByteBufHolder> extends ChannelInboundHandler {

    public void addReceiver (int eventID, MessageReceiver<T> receiver);

    public void removeReceiver (int eventID);

}
