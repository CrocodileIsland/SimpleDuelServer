package nettysocketserver;

public class Friend {
    
    int user_id = 0;
    String username;
    String pic;
    int nsfw = 0;
    User user;
    
    public Friend() {
        
    }
    
    public void init() {
        user_id = 0;
        username = null;
        pic = null;
        nsfw = 0;
        user = null;
    }
}