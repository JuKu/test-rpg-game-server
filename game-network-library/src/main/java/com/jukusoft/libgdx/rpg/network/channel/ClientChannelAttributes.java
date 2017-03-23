package com.jukusoft.libgdx.rpg.network.channel;

/**
 * Created by Justin on 23.03.2017.
 */
public class ClientChannelAttributes extends ChannelAttributes {

    public ClientChannelAttributes() {
        //client doesnt have any channel IDs
        super(1);
    }

}
