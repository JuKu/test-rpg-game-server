package com.jukusoft.libgdx.rpg.game.server.message;

import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Justin on 23.03.2017.
 */
public class PingMessageFactory {

    public static NetMessage createMessage (int ping) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write current ping
        byteBuf.writeInt(ping);

        return new NetMessage(ServerMessageID.PING_INFO_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
