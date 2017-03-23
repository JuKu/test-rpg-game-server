package com.jukusoft.libgdx.rpg.network.channel;

/**
 * Created by Justin on 23.03.2017.
 */
public class ClientChannelAttributes extends ChannelAttributes {

    public ClientChannelAttributes() {
        //client doesnt have any channel IDs
        super(1);
    }

    @Override
    public void setRTT (long rtt) {
        throw new UnsupportedOperationException("client doesnt support RTT.");
    }

}
