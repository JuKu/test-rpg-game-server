package com.jukusoft.libgdx.rpg.game.client.message.receiver;

import com.jukusoft.libgdx.rpg.game.client.entry.CharacterPosEntry;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 25.03.2017.
 */
public class PlayerPosBroadcastMessageReceiver implements MessageReceiver<NetMessage> {

    protected Map<Long,CharacterPosEntry> characterPosEntryMap = new ConcurrentHashMap<>();
    protected List<CharacterPosEntry> allCharactersList = new ArrayList<>();

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        int length = msg.content().readInt();

        System.out.println("player position broadcast message received, characters on map: " + length);

        for (int i = 0; i < length; i++) {
            //read userID and username
            long userID = msg.content().readLong();

            int lengthOfUsername = msg.content().readInt();
            byte[] usernameBytes = new byte[lengthOfUsername];

            for (int k = 0; k < lengthOfUsername; k++) {
                usernameBytes[k] = msg.content().readByte();
            }

            String username = ByteUtils.getStringFromBytes(usernameBytes, StandardCharsets.UTF_8);

            float x = msg.content().readFloat();
            float y = msg.content().readFloat();

            float speedX = msg.content().readFloat();
            float speedY = msg.content().readFloat();

            float angle = 0;

            //find character
            CharacterPosEntry characterPosEntry = this.characterPosEntryMap.get(userID);

            if (characterPosEntry == null) {
                characterPosEntry = new CharacterPosEntry(userID, username);
                this.characterPosEntryMap.put(userID, characterPosEntry);

                synchronized (this.allCharactersList) {
                    this.allCharactersList.add(characterPosEntry);
                }
            }

            characterPosEntry.update(x, y, angle, speedX, speedY);
        }
    }

    public List<CharacterPosEntry> listAllCharacters () {
        return this.allCharactersList;
    }

}
