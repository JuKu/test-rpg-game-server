package com.jukusoft.libgdx.rpg.game.server.listener;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 22.03.2017.
 */
@FunctionalInterface
public interface ConnectionClosedListener {

    public void onConnectionClosed (long connID, ChannelHandlerContext ctx);

}
