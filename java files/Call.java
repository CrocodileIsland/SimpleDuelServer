package nettysocketserver;

import java.sql.Timestamp;

public class Call {
    
    int id;
    Duel duel;
    String issue;
    int caller_id;
    String caller;
    int opponent_id;
    String opponent;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
    public Call() {
        init();
    }
    
    public void init() {
        id = 0;
        duel = null;
        issue = null;
        caller_id = 0;
        caller = null;
        opponent_id = 0;
        opponent = null;
        timestamp = new Timestamp(System.currentTimeMillis());
    }
}