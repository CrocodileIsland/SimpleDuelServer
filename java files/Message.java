package nettysocketserver;

import java.sql.Timestamp;

public class Message {
    
    int id;
    int group_id;
    String group_name;
    int duel_id;
    int user_id;
    String username;
    String color;
    String message;
    int admin;
    int html;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    int hidden = 0;
    String hidden_by = "";
    int sender_id;
    int receiver_id;
    String sender_username;
    String receiver_username;
    String receiver_user_username;
    int judge;
    int sender_judge;
    int receiver_judge;
    int seen;
    
    public Message() {
        init();
    }
    
    public void init() {
        id = 0;
        group_id = 0;
        group_name = null;
        duel_id = 0;
        user_id = 0;
        username = null;
        color = null;
        message = null;
        admin = 0;
        html = 0;
        timestamp = new Timestamp(System.currentTimeMillis());
        hidden = 0;
        hidden_by = "";
        sender_id = 0;
        receiver_id = 0;
        sender_username = null;
        receiver_username = null;
        receiver_user_username = null;
        judge = 0;
        sender_judge = 0;
        receiver_judge = 0;
        seen = 0;
    }
}