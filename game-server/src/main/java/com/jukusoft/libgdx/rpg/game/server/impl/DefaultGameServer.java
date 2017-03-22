package com.jukusoft.libgdx.rpg.game.server.impl;

import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.libgdx.rpg.game.server.GameServer;

/**
 * Created by Justin on 22.03.2017.
 */
public class DefaultGameServer implements GameServer {

    protected HazelcastInstance hazelcastInstance = null;

    public DefaultGameServer (HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

}
