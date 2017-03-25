package com.jukusoft.libgdx.rpg.game.server.message.receiver;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Justin on 23.03.2017.
 */
public class PlayerPosMessageReceiver implements MessageReceiver<NetMessage> {

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        if (msg.getEventID() == ClientMessageID.SEND_PLAYER_POS_EVENTID) {
            //read sector coordinates
            long sectorID = msg.content().readLong();
            int layerID = msg.content().readInt();
            long instanceID = msg.content().readLong();

            //get player position
            float x = msg.content().readFloat();
            float y = msg.content().readFloat();

            float angle = msg.content().readFloat();
            float speedX = msg.content().readFloat();
            float speedY = msg.content().readFloat();

            System.out.println("player position received: " + x + ", y: " + y);
        } else {
            throw new IllegalStateException("unknown message eventID: " + msg.getEventID());
        }
    }

}
