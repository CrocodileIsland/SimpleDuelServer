package nettysocketserver;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Status {
    
    int id = 0;
    int user_id = 0;
    String username;
    String status;
    int reply_to;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String pic;
    int nsfw = 0;
    ArrayList<String> usernames = new ArrayList<String>();
    User user;
    int likes = 0;
    int hidden = 0;
    String hidden_by = "";
    int html = 0;
    String nbc_address = "";
    int bot = 0;
    Boolean inserted = false;
    Boolean ready_to_purge = false;
    
    public Status() {
        init();
    }
    
    public void init() {
        id = 0;
        user_id = 0;
        username = null;
        status = null;
        reply_to = 0;
        timestamp = new Timestamp(System.currentTimeMillis());
        pic = null;
        nsfw = 0;
        usernames.clear();
        user = null;
        likes = 0;
        hidden = 0;
        hidden_by = "";
        html = 0;
        nbc_address = "";
        bot = 0;
        inserted = false;
        ready_to_purge = false;
    }
}