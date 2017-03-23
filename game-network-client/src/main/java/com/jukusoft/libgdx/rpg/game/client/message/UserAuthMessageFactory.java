package com.jukusoft.libgdx.rpg.game.client.message;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 23.03.2017.
 */
public class UserAuthMessageFactory {

    public static NetMessage createMessage (String username, String password) {
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] usernameBytes = ByteUtils.getBytesFromString(username, StandardCharsets.UTF_8);
        byte[] passwordBytes = ByteUtils.getBytesFromString(password, StandardCharsets.UTF_8);

        //write length of username and bytes of username
        byteBuf.writeInt(usernameBytes.length);
        byteBuf.writeBytes(usernameBytes);

        //write length of password and bytes of password
        byteBuf.writeInt(passwordBytes.length);
        byteBuf.writeBytes(passwordBytes);

        return new NetMessage(ClientMessageID.TRY_AUTH_USER_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
