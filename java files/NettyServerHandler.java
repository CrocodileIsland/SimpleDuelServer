package nettysocketserver;
 
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandler;
import java.util.regex.Matcher;
import javax.net.ssl.SSLEngine;
import org.json.*;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelInactive(ChannelHandlerContext nbc) {
        System.out.println("channelInactive");
        DataHandler.offlineUser(nbc, true);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext nbc, Object msg) throws Exception {
        String dataString = "";
        try {
            ByteBuf in = (ByteBuf) msg;
            dataString = in.toString(io.netty.util.CharsetUtil.US_ASCII);
            Matcher matcher = DataHandler.ASCIIPattern.matcher(dataString);
            if (!matcher.find()) {
                if (DataHandler.ReportingLostConnections == true) {
                    System.out.println("SSL connection opened");
                }
                if (Main.local == true || dataString.indexOf("www.duelingbook.com") >= 0) {
                    nbc.pipeline().addLast("cloudflareHandler", new CloudflareHandler());
                    nbc.pipeline().replace(this, "sslHandler", Main.SelfSignedSSLContext.newHandler(nbc.channel().alloc()));
                }
                else if (dataString.indexOf("duel.duelingbook.com") >= 0) {
                    SSLEngine sslEngine = Main.DuelSSLContext.createSSLEngine();
                    sslEngine.setUseClientMode(false);
                    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
                    sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
                    sslEngine.setEnableSessionCreation(true);
                    nbc.pipeline().replace(this, "sslHandler", new SslHandler(sslEngine));
                }
                nbc.pipeline().addLast("httpInitializer", new HTTPInitializer());
                nbc.pipeline().fireChannelRead(in);
                return;
            }
            if (dataString == null) {
                return;
            }
            if (!nbc.pipeline().names().contains("websocketHandler") && !nbc.pipeline().names().contains("flashHandler")) {
                if (dataString.indexOf("\0") >= 0) {
                    nbc.pipeline().addLast("flashHandler", new FlashHandler());
                }
            }
            if (DataHandler.ReportingActions == true) {
                System.out.println("dataString = " + dataString);
            }
            if (dataString.indexOf("<policy-file-request/>") == 0) {
                DataHandler.write(nbc, DataHandler.Policy);
                return;
            }
            if (dataString.equals("") || dataString.equals("\0")) {
                return;
            }
            if (dataString.indexOf("GET / HTTP/1.1") == 0) {
                nbc.pipeline().replace(this, "httpInitializer", new HTTPInitializer());
                nbc.pipeline().fireChannelRead(in);
                return;
            }
            if (dataString.indexOf("\0") >= 0) {
                while (dataString.indexOf("\0") >= 0) {
                    startData(nbc, dataString.substring(0, dataString.indexOf("\0")), null);
                    dataString = dataString.substring(dataString.indexOf("\0") + 1, dataString.length());
                }
            }
            else {
                startData(nbc, dataString, null);
            }
            in.release();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            DataHandler.getUserByNbc(nbc).error = ex.getMessage();
            serverError(nbc, "105", dataString);
        }
    }
    
    public static void startData(ChannelHandlerContext nbc, String dataString, User user) {
        try {
            if (!dataString.substring(0, 1).equals("{")) {
                user = getUser(nbc, user);
                if (!user.action.substring(0, 1).equals("{") && !user.action.equals("")) {
                    user.action = "";
                    return;
                }
                dataString = user.action + dataString;
                user.action = "";
            }
            endData(nbc, dataString, user);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void endData(ChannelHandlerContext nbc, String dataString, User user) {
        try {
            if (!dataString.substring(dataString.length() - 1, dataString.length()).equals("}")) {
                user = getUser(nbc, user);
                if (user.action.equals("")) {
                    if (dataString.indexOf("{") == 0) {
                        user.action += dataString;
                    }
                    return;
                }
                else if (!user.action.substring(0, 1).equals("{")) {
                    System.out.println("LINE 156 ENTERED"); // never happens 3/5/18
                    user.action = "";
                    return;
                }
                user.action = dataString;
            }
            else {
                convertData(nbc, dataString, user);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void convertData(ChannelHandlerContext nbc, String dataString, User user) {
        // it's possible to receive {"action":"Message","message":"Profile ha}}}}
        try {
            if (dataString.equals("")) {
                return;
            }
            JSONObject data;
            try {
                data = new JSONObject(dataString);
                DataHandler.onData(nbc, data);
            }
            catch (Exception e) {
                System.out.println("line 209 entered");
                System.out.println(e.getMessage());
                System.out.println("on line 266, dataString = " + dataString);
                user = getUser(nbc, user);
                user.error = e.getMessage();
                if (!user.action.equals("")) {
                    if (!user.action.substring(0, 1).equals("{")) {
                        System.out.println("LINE 158 ENTERED! user.action = " + user.action); // never happens 3/5/18
                        user.action = "";
                        return;
                    }
                }
                user.action = dataString;
                serverError(nbc, "221", dataString);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void serverError(ChannelHandlerContext nbc, String line, String str) {
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Server error");
            result.put("line", line);
            result.put("string", str);
            DataHandler.write(nbc, result);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static User getUser(ChannelHandlerContext nbc, User user) {
        if (user == null) {
            user = DataHandler.getUserByNbc(nbc);
            if (user == null) {
                JSONObject result = new JSONObject();
                result.put("action", "Error");
                result.put("message", "Data was fragmented. Try again.");
                DataHandler.write(nbc, result);
            }
        }
        return user;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext nbc, Throwable cause) {
        cause.printStackTrace();
        DataHandler.offlineUser(nbc, true);
    }
}