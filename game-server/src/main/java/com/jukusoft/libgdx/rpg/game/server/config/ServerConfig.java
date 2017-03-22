package com.jukusoft.libgdx.rpg.game.server.config;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 22.03.2017.
 */
public class ServerConfig {

    protected String interfaceName = "";
    protected int port = 55011;

    public ServerConfig (String interfaceName, int port) {
        this.interfaceName = interfaceName;
        this.port = port;
    }

    protected ServerConfig () {
        //
    }

    public boolean isInterfaceSpecified () {
        return !this.interfaceName.isEmpty() && !this.interfaceName.contains("none") && !this.interfaceName.contains("all");
    }

    public String getInterfaceName () {
        return this.interfaceName;
    }

    public int getPort () {
        return this.port;
    }

    public static ServerConfig createFromConfig (File configFile) throws IOException {
        Ini ini = new Ini(configFile);
        Profile.Section section = ini.get("Server");

        ServerConfig config = new ServerConfig();
        config.interfaceName = section.getOrDefault("interface", "");
        config.port = Integer.parseInt(section.getOrDefault("port", "55011"));

        return config;
    }

}
