package com.jukusoft.libgdx.rpg.game.client.message;

import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.game.client.entry.CharacterPosEntry;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Justin on 23.03.2017.
 */
public class PlayerPosMessageFactory {

    public static NetMessage createMessage (long sectorID, int layerID, long instanceID, float x, float y, float angle, float speedX, float speedY) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write sector coordinates
        byteBuf.writeLong(sectorID);
        byteBuf.writeInt(layerID);
        byteBuf.writeLong(instanceID);

        //write player position
        byteBuf.writeFloat(x);
        byteBuf.writeFloat(y);

        //write velocity & direction
        byteBuf.writeFloat(angle);
        byteBuf.writeFloat(speedX);
        byteBuf.writeFloat(speedY);

        return new NetMessage(ClientMessageID.SEND_PLAYER_POS_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

    public static NetMessage createMessage (CharacterPosEntry character) {
        ByteBuf byteBuf = Unpooled.buffer();

        //write sector coordinates
        byteBuf.writeLong(character.getSectorID());
        byteBuf.writeInt(character.getLayerID());
        byteBuf.writeLong(character.getInstanceID());

        //write player position
        byteBuf.writeFloat(character.getX());
        byteBuf.writeFloat(character.getY());

        //write velocity & direction
        byteBuf.writeFloat(character.getAngle());
        byteBuf.writeFloat(character.getSpeedX());
        byteBuf.writeFloat(character.getSpeedY());

        return new NetMessage(ClientMessageID.SEND_PLAYER_POS_EVENTID, 1, TimeUtils.getCurrentTime(), byteBuf);
    }

}
