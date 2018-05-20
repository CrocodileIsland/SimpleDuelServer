package nettysocketserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    private int port = 8443;
    public static String KEYSTORE_PATH;
    public static String KEYSTORE_PASSWORD;
    public static SslContext SelfSignedSSLContext;
    public static SSLContext DefaultSSLContext;
    public static SSLContext DuelSSLContext;
    public static Boolean local = true;
    
    public Main() {
        System.out.println("Listening on port " + port);
    }

    public void run() throws Exception {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SelfSignedSSLContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        if (System.getProperty("user.dir").indexOf("/Users/") != 0) { 
        	// This means the process is running on the website and not on my local computer
            TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore tmpKS = null;
            tmFactory.init(tmpKS);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());
            KeyManager[] km = kmf.getKeyManagers();
            TrustManager[] tm = tmFactory.getTrustManagers();
            DuelSSLContext = SSLContext.getInstance("TLS");
            DuelSSLContext.init(km, tm, null);
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("user.dir").indexOf("/Users/") != 0) {
            local = false;
        }
        try {
            String content = new String (Files.readAllBytes(Paths.get("config.txt")));
            JSONObject data = new JSONObject(content);
            KEYSTORE_PATH = (String) data.get("KEYSTORE_PATH");
            KEYSTORE_PASSWORD = (String) data.get("KEYSTORE_PASSWORD");
            DataHandler.DB_URL = (String) data.get("DB_URL");
            DataHandler.DB_DATABASE = (String) data.get("DB_DATABASE");
            DataHandler.DB_USERNAME = (String) data.get("DB_USERNAME");
            DataHandler.DB_PASSWORD = (String) data.get("DB_PASSWORD");
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        DataHandler.startUp();
        new Main().run();
    }
}