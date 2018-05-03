package nettysocketserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import java.net.URISyntaxException;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    WebSocketServerHandshaker handshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            HttpHeaders headers = httpRequest.headers();
            if (headers.get("Connection").equalsIgnoreCase("Upgrade") || headers.get("Upgrade").equalsIgnoreCase("WebSocket")) {
                ctx.pipeline().replace(this, "websocketHandler", new WebSocketHandler());
                handleHandshake(ctx, httpRequest);
            }
        }
        else {
            System.out.println("Incoming request is unknown");
        }
    }
    
    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketURL(ctx, req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }
        else {
            handshaker.handshake(ctx.channel(), (HttpRequest) req);
        }
    }
    
    protected String getWebSocketURL(ChannelHandlerContext ctx, HttpRequest req) {
        if (ctx.pipeline().names().contains("sslHandler")) {
            return "wss://" + req.headers().get("Host") + req.getUri();
        }
        return "ws://" + req.headers().get("Host") + req.getUri();
    }
}