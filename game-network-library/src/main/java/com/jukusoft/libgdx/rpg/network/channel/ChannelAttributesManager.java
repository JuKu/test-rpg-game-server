package com.jukusoft.libgdx.rpg.network.channel;

/**
 * Created by Justin on 23.03.2017.
 */
public interface ChannelAttributesManager {

    public ChannelAttributes getAttributes (long connID);

    public void removeChannel (long connID);

}
