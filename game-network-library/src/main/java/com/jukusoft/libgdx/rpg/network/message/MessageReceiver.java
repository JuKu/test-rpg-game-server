package com.jukusoft.libgdx.rpg.network.message;

import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 23.03.2017.
 */
public interface MessageReceiver<T extends ByteBufHolder> {

    public void onReceive (ChannelHandlerContext ctx, long connID, ChannelAttributes attributes, T msg);

}
