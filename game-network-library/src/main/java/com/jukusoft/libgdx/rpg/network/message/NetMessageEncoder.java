package com.jukusoft.libgdx.rpg.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Justin on 04.01.2017.
 */
public class NetMessageEncoder extends MessageToByteEncoder<NetMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NetMessage msg, ByteBuf out) throws Exception {
        try {
            //first, write header length
            out.writeInt(msg.getHeaderLenght() + msg.content().readableBytes());

            //write eventID
            out.writeInt(msg.getEventID());

            //write version of message
            out.writeInt(msg.getVersion());

            //write timestamp of message
            out.writeLong(msg.getTimestamp());

            //write content
            out.writeBytes(msg.content());
        } catch (Exception e) {
            throw e;
        }
    }

}
