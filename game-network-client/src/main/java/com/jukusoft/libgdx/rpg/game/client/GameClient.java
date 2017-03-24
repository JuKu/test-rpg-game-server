package com.jukusoft.libgdx.rpg.game.client;

import com.jukusoft.libgdx.rpg.game.client.listener.AuthListener;
import com.jukusoft.libgdx.rpg.game.client.message.PingCheckMessageFactory;
import com.jukusoft.libgdx.rpg.game.client.message.PlayerPosMessageFactory;
import com.jukusoft.libgdx.rpg.game.client.message.UserAuthMessageFactory;
import com.jukusoft.libgdx.rpg.game.client.message.receiver.AuthResponseReceiver;
import com.jukusoft.libgdx.rpg.game.client.message.receiver.RTTReceiver;
import com.jukusoft.libgdx.rpg.game.server.AuthErrorCode;
import com.jukusoft.libgdx.rpg.game.server.ServerMessageID;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;
import com.jukusoft.libgdx.rpg.network.channel.ClientChannelAttributes;
import com.jukusoft.libgdx.rpg.network.message.*;
import com.jukusoft.libgdx.rpg.network.message.impl.DefaultMessageDistributor;
import com.jukusoft.libgdx.rpg.network.netty.NettyClient;
import com.jukusoft.libgdx.rpg.network.utils.HashUtils;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Justin on 22.03.2017.
 */
public class GameClient extends NettyClient {

    protected ChannelAttributes attributes = null;
    protected MessageDistributor<NetMessage> messageDistributor = null;

    protected AuthListener authListener = null;
    protected boolean authMessageReceived = false;

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

        //add auth response receiver
        AuthResponseReceiver authResponseReceiver = new AuthResponseReceiver((boolean success, int errorCode, long userID, String message) -> {
            System.out.println("auth response received (success: " + success + ", errorCode: " + errorCode + ").");

            this.authMessageReceived = true;

            if (this.authListener != null) {
                //call listener
                this.authListener.onAuth(success, errorCode, userID, message);
            } else {
                throw new IllegalStateException("no auth listener registered.");
            }
        });
        this.messageDistributor.addReceiver(ServerMessageID.AUTH_USER_RESPONSE_EVENTID, authResponseReceiver);
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

    public void addMessageReceiver (int eventID, MessageReceiver<NetMessage> receiver) {
        this.getMessageDistributor().addReceiver(eventID, receiver);
    }

    public void removeMessageReceiver (int eventID) {
        this.getMessageDistributor().removeReceiver(eventID);
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
        System.out.println("request ping check.");

        this.send(PingCheckMessageFactory.createMessage());
    }

    public void sendPlayerPosition (long sectorID, int layerID, long instanceID, float x, float y, float angle, float speed) {
        //System.out.println("send player position");

        this.send(PlayerPosMessageFactory.createMessage(sectorID, layerID, instanceID, x, y, angle, speed));
    }

    public void setAuthListener (AuthListener listener) {
        this.authListener = listener;
    }

    public void authUser (String username, String password, long timeout) {
        if (authListener == null) {
            throw new IllegalStateException("set authListener first.");
        }

        //reset flag
        this.authMessageReceived = false;

        //hash password
        password = HashUtils.computePasswordSHAHash(password);

        System.out.println("try to authorize user '" + username + "'.");

        workerGroup.schedule(() -> {
            if (!this.authMessageReceived) {
                //call listener
                this.authListener.onAuth(false, AuthErrorCode.AUTH_CONNECTION_TIMEOUT, 0, "CONNECTION_TIMEOUT");
            }
        }, timeout, TimeUnit.MILLISECONDS);

        //try to authentificate user
        this.send(UserAuthMessageFactory.createMessage(username, password));
    }

    public void addTask (long interval, Runnable runnable) {
        this.workerGroup.scheduleAtFixedRate(runnable, 0l, interval, TimeUnit.MILLISECONDS);
    }

}
