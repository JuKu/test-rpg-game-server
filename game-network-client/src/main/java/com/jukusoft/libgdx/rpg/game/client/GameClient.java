package com.jukusoft.libgdx.rpg.game.client;

import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.channel.ClientChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageDistributor;
import com.jukusoft.libgdx.rpg.network.message.NetMessageDecoder;
import com.jukusoft.libgdx.rpg.network.message.NetMessageEncoder;
import com.jukusoft.libgdx.rpg.network.message.impl.DefaultMessageDistributor;
import com.jukusoft.libgdx.rpg.network.netty.NettyClient;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Justin on 22.03.2017.
 */
public class GameClient extends NettyClient {

    public GameClient(int nOfWorkerThreads) {
        super(nOfWorkerThreads);
    }

    @Override protected void initPipeline(ChannelPipeline pipeline) {
        LoggingHandler loggingHandler = new LoggingHandler("NetworkLogger", LogLevel.DEBUG);

        //add logger to pipeline
        pipeline.addLast("logger", loggingHandler);

        //add encoder and decoder
        pipeline.addLast("encoder", new NetMessageEncoder());
        pipeline.addLast("decoder", new NetMessageDecoder());

        //create channel attributes
        ChannelAttributes attributes = new ClientChannelAttributes();

        //create message distributor
        MessageDistributor messageDistributor = new DefaultMessageDistributor(attributes);

        //add message distributor to pipeline
        pipeline.addLast("handler", messageDistributor);
    }

}
