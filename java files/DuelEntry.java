package nettysocketserver;

import java.sql.Timestamp;

public class DuelEntry {
    
    int duel_id;
    String language;
    String rules;
    String type;
    String format;
    int rated;
    int player1_id;
    String player1_username;
    int player2_id;
    String player2_username;
    int winner_id;
    int draw;
    int host_deck;
    int opp_deck;
    int host_prev_rating;
    int host_new_rating;
    int host_prev_exp;
    int host_new_exp;
    int opp_prev_rating;
    int opp_new_rating;
    int opp_prev_exp;
    int opp_new_exp;
    int player1_points;
    int player2_points;
    Timestamp startTimestamp;
    Timestamp endTimestamp;
    String host_key;
    String opp_key;
    
    public DuelEntry() {
        init();
    }
    
    public void init() {
        duel_id = 0;
        language = null;
        rules = null;
        type = null;
        format = null;
        rated = 0;
        player1_id = 0;
        player1_username = null;
        player2_id = 0;
        player2_username = null;
        winner_id = 0;
        draw = 0;
        host_deck = 0;
        opp_deck = 0;
        host_prev_rating = 0;
        host_new_rating = 0;
        host_prev_exp = 0;
        host_new_exp = 0;
        opp_prev_rating = 0;
        opp_new_rating = 0;
        opp_prev_exp = 0;
        opp_new_exp = 0;
        player1_points = 0;
        player2_points = 0;
        startTimestamp = null;
        endTimestamp = null;
        host_key = null;
        opp_key = null;
    }
}