package com.jukusoft.libgdx.rpg.game.client.entry;

/**
 * Created by Justin on 25.03.2017.
 */
public class CharacterPosEntry {

    protected long userID = 0;
    protected String username = "";

    protected volatile long sectorID = 0;
    protected volatile int layerID = 0;
    protected volatile long instanceID = 0;
    protected volatile float x = 0;
    protected volatile float y = 0;
    protected volatile float angle = 0;
    protected volatile float speedX = 0;
    protected volatile float speedY = 0;

    public CharacterPosEntry (long userID, String username) {
        this.userID = userID;
        this.username = username;
    }

    public long getSectorID () {
        return sectorID;
    }

    public int getLayerID () {
        return layerID;
    }

    public long getInstanceID () {
        return instanceID;
    }

    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }

    public float getAngle () {
        return angle;
    }

    public float getSpeedX () {
        return speedX;
    }

    public float getSpeedY () {
        return speedY;
    }

    public void update (float x, float y, float angle, float speedX, float speedY) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speedX = speedX;
        this.speedY = speedY;
    }

}
