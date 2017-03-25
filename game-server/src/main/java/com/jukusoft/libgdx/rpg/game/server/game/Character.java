package com.jukusoft.libgdx.rpg.game.server.game;

/**
 * Created by Justin on 25.03.2017.
 */
public class Character {

    protected long userID = 0;
    protected String username = "";

    protected float x = 0;
    protected float y = 0;
    protected float speedX = 0;
    protected float speedY = 0;

    public Character (long userID, String username) {
        this.userID = userID;
        this.username = username;
    }

    public long getUserID () {
        return userID;
    }

    public String getUsername () {
        return username;
    }

    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }

    public float getSpeedX () {
        return speedX;
    }

    public float getSpeedY () {
        return speedY;
    }

    public void updatePos (float x, float y, float speedX, float speedY) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

}
