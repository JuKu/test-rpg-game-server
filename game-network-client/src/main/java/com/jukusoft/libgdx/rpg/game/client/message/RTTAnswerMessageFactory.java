package com.jukusoft.libgdx.rpg.game.client.message;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Justin on 23.03.2017.
 */
public class RTTAnswerMessageFactory {

    public static NetMessage createMessage (long senderTimestamp) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write current timestamp
        byteBuf.writeLong(senderTimestamp);

        return new NetMessage(ClientMessageID.RTT_RESPONSE_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
