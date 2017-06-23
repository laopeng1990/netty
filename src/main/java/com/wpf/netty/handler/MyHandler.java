package com.wpf.netty.handler;

import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class MyHandler extends SimpleChannelInboundHandler<byte[]> {
    private static final Logger LOG = LoggerFactory.getLogger(MyHandler.class);

    @Override protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        LOG.info("message {}", new String(bytes));
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOG.error("MyTestHandler", cause);
    }
}
