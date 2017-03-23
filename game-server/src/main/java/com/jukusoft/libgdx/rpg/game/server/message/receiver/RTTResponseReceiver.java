package com.jukusoft.libgdx.rpg.game.server.message.receiver;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.game.server.message.PingMessageFactory;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 23.03.2017.
 */
public class RTTResponseReceiver implements MessageReceiver<NetMessage> {

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        if (msg.getEventID() == ClientMessageID.RTT_RESPONSE_EVENTID) {
            System.out.println("round-trip-time message received.");

            //read first sender timestamp (server --> client --> server)
            long timestamp = msg.content().readLong();

            System.out.println("RTT response message received, original timestamp: " + timestamp);

            long now = TimeUtils.getCurrentTime();

            //calculate ping
            int rtt = (int) ((now - timestamp));
            int ping = rtt / 2;

            //set channel attributes
            attributes.setRTT(rtt);
            attributes.setPing(ping);

            System.out.println("calculated RTT: " + rtt);
            System.out.println("calculated ping: " + ping);

            //send ping info message
            ctx.writeAndFlush(PingMessageFactory.createMessage(ping));
        } else {
            throw new IllegalStateException("unknown message eventID: " + msg.getEventID());
        }
    }

}
