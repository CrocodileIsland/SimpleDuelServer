package nettysocketserver;

import java.sql.Timestamp;
import java.util.ArrayList;
import org.json.JSONObject;

public class Card {
    
    int id = 0;
    Boolean face_down = false;
    Boolean face_up = false;
    Boolean inATK = false;
    Boolean inDEF = false;
    int counters = 0;
    Player owner;
    Player controller;
    ArrayList<Card> xyz_arr = new ArrayList<Card>();
    JSONObject data = new JSONObject();
    int card_id;
    String name = "";
    String treated_as;
    String effect;
    String pendulum_effect;
    String card_type;
    String monster_color;
    int is_effect;
    String type;
    String attribute;
    int level;
    String ability;
    int flip;
    int pendulum;
    int scale_left;
    int scale_right;
    String arrows;
    String atk;
    String def;
    int restriction;
    int tcg_limit;
    int ocg_limit;
    String serial_number;
    int tcg;
    int ocg;
    String pic;
    Timestamp timestamp;
    int hidden;
    
    public static void Card() {
        
    }
}