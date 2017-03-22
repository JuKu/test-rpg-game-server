package com.jukusoft.libgdx.rpg.game.client;

import com.jukusoft.libgdx.rpg.network.netty.NettyClient;
import io.netty.channel.ChannelPipeline;

/**
 * Created by Justin on 22.03.2017.
 */
public class GameClient extends NettyClient {

    public GameClient(int nOfWorkerThreads) {
        super(nOfWorkerThreads);
    }

    @Override protected void initPipeline(ChannelPipeline pipeline) {

    }

}
