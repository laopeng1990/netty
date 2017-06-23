package com.wpf.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class MyHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = LoggerFactory.getLogger(MyHandler.class);

    @Override protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        if ("quit".equalsIgnoreCase(message)) {
            channelHandlerContext.channel().close();
            SocketChannel socketChannel = (SocketChannel)channelHandlerContext.channel();
            LOG.info("close channel {}", socketChannel.remoteAddress().getHostName().toString());
            return;
        }
        LOG.info("message {}", message);
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOG.error("MyTestHandler", cause);
    }
}
