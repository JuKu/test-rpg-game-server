package com.jukusoft.libgdx.rpg.database.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Justin on 22.03.2017.
 */
public class HazelcastFactory {

    protected static final String HAZELCAST_SECTION_NAME = "Hazelcast";

    private HazelcastFactory () {
        //
    }

    /**
     * get hazelcast instance
     *
     * @return hazelcastInstance
     */
    public static HazelcastInstance getHazelcastInstance (String ip, int port, String user, String password) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName(user).setPassword(password);
        clientConfig.getNetworkConfig().addAddress(ip + ":" + port);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    public static HazelcastInstance createInstanceFromConfig (File configFile) throws IOException {
        if (!configFile.exists()) {
            throw new FileNotFoundException("Could not find hazelcast configuration file: " + configFile.getAbsolutePath());
        }

        if (!configFile.canRead()) {
            throw new IOException("Cannot read configuration file: " + configFile.getAbsolutePath() + ", maybe you have to set correct file permissions.");
        }

        //create new instance of ini file
        Ini ini = new Ini(configFile);

        if (!ini.containsKey(HAZELCAST_SECTION_NAME)) {
            throw new IOException("Cannot find configuration section '" + HAZELCAST_SECTION_NAME + "'.");
        }

        //get hazelcast configuration section
        Profile.Section section = ini.get(HAZELCAST_SECTION_NAME);

        //check for standalone mode
        boolean standalone = Boolean.parseBoolean(section.getOrDefault("standalone", "true"));

        if (standalone) {
            //create new hazelcast instance
            Config config = new Config();
            HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

            return hazelcastInstance;
        } else {
            String ip = section.get("ip");
            int port = Integer.parseInt(section.getOrDefault("port", "5701"));
            String user = section.get("user");
            String password = section.get("password");

            return HazelcastFactory.getHazelcastInstance(ip, port, user, password);
        }
    }

}
