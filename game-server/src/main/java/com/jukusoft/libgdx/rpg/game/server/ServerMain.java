package com.jukusoft.libgdx.rpg.game.server;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 22.03.2017.
 */
public class ServerMain {

    public static void main (String[] args) {
        String hazelcastConfigFile = "./cfg/hazelcast.cfg";

        //create new game server
        try {
            GameServer server = GameServerFactory.createNewGameServer(new File(hazelcastConfigFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
