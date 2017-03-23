package com.jukusoft.libgdx.rpg.game.client.message.receiver;

import com.jukusoft.libgdx.rpg.game.client.message.RTTAnswerMessageFactory;
import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 23.03.2017.
 */
public class RTTReceiver implements MessageReceiver<NetMessage> {

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        if (msg.getEventID() == ServerMessageID.RTT_MESSAGE_EVENTID) {
            System.out.println("round-trip-time message received.");

            //read sender timestamp
            long timestamp = msg.content().readLong();

            System.out.println("RTT message was sended at " + timestamp);

            //send RTT answer message
            ctx.writeAndFlush(RTTAnswerMessageFactory.createMessage(timestamp));
        } else if (msg.getEventID() == ServerMessageID.PING_INFO_EVENTID) {
            //read current ping
            int ping = msg.content().readInt();

            System.out.println("current connection ping received: " + ping + "ms.");

            //set ping to attributes
            attributes.setPing(ping);
        } else {
            throw new IllegalStateException("unknown message eventID: " + msg.getEventID());
        }
    }

}
