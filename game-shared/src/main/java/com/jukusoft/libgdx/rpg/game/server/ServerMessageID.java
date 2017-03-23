package com.jukusoft.libgdx.rpg.game.server;

/**
 * Message IDs from server --> client
 *
 * Created by Justin on 22.03.2017.
 */
public class ServerMessageID {

    public static final int VERSION_MESSAGE_EVENTID = 1;

    //round trip time message
    public static final int RTT_MESSAGE_EVENTID = 2;

    //send current ping to client
    public static final int PING_INFO_EVENTID = 3;

    public static final int AUTH_USER_RESPONSE_EVENTID = 4;

}
