package com.wpf.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    @Override protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("lineBased", new LineBasedFrameDecoder(Integer.MAX_VALUE));
        pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
        pipeline.addLast(new MyHandler());
    }
}
