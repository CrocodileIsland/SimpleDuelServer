package nettysocketserver;

import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;

public class Watcher {
    
    ChannelHandlerContext nbc;
    String username;
    int user_id;
    int admin = 0;
    int adjudicator = 0;
    int donator = 0;
    
    public static void Watcher() {
        
    }
    
    public void init() {
        nbc = null;
        username = null;
        user_id = 0;
        admin = 0;
        adjudicator = 0;
        donator = 0;
    }
}