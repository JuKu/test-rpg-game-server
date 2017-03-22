package com.jukusoft.libgdx.rpg.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Justin on 04.01.2017.
 */
public class NetMessageTest {

    @Test
    public void testConstructor () {
        int eventID = 10;
        int version = 20;
        long timestamp = System.currentTimeMillis();

        //create new byte buffer and write and long
        ByteBuf content = Unpooled.buffer();
        content.writeLong(30l);

        //create new message
        NetMessage message = new NetMessage(eventID, version, timestamp, content);

        assertEquals("eventID isnt equals.", eventID, message.getEventID());
        assertEquals("version isnt equals.", version, message.getVersion());
        assertEquals("timestamp isnt equals", timestamp, message.getTimestamp());
        assertEquals("readed long isnt equals", 30l, message.content().readLong());
    }

}
