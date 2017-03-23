package com.jukusoft.libgdx.rpg.game.client.message;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Justin on 23.03.2017.
 */
public class PingCheckMessageFactory {

    /**
    * create message to request an ping check
    */
    public static NetMessage createMessage () {
        ByteBuf byteBuf = Unpooled.buffer();

        //write current timestamp
        byteBuf.writeLong(System.currentTimeMillis());

        return new NetMessage(ClientMessageID.REQUEST_PING_CHECK_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
