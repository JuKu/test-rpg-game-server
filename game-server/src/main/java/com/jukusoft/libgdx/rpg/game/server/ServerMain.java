package com.jukusoft.libgdx.rpg.game.server;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 22.03.2017.
 */
public class ServerMain {

    public static void main (String[] args) {
        String serverConfigFile = "./cfg/server.cfg";
        String hazelcastConfigFile = "./cfg/hazelcast.cfg";

        GameServer server = null;

        //create new game server
        try {
            server = GameServerFactory.createNewGameServer(new File(serverConfigFile), new File(hazelcastConfigFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //start game server
        server.start();

        System.out.println("server started.");

        while (true) {
            System.out.println("open connections: " + server.countOpenConnections());

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
