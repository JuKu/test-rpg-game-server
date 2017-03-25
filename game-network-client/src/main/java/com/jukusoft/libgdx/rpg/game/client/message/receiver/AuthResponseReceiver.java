package com.jukusoft.libgdx.rpg.game.client.message.receiver;

import com.jukusoft.libgdx.rpg.game.client.listener.AuthListener;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 24.03.2017.
 */
public class AuthResponseReceiver implements MessageReceiver<NetMessage> {

    protected AuthListener authListener = null;

    public AuthResponseReceiver (AuthListener authListener) {
        if (authListener == null) {
            throw new NullPointerException("auth listener cannot be null.");
        }

        this.authListener = authListener;
    }

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        //read success flag, errorCode and userID
        boolean success = msg.content().readBoolean();
        int errorCode = msg.content().readInt();
        long userID = msg.content().readLong();

        //read message
        int messageLength = msg.content().readInt();
        byte[] messageBytes = new byte[messageLength];

        for (int i = 0; i < messageLength; i++) {
            messageBytes[i] = msg.content().readByte();
        }

        //convert bytes to string
        String message = ByteUtils.getStringFromBytes(messageBytes, StandardCharsets.UTF_8);

        if (success) {
            attributes.setAuth(userID, "You");
        }

        //call listener
        this.authListener.onAuth(success, errorCode, userID, message);
    }

}
