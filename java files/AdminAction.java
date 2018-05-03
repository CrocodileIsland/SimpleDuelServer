package nettysocketserver;

import java.sql.Timestamp;

public class AdminAction {
    
    int admin_id;
    String admin_username;
    int admin;
    int judge;
    int moderator;
    String username;
    String username2 = "";
    int duel_id = 0;
    String action;
    String details;
    String ip_address;
    String db_id;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
    public AdminAction() {
        init();
    }
    
    public void init() {
        admin_id = 0;
        admin_username = null;
        admin = 0;
        judge = 0;
        moderator = 0;
        username = null;
        username2 = "";
        duel_id = 0;
        action = null;
        details = null;
        ip_address = null;
        db_id = null;
        timestamp = new Timestamp(System.currentTimeMillis());
    }
}