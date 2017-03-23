package com.jukusoft.libgdx.rpg.game.server.message;

import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Justin on 22.03.2017.
 */
public class RTTMessageFactory {

    public static NetMessage createMessage () {
        ByteBuf byteBuf = Unpooled.buffer();

        //write current timestamp
        byteBuf.writeLong(TimeUtils.getCurrentTime());

        return new NetMessage(ServerMessageID.RTT_MESSAGE_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
