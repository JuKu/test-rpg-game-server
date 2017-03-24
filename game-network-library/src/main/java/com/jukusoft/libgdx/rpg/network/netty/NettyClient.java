package com.jukusoft.libgdx.rpg.network.netty;

import com.jukusoft.libgdx.rpg.network.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Justin on 22.03.2017.
 */
public abstract class NettyClient implements Client {

    protected int nOfWorkerThreads = 1;

    //bootstrap, worker group and netty channel
    protected Bootstrap bootstrap = null;
    protected EventLoopGroup workerGroup = null;
    protected Channel channel = null;

    protected ClientChannelInitializationHandler clientChannelInitializationHandler = null;

    public NettyClient (int nOfWorkerThreads) {
        this.nOfWorkerThreads = nOfWorkerThreads;

        //create and initialize new bootstrap
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup(this.nOfWorkerThreads);
        this.bootstrap.group(this.workerGroup);

        //set TCP channel
        bootstrap.channel(NioSocketChannel.class);

        //set bootstrap options
        this.setBootstrapOptions(this.bootstrap);

        //set handler
        this.clientChannelInitializationHandler = new ClientChannelInitializationHandler();
        bootstrap.handler(this.clientChannelInitializationHandler);
    }

    protected void setBootstrapOptions (Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.AUTO_READ, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void connect(String ip, int port) {
        //connect to server
        ChannelFuture channelFuture = this.bootstrap.connect(ip, port).awaitUninterruptibly();

        if (!channelFuture.isSuccess()) {
            throw new IllegalStateException("Could not connect to server" + ip + " on port " + port + ", cause: " + channelFuture.cause());
        }

        //set channel
        this.channel = channelFuture.channel();
    }

    @Override
    public ChannelFuture connectAsync(String ip, int port) {
        //connect to server
        ChannelFuture channelFuture = this.bootstrap.connect(ip, port);

        channelFuture.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (!channelFuture.isSuccess()) {
                    throw new IllegalStateException("Could not connect to server" + ip + " on port " + port + ", cause: " + future.cause());
                }

                channel = future.channel();
            }
        });

        return channelFuture;
    }

    @Override public void close() {
        //close channel
        this.channel.close().awaitUninterruptibly();
    }

    @Override
    public void shutdown () {
        if (isConnected()) {
            //close connection first
            this.close();
        }

        //close event loop group
        this.workerGroup.shutdownGracefully().awaitUninterruptibly();
    }

    @Override public boolean isConnected() {
        return this.channel != null && this.channel.isActive();
    }

    @Override public Channel getChannel() {
        if (!isConnected()) {
            throw new IllegalStateException("client isnt connected.");
        }

        return this.channel;
    }

    protected abstract void initPipeline (ChannelPipeline pipeline);

    private final class ClientChannelInitializationHandler extends ChannelHandlerAdapter {
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            //initialize pipeline
            NettyClient.this.initPipeline(ctx.pipeline());
        }
    }

}
