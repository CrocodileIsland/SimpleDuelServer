package nettysocketserver;

import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

public class Player {
    
    ChannelHandlerContext nbc;
    String username;
    String pic;
    String default_pic;
    int nsfw;
    int friends;
    String sleeve;
    int rating;
    int prev_rating;
    int experience;
    int prev_experience;
    int user_id;
    int deck_id;
    Object[] deck;
    ArrayList<JSONObject> main;
    ArrayList<JSONObject> side;
    ArrayList<JSONObject> extra;
    ArrayList<Card> main_match_arr;
    ArrayList<Card> side_match_arr;
    ArrayList<Card> extra_match_arr;
    int token;
    int lifepoints = 8000;
    String rpsChoice = "";
    ArrayList<Card> main_arr = new ArrayList<Card>();
    ArrayList<Card> hand_arr = new ArrayList<Card>();
    ArrayList<Card> field_arr = new ArrayList<Card>();
    ArrayList<Card> grave_arr = new ArrayList<Card>();
    ArrayList<Card> banished_arr = new ArrayList<Card>();
    ArrayList<Card> extra_arr = new ArrayList<Card>();
    ArrayList<Card> side_arr = new ArrayList<Card>();
    ArrayList<Card> all_cards_arr = new ArrayList<Card>();
    Card m1;
    Card m2;
    Card m3;
    Card m4;
    Card m5;
    Card s1;
    Card s2;
    Card s3;
    Card s4;
    Card s5;
    Card fieldSpell;
    Card pendulumLeft;
    Card pendulumRight;
    Player opponent;
    Boolean active = true;
    String viewing = "";
    User user;
    int wins = 0;
    int losses = 0;
    int draws = 0;
    Boolean agree_to_draw = false;
    Boolean agree_to_rematch = false;
    int recorded_wins = 0;
    int recorded_losses = 0;
    int recorded_draws = 0;
    int points = 0;
    Boolean done_siding = false;
    Boolean exchanging = false;
    int exchanged_card;
    Boolean deck_face_up = false;
    int result = 0;
    String pronoun = "his";
    Boolean ready = false;
    ArrayList<Integer> main_ids = new ArrayList<Integer>();
    ArrayList<Integer> side_ids = new ArrayList<Integer>();
    ArrayList<Integer> extra_ids = new ArrayList<Integer>();
    int afkSeconds = 0;
    Timer afkTimer;
    TimerTask afkTimerTask;
    String zone;
    Card linkCard;
    Card linkLeft;
    Card linkRight;
    Duel duel;
    int suspicious;
    Boolean seen_deck = false;
    Boolean seen_opp_deck = false;
    Boolean seen_extra = false;
    Boolean seen_opp_extra = false;
    String lastZone = "";
    String key = DataHandler.randomHex(7);
    String status = "";
    String ip_address = "";
    String skill;
    
    public Player() {
        init();
    }
    
    public void init() {
        nbc = null;
        username = null;
        pic = null;
        default_pic = null;
        nsfw = 0;
        friends = 0;
        sleeve = null;
        rating = 0;
        prev_rating = 0;
        experience = 0;
        prev_experience = 0;
        user_id = 0;
        deck_id = 0;
        deck = null;
        main = null;
        side = null;
        extra = null;
        main_match_arr = null;
        side_match_arr = null;
        extra_match_arr = null;
        token = 0;
        lifepoints = 8000;
        rpsChoice = "";
        main_arr.clear();
        hand_arr.clear();
        field_arr.clear();
        grave_arr.clear();
        banished_arr.clear();
        extra_arr.clear();
        side_arr.clear();
        all_cards_arr.clear();
        m1 = null;
        m2 = null;
        m3 = null;
        m4 = null;
        m5 = null;
        s1 = null;
        s2 = null;
        s3 = null;
        s4 = null;
        s5 = null;
        fieldSpell = null;
        pendulumLeft = null;
        pendulumRight = null;
        opponent = null;
        active = true;
        viewing = "";
        user = null;
        wins = 0;
        losses = 0;
        draws = 0;
        agree_to_draw = false;
        agree_to_rematch = false;
        recorded_wins = 0;
        recorded_losses = 0;
        recorded_draws = 0;
        points = 0;
        done_siding = false;
        exchanging = false;
        exchanged_card = 0;
        deck_face_up = false;
        result = 0;
        pronoun = "his";
        ready = false;
        main_ids.clear();
        side_ids.clear();
        extra_ids.clear();
        afkSeconds = 0;
        afkTimer = null;
        afkTimerTask = null;
        zone = null;
        linkCard = null;
        linkLeft = null;
        linkRight = null;
        duel = null;
        suspicious = 0;
        seen_deck = false;
        seen_opp_deck = false;
        seen_extra = false;
        seen_opp_extra = false;
        lastZone = "";
        key = DataHandler.randomHex(7);
        status = "";
        ip_address = "";
        skill = null;
    }
    
