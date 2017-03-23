package com.jukusoft.libgdx.rpg.network.channel.impl;

import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributesManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 23.03.2017.
 */
public class DefaultChannelAttributesManager implements ChannelAttributesManager {

    protected Map<Long,ChannelAttributes> attributesMap = new ConcurrentHashMap<>();

    @Override public ChannelAttributes getAttributes(long connID) {
        ChannelAttributes attributes = attributesMap.get(connID);

        if (attributes == null) {
            //create new channel attributes
            attributes = new ChannelAttributes(connID);

            attributesMap.put(connID, attributes);
        }

        return attributes;
    }

    @Override public void removeChannel(long connID) {
        this.attributesMap.remove(connID);
    }

}
