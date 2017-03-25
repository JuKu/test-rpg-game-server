package com.jukusoft.libgdx.rpg.game.server.message;

import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.game.server.game.Character;
import com.jukusoft.libgdx.rpg.game.server.game.CharacterManager;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Justin on 25.03.2017.
 */
public class PlayerPosBroadcastMessageFactory {

    protected static List<Character> tmpList = new ArrayList<>();

    public static NetMessage createMessage (CharacterManager characterManager) {
        ByteBuf byteBuf = Unpooled.buffer();

        tmpList.clear();
        tmpList.addAll(characterManager.listCharacters());

        //send length of list
        byteBuf.writeInt(tmpList.size());

        for (Character character : tmpList) {
            //write userID
            byteBuf.writeLong(character.getUserID());

            //write username
            byte[] usernameBytes = ByteUtils.getBytesFromString(character.getUsername(), StandardCharsets.UTF_8);
            byteBuf.writeInt(usernameBytes.length);
            byteBuf.writeBytes(usernameBytes);

            //write position
            byteBuf.writeFloat(character.getX());
            byteBuf.writeFloat(character.getY());

            //write speed
            byteBuf.writeFloat(character.getSpeedY());
            byteBuf.writeFloat(character.getSpeedY());
        }

        return new NetMessage(ServerMessageID.PLAYER_SYNC_BROADCAST, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
