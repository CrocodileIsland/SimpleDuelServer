package nettysocketserver;

import java.sql.Timestamp;

public class JournalEntry {
    
    int user_id;
    String username;
    String entry;
    String type;
    Timestamp timestamp;
    
    public JournalEntry() {
        init();
    }
    
    public void init() {
        user_id = 0;
        username = null;
        entry = null;
        type = null;
        timestamp = new Timestamp(System.currentTimeMillis());
    }
}