package nettysocketserver;

import java.util.ArrayList;
import org.json.JSONObject;

public class Group {
    
    int id = 0;
    String name = "";
    int created_by_id = 0;
    int is_private = 0;
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
    Boolean loaded = false;
    
    public Group() {
        
    }
}