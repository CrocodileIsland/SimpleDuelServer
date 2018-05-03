package nettysocketserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONObject;

public class Duel {
    
    int id;
    Boolean active = true;
    Player player1;
    Player player2;
    String username1;
    String username2;
    String type;
    String note = "";
    String format;
    Boolean has_duel_password = false;
    Boolean has_watch_password = false;
    Boolean watching = true;
    String watch_note = "";
    String watch_password = "";
    String language = "English";
    String rules = "*";
    int rating_low;
    int rating_high;
    int experience_low;
    int experience_high;
    int friends1;
    int friends2;
    Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
    Timestamp endTimestamp;
    String status = "RPS";
    String rpsWinner;
    int tokens = 501;
    String currentPhase = "";
    String turn_player = "";
    Player winner;
    int winner_id = 0;
    int games = 1;
    int times_rematched = 0;
    Player loser;
    Boolean draw = false;
    Boolean rated = false;
    Boolean links = false;
    Boolean links_chosen = false;
    Card linkLeft;
    Card linkRight;
    int turnCount = 1;
    Call call;
    Boolean awaiting_admin = false;
    Boolean paused = false;
    ArrayList<Watcher> watchers = new ArrayList<Watcher>();
    ArrayList<JSONObject> log = new ArrayList<JSONObject>();
    public ArrayList<Message> WatchMessages = new ArrayList<Message>();
    public ArrayList<Message> DuelMessages = new ArrayList<Message>();
    int seconds = 0;
    long startTime = System.nanoTime();
    JSONObject replay_data = new JSONObject();
    ArrayList<JSONObject> replay_arr = new ArrayList<JSONObject>();
    Boolean unlocked = false;
    Boolean duel_links = false;
    
    public Duel() {
        init();
    }
    
    public void init() {
        id = 0;
        active = true;
        player1 = null;
        player2 = null;
        username1 = null;
        username2 = null;
        type = null;
        note = "";
        format = null;
        has_duel_password = false;
        has_watch_password = false;
        watching = true;
        watch_note = "";
        watch_password = "";
        language = "English";
        rules = "*";
        rating_low = 0;
        rating_high = 0;
        experience_low = 0;
        experience_high = 0;
        friends1 = 0;
        friends2 = 0;
        startTimestamp = new Timestamp(System.currentTimeMillis());
        endTimestamp = null;
        status = "RPS";
        rpsWinner = null;
        tokens = 501;
        currentPhase = "";
        turn_player = "";
        winner = null;
        winner_id = 0;
        games = 1;
        times_rematched = 0;
        loser = null;
        draw = false;
        rated = false;
        links = false;
        links_chosen = false;
        linkLeft = null;
        linkRight = null;
        turnCount = 1;
        call = null;
        awaiting_admin = false;
        paused = false;
        watchers.clear();
        log.clear();
        WatchMessages = new ArrayList<Message>();
        DuelMessages = new ArrayList<Message>();
        seconds = 0;
        startTime = System.nanoTime();
        replay_data = new JSONObject();
        replay_arr.clear();
        unlocked = false;
        duel_links = false;
    }
    
    public void addReplay(JSONObject result) {
        JSONObject obj = new JSONObject(result, JSONObject.getNames(result));
        if (obj.has("deck")) {
            if (!obj.get("play").equals("Shuffle deck")) {
                obj.remove("deck");
            }
        }
        if (replay_arr.size() > 0) {
            if (replay_arr.get(replay_arr.size() - 1).toString().equals(obj.toString()) && DataHandler.GeneralPlays.indexOf((String) obj.get("play")) < 0) {
                return;
            }
        }
        replay_arr.add(obj);
    }
    
    public void addLog(JSONObject result) {
        JSONObject obj = new JSONObject(result, JSONObject.getNames(result));
        obj.put("seconds", seconds);
        log.add(obj);
    }
    
    public void initReplayData(JSONObject result) {
        replay_data = new JSONObject();
        replay_data.put("id", id);
        replay_data.put("version", 8);
        replay_data.put("rated", rated);
        replay_data.put("links", links);
        replay_data.put("match_type", type);
        replay_data.put("format", format);
        replay_data.put("show_cards", false); // obsolete
        replay_data.put("logs", result.get("logs"));
        replay_data.put("player1", result.get("player1"));
        replay_data.put("player2", result.get("player2"));
        if (result.has("player3")) {
            replay_data.put("player3", result.get("player3"));
            replay_data.put("player4", result.get("player4"));
        }
    }
    
    public void initReplayData2() {
        if (replay_data.has("plays")) {
            replay_data.remove("plays");
        }
        replay_data.put("plays", replay_arr); // if the replay was updated before, add the up to date plays
        if (replay_data.has("date")) {
            replay_data.remove("date");
        }
        String date = new Timestamp(System.currentTimeMillis()).toString();
        if (date.indexOf(".") >= 0) {
            date = date.substring(0, date.indexOf("."));
        }
        replay_data.put("date", date);
    }
    
    public void saveReplayToDB() {
        // private
    }
}