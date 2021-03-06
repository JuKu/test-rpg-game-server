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
    protected volatile int openConnections = 0;

    public ChannelInitializationHandler (NettyServer server) {
        this.server = server;
    }

    @Override
    public void handlerAdded (ChannelHandlerContext ctx) throws Exception {
        try {
            //initialize pipeline and add handler
            this.server.initPipeline(ctx.pipeline());

            this.openConnections++;
        } catch (Exception e) {
            throw e;
        }

        //add channel to group
        this.server.getAllChannels().add(ctx.channel());
        this.server.listChannels().add(ctx.channel());

        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved (ChannelHandlerContext ctx) throws Exception {
        this.openConnections--;

        //remove channel from group
        this.server.getAllChannels().remove(ctx.channel());

        super.handlerRemoved(ctx);
    }

    public int countOpenConnections () {
        return this.openConnections;
    }

}
