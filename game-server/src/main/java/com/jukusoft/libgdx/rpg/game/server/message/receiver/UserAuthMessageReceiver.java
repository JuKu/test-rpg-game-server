package com.jukusoft.libgdx.rpg.game.server.message.receiver;

import com.jukusoft.libgdx.rpg.database.user.UserEntry;
import com.jukusoft.libgdx.rpg.database.user.UserRepository;
import com.jukusoft.libgdx.rpg.game.client.ClientMessageID;
import com.jukusoft.libgdx.rpg.game.server.AuthErrorCode;
import com.jukusoft.libgdx.rpg.game.server.message.UserAuthResponseMessageFactory;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageReceiver;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import com.jukusoft.libgdx.rpg.network.utils.ByteUtils;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 23.03.2017.
 */
public class UserAuthMessageReceiver implements MessageReceiver<NetMessage> {

    protected UserRepository userRepository = null;

    public UserAuthMessageReceiver (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override public void onReceive(ChannelHandlerContext ctx, long connID, ChannelAttributes attributes,
        NetMessage msg) {
        if (msg.getEventID() == ClientMessageID.TRY_AUTH_USER_EVENTID) {
            //read username
            int usernameLength = msg.content().readInt();
            byte[] usernameBytes = new byte[usernameLength];

            for (int i = 0; i < usernameLength; i++) {
                usernameBytes[i] = msg.content().readByte();
            }

            String username = ByteUtils.getStringFromBytes(usernameBytes, StandardCharsets.UTF_8);

            //read password
            int passwordLength = msg.content().readInt();
            byte[] passwordBytes = new byte[passwordLength];

            for (int i = 0; i < passwordLength; i++) {
                passwordBytes[i] = msg.content().readByte();
            }

            String password = ByteUtils.getStringFromBytes(passwordBytes, StandardCharsets.UTF_8);

            System.out.println("try to authorize user '" + username + "'.");

            //find user by name
            UserEntry user = this.userRepository.findUserByName(username);

            if (user == null) {
                //cannot find user

                /**
                * auto registration
                */

                //generate new userID
                long userID = this.userRepository.generateUserID();

                //create new user
                user = new UserEntry(userID, username, password);

                //add user
                this.userRepository.add(userID, user);

                this.login(user, attributes);

                System.out.println("logged in user '" + username + "' (userID: " + user.getUserID() + " successful!");

                //send auth successful message
                ctx.writeAndFlush(UserAuthResponseMessageFactory.createMessage(true, AuthErrorCode.AUTH_SUCCESSFUL, user.getUserID(), "LOGIN_SUCCESSFUL"));
            } else {
                //check password
                if (user.checkPassword(password)) {
                    //login user
                    this.login(user, attributes);

                    System.out.println("logged in user '" + username + "' (userID: " + user.getUserID() + " successful!");

                    //send auth successful message
                    ctx.writeAndFlush(UserAuthResponseMessageFactory.createMessage(true, AuthErrorCode.AUTH_SUCCESSFUL, user.getUserID(), "LOGIN_SUCCESSFUL"));
                } else {
                    //password isnt correct
                    ctx.writeAndFlush(UserAuthResponseMessageFactory.createMessage(false, AuthErrorCode.AUTH_WRONG_PASSWORD, 0, "LOGIN_WRONG_PASSWORD"));
                }
            }
        } else {
            throw new IllegalStateException("unknown message eventID: " + msg.getEventID());
        }
    }

    protected void login (UserEntry user, ChannelAttributes attributes) {
        //set channel state to logged in
        attributes.setAuth(user.getUserID(), user.getUsername());

        attributes.put("user", user);
        attributes.put("userID", user.getUserID());
        attributes.put("username", user.getUserID());
    }

}
