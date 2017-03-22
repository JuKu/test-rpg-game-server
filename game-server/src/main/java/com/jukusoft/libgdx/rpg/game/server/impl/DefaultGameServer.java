package com.jukusoft.libgdx.rpg.game.server.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;
import com.jukusoft.libgdx.rpg.game.server.GameServer;
import com.jukusoft.libgdx.rpg.game.server.config.ServerConfig;
import com.jukusoft.libgdx.rpg.game.server.handler.InitHandler;
import com.jukusoft.libgdx.rpg.network.message.NetMessageDecoder;
import com.jukusoft.libgdx.rpg.network.message.NetMessageEncoder;
import com.jukusoft.libgdx.rpg.network.netty.NettyServer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Justin on 22.03.2017.
 */
public class DefaultGameServer extends NettyServer implements GameServer {

    protected static final int DEFAULT_NUMBER_OF_BOSS_THREADS = 1;
    protected static final int DEFAULT_NUMBER_OF_WORKER_THREADS = 1;

    protected HazelcastInstance hazelcastInstance = null;
    protected ServerConfig config = null;

    protected IdGenerator connIDGenerator = null;

    public DefaultGameServer (HazelcastInstance hazelcastInstance, ServerConfig config) {
        super(DEFAULT_NUMBER_OF_BOSS_THREADS, DEFAULT_NUMBER_OF_WORKER_THREADS);

        this.hazelcastInstance = hazelcastInstance;
        this.config = config;

        //get ID generator
        this.connIDGenerator = this.hazelcastInstance.getIdGenerator("connection-id-generator");
    }

    @Override
    public void start() {
        //start netty server
        if (config.isInterfaceSpecified()) {
            this.startAsync(config.getInterfaceName(), config.getPort());
        } else {
            this.startAsync(config.getPort());
        }
    }

    @Override
    public void shutdown() {
        //shutdown server
        this.shutdownServer();
    }

    @Override public int getPort() {
        return this.config.getPort();
    }

    @Override public int countOpenConnections() {
        return this.channelInitializationHandler.countOpenConnections();
    }

    @Override protected void initPipeline(ChannelPipeline pipeline) {
        LoggingHandler loggingHandler = new LoggingHandler("NetworkLogger", LogLevel.DEBUG);

        //add logger to pipeline
        pipeline.addLast("logger", loggingHandler);

        //add encoder and decoder
        pipeline.addLast("encoder", new NetMessageEncoder());
        pipeline.addLast("decoder", new NetMessageDecoder());

        //generate new connection ID
        long connID = this.connIDGenerator.newId();

        //add init handler
        pipeline.addLast("initHandler", new InitHandler(connID));
    }

}
