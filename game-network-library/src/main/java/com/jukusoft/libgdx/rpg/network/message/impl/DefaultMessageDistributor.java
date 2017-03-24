package com.jukusoft.libgdx.rpg.network.message.impl;

import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageDistributor;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 23.03.2017.
 */
public class DefaultMessageDistributor extends SimpleChannelInboundHandler<NetMessage> implements MessageDistributor<NetMessage> {

    //map with all receivers
    protected Map<Integer,MessageReceiver<NetMessage>> receiverMap = new ConcurrentHashMap<>();

    //channel attributes
    protected ChannelAttributes attributes = null;

    public DefaultMessageDistributor (ChannelAttributes attributes) {
        this.attributes = attributes;
    }

    @Override public void addReceiver(int eventID, MessageReceiver<NetMessage> receiver) {
        this.receiverMap.put(eventID, receiver);
    }

    @Override public void removeReceiver(int eventID) {
        this.receiverMap.remove(eventID);
    }

    @Override protected void channelRead0(ChannelHandlerContext ctx, NetMessage msg)
        throws Exception {
        System.out.println("message received, eventID: " + msg.getEventID());

        //find receiver by eventID
        MessageReceiver<NetMessage> receiver = receiverMap.get(msg.getEventID());

        if (receiver == null) {
            System.err.println("no receiver found for message eventID: " + msg.getEventID() + ", skip " + msg.getContentLengthInBytes() + " bytes.");

            for (int i = 0; i < msg.getContentLengthInBytes(); i++) {
                //read bytes from discarded message
                msg.content().readByte();
            }
        } else {
            //call message receiver
            receiver.onReceive(ctx, attributes.getChannelID(), this.attributes, msg);
        }
    }
}
