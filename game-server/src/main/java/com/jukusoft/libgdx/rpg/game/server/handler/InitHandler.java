package com.jukusoft.libgdx.rpg.game.server.handler;

import com.jukusoft.libgdx.rpg.game.server.listener.ConnectionClosedListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Justin on 22.03.2017.
 */
public class InitHandler extends ChannelInboundHandlerAdapter {

    protected int receivedMessages = 0;

    //connection ID
    protected AtomicLong connID = new AtomicLong(0);

    protected ConnectionClosedListener closedListener = null;

    public InitHandler (long connID) {
        this.connID.set(connID);
    }

    public void setClosedListener (ConnectionClosedListener closedListener) {
        this.closedListener = closedListener;
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //ctx.

        super.channelRegistered(ctx);
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.receivedMessages++;

        super.channelRead(ctx, msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause.getLocalizedMessage().contains("Eine vorhandene Verbindung wurde vom Remotehost geschlossen")) {
            System.out.println("connection closed (ID: " + connID + ").");

            if (this.closedListener != null) {
                //call listener
                this.closedListener.onConnectionClosed(this.connID.get(), ctx);
            }
        } else {
            System.out.println("InitHandler: exception caught.");
            cause.printStackTrace();
        }
    }

    public long getConnectionID () {
        return this.connID.get();
    }

}
