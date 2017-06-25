package com.wpf.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    private ChannelGroup channelGroup;

    public ChannelInit(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("httpServer", new HttpServerCodec());
        pipeline.addLast("chunkedFile", new ChunkedWriteHandler());
        pipeline.addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        pipeline.addLast("httpHandler", new MyHttpHandler("/ws"));
        pipeline.addLast("websocketHandler", new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast("chatHandler", new MyWebSocketHandler(channelGroup));
    }
}