    public void checkAFK() {
        if (DataHandler.CheckingAFK == false) {
            afkSeconds = 0;
            return;
        }
        afkSeconds++;
        if (user == null) {
            return;
        }
        if (user.duel == null) {
            return;
        }
        if (user.duel.active == false) {
            afkSeconds = 0; // 12/26
            return;
        }
        if (user.duel.awaiting_admin == null) {
            return;
        }
        if (user.duel.awaiting_admin == true) {
            if (user.duel.call.issue.equals("AFK") && user.duel.call.caller_id == user_id) {
                afkSeconds = 0;
                return;
            }
            else if (!user.duel.call.issue.equals("AFK")) {
                afkSeconds = 0;
                return;
            }
            else {
                if (afkSeconds > 360) {
                    DataHandler.autoLoss(user, "being AFK");
                    return;
                }
            }
        }
        if (user.duel.turn_player == null) {
            return;
        }
        if (user.duel.currentPhase == null) {
            return;
        }
        if (user.duel.turn_player.equals(username) && !user.duel.currentPhase.equals("")) {
            try {
                if (afkSeconds > 360) {
                    DataHandler.autoLoss(user, "being AFK");
                    return;
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        else if (!user.duel.turn_player.equals(username) && user.duel.currentPhase.equals("")) {
            try {
                if (afkSeconds > 360) {
                    DataHandler.autoLoss(user, "being AFK");
                    return;
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        else if (!user.duel.turn_player.equals(username) && user.duel.currentPhase.equals("BP")) {
            try {
                if (afkSeconds > 360) {
                    DataHandler.autoLoss(user, "being AFK");
                    return;
                }
                // insert ban here
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        else {
            afkSeconds = 0;
        }
    }
    
    public void startAFKTimer() {
        afkSeconds = 0;
    }
    
    public void resetAFKTimer() {
        afkSeconds = 0;
    }
    
    public void setDeck() {
        for (int i = 0; i < main.size(); i++) {
            Card card = new Card();
            card.data.put("id", (int) main.get(i).get("id"));
            card.data.put("name", (String) main.get(i).get("name"));
            card.data.put("treated_as", (String) main.get(i).get("treated_as"));
            card.data.put("effect", (String) main.get(i).get("effect"));
            card.data.put("pendulum_effect", (String) main.get(i).get("pendulum_effect"));
            card.data.put("card_type", (String) main.get(i).get("card_type"));
            card.data.put("monster_color", (String) main.get(i).get("monster_color"));
            card.data.put("is_effect", (int) main.get(i).get("is_effect"));
            card.data.put("type", (String) main.get(i).get("type"));
            card.data.put("attribute", (String) main.get(i).get("attribute"));
            card.data.put("level", (int) main.get(i).get("level"));
            card.data.put("ability", (String) main.get(i).get("ability"));
            card.data.put("flip", (int) main.get(i).get("flip"));
            card.data.put("pendulum", (int) main.get(i).get("pendulum"));
            card.data.put("scale_left", (int) main.get(i).get("scale_left"));
            card.data.put("scale_right", (int) main.get(i).get("scale_right"));
            card.data.put("arrows", (String) main.get(i).get("arrows"));
            card.data.put("atk", (String) main.get(i).get("atk"));
            card.data.put("def", (String) main.get(i).get("def"));
            card.data.put("tcg_limit", (int) main.get(i).get("tcg_limit"));
            card.data.put("ocg_limit", (int) main.get(i).get("ocg_limit"));
            card.data.put("serial_number", (String) main.get(i).get("serial_number"));
            card.data.put("tcg", (int) main.get(i).get("tcg"));
            card.data.put("ocg", (int) main.get(i).get("ocg"));
            card.data.put("pic", (String) main.get(i).get("pic"));

            card.id = 1 + i; // THIS IS SET HERE (IN ADDITION TO RANDOMIZEDECK FOR SIDING PURPOSES
            card.card_id = (int) main.get(i).get("id");
            card.name = (String) main.get(i).get("name");
            card.treated_as = (String) main.get(i).get("treated_as");
            card.effect = (String) main.get(i).get("effect");
            card.pendulum_effect = (String) main.get(i).get("pendulum_effect");
            card.card_type = (String) main.get(i).get("card_type");
            card.monster_color = (String) main.get(i).get("monster_color");
            card.is_effect = (int) main.get(i).get("is_effect");
            card.type = (String) main.get(i).get("type");
            card.attribute = (String) main.get(i).get("attribute");
            card.level = (int) main.get(i).get("level");
            card.ability = (String) main.get(i).get("ability");
            card.flip = (int) main.get(i).get("flip");
            card.pendulum = (int) main.get(i).get("pendulum");
            card.scale_left = (int) main.get(i).get("scale_left");
            card.scale_right = (int) main.get(i).get("scale_right");
            card.arrows = (String) main.get(i).get("arrows");
            card.atk = (String) main.get(i).get("atk");
            card.def = (String) main.get(i).get("def");
            card.tcg_limit = (int) main.get(i).get("tcg_limit");
            card.ocg_limit = (int) main.get(i).get("ocg_limit");
            card.serial_number = (String) main.get(i).get("serial_number");
            card.tcg = (int) main.get(i).get("tcg");
            card.ocg = (int) main.get(i).get("ocg");
            card.pic = (String) main.get(i).get("pic");
            card.owner = this;
            card.controller = this;
            main_arr.add(card);
            all_cards_arr.add(card);
        }
        for (int i = 0; i < extra.size(); i++) {
            Card card = new Card();
            card.data.put("id", (int) extra.get(i).get("id"));
            card.data.put("name", (String) extra.get(i).get("name"));
            card.data.put("treated_as", (String) extra.get(i).get("treated_as"));
            card.data.put("effect", (String) extra.get(i).get("effect"));
            card.data.put("pendulum_effect", (String) extra.get(i).get("pendulum_effect"));
            card.data.put("card_type", (String) extra.get(i).get("card_type"));
            card.data.put("monster_color", (String) extra.get(i).get("monster_color"));
            card.data.put("is_effect", (int) extra.get(i).get("is_effect"));
            card.data.put("type", (String) extra.get(i).get("type"));
            card.data.put("attribute", (String) extra.get(i).get("attribute"));
            card.data.put("level", (int) extra.get(i).get("level"));
            card.data.put("ability", (String) extra.get(i).get("ability"));
            card.data.put("flip", (int) extra.get(i).get("flip"));
            card.data.put("pendulum", (int) extra.get(i).get("pendulum"));
            card.data.put("scale_left", (int) extra.get(i).get("scale_left"));
            card.data.put("scale_right", (int) extra.get(i).get("scale_right"));
            card.data.put("arrows", (String) extra.get(i).get("arrows"));
            card.data.put("atk", (String) extra.get(i).get("atk"));
            card.data.put("def", (String) extra.get(i).get("def"));
            card.data.put("tcg_limit", (int) extra.get(i).get("tcg_limit"));
            card.data.put("ocg_limit", (int) extra.get(i).get("ocg_limit"));
            card.data.put("serial_number", (String) extra.get(i).get("serial_number"));
            card.data.put("tcg", (int) extra.get(i).get("tcg"));
            card.data.put("ocg", (int) extra.get(i).get("ocg"));
            card.data.put("pic", (String) extra.get(i).get("pic"));

            card.id = 1 + i + main.size();
            card.card_id = (int) extra.get(i).get("id");
            card.name = (String) extra.get(i).get("name");
            card.treated_as = (String) extra.get(i).get("treated_as");
            card.effect = (String) extra.get(i).get("effect");
            card.pendulum_effect = (String) extra.get(i).get("pendulum_effect");
            card.card_type = (String) extra.get(i).get("card_type");
            card.monster_color = (String) extra.get(i).get("monster_color");
            card.is_effect = (int) extra.get(i).get("is_effect");
            card.type = (String) extra.get(i).get("type");
            card.attribute = (String) extra.get(i).get("attribute");
            card.level = (int) extra.get(i).get("level");
            card.ability = (String) extra.get(i).get("ability");
            card.flip = (int) extra.get(i).get("flip");
            card.pendulum = (int) extra.get(i).get("pendulum");
            card.scale_left = (int) extra.get(i).get("scale_left");
            card.scale_right = (int) extra.get(i).get("scale_right");
            card.arrows = (String) extra.get(i).get("arrows");
            card.atk = (String) extra.get(i).get("atk");
            card.def = (String) extra.get(i).get("def");
            card.tcg_limit = (int) extra.get(i).get("tcg_limit");
            card.ocg_limit = (int) extra.get(i).get("ocg_limit");
            card.serial_number = (String) extra.get(i).get("serial_number");
            card.tcg = (int) extra.get(i).get("tcg");
            card.ocg = (int) extra.get(i).get("ocg");
            card.pic = (String) extra.get(i).get("pic");
            card.owner = this;
            card.controller = this;
            card.face_down = true;
            extra_arr.add(card);
            all_cards_arr.add(card);
        }
        for (int i = 0; i < side.size(); i++) {
            Card card = new Card();
            card.data.put("id", (int) side.get(i).get("id"));
            card.data.put("name", (String) side.get(i).get("name"));
            card.data.put("treated_as", (String) side.get(i).get("treated_as"));
            card.data.put("effect", (String) side.get(i).get("effect"));
            card.data.put("pendulum_effect", (String) side.get(i).get("pendulum_effect"));
            card.data.put("card_type", (String) side.get(i).get("card_type"));
            card.data.put("monster_color", (String) side.get(i).get("monster_color"));
            card.data.put("is_effect", (int) side.get(i).get("is_effect"));
            card.data.put("type", (String) side.get(i).get("type"));
            card.data.put("attribute", (String) side.get(i).get("attribute"));
            card.data.put("level", (int) side.get(i).get("level"));
            card.data.put("ability", (String) side.get(i).get("ability"));
            card.data.put("flip", (int) side.get(i).get("flip"));
            card.data.put("pendulum", (int) side.get(i).get("pendulum"));
            card.data.put("scale_left", (int) side.get(i).get("scale_left"));
            card.data.put("scale_right", (int) side.get(i).get("scale_right"));
            card.data.put("arrows", (String) side.get(i).get("arrows"));
            card.data.put("atk", (String) side.get(i).get("atk"));
            card.data.put("def", (String) side.get(i).get("def"));
            card.data.put("tcg_limit", (int) side.get(i).get("tcg_limit"));
            card.data.put("ocg_limit", (int) side.get(i).get("ocg_limit"));
            card.data.put("serial_number", (String) side.get(i).get("serial_number"));
            card.data.put("tcg", (int) side.get(i).get("tcg"));
            card.data.put("ocg", (int) side.get(i).get("ocg"));
            card.data.put("pic", (String) side.get(i).get("pic"));

            card.id = 1 + i + main.size() + extra.size();
            card.card_id = (int) side.get(i).get("id");
            card.name = (String) side.get(i).get("name");
            card.treated_as = (String) side.get(i).get("treated_as");
            card.effect = (String) side.get(i).get("effect");
            card.pendulum_effect = (String) side.get(i).get("pendulum_effect");
            card.card_type = (String) side.get(i).get("card_type");
            card.monster_color = (String) side.get(i).get("monster_color");
            card.is_effect = (int) side.get(i).get("is_effect");
            card.type = (String) side.get(i).get("type");
            card.attribute = (String) side.get(i).get("attribute");
            card.level = (int) side.get(i).get("level");
            card.ability = (String) side.get(i).get("ability");
            card.flip = (int) side.get(i).get("flip");
            card.pendulum = (int) side.get(i).get("pendulum");
            card.scale_left = (int) side.get(i).get("scale_left");
            card.scale_right = (int) side.get(i).get("scale_right");
            card.arrows = (String) side.get(i).get("arrows");
            card.atk = (String) side.get(i).get("atk");
            card.def = (String) side.get(i).get("def");
            card.tcg_limit = (int) side.get(i).get("tcg_limit");
            card.ocg_limit = (int) side.get(i).get("ocg_limit");
            card.serial_number = (String) side.get(i).get("serial_number");
            card.tcg = (int) side.get(i).get("tcg");
            card.ocg = (int) side.get(i).get("ocg");
            card.pic = (String) side.get(i).get("pic");
            card.owner = this;
            card.controller = this;
            side_arr.add(card);
            all_cards_arr.add(card);
        }
    }
    
    public void randomizeDeck(int start) {
        ArrayList<Card> shuffle_arr = new ArrayList<Card>();
        int index = 0;
        while (main_arr.size() > 0) {
            index = (int) Math.floor(Math.random() * main_arr.size());
            shuffle_arr.add(main_arr.get(index));
            main_arr.remove(index);
        }
            // NEW
            for (int i = 1; i < shuffle_arr.size(); i++) {
                if (shuffle_arr.get(i).name.equals(shuffle_arr.get(i - 1).name)) {
                    shuffle_arr.add(shuffle_arr.get(i));
                    shuffle_arr.remove(i);
                }
            }
            
        for (int i = 0; i < shuffle_arr.size(); i++) {
            main_arr.add(shuffle_arr.get(i));
            main_arr.get(i).id = (start + i); // ONLY FOR HERE
        }
        // ADDING TO EXTRA 11/2
        for (int i = 0; i < extra_arr.size(); i++) {
            extra_arr.get(i).id = (start + i + main_arr.size());
        }
    }
    
    public void resetDeck() {
        main_arr = new ArrayList<Card>();
        hand_arr = new ArrayList<Card>();
        field_arr = new ArrayList<Card>();
        grave_arr = new ArrayList<Card>();
        banished_arr = new ArrayList<Card>();
        extra_arr = new ArrayList<Card>();
        side_arr = new ArrayList<Card>();
        all_cards_arr = new ArrayList<Card>();
        m1 = null;
        m2 = null;
        m3 = null;
        m4 = null;
        m5 = null;
        s1 = null;
        s2 = null;
        s3 = null;
        s4 = null;
        s5 = null;
        fieldSpell = null;
        pendulumLeft = null;
        pendulumRight = null;
        linkLeft = null;
        linkRight = null;
        lifepoints = 8000;
    }
    
    public void revertToOriginal() {
        main = (ArrayList<JSONObject>) deck[2];
        side = (ArrayList<JSONObject>) deck[3];
        extra = (ArrayList<JSONObject>) deck[4];
    }
    
    public void setMatchDecks() {
        main_match_arr = main_arr;
        side_match_arr = side_arr;
        extra_match_arr = extra_arr;
    }
    
    public void alterDeck() {
        main = new ArrayList<JSONObject>();
        side = new ArrayList<JSONObject>();
        extra = new ArrayList<JSONObject>();
        for (int i = 0; i < main_arr.size(); i++) {
            main.add(main_arr.get(i).data);
        }
        for (int i = 0; i < side_arr.size(); i++) {
            side.add(side_arr.get(i).data);
        }
        for (int i = 0; i < extra_arr.size(); i++) {
            extra.add(extra_arr.get(i).data);
        }
    }
    
    public ArrayList<JSONObject> cardsToObjects(ArrayList<Card> arr) {
        ArrayList<JSONObject> cards = new ArrayList<JSONObject>();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject card = new JSONObject();
            card.put("id", arr.get(i).id);
            card.put("data", arr.get(i).data);
            cards.add(card);
        }
        return cards;
    }
    
    public JSONObject getDeckIds() {
        ArrayList<Integer> main_ids = new ArrayList<Integer>();
        ArrayList<Integer> side_ids = new ArrayList<Integer>();
        ArrayList<Integer> extra_ids = new ArrayList<Integer>();
        for (int i = 0; i < main.size(); i++) {
            main_ids.add((int) main.get(i).get("id"));
        }
        for (int i = 0; i < side.size(); i++) {
            side_ids.add((int) side.get(i).get("id"));
        }
        for (int i = 0; i < extra.size(); i++) {
            extra_ids.add((int) extra.get(i).get("id"));
        }
        JSONObject obj = new JSONObject();
        obj.put("main", main_ids);
        obj.put("side", side_ids);
        obj.put("extra", extra_ids);
        return obj;
    }
    
    public ArrayList<Integer> cardsToIds(ArrayList<Card> arr) {
        ArrayList<Integer> cards = new ArrayList<Integer>();
        for (int i = 0; i < arr.size(); i++) {
            cards.add(arr.get(i).id);
        }
        return cards;
    }
    
    public int getCardAdvantage() {
        int total = 0;
        for (int i = 0; i < hand_arr.size(); i++) {
            total++;
        }
        for (int i = 0; i < field_arr.size(); i++) {
            total++;
        }
        return total;
    }
    
    public void refreshPlayer() {
        rpsChoice = "";
        viewing = "";
        agree_to_draw = false;
        agree_to_rematch = false;
        done_siding = false;
        exchanging = false;
        exchanged_card = 0;
        deck_face_up = false;
        seen_deck = false;
        seen_extra = false;
    }
    
    public Boolean hasAvailableMonsterZone() {
        if (m1 == null) {
            return true;
        }
        if (m2 == null) {
            return true;
        }
        if (m3 == null) {
            return true;
        }
        if (m4 == null) {
            return true;
        }
        if (m5 == null) {
            return true;
        }
        return false;
    }
}