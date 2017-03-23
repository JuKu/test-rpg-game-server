package com.jukusoft.libgdx.rpg.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Justin on 04.01.2017.
 */
public class NetMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            //because we want to use length as an integer, we needs 4 bytes
            if (in.readableBytes() < 4)
                return;

            in.markReaderIndex();

            int length = in.readInt();

            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            //read eventID, version and timestamp from byte stream
            final int eventID = in.readInt();
            final int version = in.readInt();
            final long timestamp = in.readLong();

            //read content
            ByteBuf content = in.slice(in.readerIndex(), length - NetMessage.HEADER_LENGHT);
            in.skipBytes(length - NetMessage.HEADER_LENGHT);

            // increment refcount since content is a slice of in and
            // ByteToMessageDecoder will release in
            content.retain();

            NetMessage message = new NetMessage(eventID, version, timestamp, content);

            //set content length
            message.setContentLengthInBytes(length - NetMessage.HEADER_LENGHT);

            out.add(message);
        } catch (Exception e) {
            throw e;
        }
    }

}
