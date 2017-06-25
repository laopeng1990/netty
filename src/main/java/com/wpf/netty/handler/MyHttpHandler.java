package com.wpf.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Created by wenpengfei on 2017/6/23.
 */
public class MyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(MyHttpHandler.class);

    private String weSocketUri;

    public MyHttpHandler(String httpUri) {
        this.weSocketUri = httpUri;
    }

    @Override protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        if (weSocketUri.equalsIgnoreCase(request.getUri())) {
            channelHandlerContext.fireChannelRead(request.retain());
            return;
        }

        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continues(channelHandlerContext);
        }

        URL fileUrl = getClass().getClassLoader().getResource("index.html");
        RandomAccessFile file = new RandomAccessFile(new File(fileUrl.getFile()), "r");

        DefaultHttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        channelHandlerContext.write(response);
        if (channelHandlerContext.pipeline().get(SslHandler.class) == null) {
            channelHandlerContext.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            channelHandlerContext.write(new ChunkedNioFile(file.getChannel()));
        }
        ChannelFuture future  = channelHandlerContext.channel().writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);


        if (!keepAlive) {
            future.addListeners(ChannelFutureListener.CLOSE);
        }
    }

    private void send100Continues(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOG.error("MyTestHandler", cause);
    }
}
