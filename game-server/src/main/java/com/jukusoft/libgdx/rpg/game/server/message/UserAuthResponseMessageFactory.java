package com.jukusoft.libgdx.rpg.game.server.message;

import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 23.03.2017.
 */
public class UserAuthResponseMessageFactory {

    public static NetMessage createMessage (boolean success, int errorCode, long userID, String message) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write success flag and errorCode
        byteBuf.writeBoolean(success);
        byteBuf.writeInt(errorCode);

        //write userID
        byteBuf.writeLong(userID);

        //write message
        byte[] messageBytes = ByteUtils.getBytesFromString(message, StandardCharsets.UTF_8);
        byteBuf.writeInt(messageBytes.length);
        byteBuf.writeBytes(messageBytes);

        return new NetMessage(ServerMessageID.AUTH_USER_RESPONSE_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
