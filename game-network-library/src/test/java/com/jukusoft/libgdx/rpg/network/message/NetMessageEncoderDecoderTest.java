package com.jukusoft.libgdx.rpg.network.message;

import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Justin on 04.01.2017.
 */
public class NetMessageEncoderDecoderTest {

    @Test(timeout = 5000)
    public void testEncoderAndDecoder () {
        //create new embedded netty channel to simulate netty server
        EmbeddedChannel channel = new EmbeddedChannel(new NetMessageEncoder(), new NetMessageDecoder());

        //set variables
        int eventID = 1;
        int version = 20;
        long timestamp = System.currentTimeMillis();
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ByteUtils.getBytesFromString("test", StandardCharsets.UTF_8));

        //create new message object
        NetMessage message = new NetMessage(eventID, version, timestamp, byteBuf);

        //write message to simulated netty channel
        channel.writeOutbound(message);
        channel.writeInbound((Object) channel.readOutbound());

        NetMessage receivedMessage = (NetMessage) channel.readInbound();

        assertNotNull("receivedMessage is null.", receivedMessage);
        assertEquals("eventID of original and received message isnt the same.", message.getEventID(), receivedMessage.getEventID());
        assertEquals("version of original and received message isnt the same.", message.getVersion(), receivedMessage.getVersion());
        assertEquals("timestamp of original and received message isnt the same.", message.getTimestamp(), receivedMessage.getTimestamp());
        assertEquals("content of received message isnt equal to original message.", ByteUtils.getStringFromMessage(receivedMessage, StandardCharsets.UTF_8), "test");
    }

}
