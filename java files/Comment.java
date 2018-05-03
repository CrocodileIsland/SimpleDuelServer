package nettysocketserver;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Comment {
    
    int id = 0;
    int user_id = 0;
    int receiver_id = 0;
    String receiver_username;
    String username;
    String comment;
    String pic;
    int nsfw = 0;
    int reply_to = 0;
    String reply_to_username;
    Timestamp timestamp;
    int likes;
    ArrayList<String> usernames = new ArrayList<String>();
    User user;
    
    public void Comment() {
        
    }
}