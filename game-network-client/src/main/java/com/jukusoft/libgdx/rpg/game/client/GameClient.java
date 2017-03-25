package com.jukusoft.libgdx.rpg.game.client;

import com.jukusoft.libgdx.rpg.game.client.entry.CharacterPosEntry;
import com.jukusoft.libgdx.rpg.game.client.listener.AuthListener;
import com.jukusoft.libgdx.rpg.game.client.listener.TickListener;
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
import com.jukusoft.libgdx.rpg.network.utils.TickUtils;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Justin on 22.03.2017.
 */
public class GameClient extends NettyClient {

    protected ChannelAttributes attributes = null;
    protected MessageDistributor<NetMessage> messageDistributor = null;

    protected AuthListener authListener = null;
    protected boolean authMessageReceived = false;

    protected CharacterPosEntry characterPosEntry = new CharacterPosEntry();
    protected List<TickListener> tickListenerList = new ArrayList<>();
    protected List<TickListener> tmpList = new ArrayList<>();

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

        //add player sync
        this.addTickListener(new TickListener() {
            @Override public void onTick(GameClient client, ChannelAttributes attributes) {
                //send player position
                sendPlayerPos();
            }
        });

        //add tick listener
        this.addTask(TickUtils.getTickLength(), () -> {
            //only execute tick listeners, if client is authorized
            if (!attributes.isAuth()) {
                return;
            }

            synchronized (this.tickListenerList) {
                this.tickListenerList.stream().forEach(listener -> {
                    listener.onTick(this, attributes);
                });
            }
        });
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

    @Deprecated
    public void sendPlayerPosition (long sectorID, int layerID, long instanceID, float x, float y, float angle, float speed) {
        //System.out.println("send player position");

        this.send(PlayerPosMessageFactory.createMessage(sectorID, layerID, instanceID, x, y, angle, speed, speed));
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

    public int getPing () {
        return this.attributes.getPing();
    }

    public void updatePlayerPos (float x, float y, float angle, float speedX, float speedY) {
        this.characterPosEntry.update(x, y, angle, speedX, speedY);
    }

    public void addTickListener (TickListener tickListener) {
        this.tickListenerList.add(tickListener);
    }

    public void removeTickListener (TickListener tickListener) {
        this.tickListenerList.remove(tickListener);
    }

    public void addTask (long interval, Runnable runnable) {
        this.workerGroup.scheduleAtFixedRate(runnable, 0l, interval, TimeUnit.MILLISECONDS);
    }

    public void sendPlayerPos () {
        this.send(PlayerPosMessageFactory.createMessage(this.characterPosEntry));
    }

}
