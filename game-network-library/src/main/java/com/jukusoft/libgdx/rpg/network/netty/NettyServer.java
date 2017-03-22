package com.jukusoft.libgdx.rpg.network.netty;

import com.jukusoft.libgdx.rpg.network.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Justin on 22.03.2017.
 */
public abstract class NettyServer implements Server {

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

}
