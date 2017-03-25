package com.jukusoft.libgdx.rpg.network.netty;

import com.jukusoft.libgdx.rpg.network.Server;
import com.jukusoft.libgdx.rpg.network.message.NetMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 22.03.2017.
 */
public abstract class NettyServer<T extends NetMessage> implements Server<T> {

    protected int nOfBossThreads = 1;
    protected int nOfWorkerThreads = 1;

    //bootstrap and event loop groups
    protected ServerBootstrap bootstrap = null;
    protected EventLoopGroup bossGroup = null;
    protected EventLoopGroup workerGroup = null;

    //netty channel
    protected Channel channel = null;

    protected LogLevel logLevel = LogLevel.INFO;

    protected ChannelInitializationHandler channelInitializationHandler = null;

    protected ChannelGroup allChannels = null;
    protected List<Channel> channelList = new ArrayList<>();

    public NettyServer (final int nOfBossThreads, final int nOfWorkerThreads) {
        this.nOfBossThreads = nOfBossThreads;
        this.nOfWorkerThreads = nOfWorkerThreads;

        //create bootstrap
        this.bootstrap = new ServerBootstrap();

        //create event loop groups
        this.bossGroup = new NioEventLoopGroup(this.nOfBossThreads);
        this.workerGroup = new NioEventLoopGroup(this.nOfWorkerThreads);
        this.bootstrap.group(this.bossGroup, this.workerGroup);

        //set TCP channel
        bootstrap.channel(NioServerSocketChannel.class);

        this.setChannelOptions(this.bootstrap);

        //create and set new channel initialization handler
        this.channelInitializationHandler = new ChannelInitializationHandler(this);

        //add handler
        bootstrap.handler(new LoggingHandler(this.logLevel));
        bootstrap.childHandler(this.channelInitializationHandler);

        this.allChannels = new DefaultChannelGroup("all_channels", GlobalEventExecutor.INSTANCE);
    }

    @Override public void start(String interfaceName, int port) {
        ChannelFuture channelFuture = this.bootstrap.bind(interfaceName, port).awaitUninterruptibly();

        if (!channelFuture.isSuccess()) {
            throw new IllegalStateException("Could not start server on interface " + interfaceName + " on port" + port);
        }

        //save channel
        this.channel = channelFuture.channel();
    }

    @Override public void start(int port) {
        ChannelFuture channelFuture = this.bootstrap.bind(port).awaitUninterruptibly();

        if (!channelFuture.isSuccess()) {
            throw new IllegalStateException("Could not start server on port" + port);
        }

        //save channel
        this.channel = channelFuture.channel();
    }

    @Override public void shutdownServer () {
        //close channel
        this.channel.flush();
        this.channel.close();

        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    @Override public void startAsync (String interfaceName, int port) {
        ChannelFuture channelFuture = this.bootstrap.bind(interfaceName, port);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    throw new IllegalStateException("Could not start server on interface " + interfaceName + " on port" + port);
                }

                NettyServer.this.channel = channelFuture.channel();
            }
        });
    }

    @Override public void startAsync (int port) {
        ChannelFuture channelFuture = this.bootstrap.bind(port);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    throw new IllegalStateException("Could not start server on port" + port);
                }

                NettyServer.this.channel = channelFuture.channel();
            }
        });
    }

    protected abstract void initPipeline (ChannelPipeline channelPipeline);

    protected void setChannelOptions (ServerBootstrap bootstrap) {
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.AUTO_READ, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
    }

    protected ChannelGroup getAllChannels () {
        return this.allChannels;
    }

    public List<Channel> listChannels () {
        return this.channelList;
    }

    @Override
    public void sendBroadcastMessage (T message) {
        //send message to all clients
        //this.getAllChannels().writeAndFlush(message);

        List<Channel> closedChannels = new ArrayList<>();

        for (Channel channel : this.channelList) {
            if (channel.isOpen() && channel.isActive()) {
                NetMessage message1 = message.clone();

                message1.retain();
                channel.writeAndFlush(message1);
            } else {
                closedChannels.add(channel);
            }
        }

        channelList.removeAll(closedChannels);
    }

}
