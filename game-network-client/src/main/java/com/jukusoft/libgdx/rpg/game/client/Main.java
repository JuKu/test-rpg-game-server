package com.jukusoft.libgdx.rpg.game.client;

/**
 * Created by Justin on 22.03.2017.
 */
public class Main {

    public static void main (String[] args) throws InterruptedException {
        //create new network client
        GameClient client = new GameClient(2);

        try {
            //connect to server
            client.connect("localhost", 55011);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        if (!client.isConnected()) {
            throw new IllegalStateException("Could not connect to game server.");
        } else {
            System.out.println("connection to game server established.");
        }

        //add ping check every 2 seconds
        client.addTask(2000l, () -> {
            client.requestPingCheck();
        });

        //set authentification listener
        client.setAuthListener((boolean success, int errorCode, long userID, String message) -> {
            if (success) {
                System.out.println("authentification successful, userID: " + userID);
            } else {
                System.out.println("authentification failed.");
            }
        });

        //try to authorize user
        client.authUser("test", "test1234", 1000);

        //send player position every 20ms to server
        client.addTask(20l, () -> {
            client.sendPlayerPosition(1, 1, 1, 40, 40, 0, 1);
        });
    }

}
