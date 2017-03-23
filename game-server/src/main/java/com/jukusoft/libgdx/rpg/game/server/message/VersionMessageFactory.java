package com.jukusoft.libgdx.rpg.game.server.message;

import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 22.03.2017.
 */
public class VersionMessageFactory {

    public static NetMessage createMessage (int version, String versionStr) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write integer version
        byteBuf.writeInt(version);

        byte[] bytes = ByteUtils.getBytesFromString(versionStr, StandardCharsets.UTF_8);

        //write length of string
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return new NetMessage(ServerMessageID.VERSION_MESSAGE_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
