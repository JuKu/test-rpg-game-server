package com.jukusoft.libgdx.rpg.game.server.listener;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 22.03.2017.
 */
@FunctionalInterface
public interface ConnectionInitListener {

    public void onConnectinInit (long connID, ChannelHandlerContext ctx);

}
