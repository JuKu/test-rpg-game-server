package com.jukusoft.libgdx.rpg.game.server;

import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.libgdx.rpg.database.hazelcast.HazelcastFactory;
import com.jukusoft.libgdx.rpg.game.server.impl.DefaultGameServer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 22.03.2017.
 */
public class GameServerFactory {

    public static GameServer createNewGameServer (File hzConfigFile) throws IOException {
        //create new hazelcast instance from configuration file
        HazelcastInstance hazelcastInstance = HazelcastFactory.createInstanceFromConfig(hzConfigFile);

        return new DefaultGameServer(hazelcastInstance);
    }

}
