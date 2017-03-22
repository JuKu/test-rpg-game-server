package com.jukusoft.libgdx.rpg.network.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 22.03.2017.
 */
@ChannelHandler.Sharable
public class ChannelInitializationHandler extends ChannelHandlerAdapter {

    protected NettyServer server = null;

    public ChannelInitializationHandler (NettyServer server) {
        this.server = server;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        try {
            //initialize pipeline and add handler
            this.server.initPipeline(ctx.pipeline());
        } catch (Exception e) {
            throw e;
        }
    }

}
