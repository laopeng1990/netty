package com.wpf.netty;

import com.google.common.util.concurrent.AbstractIdleService;
import com.wpf.netty.handler.ChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.ImmediateExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class ChatServer extends AbstractIdleService {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class);

    private ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @Override public void startUp() throws Exception {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        NioEventLoopGroup accessGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInit(channels));

            ChannelFuture future = bootstrap.bind(7658).syncUninterruptibly();
            future.channel().closeFuture().syncUninterruptibly();
        } finally {
            workGroup.shutdownGracefully();
            accessGroup.shutdownGracefully();
        }
    }

    @Override public void shutDown() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                LOG.info("chat server shut down!");
            }
        }));
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        ChatServer chatServer = new ChatServer();
        chatServer.startAsync();

        LOG.info("char server start in {} mils", System.currentTimeMillis() - start);
    }
}
