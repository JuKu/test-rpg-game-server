package com.jukusoft.libgdx.rpg.game.client;

/**
 * message IDs from client to server
 *
 * Created by Justin on 23.03.2017.
 */
public class ClientMessageID {

    public static final int VERSION_MESSAGE_EVENTID = 1;

    //round trip time message
    public static final int RTT_RESPONSE_EVENTID = 2;

    //request ping check
    public static final int REQUEST_PING_CHECK_EVENTID = 3;

    public static final int TRY_AUTH_USER_EVENTID = 4;

    public static final int SEND_PLAYER_POS_EVENTID = 5;

}
