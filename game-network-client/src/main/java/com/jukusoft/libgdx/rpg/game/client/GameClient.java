package com.jukusoft.libgdx.rpg.game.client;

import com.jukusoft.libgdx.rpg.game.client.message.PingCheckMessageFactory;
import com.jukusoft.libgdx.rpg.game.client.message.receiver.RTTReceiver;
import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.channel.ClientChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.MessageDistributor;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
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

    protected ChannelAttributes attributes = null;
    protected MessageDistributor<NetMessage> messageDistributor = null;

    public GameClient(int nOfWorkerThreads) {
        super(nOfWorkerThreads);

        //create channel attributes
        this.attributes = new ClientChannelAttributes();

        //create message distributor
        this.messageDistributor = new DefaultMessageDistributor(attributes);

        RTTReceiver rttReceiver = new RTTReceiver();

        //add RTT receiver
        this.messageDistributor.addReceiver(ServerMessageID.RTT_MESSAGE_EVENTID, rttReceiver);
        this.messageDistributor.addReceiver(ServerMessageID.PING_INFO_EVENTID, rttReceiver);
    }

    @Override protected void initPipeline(ChannelPipeline pipeline) {
        LoggingHandler loggingHandler = new LoggingHandler("NetworkLogger", LogLevel.DEBUG);

        //add logger to pipeline
        pipeline.addLast("logger", loggingHandler);

        //add encoder and decoder
        pipeline.addLast("encoder", new NetMessageEncoder());
        pipeline.addLast("decoder", new NetMessageDecoder());

        //add message distributor to pipeline
        pipeline.addLast("handler", messageDistributor);
    }

    public ChannelAttributes getAttributes () {
        return this.attributes;
    }

    public MessageDistributor<NetMessage> getMessageDistributor () {
        return this.messageDistributor;
    }

    public void send (NetMessage message) {
        if (!isConnected()) {
            throw new IllegalStateException("Cannot send message, because client isnt connected.");
        }

        this.channel.writeAndFlush(message);
    }

    /**
    * request server to check client ping
    */
    public void requestPingCheck () {
        this.send(PingCheckMessageFactory.createMessage());
    }

}
