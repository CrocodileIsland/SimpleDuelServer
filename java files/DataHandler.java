package nettysocketserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.net.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;

public class DataHandler {
    
    public static ArrayList<Connection> Connections = new ArrayList<Connection>();
    public static ArrayList<User> Users = new ArrayList<User>();
    //public static ArrayList<User> ConnectingUsers = new ArrayList<User>();
    public static ArrayList<User> UserStates = new ArrayList<User>();
    public static ArrayList<User> Admins = new ArrayList<User>();
    public static ArrayList<User> Pool = new ArrayList<User>();
    public static ArrayList<User> Hosts = new ArrayList<User>();
    public static ArrayList<Duel> Duels = new ArrayList<Duel>();
    public static ArrayList<Duel> RecycledDuels = new ArrayList<Duel>();
    public static ArrayList<Player> RecycledPlayers = new ArrayList<Player>();
    public static ArrayList<Message> RecycledMessages = new ArrayList<Message>();
    public static ArrayList<Status> RecycledStatuses = new ArrayList<Status>();
    public static ArrayList<JournalEntry> RecycledJournalEntries = new ArrayList<JournalEntry>();
    public static ArrayList<DuelEntry> RecycledDuelEntries = new ArrayList<DuelEntry>();
    public static ArrayList<Friend> RecycledFriends = new ArrayList<Friend>();
    public static ArrayList<Watcher> RecycledWatchers = new ArrayList<Watcher>();
    public static ArrayList<User> DuelRoom = new ArrayList<User>();
    public static ArrayList<Call> Calls = new ArrayList<Call>();
    public static ArrayList<String> GoatFormatCards = new ArrayList<String>();
    public static ArrayList<Integer> GoatFormatLimits = new ArrayList<Integer>();
    public static ArrayList<String> DuelLinksCards = new ArrayList<String>();
    public static ArrayList<Integer> DuelLinksLimits = new ArrayList<Integer>();
    public static ArrayList<Status> StatusUpdates = new ArrayList<Status>();
    public static ArrayList<Card> Cards = new ArrayList<Card>();
    public static ArrayList<Card> CardsAlpha = new ArrayList<Card>();
    public static ArrayList<JSONObject> CardObjects = new ArrayList<JSONObject>();
    public static ArrayList<Group> Groups = new ArrayList<Group>();
    public static ArrayList<Message> PublicMessages = new ArrayList<Message>();
    public static ArrayList<Message> WatchMessages = new ArrayList<Message>();
    public static ArrayList<Message> GroupMessages = new ArrayList<Message>();
    public static ArrayList<Message> PrivateMessages = new ArrayList<Message>();
    public static ArrayList<AdminAction> AdminActions = new ArrayList<AdminAction>();
    //public static ArrayList<Status> StatusUpdatesQueue = new ArrayList<Status>();
    public static ArrayList<JournalEntry> JournalEntries = new ArrayList<JournalEntry>();
    public static ArrayList<DuelEntry> DuelEntries = new ArrayList<DuelEntry>();
    public static ArrayList<String> BlacklistedIPs = new ArrayList<String>();
    public static ArrayList<String> BlacklistedDBIds = new ArrayList<String>();
    public static ArrayList<String> WhitelistedIPs = new ArrayList<String>();
    public static ArrayList<String> WhitelistedUsernames = new ArrayList<String>();
    public static ArrayList<String> FrozenIPs = new ArrayList<String>();
    public static ArrayList<String> Donators = new ArrayList<String>();
    public static ArrayList<String> RestrictedPhrases = new ArrayList<String>();
    public static ArrayList<String> IllegalPhrases = new ArrayList<String>();
    public static ArrayList<Integer> GithubAttemptArray = new ArrayList<Integer>();
    public static ArrayList<Integer> DuelingbookAttemptArray = new ArrayList<Integer>();
    public static ArrayList<Integer> GithubSuccessArray = new ArrayList<Integer>();
    public static ArrayList<Integer> DuelingbookSuccessArray = new ArrayList<Integer>();
    public static ArrayList<String> UnseenMessageUsernames = new ArrayList<String>();
    public static ArrayList<String> NewUnseenMessageUsernames = new ArrayList<String>();
    public static ArrayList<Message> UnseenMessages = new ArrayList<Message>();
    public static ArrayList<String> FriendRequestsUsernames = new ArrayList<String>();
    public static ArrayList<String> UnseenCommentsUsernames = new ArrayList<String>();
    public static ArrayList<String> SmallDonations = new ArrayList<String>();
    public static ArrayList<String> InvalidDonations = new ArrayList<String>();
    public static ArrayList<String> BadRatings = new ArrayList<String>();
    public static ArrayList<String> DuplicateRatings = new ArrayList<String>();
    //public static ArrayList<String> WebsocketUsers = new ArrayList<String>();
    public static ArrayList<String> Moderators = new ArrayList<String>();
    public static String InfoLog = "";
    public static String DuplicateLog = "";
    public static String WriteLog = "";
    public static String ActionsLog = "";
    public static String ActionsLogging = "";
    public static int ActionLoggingTimes = 0;
    public static String DuplicateRatingLog = "";
    public static String CardError = "";
    public static String LostConnectionLog = "";
    public static String IllegalPhrasesLog = "";
    public static Timer messagesTimer;
    public static TimerTask messagesTimerTask;
    public static int messageSeconds = 0;
    public static int PublicMessageIds = 0;
    public static int DuelIds = 0;
    public static int GroupIds = 0;
    public static int StatusIds = 0;
    public static int DeckIds = 0;
    public static int TotalDuels = 0;
    public static int TotalCalls = 0;
    public static int TotalUsers = 0;
    //public static int CurrentFolder = 0;
    public static int BotLimit = 0;
    public static int ConnectionIndex = 0;
    public static Timer rebootTimer;
    public static TimerTask rebootTimerTask;
    public static int rebootSeconds = 0;
    public static Boolean rebooting = false;
    public static int announcementId;
    public static String announcementHeading;
    public static String announcementBody;
    public static Boolean caution = false;
    public static int GroupIterations = 0;
    public static int QPS = 0;
    public static int lastQPS = 0;
    public static int TotalQPS = 0;
    public static int Minutes = 0;
    public static long LastActionMillis = 0;
    public static int TotalDuelObjects = 0;
    public static int TotalPlayers = 0;
    public static int TotalMessages = 0;
    public static int TotalStatuses = 0;
    public static int TotalJournalEntries = 0;
    public static int TotalDuelEntries = 0;
    public static int TotalFriends = 0;
    public static int TotalWatchers = 0;
    public static Boolean PoolEnabled = true;
    public static int PoolEvents = 0;
    public static Timer qpsTimer;
    public static TimerTask qpsTimeoutTask;
    public static Pattern DuplicatePattern = Pattern.compile("([a-zA-Z0-9 ])\\1{10}");
    public static Pattern ASCIIPattern = Pattern.compile("\\A\\p{ASCII}*\\z");
    public static Pattern NormailizePattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    public static final Pattern EmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static SortIgnoreCase sortIgnoreCase = new SortIgnoreCase();
    public static CardSorter cardSorter = new CardSorter();
    //public static CallableStatement getDuelingbookUserCS;
    //public static CallableStatement getLoginCS;
    //public static CallableStatement getDecksCS;
    //public static CallableStatement getFollowingCS;
    //public static CallableStatement getFriendsCS;
    //public static CallableStatement loadMessagesCS;
    //public static CallableStatement loadAllMessagesCS;
    public static Boolean CheckingAFK = false;
    public static Boolean ReportingLostConnections = false;
    public static Boolean ReportingActions = true;
    public static Boolean ReportingQueries = true;
    public static Boolean BypassingDuelingbookUser = true;
    public static Boolean NewUsersCannotPostStatuses = false;
    public static Boolean LowExpCannotPostStatuses = false;
    public static Boolean DebuggingStatuses = false;
    public static Boolean UpdatingRatings = true;
    public static Boolean InsertingMessages = false;
    public static String[] GeneralPlaysArr = {"Call admin", "Cancel call", "Admit defeat", "Quit duel", "Offer draw", "Revoke draw", "Accept draw", "Offer rematch", "Revoke rematch", "Game loss", "Match loss", "Loss", "Cancel game", "Accept rematch", "Siding", "Begin next duel", "Back to RPS", "Add watcher", "Remove watcher", "Duel message", "Watcher message", "Start turn", "Summon token", "Call admin"};
    public static ArrayList<String> GeneralPlays = new ArrayList<String>(Arrays.asList(GeneralPlaysArr));
    public static String Policy = "<?xml version=\"1.0\"?>\n"
        + "<!DOCTYPE cross-domain-policy SYSTEM \"https://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">\n"
        + "<cross-domain-policy>\n"
        + "	<site-control permitted-cross-domain-policies=\"all\"/>\n"
        + "	<allow-access-from domain=\"*\" to-ports=\"*\" secure=\"false\"/>\n"
        + "	<allow-http-request-headers-from domain=\"*\" headers=\"*\" secure=\"false\"/>\n"
        + "</cross-domain-policy>";
    public static String[] Suffixes =
        //    0     1     2     3     4     5     6     7     8     9
        { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        //    10    11    12    13    14    15    16    17    18    19
        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
        //    20    21    22    23    24    25    26    27    28    29
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        //    30    31    32
        "th", "st", "nd"};
    public static int[] Avatars = {77, 226, 293, 303, 5661, 6540, 675, 808, 826, 4929, 1075, 1079, 1393, 1363, 1510, 1874, 7414, 1763, 7175, 1896, 2095, 2127, 5215, 2319, 2368, 2492, 4228, 2632, 2699, 3035, 7229, 3127, 3200, 3276, 7590, 3904, 4251, 4397, 4432, 2715, 4840, 8369, 513, 842, 1001, 4094, 4945, 3540, 158, 886, 1138, 1374, 3466, 4885, 85, 145, 214, 5097, 5096, 5095, 144, 430, 437, 3360, 3506, 4032, 8831, 7188, 7232, 5022, 6786, 8217, 8056, 8653, 5875, 5873, 5876, 5874, 424, 668, 4932, 1051, 2698, 6457, 8937, 8092, 8766, 8365, 7114, 8343, 512, 8560, 1604, 5920, 4476, 4959, 6013, 6011, 6012, 240, 1247, 5288};
    public static int[] WinsNeededArr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 25, 25, 25, 25, 50, 50, 50, 50, 50, 50, 75, 75, 75, 75, 75, 75, 100, 100, 100, 100, 100, 100, 250, 250, 250, 250, 250, 250, 500, 500, 500, 500, 500, 500, 750, 750, 750, 750, 750, 750, 1000, 1000, 1000, 1000, 1000, 1000, 2000, 2000, 2000, 2000, 2000, 2000, 3000, 3000, 3000, 3000, 3000, 3000};
    public static Timestamp StartupTimestamp = new Timestamp(System.currentTimeMillis());
    public static int MaxUsers = 9000;
    public static int MostUsers = 0;
    public static int JudgesGroup = 8;
    public static String DB_URL;
    public static String DB_DATABASE;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;
    public static String GITHUB_TOKEN;
    public static String PAYPAL_EMAIL;
    public static String PAYPAL_PASSWORD;
    public static String PAYPAL_SIGNATURE;
    public static String CLEVERBOT_KEY;
    public static String SECRET_PASSWORD;
    public static ArrayList<String> SECRET_USERS = new ArrayList<String>();
    public static ArrayList<String> VIP_USERS = new ArrayList<String>();
    public static String PROFILE_PICS_PATH;
    public static int Version = 108;
    
    public static void startUp() {
        if (Main.local == true) {
            //caution = true;
            WhitelistedIPs.add("127.0.0.1");
        }
        if (InsertingMessages == false) {
            //caution = true;
            UpdatingRatings = false;
        }
        createConnection();
        loadCards();
        try {
            String query = "SELECT MAX(duel_id) AS id FROM duels";
            Statement st = getConnection().createStatement();       
            ResultSet rs = executeQuery(st, query);
            if (rs.next()) {
                DuelIds = rs.getInt("id");
            }
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            String query = "SELECT MAX(id) as id FROM decks";
            Statement st = getConnection().createStatement();      
            ResultSet rs = executeQuery(st, query);
            if (rs.next()) {
                DeckIds = rs.getInt("id");
            }
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        qpsTimer = new Timer();
        qpsTimeoutTask = new TimerTask() {
            public void run() {
                try {
                    lastQPS = QPS;
                    TotalQPS += QPS;
                    QPS = 0;
                    PoolEvents++;
                    poolEvent();
                    for (int i = 0; i < Duels.size(); i++) {
                        Duels.get(i).seconds++;
                        if (Duels.get(i).rated == true && Duels.get(i).active == true) {
                            Duels.get(i).player1.checkAFK();
                            Duels.get(i).player2.checkAFK();
                        }
                    }
                    for (int i = 0; i < Hosts.size(); i++) {
                        if (Hosts.get(i) == null) {
                            Hosts.remove(i);
                            i--;
                            continue;
                        }
                        Hosts.get(i).pool_seconds++;
                    }
                    for (int i = 0; i < Pool.size(); i++) {
                        if (Pool.get(i) == null) {
                            Pool.remove(i);
                            i--;
                            continue;
                        }
                        Pool.get(i).pool_seconds++;
                    }
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        };
        qpsTimer.scheduleAtFixedRate(qpsTimeoutTask,1000,1000);
    }
    
    public static void onData(final ChannelHandlerContext nbc, final JSONObject data) {
        try {
            User user = null;
            JSONObject result = new JSONObject();
            Matcher matcher = ASCIIPattern.matcher(data.toString());
            if (!matcher.find()) {
                errorE(nbc, "Special characters were detected. Your request did not go through.");
                return;
            }
            if (InsertingMessages == true) {
                if (System.currentTimeMillis() - LastActionMillis > 5000) {
                    ActionLoggingTimes++;
                    ActionsLog = ActionsLogging + "\nOnline users: " + Users.size() + "\nSeconds: " + (System.currentTimeMillis() - LastActionMillis) + "\nTime: " + new Timestamp(System.currentTimeMillis()).toString() + "\nTimes:" + ActionLoggingTimes + "\nNext Data String: " + data.toString();
                }
                LastActionMillis = System.currentTimeMillis();
                ActionsLogging += data.toString() + "\n";
                if (ActionsLogging.length() > 5000) {
                    ActionsLogging = "";
                }
            }
            for (int i = 0; i < Users.size(); i++) {
                if (Users.get(i).nbc != null) {
                    if (Users.get(i).nbc.equals(nbc)) {
                        user = Users.get(i);
                        user.nbc = nbc;
                        if (data.has("action")) {
                            if (!data.get("action").equals("Heartbeat") && !data.get("action").equals("Activate") && !data.get("action").equals("Deactivate")) {
                                if (!data.get("action").equals("Load statuses") && !data.get("action").equals("Get groups") && !data.get("action").equals("Connect")) {
                                    if (user.timeout_timestamp != null) {
                                        if (user.timeout_timestamp.getTime() > System.currentTimeMillis() - 300) {
                                            if (data.toString().equals(user.lastDataString) || data.get("action").toString().indexOf("message") >= 0) {
                                                return;
                                            }
                                        }
                                    }
                                }
                                user.seconds = 0;
                                user.timeout_timestamp = new Timestamp(System.currentTimeMillis());
                            }
                            else {
                                user.connection_timestamp = new Timestamp(System.currentTimeMillis());
                            }
                            if (data.has("message")) {
                                if (((String) data.get("message")).length() > 500) {
                                    invalidRequest(nbc);
                                    return;
                                }
                            }
                        }
                        else {
                            System.out.println("data = " + data);
                        }
                    }
                }
            }
            String action = (String) data.get("action");
            if (user != null) {
                if (user.banned == true) {
                    result = new JSONObject();
                    result.put("action", "Banned");
                    write(nbc, result);
                    InfoLog += "On line 571, " + user.username + " is banned. ip_address = " + user.ip_address + ", nbc_address = " + user.nbc_address + "\n";
                    return;
                }
                user.defragmenting = false;
                user.lastDataString = data.toString();
                if (user.actions.size() > 50) {
                    JSONObject dead_action = user.actions.get(0);
                    dead_action = null;
                    user.actions.remove(0);
                }
                JSONObject new_action = new JSONObject(data, JSONObject.getNames(data));
                new_action.put("time", System.currentTimeMillis());
                user.actions.add(new_action);
                //user.actions.add(new Timestamp(System.currentTimeMillis()) + " - " + (String) data.get("action"));
                
                for (int i = 0; i < IllegalPhrases.size(); i++) {
                    if (data.toString().contains(IllegalPhrases.get(i))) {
                        if (!data.toString().contains("Remove illegal phrase") && !data.toString().contains("Add illegal phrase")) {
                            IllegalPhrasesLog += user.username + " tried to say " + IllegalPhrases.get(i) + " in " + data.toString() + "\n";
                            result.put("action", "Unlock");
                            write(nbc, result);
                            return;
                        }
                    }
                }
                if (!action.equals("Heartbeat") && !action.equals("Get user id")) {
                    user.lastAction = data.toString();
                }
            }
            switch(action) {
                case "Connect":
                    //connect(nbc, data, true);
                    connect(nbc, data);
                    return;
            }
            if (user == null) {
                if (action.equals("Activate") || action.equals("Deactivate")) {
                    return;
                }
                result = new JSONObject();
                result.put("action", "Rejected");
                result.put("message", "User is null");
                write(nbc, result);
                return;
            }
            switch (action) {
                case "Join pool":
                    joinPool(nbc, data, user);
                    return;
                case "Leave pool":
                    leavePool(nbc, data, user);
                    return;
                case "Ready":
                    readyE(nbc, data, user);
                    return;
                case "Host duel":
                    hostDuel(nbc, data, user);
                    return;
                case "Cancel duel":
                    cancelDuel(nbc, data, user);
                    return;
                case "Join duel":
                    joinDuel(nbc, data, user);
                    return;
                case "Leave duel":
                    leaveDuel(nbc, data, user);
                    return;
                case "Reject user":
                    rejectUser(nbc, data, user);
                    return;
                case "Accept user":
                    acceptDuel(nbc, data, user);
                    return;
                case "Duel type error":
                    duelTypeError(nbc, data, user);
                    return;
                case "Duel":
                    getDuel(nbc, data, user);
                    return;
                case "Watch duel":
                    watchDuel(nbc, data, user);
                    return;
                case "Watcher message":
                    watcherMessage(nbc, data, user);
                    return;
                case "Exit duel":
                    exitDuel(nbc, data, user);
                    return;
                case "Exit duel room":
                    exitDuelRoom(nbc, data, user);
                    return;
                case "Host loss":
                case "Opponent loss":
                    gameloss(nbc, data, user);
                    return;
                case "Cancel game":
                    cancelGame(nbc, data, user);
                    return;
                case "Refresh log":
                    refreshLog(nbc, data, user);
                    return;
                /*case "Answer call":
                    answerCall(nbc, data, user);
                    return;*/
            }
            doData(action, nbc, data, user);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void doData(final String action, final ChannelHandlerContext nbc, final JSONObject data, final User user) {
        new Thread(new Runnable() {
            public void run(){
                try {
                    switch(action) {
                        case "Heartbeat":
                            heartbeat(nbc, data, user);
                            break;
                        /*case "Goat format":
                            goatFormat(nbc, data, user);
                            break;
                        case "Delete goat":
                            deleteGoat(nbc, data, user);
                            break;
                        case "Load goat":
                            loadGoat(nbc, data, user);
                            break;
                        case "Verify goat password":
                            verifyGoatPassword(nbc, data, user);
                            break;
                        case "Load statuses":
                            refreshStatusUpdates(nbc, data, user);
                            break;
                        case "Post status":
                            postStatusUpdate(nbc, data, user);
                            break;
                        case "Delete status":
                            deleteStatus(nbc, data, user);
                            break;*/
                        case "Public message":
                            publicMessage(nbc, data, user);
                            break;
                        case "Private message":
                        case "Offline message":
                            privateMessage(nbc, data, user);
                            break;
                        /*case "Group message":
                            groupMessage(nbc, data, user);
                            break;
                        case "Load all messages":
                            loadAllMessages(nbc, data, user);
                            break;
                        case "Load private chat":
                        case "Load all private chat":
                            loadMessages(nbc, data, user);
                            break;
                        case "Load public chat":
                            loadPublicChat(nbc, data, user);
                            break;*/
                        case "Load watchers chat":
                            loadWatchersChat(nbc, data, user);
                            break;
                        case "Load duel chat":
                            loadDuelChat(nbc, data, user);
                            break;
                        /*case "Load group chat":
                            loadGroupChat(nbc, data, user);
                            break;
                        case "Get groups":
                            getGroups(nbc, data, user);
                            break;
                        case "Create group":
                            createGroup(nbc, data, user);
                            break;
                        case "Add user to group":
                            addUserToGroup(nbc, data, user);
                            break;
                        case "Remove user from group":
                            removeUserFromGroup(nbc, data, user);
                            break;
                        case "Join group":
                            joinGroup(nbc, data, user);
                            break;
                        case "Leave group":
                            leaveGroup(nbc, data, user);
                            break;
                        case "Make group public":
                            makeGroupPublic(nbc, data, user);
                            break;
                        case "Make group private":
                            makeGroupPrivate(nbc, data, user);
                            break;
                        case "Hide public message":
                            hidePublicMessage(nbc, data, user);
                            break;
                        case "Hide public messages":
                            hidePublicMessages(nbc, data, user);
                            break;
                        case "Delete private message":
                            deletePrivateMessage(nbc, data, user);
                            break;
                        case "Auto complete":
                            autoComplete(nbc, data, user);
                            break;*/
                        case "Load profile":
                            loadProfile(nbc, data, user);
                            break;
                        case "Load my profile":
                            loadMyProfile(nbc, data, user);
                            break;
                        /*case "Load settings":
                            loadSettings(nbc, data, user);
                            break;
                        case "Get profile pic":
                            getProfilePic(nbc, data, user);
                            break;
                        case "Get sleeve":
                            getSleeve(nbc, data, user);
                            break;*/
                        case "Save profile":
                            saveProfile(nbc, data, user);
                            break;
                        /*case "Save judge profile":
                            saveJudgeProfile(nbc, data, user);
                            break;
                        case "Refresh user":
                            refreshUser(nbc, data, user);
                            break;
                        case "Remove image":
                            removeImage(nbc, data, user);
                            break;
                        case "Set NSFW":
                            setNSFW(nbc, data, user);
                            break;*/
                        case "Reload profile":
                            loadProfile(nbc, data, user);
                            break;
                        /*case "Like comment":
                            likeComment(nbc, data, user);
                            break;
                        case "Unlike comment":
                            unlikeComment(nbc, data, user);
                            break;
                        case "Like status":
                            likeStatus(nbc, data, user);
                            break;
                        case "Unlike status":
                            unlikeStatus(nbc, data, user);
                            break;*/
                        case "Calculate single ranking":
                            calculateSingleRanking(nbc, data, user);
                            break;
                        case "Calculate match ranking":
                            calculateMatchRanking(nbc, data, user);
                            break;
                        /*case "Post comment":
                            postComment(nbc, data, user);
                            break;
                        case "Approve comment":
                            approveComment(nbc, data, user);
                            break;
                        case "Delete comment":
                            deleteComment(nbc, data, user);
                            break;
                        case "Delete testimonial":
                            deleteTestimonial(nbc, data, user);
                            break;
                        case "Post testimonial":
                            postTestimonial(nbc, data, user);
                            break;
                        case "Browse users":
                            browseUsers(nbc, data, user);
                            break;*/
                        case "Ranking by rating":
                            rankingByRating(nbc, data, user);
                            break;
                        case "Ranking by wins":
                            rankingByWins(nbc, data, user);
                            break;
                        case "Ranking by rep":
                            rankingByRep(nbc, data, user);
                            break;
                        case "Ranking by total rep":
                            rankingByTotalRep(nbc, data, user);
                            break;
                        /*case "Load following":
                            loadFollowing(nbc, data, user);
                            break;
                        case "Load friend requests":
                            loadFriendRequests(nbc, data, user);
                            break;
                        case "Load blocked users":
                            loadBlockedUsers(nbc, data, user);
                            break;
                        case "Load comments":
                            loadComments(nbc, data, user);
                            break;
                        case "Load user comments":
                            loadUserComments(nbc, data, user);
                            break;
                        case "Save about me":
                            saveAboutMe(nbc, data, user);
                            break;
                        case "Save social media":
                            saveSocialMedia(nbc, data, user);
                            break;
                        case "Get social media":
                            getSocialMedia(nbc, data, user);
                            break;
                        case "Save profile song":
                            refreshProfileSong(user);
                            break;
                        case "Remove song":
                            removeSong(nbc, data, user);
                            break;
                        case "Save timezone":
                            saveTimezone(nbc, data, user);
                            break;
                        case "Save message settings":
                            saveMessageSettings(nbc, data, user);
                            break;
                        case "Save comments settings":
                            saveCommentSettings(nbc, data, user);
                            break;
                        case "Save group settings":
                            saveGroupSettings(nbc, data, user);
                            break;
                        case "Save animation":
                            saveAnimation(nbc, data, user);
                            break;
                        case "Save preferences":
                            savePreferences(nbc, data, user);
                            break;
                        case "Load all images":
                            loadAllImages(nbc, data, user);
                            break;
                        case "Remember me":
                            rememberMe(nbc, data, user);
                            break;
                        case "Redeem donation":
                            redeemDonation(nbc, data, user);
                            break;
                        case "Get donation info":
                            getDonationInfo(nbc, data, user);
                            break;
                        case "Load customs":
                            loadCustoms(nbc, data, user);
                            break;
                        case "Load frozen users":
                            loadFrozenUsers(nbc, data, user);
                            break;
                        case "Load beginners":
                            loadBeginners(nbc, data, user);
                            break;
                        case "Remove customs":
                            removeCustoms(nbc, data, user);
                            break;
                        case "Load admin actions":
                            loadAdminActions(nbc, data, user);
                            break;
                        case "Update position":
                            updatePosition(nbc, data, user);
                            break;
                        case "Follow user":
                            followUser(nbc, data, user);
                            break;
                        case "Unfollow user":
                            unfollowUser(nbc, data, user);
                            break;
                        case "Add friend":
                            addFriend(nbc, data, user);
                            break;
                        case "Revoke request":
                            revokeRequest(nbc, data, user);
                            break;
                        case "Accept request":
                            acceptRequest(nbc, data, user);
                            break;
                        case "Accept all requests":
                            acceptAllRequests(nbc, data, user);
                            break;
                        case "Reject request":
                            rejectRequest(nbc, data, user);
                            break;
                        case "Delete friend":
                            deleteFriend(nbc, data, user);
                            break;
                        case "Block user":
                            blockUser(nbc, data, user);
                            break;
                        case "Unblock user":
                            unblockUser(nbc, data, user);
                            break;
                        case "Load my custom cards":
                            loadMyCustomCards(nbc, data, user);
                            break;*/
                        case "Search cards":
                            searchCards(nbc, data, user);
                            break;
                        case "Load deck":
                            loadDeckE(nbc, data, user);
                            break;
                        case "Load decklists":
                            loadDecklistsE(nbc, data, user);
                            break;
                        case "Save token":
                            saveToken(nbc, data, user);
                            break;
                        case "Save deck":
                        case "Save deck as":
                            saveDeck(nbc, data, user);
                            break;
                        case "Set default":
                            setDefault(nbc, data, user);
                            break;
                        case "Rename deck":
                            renameDeck(nbc, data, user);
                            break;
                        case "Delete deck":
                            deleteDeck(nbc, data, user);
                            break;
                        case "New deck":
                            newDeck(nbc, data, user);
                            break;
                        case "Check goat":
                        case "Check deck":
                            checkDeck(nbc, data, user);
                            break;
                        case "Verify deck":
                            verifyDeckE(nbc, data, user);
                            break;
                        case "Verify YDK":
                            verifyYDK(nbc, data, user);
                            break;
                        case "Import deck":
                            importDeck(nbc, data, user);
                            break;
                        case "Load hosting":
                            loadHosting(nbc, data, user);
                            break;
                        case "Load watching":
                            loadWatching(nbc, data, user);
                            break;
                        /*case "Duel records":
                            duelRecords(nbc, data, user);
                            break;
                        case "Ban status":
                            getBanStatus(nbc, data, user);
                            break;
                        case "Set moderator":
                            setModerator(nbc, data, user);
                            break;
                        case "Unset moderator":
                            unsetModerator(nbc, data, user);
                            break;
                        case "Mute user":
                            muteUser(nbc, data, user);
                            break;
                        case "Unmute user":
                            unmuteUser(nbc, data, user);
                            break;
                        case "Set beginner":
                            setBeginner(nbc, data, user);
                            break;
                        case "Unset beginner":
                            unsetBeginner(nbc, data, user);
                            break;
                        case "Set ignored":
                            setIgnored(nbc, data, user);
                            break;
                        case "Unset ignored":
                            unsetIgnored(nbc, data, user);
                            break;
                        case "Grant customs":
                            grantCustomsE(nbc, data, user);
                            break;
                        case "Freeze user":
                            freezeUser(nbc, data, user);
                            break;
                        case "Ban user":
                            freezeUser(nbc, data, user);
                            break;
                        case "Unban user":
                            unbanUser(nbc, data, user);
                            break;
                        case "Kick user":
                            kickUserE(nbc, data, user);
                            break;
                        case "Add warning":
                            addWarning(nbc, data, user);
                            break;
                        case "IP check":
                            ipCheck(nbc, data, user);
                            break;
                        case "Comp check":
                            compCheck(nbc, data, user);
                            break;
                        case "Set admin":
                            setAdmin(nbc, data, user);
                            break;
                        case "Get admin":
                            getAdmin(nbc, data, user);
                            break;
                        case "Reset stats":
                            resetStats(nbc, data, user);
                            break;
                        case "Super ban":
                            superBan(nbc, data, user);
                            break;
                        case "Reboot server":
                            rebootE(nbc, data, user);
                            break;
                        case "Cancel reboot":
                            cancelReboot(nbc, data, user);
                            break;*/
                        case "Caution":
                            cautionE(nbc, data, user);
                            break;
                        case "Refresh cards":
                            refreshCards(nbc, data, user);
                            break;
                        /*case "QPS":
                            checkQPS(nbc, data, user);
                            break;
                        case "Skim duels":
                            skimDuels(nbc, data, user);
                            break;
                        case "Empty users":
                            emptyUserStatesE(nbc, data, user);
                            break;
                        case "GC":
                            garbageCollection(nbc, data, user);
                            break;
                        case "Max users":
                            maxUsers(nbc, data, user);
                            break;*/
                        case "Toggle rated":
                            toggleRated(nbc, data, user);
                            break;
                        /*case "Toggle ratings":
                            toggleRatings(nbc, data, user);
                            break;
                        case "Repair status updates":
                            repairStatusUpdates(nbc, data, user);
                            break;*/
                        case "Get actions":
                            getActions(nbc, data, user);
                            break;
                        case "Get screenshot":
                            getScreenshot(nbc, data, user);
                            break;
                        case "Send screenshot":
                            sendScreenshot(nbc, data, user);
                            break;
                        case "Check decks":
                            checkDecks(nbc, data, user);
                            break;
                        /*case "Add restricted phrase":
                            addRestrictedPhrase(nbc, data, user);
                            break;
                        case "Remove restricted phrase":
                            removeRestrictedPhrase(nbc, data, user);
                            break;
                        case "Add illegal phrase":
                            addIllegalPhrase(nbc, data, user);
                            break;
                        case "Remove illegal phrase":
                            removeIllegalPhrase(nbc, data, user);
                            break;
                        case "Load illegal phrases":
                            loadIllegalPhrasesE(nbc, data, user);
                            break;
                        case "Load restricted phrases":
                            loadRestrictedPhrases(nbc, data, user);
                            break;
                        case "Get user id":
                            getUserID(nbc, data, user);
                            break;
                        case "Check statuses":
                            checkStatuses(nbc, data, user);
                            break;
                        case "Debug statuses":
                            debugStatuses(nbc, data, user);
                            break;
                        case "Get duel info":
                            getDuelInfo(nbc, data, user);
                            break;
                        case "Change username":
                            changeUsername(nbc, data, user);
                            break;
                        case "Change email":
                            changeEmail(nbc, data, user);
                            break;
                        case "Restrict new users":
                            restrictNewUsers(nbc, data, user);
                            break;
                        case "Restrict by exp":
                            restrictByExp(nbc, data, user);
                            break;
                        case "Reset decks":
                            resetDecks(nbc, data, user);
                            break;
                        case "Update user":
                            updateUser(nbc, data, user);
                            break;
                        case "Skim sockets":
                            skimSockets(nbc, data, user);
                            break;
                        case "Report lost connections":
                            reportLostConnections(nbc, data, user);
                            break;
                        case "Report actions":
                            reportActions(nbc, data, user);
                            break;
                        case "Report queries":
                            reportQueries(nbc, data, user);
                            break;
                        case "Load announcement":
                            loadAnnouncementE(nbc, data, user);
                            break;
                        case "Add bot":
                            addBot(nbc, data, user);
                            return;
                        case "Remove bot":
                            removeBot(nbc, data, user);
                            return;
                        case "Print user clones":
                        case "Print user states":
                            printUserStates(nbc, data, user);
                            break;
                        case "End all sessions":
                            endAllSessions(nbc, data, user);
                            break;
                        case "Close":
                            offlineUser(nbc, true);
                            break;*/
                    }
                }
                catch (Exception ex) {
                    System.out.println("*************** General Exception *************** on line 1312");
                    System.out.println(ex.getMessage());
                    System.out.println("data.toString() = " + data.toString());
                }
           }
        }).start(); // undisabled 10/27, redisabled 11/13
    }
    
    public static void heartbeat(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Heartbeat");
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void getDuelingbookUser(User user, JSONObject data) {
        String username = (String) data.get("username");
        Boolean administrate = (Boolean) data.get("administrate");
        int remember_me = (int) data.get("remember_me");
        if (remember_me == 1) {
            remember_me = 2;
        }
        JSONObject result = new JSONObject();
        user.log += "Loading duelingbook_user at " + new Timestamp(System.currentTimeMillis()) + "\n";
        if (user.updateUser == true || administrate == true || user.judge > 0 || user.admin_status > 0 || BypassingDuelingbookUser == false || user.activated == 0) {
            user.db_millis = System.currentTimeMillis();
            try {
                System.out.println("Loading duelingbook_user for " + username);
                CallableStatement cs = getConnection().prepareCall("{call getDuelingbookUser(?)}");
                cs.setString(1, username);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                if (rs.next()) {
                    user.id = rs.getInt("id");
                    user.username = rs.getString("username");
                    user.pic = rs.getString("pic");
                    user.default_deck = rs.getString("default_deck");
                }
                else {
                    result.put("action", "Rejected");
                    result.put("message", "Invalid username");
                    write(user.nbc, result);
                    return;
                }
                cs.close();
                user.db_millis = System.currentTimeMillis() - user.db_millis;
                user.updateUser = false;
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                InfoLog += "Line 1541 entered for " + username + "\n";
            }
        }
        getDecks(user, data);
    }
    
    public static void getDecks(User user, JSONObject data) {
        if (user.decks_loaded == true) {
            finishLogin(user, data);
            return;
        }
        System.out.println("Loading decks for " + user.username);
        user.log += "Loading decks at " + new Timestamp(System.currentTimeMillis()) + "\n";
        user.deck_millis = System.currentTimeMillis();
        user.decks = loadDecklists(user.id, user.default_deck);
        user.deck_millis = System.currentTimeMillis() - user.deck_millis;
        user.deck_status = "No decks";
        if (user.decks.size() > 0) {
            user.deck_status = "Error";
        }
        for (int i = 0; i < user.decks.size(); i++) {
            if (user.decks.get(i).get("name").equals(user.default_deck)) {
                user.deck_status = "Good";
                break;
            }
        }
        if (user.deck_status.equals("Error")) {
            user.deck_status += ": default_deck = " + user.default_deck + ", decks = " + user.decks;
        }
        user.decks_loaded = true;
        finishLogin(user, data);
    }
    
    public static void finishLogin(final User user, final JSONObject data) {
        JSONObject result = new JSONObject();
        if (isOnline(user.username) == true) {
            result.put("action", "Already logged in");
            write(user.nbc, result);
            user.nbc.close();
            return;
        }
        user.connecting = false;
        user.online = true;
        
        String freeze_id = user.nbc_address.replaceAll("\\.", "_");
        ArrayList<JSONObject> allGroups = new ArrayList<JSONObject>();
        String message = "";
        int days = 0;
        
        java.util.Date date = new java.util.Date();
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("d");
        String month2 = month.format(date);
        String day2 = day.format(date);
        String loginDate = month2 + " " + day2 + getSuffix(day2);
        user.user_username = user.username;
        user.timeout_timestamp = new Timestamp(System.currentTimeMillis());
        user.connection_timestamp = new Timestamp(System.currentTimeMillis());
        user.version = (int) data.get("version");
        user.cs = null;
        user.duel_id = 0;
        user.last_duel_id = 0;
        user.publicChatLimit = 0;
        Users.add(user);
        if (MostUsers < Users.size()) {
            MostUsers = Users.size();
        }
        
        ArrayList<JSONObject> users = new ArrayList<JSONObject>();
        for (int i = 0; i < Users.size(); i++) {
            JSONObject online_user = new JSONObject();
            online_user.put("username", Users.get(i).username);
            online_user.put("admin", Users.get(i).admin);
            if (Users.get(i).firstLogin == true) {
                online_user.put("firstLogin", Users.get(i).firstLogin);
            }
            if (Users.get(i).donator == 2) {
                online_user.put("donator", Users.get(i).donator);
            }
            users.add(online_user);
        }
        System.out.println("2280");
        result.put("action", "Connected");
        result.put("username", user.username);
        result.put("alt_username", user.alt_username);
        result.put("email", user.email);
        result.put("id", user.id);
        result.put("admin", user.admin);
        result.put("judge", user.judge);
        result.put("adjudicator", user.adjudicator);
        result.put("frozen", user.frozen);
        if (user.muted_timestamp != null && user.muted_timestamp.getTime() > System.currentTimeMillis()) {
            user.muted = true;
            user.muteSeconds = (int) ((user.muted_timestamp.getTime() - System.currentTimeMillis()) / 1000);
        }
        else if (user.muted == false) {
            user.muted = false;
            user.muteSeconds = 0;
        }
        result.put("muted", user.muted);
        result.put("seconds", user.muteSeconds);
        result.put("beginner", user.beginner);
        result.put("moderator", user.moderator);
        result.put("customs", user.customs);
        result.put("expert", user.expert);
        result.put("html", user.html);
        result.put("currentGroupId", user.group_id);
        result.put("default_deck", user.default_deck);
        result.put("timezone", user.timezone);
        result.put("following", user.following);
        result.put("friends", user.friends);
        result.put("sentFriendRequests", user.sentFriendRequests);
        result.put("receivedFriendRequests", user.receivedFriendRequests);
        result.put("blocked", user.blocked);
        result.put("blockedByYou", user.blockedByYou);
        result.put("privateChatUsernames", user.privateChatUsernames);
        result.put("privateChatLimits", user.privateChatLimits);
        result.put("newFriendRequests", user.newFriendRequests.size());
        result.put("newComments", user.newComments);
        result.put("groups", user.groups);
        result.put("groups_loaded", user.groups_loaded);
        result.put("simple", user.simple);
        result.put("resolution", user.resolution);
        result.put("auto_close_notifications", user.auto_close_notifications);
        result.put("auto_load_messages", user.auto_load_messages);
        result.put("require_comment_approval", user.require_comment_approval);
        result.put("always_play_profile_song", user.always_play_profile_song);
        result.put("always_show_nsfw", user.always_show_nsfw);
        result.put("allGroups", allGroups);
        result.put("pic", user.pic);
        result.put("decks", user.decks);
        //result.put("user_agent", user.user_agent);
        result.put("loginDate", loginDate);
        result.put("times_online", user.times_online);
        result.put("total_wins", user.single_wins + user.match_wins);
        result.put("customs", user.customs);
        result.put("users", users);
        result.put("tag", user.tag);
        result.put("checking_decks", true);
        if (user.announcement.size() > 0) {
            result.put("announcement", user.announcement);
        }
        if (user.admin > 0) {
            ArrayList<JSONObject> calls = new ArrayList<JSONObject>();
            for (int i = 0; i < Calls.size(); i++) {
                JSONObject call = new JSONObject();
                call.put("id", Calls.get(i).id);
                call.put("issue", Calls.get(i).issue);
                call.put("caller", Calls.get(i).caller);
                call.put("opponent", Calls.get(i).opponent);
                calls.add(call);
            }
            result.put("calls", calls);
        }
        if (user.beginner == 1 || user.frozen == true || InsertingMessages == false) {
            result.put("message", message);
        }
        user.connect_timestamp = new Timestamp(System.currentTimeMillis());
        user.connecting_millis = System.currentTimeMillis() - user.connecting_millis;
        write(user.nbc, result);
        System.out.println("Written at " + System.currentTimeMillis());

        // NOTIFY THE OTHER USERS YOU LOGGED ON
        result = new JSONObject();
        result.put("action", "Online user");
        result.put("username", user.username);
        result.put("admin", user.admin);
        if (user.firstLogin == true) {
            result.put("firstLogin", user.firstLogin);
        }
        if (user.donator == 2) {
            result.put("donator", user.donator);
        }
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).nbc != null) {
                if (!Users.get(i).nbc.equals(user.nbc)) {
                    write(Users.get(i).nbc, result);
                }
            }
        }
        if (rebooting == true) {
            result = new JSONObject();
            result.put("action", "Reboot server");
            result.put("seconds", rebootSeconds);
            write(user.nbc, result);
        }
        //if (user.activated == 0 && user.email != null && user.announcement.size() == 0) {
        if (user.activated == 0 && user.email != null) {
            result = new JSONObject();
            result.put("action", "Announcement");
            result.put("title", "Confirm Email");
            result.put("message", "<a href=\"event:confirm\"><u><font color=\"#00CCFF\">Confirm</font></u><a></font> your email at " + escapeHTML(user.email) + " to help secure your account");
            write(user.nbc, result);
        }
        if (user.announcement.size() > 0) {
            user.announcement = new ArrayList<String>();
        }
        user.times_online++;
        user.nbc.flush();
    }
    
    public static void connect(ChannelHandlerContext nbc, JSONObject data) {
        String text = "";
        try {
            System.out.println("Connecting at " + System.currentTimeMillis());
            String username = (String) data.get("username");
            String alt_username = "";
            String password = (String) data.get("password");
            String session_id = (String) data.get("session");
            String db_id = (String) data.get("db_id");
            Boolean administrate = (Boolean) data.get("administrate");
            int version = (int) data.get("version");
            String capabilities = (String) data.get("capabilities");
            int remember_me = (int) data.get("remember_me");
            if (remember_me == 1) {
                remember_me = 2;
            }
            long connect_time = 0;
            if (data.has("connect_time")) {
                connect_time = Long.valueOf((int) data.get("connect_time"));
            }
            Boolean dn_client = false;
            if (data.has("dn_client")) {
                dn_client = true;
            }
            String nbc_address = nbc.channel().remoteAddress().toString().replaceAll("/", "");
            if (nbc_address.indexOf(":") >= 0) {
                nbc_address = nbc_address.substring(0, nbc_address.indexOf(":"));
            }
            String ip_address = nbc_address;
            String freeze_id = nbc_address.replaceAll("\\.", "_");
            JSONObject result = new JSONObject();
            if (version < Version) {
                result.put("action", "Rejected");
                result.put("message", "Client is out of date. Please refresh the page");
                write(nbc, result);
                return;
            }
            if (Users.size() > MaxUsers && !WhitelistedIPs.contains(ip_address) && !Donators.contains(username)) {
                result.put("action", "Max users reached");
                write(nbc, result);
                return;
            }
            for (int i = 0; i < Users.size(); i++) {
                if (Users.get(i) != null) {
                    if (Users.get(i).username.equals(username) || Users.get(i).alt_username.equals(username) || Users.get(i).user_username.equals(username)) {
                        System.out.println("2631");
                        if (Users.get(i).session_id.equals(session_id)) {
                            lostConnection(Users.get(i).nbc, Users.get(i), false);
                        }
                        else {
                            result.put("action", "Already logged in");
                            write(nbc, result);
                            nbc.close();
                            return;
                        }
                    }
                }
            }
            User user = getFromUserStates(username, password, ip_address, db_id);
            if (user == null) {
                result.put("action", "Rejected");
                result.put("message", "Password is different and invalid");
                write(nbc, result);
                InfoLog += username + " was rejected because of his password was different and invalid\n";
                return;
            }
            if (user.connecting == true) {
                return;
            }
            user.nbc = nbc;
            user.username = username;
            user.password = password;
            user.nbc_address = nbc_address;
            user.capabilities = capabilities;
            user.connect_time = connect_time;
            user.connecting_millis = System.currentTimeMillis();
            user.dn_client = dn_client;
            user.connecting = true;
            user.session_id = session_id;
            getDuelingbookUser(user, data);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            InfoLog += text;
            try {
                System.out.println("Rejected because of an unknown error");
                System.out.println("text = " + text);
                JSONObject result = new JSONObject();
                result.put("action", "Rejected");
                result.put("message", "Unknown Error");
                result.put("text", text + " ");
                write(nbc, result);
                InfoLog += "user " + nbc.channel().remoteAddress().toString().replaceAll("/", "") + " was rejected because of an unknown error.\n";
                InfoLog += data.toString() + "\n";
            }
            catch (Exception f) {
                System.err.println(f.getMessage());
            }
        }
    }
    
    public static void publicMessage(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String message = (String) data.get("message");
            message = stripSpaces(message);
            message = replaceReturns(message);
            int html = (int) data.get("html");
            int moderator = (int) data.get("moderator");
            int admin = user.admin;
            Boolean frozen = (Boolean) user.frozen;
            JSONObject result = new JSONObject();
            if (message.length() > 10 && message.equals(message.toUpperCase())) {
                message = message.toLowerCase();
            }
            if (user.muted == true) {
                invalidRequest(nbc);
                return;
            }
            if (frozen == true) {
                result.put("action", "Chat error");
                result.put("message", "You are frozen");
                result.put("text", message);
                write(nbc, result);
                return;
            }
            if (user.html == 0) {
                html = 0;
            }
            String color = "0000FF";
            if (moderator == 2) {
                color = "00CCFF";
            }
            else if (user.donator == 2) {
                color = "FFCCFF";
            }
            if (admin == 1) {
                color = "009900";
            }
            else if (admin == 2) {
                color = "707070";
            }
            else if (admin >= 3) {
                color = "CC9900";
            }
            PublicMessageIds++;
            Message publicMessage = newMessage();
            publicMessage.id = PublicMessageIds;
            publicMessage.username = user.username;
            publicMessage.message = message;
            publicMessage.color = color;
            publicMessage.html = html;
            publicMessage.hidden = user.ignored;
            for (int i = 0; i < RestrictedPhrases.size(); i++) {
                if (RestrictedPhrases.get(i).length() > 10) {
                    if (message.toUpperCase().indexOf(RestrictedPhrases.get(i).toUpperCase()) >= 0 || message.equals(" ")) {
                        publicMessage.hidden = 1;
                    }
                }
            }
            if (publicMessage.hidden == 1) {
                publicMessage.hidden_by = "Duelingbook";
            }
            PublicMessages.add(publicMessage);
            
            result.put("action", "Public message");
            result.put("id", PublicMessageIds);
            result.put("username", user.username);
            result.put("message", message);
            result.put("color", color);
            result.put("html", html);
            result.put("donator", user.donator);
            if (publicMessage.hidden == 1) {
                write(nbc, result);
                for (int i = 0; i < Users.size(); i++) {
                    Users.get(i).publicChatLimit++;
                }
            }
            else {
                broadcastE(result);
                for (int i = 0; i < Users.size(); i++) {
                    Users.get(i).publicChatLimit++;
                }
            }
            user.lastPublicMessage = (String) data.get("message");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void recycleMessages(ArrayList<Message> arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            RecycledMessages.add(arr.get(i));
        }
        arr.clear();
    }
    
    public static void recycleStatus(Status status) {
        status.user = null;
        RecycledStatuses.add(status);
    }
    
    public static void recycleJournalEntries(ArrayList<JournalEntry> arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            RecycledJournalEntries.add(arr.get(i));
        }
        arr.clear();
    }
    
    public static void recycleDuelEntries(ArrayList<DuelEntry> arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            RecycledDuelEntries.add(arr.get(i));
        }
        arr.clear();
    }
    
    public static void recycleFriends(ArrayList<Friend> arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            arr.get(i).init();
            RecycledFriends.add(arr.get(i));
        }
        arr.clear();
    }
    
    public static void recycleWatcher(Watcher watcher) {
        watcher.init();
        RecycledWatchers.add(watcher);
    }
    
    public static void privateMessage(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            int admin = user.admin;
            String sender = user.username;
            String receiver = (String) data.get("username");
            String message = (String) data.get("message");
            int html = (int) data.get("html");
            int seen = 0;
            Boolean frozen = user.frozen;
            Boolean is_friend = isFriend(user, receiver);
            int allow_messages = 1;
            int message_friends_only = 0;
            int allow_offline_messages = 1;
            int receiver_is_frozen = 0;
            Boolean blocked = false;
            Boolean receiver_is_admin = false;
            String receiver_ip_address = "";
            String receiver_user_username = receiver;
            int receiver_id = 0;
            String sender_user_username = user.user_username;
            int sender_judge = 0;
            int receiver_judge = 0;
            int judge = 0;
            Boolean fake = false;
            if (sender.indexOf("Resource Judge") >= 0) {
                judge = 1;
                sender_judge = 1;
            }
            if (receiver.indexOf("Resource Judge") >= 0) {
                judge = 1;
                receiver_judge = 1;
            }
            if (user.admin > 0 && message.toLowerCase().indexOf("block ") == 0) {
                message = "I'm sorry he's bothering you. If you visit his profile you will be able to block him from contacting you.";
            }
            
            int i = 0;
            User person = null;
            JSONObject result = new JSONObject();
            // FIND THE RECEIVER
            if (receiver.equalsIgnoreCase(user.username) || receiver.equalsIgnoreCase(user.user_username)) {
                result.put("action", "Private chat error");
                result.put("message", "You cannot message yourself");
                result.put("text", message);
                write(nbc, result);
                return;
            }
            for (i = 0; i < Users.size(); i++) {
                if (Users.get(i).username.equals(receiver)) {
                    person = Users.get(i);
                    if (Users.get(i).admin > 0) {
                        receiver_is_admin = true;
                    }
                    receiver_id = Users.get(i).id;
                    receiver_ip_address = Users.get(i).ip_address;
                    allow_messages = Users.get(i).allow_messages;
                    message_friends_only = Users.get(i).message_friends_only;
                    seen = 1;
                    if (person.nbc == null) {
                        fake = true;
                    }
                    break;
                }
            }
            if (frozen == true && receiver_is_admin == false && user.admin == 0 && user.moderator == 0) {
                // MAKE SURE YOU'RE A FRIEND
                if (is_friend == false) {
                    result.put("action", "Private chat error");
                    result.put("message", "You are frozen. You must be a friend to message this user");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
            }
            if (user.allow_messages == 0 && user.admin == 0) {
                result.put("action", "Private chat error");
                result.put("message", "Your settings are set so that you cannot send messages");
                result.put("text", message);
                write(nbc, result);
                return;
            }
            if (user.message_friends_only == 1 && receiver_is_admin == false) {
                if (is_friend == false) {
                    result.put("action", "Private chat error");
                    result.put("message", "Your settings are set so that you can only message friends");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
            }
            if (seen == 1) {
                // MAKE SURE HE'S NOT FROZEN
                if (Users.get(i).frozen == true) {
                    receiver_is_frozen = 1;
                }

                // MAKE SURE YOU'RE NOT BLOCKED
                for (int j = 0; j < Users.get(i).blocked.size(); j++) {
                    if (Users.get(i).blocked.get(j).equals(sender)) {
                        blocked = true;
                        break;
                    }
                }
            }
            else {
                if (data.get("action").equals("Private message")) {
                    if (receiver.indexOf("Resource Judge") >= 0) {
                        result.put("action", "Private chat error");
                        result.put("message", receiver + " is offline");
                        result.put("text", message);
                        write(nbc, result);
                        return;
                    }
                    else {
                        result.put("action", "Offline message");
                        result.put("message", receiver + " is offline. Send an offline message?");
                        result.put("text", message);
                        write(nbc, result);
                        return;
                    }
                }
            }
            if (user.blocked.contains(receiver)) {
                blocked = true;
            }
            if (blocked == true) {
                result.put("action", "Private chat error");
                result.put("message", "You are blocked from messaging this user");
                result.put("text", message);
                write(nbc, result);
                return;
            }
            if (receiver_is_frozen > 0 && user.admin == 0 && user.moderator == 0) {
                if (is_friend == false) {
                    result.put("action", "Private chat error");
                    result.put("message", receiver + " is frozen. You must be a friend to message this user");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
            }
            if (receiver_judge == 0) {
                // CHECK IF HE ACCEPTS MESSAGES
                if (allow_messages == 0 && receiver_is_admin == false && user.admin == 0) {
                    result.put("action", "Private chat error");
                    result.put("message", receiver + " does not accept messages");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
                // CHECK IF HE ONLY MESSAGES FRIENDS
                if (message_friends_only == 1) {
                    if (is_friend == false && user.admin < 1) {
                        result.put("action", "Private chat error");
                        result.put("message", receiver + " only accepts messages from friends");
                        result.put("text", message);
                        write(nbc, result);
                        return;
                    }
                }
            }
            if (allow_offline_messages == 0) {
                if (user.admin == 0) {
                    result.put("action", "Private chat error");
                    result.put("message", receiver + " does not accept offline messages");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
            }
            if (user.html == 0) {
                html = 0;
            }
            String color = "0000FF";
            if (user.donator == 2) {
                color = "FFCCFF";
            }
            if (admin == 1) {
                color = "009900";
            }
            else if (admin == 2) {
                color = "707070";
            }
            else if (admin >= 3) {
                color = "CC9900";
            }
            result.put("action", "Private message");
            result.put("username", sender);
            result.put("receiver", receiver);
            result.put("message", message);
            result.put("color", color);
            result.put("html", html);
            if (user.donator == 2) {
                result.put("donator", user.donator);
            }
            if (seen == 1) {
                if (write(Users.get(i).nbc, result) == false) {
                    result.put("action", "Offline message");
                    result.put("message", receiver + " is offline. Send an offline message?");
                    result.put("text", message);
                    write(nbc, result);
                    return;
                }
            }
            write(nbc, result);
            if (receiver.indexOf("Resource Judge") >= 0) {
                receiver_user_username = getUser(receiver).user_username;
            }
            if (fake == false) {
                Message privateMessage = newMessage();
                privateMessage.color = color;
                privateMessage.sender_id = user.id;
                privateMessage.receiver_id = receiver_id;
                privateMessage.username = sender;
                privateMessage.sender_username = sender;
                privateMessage.receiver_username = receiver;
                privateMessage.receiver_user_username = receiver_user_username;
                privateMessage.message = message;
                privateMessage.seen = seen;
                privateMessage.html = html;
                privateMessage.judge = judge;
                privateMessage.sender_judge = sender_judge;
                privateMessage.receiver_judge = receiver_judge;
                PrivateMessages.add(privateMessage);
            }
            if (seen == 0) {
                if (!UnseenMessageUsernames.contains(receiver)) {
                    UnseenMessageUsernames.add(receiver);
                }
            }
            if (seen == 1 && person.bot == 1) {
                result.put("receiver", receiver);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static JSONObject cardToObject(Card card) {
        JSONObject obj = new JSONObject();
        obj.put("id", card.card_id);
        obj.put("name", card.name);
        obj.put("treated_as", card.treated_as);
        obj.put("effect", card.effect);
        obj.put("pendulum_effect", card.pendulum_effect);
        obj.put("card_type", card.card_type);
        obj.put("monster_color", card.monster_color);
        obj.put("is_effect", card.is_effect);
        obj.put("type", card.type);
        obj.put("attribute", card.attribute);
        obj.put("level", card.level);
        obj.put("ability", card.ability);
        obj.put("flip", card.flip);
        obj.put("pendulum", card.pendulum);
        obj.put("scale_left", card.scale_left);
        obj.put("scale_right", card.scale_right);
        obj.put("arrows", card.arrows);
        obj.put("atk", card.atk);
        obj.put("def", card.def);
        obj.put("tcg_limit", card.tcg_limit);
        obj.put("ocg_limit", card.ocg_limit);
        obj.put("serial_number", card.serial_number);
        obj.put("tcg", card.tcg);
        obj.put("ocg", card.ocg);
        //obj.put("pic", "1");
        obj.put("pic", card.pic);
        if (!obj.has("atk")) {
            if (obj.get("card_type").equals("Monster")) {
                obj.put("atk", "?");
            }
        }
        if (!obj.has("def")) {
            if (obj.get("card_type").equals("Monster")) {
                obj.put("def", "?");
            }
        }
        return obj;
    }
    
    public static JSONObject messageToObject(Message message) {
        JSONObject obj = new JSONObject();
        obj.put("id", message.id);
        obj.put("color", message.color);
        obj.put("username", message.username);
        obj.put("message", message.message);
        obj.put("html", message.html);
        obj.put("hidden", message.hidden);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat month = new SimpleDateFormat("MMM");
            SimpleDateFormat day = new SimpleDateFormat("d");
            String month2 = month.format(date);
            String day2 = day.format(date);
        obj.put("date", month2 + " " + day2 + getSuffix(day2));
        return obj;
    }
    
    public static void loadWatchersChat(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            int limit = (int) data.get("limit");
            String timezone = (String) user.timezone;
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
            if (limit > user.duel.WatchMessages.size()) {
                limit = user.duel.WatchMessages.size();
            }
            for (int i = user.duel.WatchMessages.size() - 1; i > user.duel.WatchMessages.size() - limit - 1; i--) {
                if (user.duel.WatchMessages.get(i).hidden == 0 || user.moderator > 0) {
                    JSONObject message = messageToObject(user.duel.WatchMessages.get(i));
                    messages.add(message);
                }
            }
            System.out.println("user.duel_id = " + user.duel_id);
            System.out.println("user.duel.id = " + user.duel.id);
            Collections.reverse(messages);
            result.put("action", "Loaded watchers chat");
            result.put("messages", messages);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void loadDuelChat(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
            for (int i = user.duel.DuelMessages.size() - 1; i > -1; i--) {
                if (user.duel.DuelMessages.get(i).color.equals("0000FF") && (!user.duel.DuelMessages.get(i).username.equals(user.duel.player1.username) && !user.duel.DuelMessages.get(i).username.equals(user.duel.player2.username))) {
                    continue;
                }
                JSONObject message = messageToObject(user.duel.DuelMessages.get(i));
                messages.add(message);
            }
            Collections.reverse(messages);
            result.put("action", "Loaded duel chat");
            result.put("messages", messages);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void offlineUser(ChannelHandlerContext nbc, Boolean close_connection) {
        try {
            System.out.println("offlineUser entered");
            String username = null;
            String user_username = null;
            User user = null;
            JSONObject data = null;
            if (nbc != null) {
                if (close_connection == true) {
                    nbc.close();
                }
                for (int i = 0; i < Users.size(); i++) {
                    if (Users.get(i).nbc != null) {
                        if (Users.get(i).nbc.equals(nbc)) {
                            user = Users.get(i);
                            user.action = "";
                            username = Users.get(i).username;
                            user_username = Users.get(i).user_username;
                            Users.remove(i);
                            break;
                        }
                    }
                }
            }
            if (username != null) {
                System.out.println("offlineUser entered for " + username);
                if (Admins.contains(user)) {
                    Admins.remove(user);
                }
                user.connecting = false; // idk
                user.online = false;
                JSONObject result = new JSONObject();
                result.put("action", "Offline user");
                result.put("username", username);
                broadcastE(result);
                exitDuelRoom(nbc, data, user);
                if (user.duel != null && user.duel_id != 0) {
                    Duel duel = user.duel;
                    if (duel.player1.username.equals(username)) {
                        data = new JSONObject();
                        data.put("action", "Duel");
                        data.put("play", "Quit duel");
                        quitDuel(nbc, data, user, duel, duel.player1, duel.player2);
                    }
                    else if (duel.player2.username.equals(username)) {
                        data = new JSONObject();
                        data.put("action", "Duel");
                        data.put("play", "Quit duel");
                        quitDuel(nbc, data, user, duel, duel.player2, duel.player1);
                    }
                    else {
                        for (int i = 0; i < duel.watchers.size(); i++) {
                            if (duel.watchers.get(i).username.equals(username)) {
                                recycleWatcher(duel.watchers.remove(i));
                                i--; // added 3/2/18
                            }
                        }
                        result = new JSONObject();
                        result.put("action", "Remove watcher");
                        result.put("username", username);
                        if (duel.player1.active == true) {
                            write(duel.player1.nbc, result);
                        }
                        if (duel.player2.active == true) {
                            write(duel.player2.nbc, result);
                        }
                        for (int i = 0; i < duel.watchers.size(); i++) {
                            write(duel.watchers.get(i).nbc, result);
                        }
                    }
                    if (duel.player1.active == false && duel.player2.active == false && duel.watchers.size() == 0) {
                        recycleDuel(duel);
                    }
                }
                double time_online = System.currentTimeMillis() - user.connect_timestamp.getTime();
                if (InsertingMessages == true) {
                    try {
                        String query = "UPDATE duelingbook_user SET last_seen = NOW(), current_group = " + user.group_id + ", time_online = time_online + " + time_online + " WHERE username = '" + escapeForSQL(user_username) + "'";
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                addToUserStates(user);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void addToUserStates(User user) {
        if (user.username.indexOf("Resource Judge") == 0 || user.ban_status > 0) {
            user = null;
            return;
        }
        user.login_complete = false;
        user.action = "";
        UserStates.add(0, user);
    }
    
    public static void refreshLog(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 1 && user.adjudicator < 1) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("action", "Refresh log");
            result.put("log", getDuelByID(user.last_duel_id).log);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    // This querys the database instead of searching from the Cards array. It is not used.
    public static void searchCards0(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String name = (String) ((JSONObject) data.get("search")).get("name");
            String effect = (String) ((JSONObject) data.get("search")).get("effect");
            String card_type = (String) ((JSONObject) data.get("search")).get("card_type");
            String monster_color = (String) ((JSONObject) data.get("search")).get("monster_color");
            String type = (String) ((JSONObject) data.get("search")).get("type");
            String ability = (String) ((JSONObject) data.get("search")).get("ability");
            String attribute = (String) ((JSONObject) data.get("search")).get("attribute");
            String level_low = (String) ((JSONObject) data.get("search")).get("level_low");
            String level_high = (String) ((JSONObject) data.get("search")).get("level_high");
            String atk_low = (String) ((JSONObject) data.get("search")).get("atk_low");
            String atk_high = (String) ((JSONObject) data.get("search")).get("atk_high");
            String def_low = (String) ((JSONObject) data.get("search")).get("def_low");
            String def_high = (String) ((JSONObject) data.get("search")).get("def_high");
            String limit = (String) ((JSONObject) data.get("search")).get("limit");
            String order = (String) ((JSONObject) data.get("search")).get("order");
            int pendulum = (int) ((JSONObject) data.get("search")).get("pendulum");
            String scale_low = (String) ((JSONObject) data.get("search")).get("scale_low");
            String scale_high = (String) ((JSONObject) data.get("search")).get("scale_high");
            int tcg = (int) ((JSONObject) data.get("search")).get("tcg");
            int ocg = (int) ((JSONObject) data.get("search")).get("ocg");
            int total = 0;
            int total_searched = 0;
            int page = (int) data.get("page");
            page = (20 * page) - 20;
            ArrayList<JSONObject> cards = new ArrayList<JSONObject>();
            JSONObject result = new JSONObject();
            name = stripSpaces(name);
            name = replaceReturns(name);
            effect = stripSpaces(effect);
            effect = replaceReturns(effect);
            
            ArrayList<String> names = new ArrayList<String>(Arrays.asList(name.split("\\*")));
            ArrayList<String> effects = new ArrayList<String>(Arrays.asList(effect.split("\\*")));
            
            String query1 = "SELECT COUNT(*) ";
            String query2 = "SELECT * ";
            String query = "FROM card_library WHERE";
            for (int i = 0; i < names.size(); i++) {
                if (i != 0) {
                    query += " AND";
                }
                query += " name LIKE '%" + escapeForSQL(names.get(i)) + "%'";
            }
            if (effects.size() > 0) {
                query += " AND (";
                for (int i = 0; i < effects.size(); i++) {
                    query += " effect LIKE '%" + escapeForSQL(effects.get(i)) + "%'";
                }
                query += " OR ";
                for (int i = 0; i < effects.size(); i++) {
                    query += " pendulum_effect LIKE '%" + escapeForSQL(effects.get(i)) + "%'";
                }
                query += ")";
            }
            if (!card_type.equals("")) {
                query += " AND card_type = '" + escapeForSQL(card_type) + "'";
            }
            if (!monster_color.equals("")) {
                query += " AND monster_color = '" + escapeForSQL(monster_color) + "'";
            }
            if (!type.equals("")) {
                query += " AND type = '" + escapeForSQL(type) + "'";
            }
            if (!ability.equals("")) {
                query += " AND ability LIKE '%" + escapeForSQL(ability) + "%'";
            }
            if (!attribute.equals("")) {
                query += " AND attribute = '" + escapeForSQL(attribute) + "'";
            }
            if (!level_low.equals("")) {
                query += " AND level >= '" + escapeForSQL(level_low) + "'";
            }
            if (!level_high.equals("")) {
                query += " AND level <= '" + escapeForSQL(level_high) + "'";
            }
            if (!atk_low.equals("")) {
                if (atk_low.equals("?")) {
                    query += " AND atk IS NULL";
                }
                else {
                    query += " AND atk >= '" + escapeForSQL(atk_low) + "'";
                }
            }
            if (!atk_high.equals("")) {
                if (atk_high.equals("?")) {
                    query += " AND atk IS NULL";
                }
                else {
                    query += " AND atk <= '" + escapeForSQL(atk_high) + "'";
                }
            }
            if (!def_low.equals("")) {
                if (def_low.equals("?")) {
                    query += " AND def IS NULL";
                }
                else {
                    query += " AND def >= '" + escapeForSQL(def_low) + "'";
                }
            }
            if (!def_high.equals("")) {
                if (def_high.equals("?")) {
                    query += " AND def IS NULL";
                }
                else {
                    query += " AND def <= '" + escapeForSQL(def_high) + "'";
                }
            }
            if (pendulum == 1) {
                query += " AND pendulum = 1";
            }
            if (!scale_low.equals("")) {
                query += " AND scale_left >= '" + escapeForSQL(scale_low) + "'";
            }
            if (!scale_high.equals("")) {
                query += " AND scale_left <= '" + escapeForSQL(scale_high) + "'";
            }
            if (tcg == 1) {
                query += " AND tcg = 1 AND ocg = 0";
            }
            if (ocg == 1) {
                query += " AND ocg = 1 AND tcg = 0";
            }
            if (!limit.equals("")) {
                query += " AND restriction = '" + escapeForSQL(limit) + "'";
            }
            query += " AND hidden = 0";
            if (order.equals("Alpha")) {
                query += " ORDER BY name";
            }
            else {
                query += " ORDER BY id DESC";
            }
            query1 += query;
            System.out.println("query1 = " + query1);
            try {
		Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery(query1);
                if (rs.last()) {
                    total = rs.getInt("COUNT(*)");
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            query += " LIMIT " + page + " , 20";
            query2 += query;
            System.out.println("query2 = " + query2);
            try {
		Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery(query2);
                while (rs.next()) {
                    JSONObject card = new JSONObject();
                    card.put("id", rs.getInt("id"));
                    card.put("name", rs.getString("name"));
                    card.put("treated_as", rs.getString("treated_as"));
                    card.put("effect", rs.getString("effect"));
                    card.put("pendulum_effect", rs.getString("pendulum_effect"));
                    card.put("card_type", rs.getString("card_type"));
                    card.put("monster_color", rs.getString("monster_color"));
                    card.put("is_effect", rs.getInt("is_effect"));
                    card.put("type", rs.getString("type"));
                    card.put("attribute", rs.getString("attribute"));
                    card.put("level", rs.getInt("level"));
                    card.put("ability", rs.getString("ability"));
                    card.put("flip", rs.getInt("flip"));
                    card.put("pendulum", rs.getInt("pendulum"));
                    card.put("scale_left", rs.getInt("scale_left"));
                    card.put("scale_right", rs.getInt("scale_right"));
                    card.put("arrows", rs.getString("arrows"));
                    card.put("atk", rs.getString("atk"));
                    card.put("def", rs.getString("def"));
                    card.put("restriction", rs.getInt("restriction"));
                    card.put("serial_number", getSerialNumber(rs.getInt("serial_number")));
                    card.put("tcg", rs.getInt("tcg"));
                    card.put("ocg", rs.getInt("ocg"));
                    card.put("pic", rs.getString("pic"));
                    if (!card.has("atk") && card.get("card_type").equals("Monster")) {
                        card.put("atk", "?");
                    }
                    if (!card.has("def") && card.get("card_type").equals("Monster")) {
                        card.put("def", "?");
                    }
                    cards.add(card);
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "Search cards");
            result.put("cards", cards);
            result.put("total", total);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void searchCards(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String name = (String) ((JSONObject) data.get("search")).get("name");
            String effect = (String) ((JSONObject) data.get("search")).get("effect");
            String card_type = (String) ((JSONObject) data.get("search")).get("card_type");
            String monster_color = (String) ((JSONObject) data.get("search")).get("monster_color");
            String type = (String) ((JSONObject) data.get("search")).get("type");
            String ability = (String) ((JSONObject) data.get("search")).get("ability");
            String attribute = (String) ((JSONObject) data.get("search")).get("attribute");
            String level_low = (String) ((JSONObject) data.get("search")).get("level_low");
            String level_high = (String) ((JSONObject) data.get("search")).get("level_high");
            String atk_low = (String) ((JSONObject) data.get("search")).get("atk_low");
            String atk_high = (String) ((JSONObject) data.get("search")).get("atk_high");
            String def_low = (String) ((JSONObject) data.get("search")).get("def_low");
            String def_high = (String) ((JSONObject) data.get("search")).get("def_high");
            String limit = (String) ((JSONObject) data.get("search")).get("limit");
            String order = (String) ((JSONObject) data.get("search")).get("order");
            int pendulum = (int) ((JSONObject) data.get("search")).get("pendulum");
            String scale_low = (String) ((JSONObject) data.get("search")).get("scale_low");
            String scale_high = (String) ((JSONObject) data.get("search")).get("scale_high");
            int tcg = (int) ((JSONObject) data.get("search")).get("tcg");
            int ocg = (int) ((JSONObject) data.get("search")).get("ocg");
            int ocg_list = (int) ((JSONObject) data.get("search")).get("ocg_list");
            String link_low = (String) ((JSONObject) data.get("search")).get("link_low");
            String link_high = (String) ((JSONObject) data.get("search")).get("link_high");
            String arrows = (String) ((JSONObject) data.get("search")).get("arrows");
            int total = 0;
            int total_searched = 0;
            int page = (int) data.get("page");
            page = (20 * page) - 20;
            Boolean full_search = false;
            ArrayList<JSONObject> cards = new ArrayList<JSONObject>();
            JSONObject result = new JSONObject();
            name = stripSpaces(name);
            name = replaceReturns(name);
            if (name.indexOf("page:") >= 0) {
                page = Integer.parseInt(name.substring(name.indexOf("page:") + 5, name.length()));
                name = name.replaceAll("page:", "");
            }
            effect = stripSpaces(effect);
            effect = replaceReturns(effect);
            if (name.length() >= 3 || name.length() >= 3) {
                full_search = true;
            }
            ArrayList<String> names = new ArrayList<String>(Arrays.asList(name.split("\\*")));
            ArrayList<String> effects = new ArrayList<String>(Arrays.asList(effect.split("\\*")));
            
            ArrayList<Card> cards0 = new ArrayList<Card>(Cards);
            if (!order.equals("Alpha")) {
                Collections.reverse(cards0);
            }
            else {
                Collections.sort(cards0, cardSorter);
            }
            outerloop:
            for (int i = 0; i < cards0.size(); i++) {
                if (cards0.get(i).name == null) {
                    continue;
                }
                if (cards0.get(i).name.equals("")) {
                    continue;
                }
                if (cards0.get(i).hidden == 1) {
                    continue;
                }
                for (int j = 0; j < names.size(); j++) {
                    if (cards0.get(i).name.toLowerCase().indexOf(names.get(j).toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue outerloop;
                    }
                }
                for (int j = 0; j < effects.size(); j++) {
                    if (cards0.get(i).effect.toLowerCase().indexOf((effects.get(j).toLowerCase())) < 0 && cards0.get(i).pendulum_effect.toLowerCase().indexOf(effects.get(j).toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue outerloop;
                    }
                }
                if (full_search == true) {
                    cards.add(cardToObject(cards0.get(i)));
                }
                if (!card_type.equals("")) {
                    if (cards0.get(i).card_type.toLowerCase().indexOf(card_type.toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!monster_color.equals("")) {
                    if (monster_color.equals("Pendulum")) {
                        if (cards0.get(i).pendulum != 1) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else if (cards0.get(i).monster_color.toLowerCase().indexOf(monster_color.toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!type.equals("")) {
                    if (!cards0.get(i).type.toLowerCase().equals(type.toLowerCase())) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!ability.equals("")) {
                    if (cards0.get(i).ability.toLowerCase().indexOf(ability.toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!attribute.equals("")) {
                    if (cards0.get(i).attribute.toLowerCase().indexOf(attribute.toLowerCase()) < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!level_low.equals("")) {
                    if (cards0.get(i).level < Integer.parseInt(level_low)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!level_high.equals("")) {
                    if (cards0.get(i).level > Integer.parseInt(level_high)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!atk_low.equals("")) {
                    if (atk_low.equals("?")) {
                        if (!cards0.get(i).atk.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else {
                        if (cards0.get(i).atk.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                        if (Integer.parseInt(cards0.get(i).atk) < Integer.parseInt(atk_low)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                if (!atk_high.equals("")) {
                    if (atk_high.equals("?")) {
                        if (!cards0.get(i).atk.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else {
                        if (cards0.get(i).atk.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                        if (Integer.parseInt(cards0.get(i).atk) > Integer.parseInt(atk_high)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                if (!def_low.equals("")) {
                    if (def_low.equals("?")) {
                        if (!cards0.get(i).def.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else {
                        if (cards0.get(i).def.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                        if (Integer.parseInt(cards0.get(i).def) < Integer.parseInt(def_low)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                if (!def_high.equals("")) {
                    if (def_high.equals("?")) {
                        if (!cards0.get(i).def.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else {
                        if (cards0.get(i).def.equals("?")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                        if (Integer.parseInt(cards0.get(i).def) > Integer.parseInt(def_high)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                if (pendulum == 1) {
                    if (cards0.get(i).pendulum != 1) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!scale_low.equals("")) {
                    if (cards0.get(i).pendulum == 1 && cards0.get(i).scale_left < Integer.parseInt(scale_low)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!scale_high.equals("")) {
                    if (cards0.get(i).pendulum == 1 && cards0.get(i).scale_left > Integer.parseInt(scale_high)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (tcg == 1) {
                    if (cards0.get(i).tcg == 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (ocg == 1) {
                    if (cards0.get(i).ocg == 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!limit.equals("")) {
                    if (ocg_list == 1) {
                        if (cards0.get(i).ocg_limit != Integer.parseInt(limit)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    else {
                        if (cards0.get(i).tcg_limit != Integer.parseInt(limit)) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                if (!link_low.equals("")) {
                    if (cards0.get(i).monster_color.toLowerCase().indexOf("link") < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                    if (cards0.get(i).level < Integer.parseInt(link_low)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!link_high.equals("")) {
                    if (cards0.get(i).monster_color.toLowerCase().indexOf("link") < 0) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                    if (cards0.get(i).level > Integer.parseInt(link_high)) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                }
                if (!arrows.equals("00000000")) {
                    if (cards0.get(i).arrows.equals("")) {
                        cards0.remove(i);
                        i--;
                        continue;
                    }
                    if (arrows.substring(0, 1).equals("1")) {
                        if (!cards0.get(i).arrows.substring(0, 1).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(1, 2).equals("1")) {
                        if (!cards0.get(i).arrows.substring(1, 2).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(2, 3).equals("1")) {
                        if (!cards0.get(i).arrows.substring(2, 3).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(3, 4).equals("1")) {
                        if (!cards0.get(i).arrows.substring(3, 4).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(4, 5).equals("1")) {
                        if (!cards0.get(i).arrows.substring(4, 5).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(5, 6).equals("1")) {
                        if (!cards0.get(i).arrows.substring(5, 6).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(6, 7).equals("1")) {
                        if (!cards0.get(i).arrows.substring(6, 7).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if (arrows.substring(7, 8).equals("1")) {
                        if (!cards0.get(i).arrows.substring(7, 8).equals("1")) {
                            cards0.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
                total++;
                if ((total > page && total <= page + 20) && full_search == false) {
                    cards.add(cardToObject(cards0.get(i)));
                }
            }
            result.put("action", "Search cards");
            result.put("cards", cards);
            result.put("total", total);
            result.put("full_search", full_search);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static String getSerialNumber(int serial_number) {
        if (serial_number != 0) {
            String str = Integer.toString(serial_number);
            if (str.length() < 8) {
                for (int i = 0; i < 8 - str.length(); i++) {
                    str = "0" + str;
                }
            }
            return str;
        }
        return "";
    }
    
    public static String idToString(int id) {
        String str = Integer.toString(id);
        if (str.length() < 4) {
            while (str.length() < 4) {
                str = "0" + str;
            }
        }
        else {
            str = str.substring(str.length() - 4, str.length());
        }
        return str;
    }

    public static void loadDecklistsE(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("name");
            String default_deck = user.default_deck;
            ArrayList<JSONObject> decks = loadDecklists(user.id, default_deck);
            JSONObject result = new JSONObject();
            result.put("action", "Load decklists");
            result.put("decks", decks);
            result.put("name", deck_name);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static ArrayList<JSONObject> loadDecklists(int user_id, String default_deck) {
        ArrayList<JSONObject> decks = new ArrayList<JSONObject>();
        try {
            CallableStatement cs = getConnection().prepareCall("{call getDecks(?)}");
            cs.setInt(1, user_id);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            
            //getDecksCS.setInt(1, user_id);
            //getDecksCS.execute();
            //ResultSet rs = getDecksCS.getResultSet();
            while (rs.next()) {
                JSONObject deck = new JSONObject();
                deck.put("name", rs.getString("deck_name"));
                deck.put("id", rs.getInt("id"));
                deck.put("legality", rs.getString("legality"));
                deck.put("tcg", rs.getInt("tcg"));
                deck.put("ocg", rs.getInt("ocg"));
                deck.put("goat", rs.getInt("goat"));
                deck.put("links", rs.getInt("links"));
                decks.add(deck);
            }
            cs.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return decks;
    }
    
    public static void saveVerifiedDeck(int id, String legality, int tcg, int ocg, int goat, int links) {
        try {
            String query = "UPDATE decks SET legality = '" + escapeForSQL(legality) + "', tcg = " + tcg + ", ocg = " + ocg + ", goat = " + goat + ", links = " + links + " WHERE id = " + id;
            Statement st = getConnection().createStatement();	
            int numRowsChanged = executeUpdate(st, query);
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setDefault(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("name");
            JSONObject result = new JSONObject();
            if (!user.default_deck.equals(deck_name)) {
                user.default_deck = deck_name;
                try {
                    String query = "UPDATE duelingbook_user SET default_deck = '" + escapeForSQL(deck_name) + "' WHERE id = " + user.id;
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            result.put("action", "Set default");
            result.put("message", deck_name + " has been set as your default deck");
            result.put("name", deck_name);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void renameDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String old_name = (String) data.get("deck");
            String default_deck = user.default_deck;
            String new_name = (String) data.get("name");
            ArrayList<JSONObject> decks = new ArrayList<JSONObject>();
            JSONObject result = new JSONObject();
            result.put("action", "Rename deck");
            result.put("message", new_name + " has been saved");
            result.put("name", new_name);
            write(nbc, result);
            if (old_name.equals(default_deck)) {
                user.default_deck = new_name;
                try {
                    String query = "UPDATE duelingbook_user SET default_deck = '" + escapeForSQL(new_name) + "' WHERE id = " + user.id;
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            if (!old_name.equals(new_name)) {
                try {
                    String query = "UPDATE decks SET deck_name = '" + escapeForSQL(new_name) + "' WHERE deck_name = '" + escapeForSQL(old_name) + "' AND user_id = " + user.id;
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            for (int i = 0; i < user.decks.size(); i++) {
                String s1 = (String) user.decks.get(i).get("name");
                if (s1.equals(old_name)) {
                    user.decks.get(i).remove("name");
                    user.decks.get(i).put("name", new_name);
                    user.decks = sortJSONArr(user.decks);
                    break;
                }
            }
            
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void deleteDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("deck");
            String default_deck = user.default_deck;
            String new_default_deck = "";
            JSONObject result = new JSONObject();
            result.put("action", "Delete deck");
            result.put("name", deck_name);
            write(nbc, result);
            try {
                String query = "INSERT INTO deleted_decks (SELECT id, user_id, username, deck_name, "
                    + " main_1, main_2, main_3, main_4, main_5, main_6, main_7, main_8, main_9, main_10, "
                    + " main_11, main_12, main_13, main_14, main_15, main_16, main_17, main_18, main_19, main_20, "
                    + " main_21, main_22, main_23, main_24, main_25, main_26, main_27, main_28, main_29, main_30, "
                    + " main_31, main_32, main_33, main_34, main_35, main_36, main_37, main_38, main_39, main_40, "
                    + " main_41, main_42, main_43, main_44, main_45, main_46, main_47, main_48, main_49, main_50, "
                    + " main_51, main_52, main_53, main_54, main_55, main_56, main_57, main_58, main_59, main_60, "
                    + " side_1, side_2, side_3, side_4, side_5, side_6, side_7, side_8, side_9, side_10, side_11, side_12, side_13, side_14, side_15, "
                    + " extra_1, extra_2, extra_3, extra_4, extra_5, extra_6, extra_7, extra_8, extra_9, extra_10, extra_11, extra_12, extra_13, extra_14, extra_15, token, legality, tcg, ocg, goat, links, privacy, date FROM decks WHERE deck_name = '" + escapeForSQL(deck_name) + "' AND user_id = " + user.id + ")";
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                String query = "DELETE FROM decks WHERE deck_name = '" + escapeForSQL(deck_name) + "' AND user_id = " + user.id;
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            for (int i = 0; i < user.decks.size(); i++) {
                String s1 = (String) user.decks.get(i).get("name");
                if (s1.equals(deck_name)) {
                    user.decks.remove(i);
                    if (s1.equals(default_deck)) {
                        if (user.decks.size() > 0) {
                            if (i != 0) {
                                user.default_deck = (String) user.decks.get(i - 1).get("name");
                            }
                            else {
                                user.default_deck = (String) user.decks.get(i).get("name");
                            }
                        }
                        else {
                            user.default_deck = "";
                        }
                        try {
                            String query = "UPDATE duelingbook_user SET default_deck = '" + escapeForSQL(user.default_deck) + "' WHERE id = " + user.id;
                            Statement st = getConnection().createStatement();
                            int numRowsChanged = executeUpdate(st, query);
                            st.close();
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void newDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("name");
            String default_deck = user.default_deck;
            int deck_id = 0;
            JSONObject result = new JSONObject();
            if (caution == true) {
                result.put("action", "Caution");
                write(nbc, result);
                return;
            }
            DeckIds++;
            deck_id = DeckIds;
            try {
                String query = "INSERT INTO decks (id, user_id, username, deck_name) VALUES (" + deck_id + ", " + user.id + ", '" + escapeForSQL(user.user_username) + "', '" + escapeForSQL(deck_name) + "')";
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "New deck");
            result.put("name", deck_name);
            result.put("id", deck_id);
            write(nbc, result);
            if (default_deck.equals("")) {
                try {
                    String query = "UPDATE duelingbook_user SET default_deck = '" + escapeForSQL(deck_name) + "' WHERE id = " + user.id;
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                user.default_deck = deck_name;
            }
            JSONObject deck = new JSONObject();
            deck.put("name", deck_name);
            deck.put("id", deck_id);
            //deck.put("legality", "");
            deck.put("legality", "Illegal");
            deck.put("tcg", 0);
            deck.put("ocg", 0);
            deck.put("goat", 0);
            deck.put("links", 0);
            user.decks.add(deck);
            user.decks = sortJSONArr(user.decks);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static ArrayList<JSONObject> sortJSONArr(ArrayList<JSONObject> arr) {
        ArrayList<JSONObject> sorted_arr = new ArrayList<JSONObject>();
        while (arr.size() > 0) {
            JSONObject obj = arr.get(0);
            arr.remove(0);
            if (sorted_arr.size() >= 1) {
                for (int i = 0; i < sorted_arr.size(); i++) {
                    String s1 = (String) obj.get("name");
                    String s2 = (String) sorted_arr.get(i).get("name");
                    if (s1.compareToIgnoreCase(s2) < 0) {
                        sorted_arr.add(i, obj);
                        break;
                    }
                    else if (i >= sorted_arr.size() - 1) {
                        sorted_arr.add(obj);
                        break;
                    }
                }
            }
            else {
                sorted_arr.add(obj);
            }
        }
        return sorted_arr;
    }
    
    public static ArrayList<JSONObject> sortJSONArrByIdDesc(ArrayList<JSONObject> arr) {
        ArrayList<JSONObject> sorted_arr = new ArrayList<JSONObject>();
        while (arr.size() > 0) {
            JSONObject obj = arr.get(0);
            arr.remove(0);
            if (sorted_arr.size() >= 1) {
                for (int i = 0; i < sorted_arr.size(); i++) {
                    String s1 = Integer.toString((int) obj.get("id"));
                    String s2 = Integer.toString((int) sorted_arr.get(i).get("id"));
                    if (s1.compareToIgnoreCase(s2) > 0) {
                        sorted_arr.add(i, obj);
                        break;
                    }
                    else if (i >= sorted_arr.size() - 1) {
                        sorted_arr.add(obj);
                        break;
                    }
                }
            }
            else {
                sorted_arr.add(obj);
            }
        }
        return sorted_arr;
    }
    
    public static ArrayList<String> sortArray(ArrayList<String> arr) {
        ArrayList<String> new_arr = new ArrayList<String>();
        new_arr.add(arr.get(0));
        arr.remove(0);
        while (arr.size() > 0) {
            int i = 0;
            for (i = 0; i < new_arr.size(); i++) {
                if (arr.get(0).compareToIgnoreCase(new_arr.get(i)) < 0) {
                    new_arr.add(0, arr.get(0));
                    arr.remove(0);
                    break;
                }
            }
            if (i == new_arr.size()) {
                new_arr.add(arr.get(0));
                arr.remove(0);
            }
        }
        return new_arr;
    }
    
    public static void checkDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            int id = (int) data.get("id");
            String rules = (String) data.get("rules");
            Object[] deckData = loadDeck(user.id, id, user.default_deck);
            ArrayList<JSONObject> main = (ArrayList<JSONObject>) deckData[2];
            ArrayList<JSONObject> side = (ArrayList<JSONObject>) deckData[3];
            ArrayList<JSONObject> extra = (ArrayList<JSONObject>) deckData[4];
            ArrayList<Integer> values = new ArrayList<Integer>();
            for (int i = 0; i < 60; i++) {
                if (i < main.size()) {
                    values.add((int) main.get(i).get("id"));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < side.size()) {
                    values.add((int) side.get(i).get("id"));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < extra.size()) {
                    values.add((int) extra.get(i).get("id"));
                }
                else {
                    values.add(0);
                }
            }
            Object[] status = verifyDeck2(values);
            String message;
            switch (rules.toLowerCase()) {
                case "tcg":
                    message = (String) ((ArrayList<String>) status[5]).get(1);
                    break;
                case "ocg":
                    message = (String) ((ArrayList<String>) status[5]).get(2);
                    break;
                case "goat":
                    message = (String) ((ArrayList<String>) status[5]).get(3);
                    break;
                default:
                    message = (String) ((ArrayList<String>) status[5]).get(0);
            }
            errorE(nbc, message);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void verifyDeckE(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            int id = (int) data.get("id");
            String deck_name = (String) data.get("name");
            Object[] status = verifyDeck(id, user);
            JSONObject result = new JSONObject();
            result.put("action", "Verify deck");
            result.put("legality", status[0]);
            result.put("tcg", status[1]);
            result.put("ocg", status[2]);
            result.put("goat", status[3]);
            result.put("links", status[4]);
            write(nbc, result);
            saveVerifiedDeck(id, (String) status[0], (int) status[1], (int) status[2], (int) status[3], (int) status[4]);
            
            JSONObject deck = new JSONObject();
            deck.put("id", id);
            deck.put("name", deck_name);
            deck.put("legality", status[0]);
            deck.put("tcg", status[1]);
            deck.put("ocg", status[2]);
            deck.put("goat", status[3]);
            deck.put("links", status[4]);
            for (int i = 0; i < user.decks.size(); i++) {
                String s1 = (String) user.decks.get(i).get("name");
                if (s1.equals(deck_name)) {
                    user.decks.remove(i);
                    break;
                }
            }
            user.decks.add(deck);
            user.decks = sortJSONArr(user.decks);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Object[] verifyDeck(int deck, User user) {
        Object[] deckData = loadDeck(user.id, deck, user.default_deck);
        ArrayList<JSONObject> main = (ArrayList<JSONObject>) deckData[2];
        ArrayList<JSONObject> side = (ArrayList<JSONObject>) deckData[3];
        ArrayList<JSONObject> extra = (ArrayList<JSONObject>) deckData[4];
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < 60; i++) {
            if (i < main.size()) {
                values.add((int) main.get(i).get("id"));
            }
            else {
                values.add(0);
            }
        }
        for (int i = 0; i < 15; i++) {
            if (i < side.size()) {
                values.add((int) side.get(i).get("id"));
            }
            else {
                values.add(0);
            }
        }
        for (int i = 0; i < 15; i++) {
            if (i < extra.size()) {
                values.add((int) extra.get(i).get("id"));
            }
            else {
                values.add(0);
            }
        }
        Object[] status = verifyDeck2(values);
        return status;
    }
    
    public static Object[] verifyDeck2(ArrayList<Integer> values) {
        String result = "Advanced";
        int goat = 1;
        int tcg = 1;
        int ocg = 1;
        int links = 0;
        String message = "";
        String tcg_message = "";
        String ocg_message = "";
        String goat_message = "";
        
        ArrayList<JSONObject> mainCards = new ArrayList<JSONObject>();
        ArrayList<JSONObject> sideCards = new ArrayList<JSONObject>();
        ArrayList<JSONObject> extraCards = new ArrayList<JSONObject>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) == 0) {
                continue;
            }
            for (int j = 0; j < Cards.size(); j++) {
                if (values.get(i) == Cards.get(j).card_id) {
                    JSONObject card = new JSONObject();
                    card.put("id", Cards.get(j).card_id);
                    card.put("name", Cards.get(j).name);
                    card.put("treated_as", Cards.get(j).treated_as);
                    card.put("monster_color", Cards.get(j).monster_color);
                    card.put("tcg", Cards.get(j).tcg);
                    card.put("ocg", Cards.get(j).ocg);
                    card.put("tcg_limit", Cards.get(j).tcg_limit);
                    card.put("ocg_limit", Cards.get(j).ocg_limit);
                    if (i < 60) {
                        mainCards.add(card);
                    }
                    else if (i < 75) {
                        sideCards.add(card);
                    }
                    else {
                        extraCards.add(card);
                    }
                    break;
                }
                if (j >= Cards.size() - 1) {
                    return null;
                }
            }
        }
        ArrayList<JSONObject> allCards = new ArrayList<JSONObject>();
        for (int i = 0; i < mainCards.size(); i++) {
            allCards.add(mainCards.get(i));
        }
        for (int i = 0; i < sideCards.size(); i++) {
            allCards.add(sideCards.get(i));
        }
        for (int i = 0; i < extraCards.size(); i++) {
            allCards.add(extraCards.get(i));
        }
        for (int i = 0; i < mainCards.size(); i++) {
            String monster_color = (String) mainCards.get(i).get("monster_color");
            if (monster_color.equals("Fusion") || monster_color.equals("Synchro") || monster_color.equals("Xyz") || monster_color.equals("Link")) {
                message = "Deck contains Extra Deck cards in the Main Deck";
                Object[] status = {"Error", 0, 0, 0, 0};
                return status;
            }
        }
        for (int i = 0; i < extraCards.size(); i++) {
            String monster_color = (String) extraCards.get(i).get("monster_color");
            if (!monster_color.equals("Fusion") && !monster_color.equals("Synchro") && !monster_color.equals("Xyz") && !monster_color.equals("Link")) {
                message = "Deck contains Non-Extra Deck cards in the Extra Deck";
                Object[] status = {"Error", 0, 0, 0, 0};
                return status;
            }
        }
        for (int i = 0; i < allCards.size(); i++) {
            int amountAlreadyInDeck = 0;
            for (int j = 0; j < allCards.size(); j++) {
                if (allCards.get(i).get("treated_as").equals(allCards.get(j).get("treated_as"))) {
                    String monster_color = (String) allCards.get(i).get("monster_color");
                    amountAlreadyInDeck++;
                    if (amountAlreadyInDeck > 3) {
                        message = "Deck contains more than 3 copies of " + allCards.get(i).get("treated_as");
                        Object[] status = {"Error", 0, 0, 0, 0};
                        return status;
                    }
                    if (!result.equals("Unlimited")) {
                        if ((int) allCards.get(i).get("tcg_limit") < amountAlreadyInDeck) {
                            //if (tcg == 2) {
                            //    tcg = 1;
                            //}
                            tcg = 0;
                            
                            if ((int) allCards.get(i).get("tcg_limit") == 0) {
                                 message = allCards.get(i).get("treated_as") + " is forbidden";
                                 tcg_message = allCards.get(i).get("treated_as") + " is forbidden in TCG games";
                            }
                            else {
                                message = "Deck contains too many copies of " + allCards.get(i).get("treated_as");
                                tcg_message = "Deck contains too many copies of " + allCards.get(i).get("treated_as");
                            }
                            if ((int) allCards.get(i).get("tcg_limit") == 0 && amountAlreadyInDeck == 1) {
                                message = allCards.get(i).get("treated_as") + " is forbidden in TCG games";
                                result = "Traditional";
                            }
                            else {
                                result = "Unlimited";
                            }
                        }
                    }
                    if ((int) allCards.get(i).get("ocg_limit") < amountAlreadyInDeck) {
                        if ((int) allCards.get(i).get("ocg_limit") == 0) {
                            ocg_message = allCards.get(i).get("treated_as") + " is forbidden in OCG games";
                        }
                        else {
                            ocg_message = "Deck contains too many copies of " + allCards.get(i).get("treated_as");
                        }
                        ocg = 0;
                        //if (ocg == 2) {
                        //    ocg = 1;
                        //}
                    }
                    if ((int) allCards.get(i).get("tcg") == 0) {
                        tcg_message = allCards.get(i).get("treated_as") + " is not TCG";
                        tcg = 0;
                    }
                    if ((int) allCards.get(i).get("ocg") == 0) {
                        ocg_message = allCards.get(i).get("treated_as") + " is not OCG";
                        ocg = 0;
                    }
                    if (monster_color.equals("Link")) {
                        links = 1;
                    }
                    if (goat == 1) {
                        if (GoatFormatCards.contains((String) allCards.get(i).get("treated_as"))) {
                            if (GoatFormatLimits.get(GoatFormatCards.indexOf((String) allCards.get(i).get("treated_as"))) < amountAlreadyInDeck) {
                                goat = 0;
                                goat_message = "More than " + GoatFormatLimits.get(GoatFormatCards.indexOf((String) allCards.get(i).get("treated_as"))) + " copies of \"" + (String) allCards.get(i).get("name") + "\" are in this deck";
                            }
                        }
                        else {
                            goat = 0;
                            goat_message = (String) allCards.get(i).get("name") + " is not allowed in Goat Format";
                        }
                    }
                }
            }
        }
        if (tcg > 0 || ocg > 0) {
            result = "Advanced";
        }
        if (mainCards.size() < 40) {
            result = "Illegal";
            if (mainCards.size() >= 20) {
                result = "Small";
            }
            if (mainCards.size() == 0) {
                result = "Empty";
            }
        }
        ArrayList<String> messages = new ArrayList<String>();
        messages.add(message);
        messages.add(tcg_message);
        messages.add(ocg_message);
        messages.add(goat_message);
        System.out.println(messages);
        Object[] status = {result, tcg, ocg, goat, links, messages};
        return status;
    }
    
    public static void verifyYDK(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            JSONArray main0 = (JSONArray) ((JSONObject) data.get("cards")).get("main");
            JSONArray side0 = (JSONArray) ((JSONObject) data.get("cards")).get("side");
            JSONArray extra0 = (JSONArray) ((JSONObject) data.get("cards")).get("extra");
            ArrayList<Integer> main = new ArrayList<Integer>();
            ArrayList<Integer> side = new ArrayList<Integer>();
            ArrayList<Integer> extra = new ArrayList<Integer>();
            ArrayList<JSONObject> mainCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> sideCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> extraCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> mainAndSideCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> allCards = new ArrayList<JSONObject>();
            ArrayList<Integer> values = new ArrayList<Integer>();
            ArrayList<JSONObject> cardpool = new ArrayList<JSONObject>();
            ArrayList<Integer> mainIds = new ArrayList<Integer>();
            ArrayList<Integer> sideIds = new ArrayList<Integer>();
            ArrayList<Integer> extraIds = new ArrayList<Integer>();
            JSONObject result = new JSONObject();
            for (Object obj: main0) {
                main.add((int) obj);
            }
            for (Object obj: side0) {
                side.add((int) obj);
            }
            for (Object obj: extra0) {
                extra.add((int) obj);
            }
            for (int i = 0; i < 60; i++) {
                if (i < main.size()) {
                    values.add(main.get(i));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < side.size()) {
                    values.add(side.get(i));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < extra.size()) {
                    values.add(extra.get(i));
                }
                else {
                    values.add(0);
                }
            }
            try {
                String query = "SELECT * FROM card_library WHERE "
                    + " serial_number = " + values.get(0) + " OR serial_number = " + values.get(1) + " OR serial_number = " + values.get(2) + " OR serial_number = " + values.get(3) + " OR serial_number = " + values.get(4) + " OR "
                    + " serial_number = " + values.get(5) + " OR serial_number = " + values.get(6) + " OR serial_number = " + values.get(7) + " OR serial_number = " + values.get(8) + " OR serial_number = " + values.get(9) + " OR "
                    + " serial_number = " + values.get(10) + " OR serial_number = " + values.get(11) + " OR serial_number = " + values.get(12) + " OR serial_number = " + values.get(13) + " OR serial_number = " + values.get(14) + " OR "
                    + " serial_number = " + values.get(15) + " OR serial_number = " + values.get(16) + " OR serial_number = " + values.get(17) + " OR serial_number = " + values.get(18) + " OR serial_number = " + values.get(19) + " OR "
                    + " serial_number = " + values.get(20) + " OR serial_number = " + values.get(21) + " OR serial_number = " + values.get(22) + " OR serial_number = " + values.get(23) + " OR serial_number = " + values.get(24) + " OR "
                    + " serial_number = " + values.get(25) + " OR serial_number = " + values.get(26) + " OR serial_number = " + values.get(27) + " OR serial_number = " + values.get(28) + " OR serial_number = " + values.get(29) + " OR "
                    + " serial_number = " + values.get(30) + " OR serial_number = " + values.get(31) + " OR serial_number = " + values.get(32) + " OR serial_number = " + values.get(33) + " OR serial_number = " + values.get(34) + " OR "
                    + " serial_number = " + values.get(35) + " OR serial_number = " + values.get(36) + " OR serial_number = " + values.get(37) + " OR serial_number = " + values.get(38) + " OR serial_number = " + values.get(39) + " OR "
                    + " serial_number = " + values.get(40) + " OR serial_number = " + values.get(41) + " OR serial_number = " + values.get(42) + " OR serial_number = " + values.get(43) + " OR serial_number = " + values.get(44) + " OR "
                    + " serial_number = " + values.get(45) + " OR serial_number = " + values.get(46) + " OR serial_number = " + values.get(47) + " OR serial_number = " + values.get(48) + " OR serial_number = " + values.get(49) + " OR "
                    + " serial_number = " + values.get(50) + " OR serial_number = " + values.get(51) + " OR serial_number = " + values.get(52) + " OR serial_number = " + values.get(53) + " OR serial_number = " + values.get(54) + " OR "
                    + " serial_number = " + values.get(55) + " OR serial_number = " + values.get(56) + " OR serial_number = " + values.get(57) + " OR serial_number = " + values.get(58) + " OR serial_number = " + values.get(59) + " OR "
                    + " serial_number = " + values.get(60) + " OR serial_number = " + values.get(61) + " OR serial_number = " + values.get(62) + " OR serial_number = " + values.get(63) + " OR serial_number = " + values.get(64) + " OR "
                    + " serial_number = " + values.get(65) + " OR serial_number = " + values.get(66) + " OR serial_number = " + values.get(67) + " OR serial_number = " + values.get(68) + " OR serial_number = " + values.get(69) + " OR "
                    + " serial_number = " + values.get(70) + " OR serial_number = " + values.get(71) + " OR serial_number = " + values.get(72) + " OR serial_number = " + values.get(73) + " OR serial_number = " + values.get(74) + " OR "
                    + " serial_number = " + values.get(75) + " OR serial_number = " + values.get(76) + " OR serial_number = " + values.get(77) + " OR serial_number = " + values.get(78) + " OR serial_number = " + values.get(79) + " OR "
                    + " serial_number = " + values.get(80) + " OR serial_number = " + values.get(81) + " OR serial_number = " + values.get(82) + " OR serial_number = " + values.get(83) + " OR serial_number = " + values.get(84) + " OR "
                    + " serial_number = " + values.get(85) + " OR serial_number = " + values.get(86) + " OR serial_number = " + values.get(87) + " OR serial_number = " + values.get(88) + " OR serial_number = " + values.get(89);
		Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    if (rs.getInt("serial_number") > 0 && rs.getInt("hidden") == 0) {
                        JSONObject card = new JSONObject();
                        card.put("id", rs.getInt("id"));
                        card.put("name", rs.getString("name"));
                        card.put("treated_as", rs.getString("treated_as"));
                        card.put("monster_color", rs.getString("monster_color"));
                        card.put("serial_number", rs.getInt("serial_number"));
                        cardpool.add(card);
                    }
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            for (int i = 0; i < main.size(); i++) {
                for (int j = 0; j < cardpool.size(); j++) {
                    if (cardpool.get(j).get("serial_number").equals(main.get(i))) {
                        mainIds.add((int) cardpool.get(j).get("id"));
                        mainCards.add(cardpool.get(j));
                        mainAndSideCards.add(cardpool.get(j));
                        allCards.add(cardpool.get(j));
                        break;
                    }
                }
            }
            for (int i = 0; i < side.size(); i++) {
                for (int j = 0; j < cardpool.size(); j++) {
                    if (cardpool.get(j).get("serial_number").equals(side.get(i))) {
                        sideIds.add((int) cardpool.get(j).get("id"));
                        sideCards.add(cardpool.get(j));
                        mainAndSideCards.add(cardpool.get(j));
                        allCards.add(cardpool.get(j));
                        break;
                    }
                }
            }
            for (int i = 0; i < extra.size(); i++) {
                for (int j = 0; j < cardpool.size(); j++) {
                    if (cardpool.get(j).get("serial_number").equals(extra.get(i))) {
                        extraIds.add((int) cardpool.get(j).get("id"));
                        extraCards.add(cardpool.get(j));
                        allCards.add(cardpool.get(j));
                        break;
                    }
                }
            }
            // MAKE SURE MONSTER COLOR IS CORRECT
            for (int i = 0; i < mainCards.size(); i++) {
                if (mainCards.get(i).get("monster_color").equals("Fusion") || mainCards.get(i).get("monster_color").equals("Link") || mainCards.get(i).get("monster_color").equals("Synchro") || mainCards.get(i).get("monster_color").equals("Xyz")) {
                    errorE(nbc, "There was a copy of " + mainCards.get(i).get("name") + " found in the main deck");
                    return;
                }
            }
            for (int i = 0; i < extraCards.size(); i++) {
                if (!extraCards.get(i).get("monster_color").equals("Fusion") && !extraCards.get(i).get("monster_color").equals("Link") && !extraCards.get(i).get("monster_color").equals("Synchro") && !extraCards.get(i).get("monster_color").equals("Xyz")) {
                    errorE(nbc, "There was a copy of " + extraCards.get(i).get("name") + " found in the extra deck");
                    return;
                }
            }
            // CHECK FOR DUPLICATES FOR TREATED_AS
            for (int i = 0; i < allCards.size(); i++) {
                int amountAlreadyInDeck = 0;
                for (int j = 0; j < allCards.size(); j++) {
                    if (allCards.get(i).get("treated_as").equals(allCards.get(j).get("treated_as"))) {
                        amountAlreadyInDeck++;
                        if (amountAlreadyInDeck > 3) {
                            errorE(nbc, "There are more than 3 cards treated as " + allCards.get(i).get("treated_as") + " in the deck");
                            return;
                        }
                    }
                }
            }
            JSONObject deck = new JSONObject();
            deck.put("main", mainIds);
            deck.put("side", sideIds);
            deck.put("extra", extraIds);
            result.put("action", "Verify YDK");
            result.put("deck", deck);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void importDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String default_deck = user.default_deck;
            String deck_name = (String) data.get("name");
            int deck_id = 0;
            int total_decks = 0;
            JSONArray main0 = (JSONArray) ((JSONObject) data.get("cards")).get("main");
            JSONArray side0 = (JSONArray) ((JSONObject) data.get("cards")).get("side");
            JSONArray extra0 = (JSONArray) ((JSONObject) data.get("cards")).get("extra");
            ArrayList<Integer> main = new ArrayList<Integer>();
            ArrayList<Integer> side = new ArrayList<Integer>();
            ArrayList<Integer> extra = new ArrayList<Integer>();
            ArrayList<JSONObject> mainCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> sideCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> extraCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> mainAndSideCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> allCards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> cardpool = new ArrayList<JSONObject>();
            ArrayList<Integer> mainIds = new ArrayList<Integer>();
            ArrayList<Integer> sideIds = new ArrayList<Integer>();
            ArrayList<Integer> extraIds = new ArrayList<Integer>();
            JSONObject result = new JSONObject();
            if (caution == true) {
                result.put("action", "Caution");
                write(nbc, result);
                return;
            }
            for (Object obj: main0) {
                main.add((int) obj);
            }
            for (Object obj: side0) {
                side.add((int) obj);
            }
            for (Object obj: extra0) {
                extra.add((int) obj);
            }
            ArrayList<Integer> values = cardObjectsToValues(main, side, extra);
            Object[] status = verifyDeck2(values);
            String legality = (String) status[0];
            int tcg = (int) status[1];
            int ocg = (int) status[2];
            int goat = (int) status[3];
            int links = (int) status[4];
            int token = 1;
            DeckIds++;
            deck_id = DeckIds;
            try {
                String query = "INSERT INTO decks (id, user_id, username, deck_name, "
                    + " main_1, main_2, main_3, main_4, main_5, main_6, main_7, main_8, main_9, main_10, "
                    + " main_11, main_12, main_13, main_14, main_15, main_16, main_17, main_18, main_19, main_20, "
                    + " main_21, main_22, main_23, main_24, main_25, main_26, main_27, main_28, main_29, main_30, "
                    + " main_31, main_32, main_33, main_34, main_35, main_36, main_37, main_38, main_39, main_40, "
                    + " main_41, main_42, main_43, main_44, main_45, main_46, main_47, main_48, main_49, main_50, "
                    + " main_51, main_52, main_53, main_54, main_55, main_56, main_57, main_58, main_59, main_60, "
                    + " side_1, side_2, side_3, side_4, side_5, side_6, side_7, side_8, side_9, side_10, side_11, side_12, side_13, side_14, side_15, "
                    + " extra_1, extra_2, extra_3, extra_4, extra_5, extra_6, extra_7, extra_8, extra_9, extra_10, extra_11, extra_12, extra_13, extra_14, extra_15, token, legality, tcg, ocg, goat, links) "
                    + " VALUES (" + deck_id + ", " + user.id + ", '" + escapeForSQL(user.user_username) + "', '" + escapeForSQL(deck_name) + "', " 
                    + values.get(0) + ", " + values.get(1) + ", " + values.get(2) + ", " + values.get(3) + ", " + values.get(4) + ", " 
                    + values.get(5) + ", " + values.get(6) + ", " + values.get(7) + ", " + values.get(8) + ", " + values.get(9) + ", " 
                    + values.get(10) + ", " + values.get(11) + ", " + values.get(12) + ", " + values.get(13) + ", " + values.get(14) + ", " 
                    + values.get(15) + ", " + values.get(16) + ", " + values.get(17) + ", " + values.get(18) + ", " + values.get(19) + ", " 
                    + values.get(20) + ", " + values.get(21) + ", " + values.get(22) + ", " + values.get(23) + ", " + values.get(24) + ", " 
                    + values.get(25) + ", " + values.get(26) + ", " + values.get(27) + ", " + values.get(28) + ", " + values.get(29) + ", " 
                    + values.get(30) + ", " + values.get(31) + ", " + values.get(32) + ", " + values.get(33) + ", " + values.get(34) + ", " 
                    + values.get(35) + ", " + values.get(36) + ", " + values.get(37) + ", " + values.get(38) + ", " + values.get(39) + ", " 
                    + values.get(40) + ", " + values.get(41) + ", " + values.get(42) + ", " + values.get(43) + ", " + values.get(44) + ", " 
                    + values.get(45) + ", " + values.get(46) + ", " + values.get(47) + ", " + values.get(48) + ", " + values.get(49) + ", " 
                    + values.get(50) + ", " + values.get(51) + ", " + values.get(52) + ", " + values.get(53) + ", " + values.get(54) + ", " 
                    + values.get(55) + ", " + values.get(56) + ", " + values.get(57) + ", " + values.get(58) + ", " + values.get(59) + ", " 
                    + values.get(60) + ", " + values.get(61) + ", " + values.get(62) + ", " + values.get(63) + ", " + values.get(64) + ", " 
                    + values.get(65) + ", " + values.get(66) + ", " + values.get(67) + ", " + values.get(68) + ", " + values.get(69) + ", " 
                    + values.get(70) + ", " + values.get(71) + ", " + values.get(72) + ", " + values.get(73) + ", " + values.get(74) + ", " 
                    + values.get(75) + ", " + values.get(76) + ", " + values.get(77) + ", " + values.get(78) + ", " + values.get(79) + ", " 
                    + values.get(80) + ", " + values.get(81) + ", " + values.get(82) + ", " + values.get(83) + ", " + values.get(84) + ", " 
                    + values.get(85) + ", " + values.get(86) + ", " + values.get(87) + ", " + values.get(88) + ", " + values.get(89) + ", "
                    + token + ", '" + escapeForSQL(legality) + "', " + tcg + ", " + ocg + ", " + goat + ", " + links + ")";
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                String query = "SELECT COUNT(*) AS total FROM decks WHERE user_id = " + user.id;
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    total_decks = rs.getInt("total");
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            if (default_deck.equals("") || total_decks == 1) {
                try {
                    String query = "UPDATE duelingbook_user SET default_deck = '" + escapeForSQL(deck_name) + "' WHERE id = " + user.id;
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                user.default_deck = deck_name;
            }
            ArrayList<JSONObject> decks = loadDecklists(user.id, default_deck);
            Object[] deckData = loadDeck(user.id, deck_id, deck_name);
            result.put("action", "Import deck");
            result.put("id", deckData[0]);
            result.put("name", deckData[1]);
            result.put("main", deckData[2]);
            result.put("side", deckData[3]);
            result.put("extra", deckData[4]);
            result.put("decks", decks);
            result.put("versions", deckData[5]);
            result.put("token", deckData[6]);
            result.put("privacy", deckData[7]);
            result.put("legality", deckData[8]);
            result.put("tcg", deckData[9]);
            result.put("ocg", deckData[10]);
            result.put("goat", deckData[11]);
            result.put("links", deckData[12]);
            write(nbc, result);
            
            JSONObject deck = new JSONObject();
            deck.put("id", deckData[0]);
            deck.put("name", deckData[1]);
            deck.put("legality", deckData[8]);
            deck.put("tcg", deckData[9]);
            deck.put("ocg", deckData[10]);
            deck.put("goat", deckData[11]);
            deck.put("links", deckData[12]);
            user.decks.add(deck);
            user.decks = sortJSONArr(user.decks);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadDeckE(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            int deck_id = 0;
            if (data.has("deck")) {
                deck_id = (int) data.get("deck");
            }
            String default_deck = user.default_deck;
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> decks = new ArrayList<JSONObject>();
            if (deck_id == 0) {
                decks = loadDecklists(user.id, default_deck);
            }
            System.out.println("decks = " + decks);
            Object[] deckData = loadDeck(user.id, deck_id, default_deck);
            result.put("action", "Load decks");
            result.put("id", deckData[0]);
            result.put("name", deckData[1]);
            result.put("main", deckData[2]);
            result.put("side", deckData[3]);
            result.put("extra", deckData[4]);
            result.put("decks", decks);
            result.put("versions", deckData[5]);
            result.put("token", deckData[6]);
            result.put("privacy", deckData[7]);
            result.put("legality", deckData[8]);
            result.put("tcg", deckData[9]);
            result.put("ocg", deckData[10]);
            result.put("goat", deckData[11]);
            result.put("links", deckData[12]);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static ArrayList<Integer> cardObjectsToValues(ArrayList<Integer> main, ArrayList<Integer> side, ArrayList<Integer> extra) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < 60; i++) {
            if (i < main.size()) {
                values.add(main.get(i));
            }
            else {
                values.add(0);
            }
        }
        for (int i = 0; i < 15; i++) {
            if (i < side.size()) {
                values.add(side.get(i));
            }
            else {
                values.add(0);
            }
        }
        for (int i = 0; i < 15; i++) {
            if (i < extra.size()) {
                values.add(extra.get(i));
            }
            else {
                values.add(0);
            }
        }
        return values;
    }
    
    public static Object[] loadDeck(int user_id, int deck_id, String default_deck) {
            ArrayList<Integer> mainIds = new ArrayList<Integer>();
            ArrayList<Integer> sideIds = new ArrayList<Integer>();
            ArrayList<Integer> extraIds = new ArrayList<Integer>();
            ArrayList<Integer> versions = new ArrayList<Integer>();
            ArrayList<JSONObject> cardpool = new ArrayList<JSONObject>();
            ArrayList<JSONObject> main = new ArrayList<JSONObject>();
            ArrayList<JSONObject> side = new ArrayList<JSONObject>();
            ArrayList<JSONObject> extra = new ArrayList<JSONObject>();
            int token = 0;
            int privacy = 0;
            String legality = "";
            int tcg = 0;
            int ocg = 0;
            int goat = 0;
            int links = 0;
            String actualDeckName = "";      
            if (deck_id == 0) {
                // SELECT THE ID OF THE DEFAULT DECK
                try {
                    String query = "SELECT id FROM decks WHERE user_id = " + user_id + " AND deck_name = '" + escapeForSQL(default_deck) + "' ORDER BY id DESC";
                    Statement st = getConnection().createStatement();
                    ResultSet rs = executeQuery(st, query);
                    if (rs.next()) {
                        deck_id = rs.getInt("id");
                    }
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            try {
                String query = "SELECT * FROM decks WHERE user_id = " + user_id + " AND id = " + deck_id + " ORDER BY id DESC";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    versions.add(rs.getInt("id"));
                    if (deck_id == rs.getInt("id")) {
                        actualDeckName = rs.getString("deck_name");
                        if (rs.getInt("main_1") > 0) { mainIds.add(rs.getInt("main_1")); }
                        if (rs.getInt("main_2") > 0) { mainIds.add(rs.getInt("main_2")); }
                        if (rs.getInt("main_3") > 0) { mainIds.add(rs.getInt("main_3")); }
                        if (rs.getInt("main_4") > 0) { mainIds.add(rs.getInt("main_4")); }
                        if (rs.getInt("main_5") > 0) { mainIds.add(rs.getInt("main_5")); }
                        if (rs.getInt("main_6") > 0) { mainIds.add(rs.getInt("main_6")); }
                        if (rs.getInt("main_7") > 0) { mainIds.add(rs.getInt("main_7")); }
                        if (rs.getInt("main_8") > 0) { mainIds.add(rs.getInt("main_8")); }
                        if (rs.getInt("main_9") > 0) { mainIds.add(rs.getInt("main_9")); }
                        if (rs.getInt("main_10") > 0) { mainIds.add(rs.getInt("main_10")); }
                        if (rs.getInt("main_11") > 0) { mainIds.add(rs.getInt("main_11")); }
                        if (rs.getInt("main_12") > 0) { mainIds.add(rs.getInt("main_12")); }
                        if (rs.getInt("main_13") > 0) { mainIds.add(rs.getInt("main_13")); }
                        if (rs.getInt("main_14") > 0) { mainIds.add(rs.getInt("main_14")); }
                        if (rs.getInt("main_15") > 0) { mainIds.add(rs.getInt("main_15")); }
                        if (rs.getInt("main_16") > 0) { mainIds.add(rs.getInt("main_16")); }
                        if (rs.getInt("main_17") > 0) { mainIds.add(rs.getInt("main_17")); }
                        if (rs.getInt("main_18") > 0) { mainIds.add(rs.getInt("main_18")); }
                        if (rs.getInt("main_19") > 0) { mainIds.add(rs.getInt("main_19")); }
                        if (rs.getInt("main_20") > 0) { mainIds.add(rs.getInt("main_20")); }
                        if (rs.getInt("main_21") > 0) { mainIds.add(rs.getInt("main_21")); }
                        if (rs.getInt("main_22") > 0) { mainIds.add(rs.getInt("main_22")); }
                        if (rs.getInt("main_23") > 0) { mainIds.add(rs.getInt("main_23")); }
                        if (rs.getInt("main_24") > 0) { mainIds.add(rs.getInt("main_24")); }
                        if (rs.getInt("main_25") > 0) { mainIds.add(rs.getInt("main_25")); }
                        if (rs.getInt("main_26") > 0) { mainIds.add(rs.getInt("main_26")); }
                        if (rs.getInt("main_27") > 0) { mainIds.add(rs.getInt("main_27")); }
                        if (rs.getInt("main_28") > 0) { mainIds.add(rs.getInt("main_28")); }
                        if (rs.getInt("main_29") > 0) { mainIds.add(rs.getInt("main_29")); }
                        if (rs.getInt("main_30") > 0) { mainIds.add(rs.getInt("main_30")); }
                        if (rs.getInt("main_31") > 0) { mainIds.add(rs.getInt("main_31")); }
                        if (rs.getInt("main_32") > 0) { mainIds.add(rs.getInt("main_32")); }
                        if (rs.getInt("main_33") > 0) { mainIds.add(rs.getInt("main_33")); }
                        if (rs.getInt("main_34") > 0) { mainIds.add(rs.getInt("main_34")); }
                        if (rs.getInt("main_35") > 0) { mainIds.add(rs.getInt("main_35")); }
                        if (rs.getInt("main_36") > 0) { mainIds.add(rs.getInt("main_36")); }
                        if (rs.getInt("main_37") > 0) { mainIds.add(rs.getInt("main_37")); }
                        if (rs.getInt("main_38") > 0) { mainIds.add(rs.getInt("main_38")); }
                        if (rs.getInt("main_39") > 0) { mainIds.add(rs.getInt("main_39")); }
                        if (rs.getInt("main_40") > 0) { mainIds.add(rs.getInt("main_40")); }
                        if (rs.getInt("main_41") > 0) { mainIds.add(rs.getInt("main_41")); }
                        if (rs.getInt("main_42") > 0) { mainIds.add(rs.getInt("main_42")); }
                        if (rs.getInt("main_43") > 0) { mainIds.add(rs.getInt("main_43")); }
                        if (rs.getInt("main_44") > 0) { mainIds.add(rs.getInt("main_44")); }
                        if (rs.getInt("main_45") > 0) { mainIds.add(rs.getInt("main_45")); }
                        if (rs.getInt("main_46") > 0) { mainIds.add(rs.getInt("main_46")); }
                        if (rs.getInt("main_47") > 0) { mainIds.add(rs.getInt("main_47")); }
                        if (rs.getInt("main_48") > 0) { mainIds.add(rs.getInt("main_48")); }
                        if (rs.getInt("main_49") > 0) { mainIds.add(rs.getInt("main_49")); }
                        if (rs.getInt("main_50") > 0) { mainIds.add(rs.getInt("main_50")); }
                        if (rs.getInt("main_51") > 0) { mainIds.add(rs.getInt("main_51")); }
                        if (rs.getInt("main_52") > 0) { mainIds.add(rs.getInt("main_52")); }
                        if (rs.getInt("main_53") > 0) { mainIds.add(rs.getInt("main_53")); }
                        if (rs.getInt("main_54") > 0) { mainIds.add(rs.getInt("main_54")); }
                        if (rs.getInt("main_55") > 0) { mainIds.add(rs.getInt("main_55")); }
                        if (rs.getInt("main_56") > 0) { mainIds.add(rs.getInt("main_56")); }
                        if (rs.getInt("main_57") > 0) { mainIds.add(rs.getInt("main_57")); }
                        if (rs.getInt("main_58") > 0) { mainIds.add(rs.getInt("main_58")); }
                        if (rs.getInt("main_59") > 0) { mainIds.add(rs.getInt("main_59")); }
                        if (rs.getInt("main_60") > 0) { mainIds.add(rs.getInt("main_60")); }
                        if (rs.getInt("side_1") > 0) { sideIds.add(rs.getInt("side_1")); }
                        if (rs.getInt("side_2") > 0) { sideIds.add(rs.getInt("side_2")); }
                        if (rs.getInt("side_3") > 0) { sideIds.add(rs.getInt("side_3")); }
                        if (rs.getInt("side_4") > 0) { sideIds.add(rs.getInt("side_4")); }
                        if (rs.getInt("side_5") > 0) { sideIds.add(rs.getInt("side_5")); }
                        if (rs.getInt("side_6") > 0) { sideIds.add(rs.getInt("side_6")); }
                        if (rs.getInt("side_7") > 0) { sideIds.add(rs.getInt("side_7")); }
                        if (rs.getInt("side_8") > 0) { sideIds.add(rs.getInt("side_8")); }
                        if (rs.getInt("side_9") > 0) { sideIds.add(rs.getInt("side_9")); }
                        if (rs.getInt("side_10") > 0) { sideIds.add(rs.getInt("side_10")); }
                        if (rs.getInt("side_11") > 0) { sideIds.add(rs.getInt("side_11")); }
                        if (rs.getInt("side_12") > 0) { sideIds.add(rs.getInt("side_12")); }
                        if (rs.getInt("side_13") > 0) { sideIds.add(rs.getInt("side_13")); }
                        if (rs.getInt("side_14") > 0) { sideIds.add(rs.getInt("side_14")); }
                        if (rs.getInt("side_15") > 0) { sideIds.add(rs.getInt("side_15")); }
                        if (rs.getInt("extra_1") > 0) { extraIds.add(rs.getInt("extra_1")); }
                        if (rs.getInt("extra_2") > 0) { extraIds.add(rs.getInt("extra_2")); }
                        if (rs.getInt("extra_3") > 0) { extraIds.add(rs.getInt("extra_3")); }
                        if (rs.getInt("extra_4") > 0) { extraIds.add(rs.getInt("extra_4")); }
                        if (rs.getInt("extra_5") > 0) { extraIds.add(rs.getInt("extra_5")); }
                        if (rs.getInt("extra_6") > 0) { extraIds.add(rs.getInt("extra_6")); }
                        if (rs.getInt("extra_7") > 0) { extraIds.add(rs.getInt("extra_7")); }
                        if (rs.getInt("extra_8") > 0) { extraIds.add(rs.getInt("extra_8")); }
                        if (rs.getInt("extra_9") > 0) { extraIds.add(rs.getInt("extra_9")); }
                        if (rs.getInt("extra_10") > 0) { extraIds.add(rs.getInt("extra_10")); }
                        if (rs.getInt("extra_11") > 0) { extraIds.add(rs.getInt("extra_11")); }
                        if (rs.getInt("extra_12") > 0) { extraIds.add(rs.getInt("extra_12")); }
                        if (rs.getInt("extra_13") > 0) { extraIds.add(rs.getInt("extra_13")); }
                        if (rs.getInt("extra_14") > 0) { extraIds.add(rs.getInt("extra_14")); }
                        if (rs.getInt("extra_15") > 0) { extraIds.add(rs.getInt("extra_15")); }
                        token = rs.getInt("token");
                        privacy = rs.getInt("privacy");
                        legality = rs.getString("legality");
                        tcg = rs.getInt("tcg");
                        ocg = rs.getInt("ocg");
                        goat = rs.getInt("goat");
                        links = rs.getInt("links");
                    }
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            if (versions.size() != 0) {
                for (int i = 0; i < mainIds.size(); i++) {
                    main.add(cardToObject(Cards.get(mainIds.get(i) - 1)));
                }
                for (int i = 0; i < sideIds.size(); i++) {
                    side.add(cardToObject(Cards.get(sideIds.get(i) - 1)));
                }
                for (int i = 0; i < extraIds.size(); i++) {
                    extra.add(cardToObject(Cards.get(extraIds.get(i) - 1)));
                }
            }
            Object[] deckData = {deck_id, actualDeckName, main, side, extra, versions, token, privacy, legality, tcg, ocg, goat, links};
            return deckData;
    }
    
    public static void saveToken(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("deck");
            int token = (int) data.get("token");
            if (token == 0) {
                token = 1;
            }
            try {
                String query = "UPDATE decks SET token = " + token + " WHERE deck_name = '" + escapeForSQL(deck_name) + "' AND user_id = " + user.id;
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            messageE(nbc, "Token saved for " + deck_name);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void saveDeck(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String deck_name = (String) data.get("deck");
            int deck_id = (int) data.get("id");
            int token = (int) data.get("token");
            Boolean isNewDeck = true;
            JSONArray main0 = (JSONArray) ((JSONObject) data.get("cards")).get("main");
            JSONArray side0 = (JSONArray) ((JSONObject) data.get("cards")).get("side");
            JSONArray extra0 = (JSONArray) ((JSONObject) data.get("cards")).get("extra");
            ArrayList<Integer> main = new ArrayList<Integer>();
            ArrayList<Integer> side = new ArrayList<Integer>();
            ArrayList<Integer> extra = new ArrayList<Integer>();
            ArrayList<Integer> values = new ArrayList<Integer>();
            int total = 0;
            int new_id = 0;
            JSONObject result = new JSONObject();
            for (Object obj: main0) {
                main.add((int) obj);
            }
            for (Object obj: side0) {
                side.add((int) obj);
            }
            for (Object obj: extra0) {
                extra.add((int) obj);
            }
            for (int i = 0; i < 60; i++) {
                if (i < main.size()) {
                    values.add(main.get(i));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < side.size()) {
                    values.add(side.get(i));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < 15; i++) {
                if (i < extra.size()) {
                    values.add(extra.get(i));
                }
                else {
                    values.add(0);
                }
            }
            for (int i = 0; i < user.decks.size(); i++) {
                String s1 = (String) user.decks.get(i).get("name");
                if (s1.equals(deck_name)) {
                    isNewDeck = false;
                    user.decks.remove(i);
                    break;
                }
            }
            if (isNewDeck == true) {
                if (caution == true) {
                    result.put("action", "Caution");
                    write(nbc, result);
                    return;
                }
                DeckIds++;
                deck_id = DeckIds;
            }
            
            Object[] status = verifyDeck2(values);
            if (status == null) {
                status = verifyDeck(deck_id, user);
            }
            else if (status[0].equals("Error")) {
                errorE(nbc, "Illegal decklist");
                return;
            }
            //System.out.println("status[0] = " + status[0]);
            //System.out.println("status[1] = " + status[1]);
            //System.out.println("status[2] = " + status[2]);
            //System.out.println("status[3] = " + status[3]);
            //System.out.println("status[4] = " + status[4]);
            String legality = (String) status[0];
            int tcg = (int) status[1];
            int ocg = (int) status[2];
            int goat = (int) status[3];
            int links = (int) status[4];
            
            JSONObject deck = new JSONObject();
            deck.put("name", deck_name);
            deck.put("id", deck_id);
            deck.put("legality", legality);
            deck.put("tcg", tcg);
            deck.put("ocg", ocg);
            deck.put("goat", goat);
            deck.put("links", links);
            if (user.decks.size() == 0) {
                user.default_deck = deck_name;
            }
            user.decks.add(deck);
            user.decks = sortJSONArr(user.decks);
            
            result.put("action", "Save deck");
            result.put("message", deck_name + " has been saved");
            result.put("name", deck_name);
            result.put("id", deck_id);
            result.put("legality", status[0]);
            result.put("tcg", status[1]);
            result.put("ocg", status[2]);
            result.put("goat", status[3]);
            result.put("links", status[4]);
            result.put("decks", user.decks); // 11/19
            write(nbc, result);
            
            if (isNewDeck == false) {
                try {
                    String query = "UPDATE decks SET "
                        + "main_1 = " + values.get(0) + ", main_2 = " + values.get(1) + ", main_3 = " + values.get(2) + ", main_4 = " + values.get(3) + ", main_5 = " + values.get(4) + ", main_6 = " + values.get(5) + ", main_7 = " + values.get(6) + ", main_8 = " + values.get(7) + ", main_9 = " + values.get(8) + ", main_10 = " + values.get(9) + ", "
                        + "main_11 = " + values.get(10) + ", main_12 = " + values.get(11) + ", main_13 = " + values.get(12) + ", main_14 = " + values.get(13) + ", main_15 = " + values.get(14) + ", main_16 = " + values.get(15) + ", main_17 = " + values.get(16) + ", main_18 = " + values.get(17) + ", main_19 = " + values.get(18) + ", main_20 = " + values.get(19) + ", "
                        + "main_21 = " + values.get(20) + ", main_22 = " + values.get(21) + ", main_23 = " + values.get(22) + ", main_24 = " + values.get(23) + ", main_25 = " + values.get(24) + ", main_26 = " + values.get(25) + ", main_27 = " + values.get(26) + ", main_28 = " + values.get(27) + ", main_29 = " + values.get(28) + ", main_30 = " + values.get(29) + ", "
                        + "main_31 = " + values.get(30) + ", main_32 = " + values.get(31) + ", main_33 = " + values.get(32) + ", main_34 = " + values.get(33) + ", main_35 = " + values.get(34) + ", main_36 = " + values.get(35) + ", main_37 = " + values.get(36) + ", main_38 = " + values.get(37) + ", main_39 = " + values.get(38) + ", main_40 = " + values.get(39) + ", "
                        + "main_41 = " + values.get(40) + ", main_42 = " + values.get(41) + ", main_43 = " + values.get(42) + ", main_44 = " + values.get(43) + ", main_45 = " + values.get(44) + ", main_46 = " + values.get(45) + ", main_47 = " + values.get(46) + ", main_48 = " + values.get(47) + ", main_49 = " + values.get(48) + ", main_50 = " + values.get(49) + ", "
                        + "main_51 = " + values.get(50) + ", main_52 = " + values.get(51) + ", main_53 = " + values.get(52) + ", main_54 = " + values.get(53) + ", main_55 = " + values.get(54) + ", main_56 = " + values.get(55) + ", main_57 = " + values.get(56) + ", main_58 = " + values.get(57) + ", main_59 = " + values.get(58) + ", main_60 = " + values.get(59) + ", "
                        + "side_1 = " + values.get(60) + ", side_2 = " + values.get(61) + ", side_3 = " + values.get(62) + ", side_4 = " + values.get(63) + ", side_5 = " + values.get(64) + ", side_6 = " + values.get(65) + ", side_7 = " + values.get(66) + ", side_8 = " + values.get(67) + ", side_9 = " + values.get(68) + ", side_10 = " + values.get(69) + ", side_11 = " + values.get(70) + ", side_12 = " + values.get(71) + ", side_13 = " + values.get(72) + ", side_14 = " + values.get(73) + ", side_15 = " + values.get(74) + ", "
                        + "extra_1 = " + values.get(75) + ", extra_2 = " + values.get(76) + ", extra_3 = " + values.get(77) + ", extra_4 = " + values.get(78) + ", extra_5 = " + values.get(79) + ", extra_6 = " + values.get(80) + ", extra_7 = " + values.get(81) + ", extra_8 = " + values.get(82) + ", extra_9 = " + values.get(83) + ", extra_10 = " + values.get(84) + ", extra_11 = " + values.get(85) + ", extra_12 = " + values.get(86) + ", extra_13 = " + values.get(87) + ", extra_14 = " + values.get(88) + ", extra_15 = " + values.get(89) + ", "
                        + "token = " + token + ", legality = '" + escapeForSQL(legality) + "', tcg = " + tcg + ", ocg = " + ocg + ", goat = " + goat + ", links = " + links;
                    if (deck_id != 0) {
                        query += " WHERE id = " + deck_id;
                    }
                    else {
                        query += " WHERE deck_name = '" + escapeForSQL(deck_name) + "' AND user_id = " + user.id; // this is to make sure there is no problems.
                    }
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            else {
                try {
                    String query = "INSERT INTO decks (id, user_id, username, deck_name, "
                        + " main_1, main_2, main_3, main_4, main_5, main_6, main_7, main_8, main_9, main_10, "
                        + " main_11, main_12, main_13, main_14, main_15, main_16, main_17, main_18, main_19, main_20, "
                        + " main_21, main_22, main_23, main_24, main_25, main_26, main_27, main_28, main_29, main_30, "
                        + " main_31, main_32, main_33, main_34, main_35, main_36, main_37, main_38, main_39, main_40, "
                        + " main_41, main_42, main_43, main_44, main_45, main_46, main_47, main_48, main_49, main_50, "
                        + " main_51, main_52, main_53, main_54, main_55, main_56, main_57, main_58, main_59, main_60, "
                        + " side_1, side_2, side_3, side_4, side_5, side_6, side_7, side_8, side_9, side_10, side_11, side_12, side_13, side_14, side_15, "
                        + " extra_1, extra_2, extra_3, extra_4, extra_5, extra_6, extra_7, extra_8, extra_9, extra_10, extra_11, extra_12, extra_13, extra_14, extra_15, token, legality, tcg, ocg, goat, links) "
                        + " VALUES (" + deck_id + ", " + user.id + ", '" + escapeForSQL(user.user_username) + "', '" + escapeForSQL(deck_name) + "', " 
                        + values.get(0) + ", " + values.get(1) + ", " + values.get(2) + ", " + values.get(3) + ", " + values.get(4) + ", " 
                        + values.get(5) + ", " + values.get(6) + ", " + values.get(7) + ", " + values.get(8) + ", " + values.get(9) + ", " 
                        + values.get(10) + ", " + values.get(11) + ", " + values.get(12) + ", " + values.get(13) + ", " + values.get(14) + ", " 
                        + values.get(15) + ", " + values.get(16) + ", " + values.get(17) + ", " + values.get(18) + ", " + values.get(19) + ", " 
                        + values.get(20) + ", " + values.get(21) + ", " + values.get(22) + ", " + values.get(23) + ", " + values.get(24) + ", " 
                        + values.get(25) + ", " + values.get(26) + ", " + values.get(27) + ", " + values.get(28) + ", " + values.get(29) + ", " 
                        + values.get(30) + ", " + values.get(31) + ", " + values.get(32) + ", " + values.get(33) + ", " + values.get(34) + ", " 
                        + values.get(35) + ", " + values.get(36) + ", " + values.get(37) + ", " + values.get(38) + ", " + values.get(39) + ", " 
                        + values.get(40) + ", " + values.get(41) + ", " + values.get(42) + ", " + values.get(43) + ", " + values.get(44) + ", " 
                        + values.get(45) + ", " + values.get(46) + ", " + values.get(47) + ", " + values.get(48) + ", " + values.get(49) + ", " 
                        + values.get(50) + ", " + values.get(51) + ", " + values.get(52) + ", " + values.get(53) + ", " + values.get(54) + ", " 
                        + values.get(55) + ", " + values.get(56) + ", " + values.get(57) + ", " + values.get(58) + ", " + values.get(59) + ", " 
                        + values.get(60) + ", " + values.get(61) + ", " + values.get(62) + ", " + values.get(63) + ", " + values.get(64) + ", " 
                        + values.get(65) + ", " + values.get(66) + ", " + values.get(67) + ", " + values.get(68) + ", " + values.get(69) + ", " 
                        + values.get(70) + ", " + values.get(71) + ", " + values.get(72) + ", " + values.get(73) + ", " + values.get(74) + ", " 
                        + values.get(75) + ", " + values.get(76) + ", " + values.get(77) + ", " + values.get(78) + ", " + values.get(79) + ", " 
                        + values.get(80) + ", " + values.get(81) + ", " + values.get(82) + ", " + values.get(83) + ", " + values.get(84) + ", " 
                        + values.get(85) + ", " + values.get(86) + ", " + values.get(87) + ", " + values.get(88) + ", " + values.get(89) + ", "
                        + token + ", '" + escapeForSQL(legality) + "', " + tcg + ", " + ocg + ", " + goat + ", " + links + ")";
                    Statement st = getConnection().createStatement();
                    int numRowsChanged = executeUpdate(st, query);
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void loadProfile(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = "";
            int user_id = 0;
            JSONObject result = new JSONObject();
            if (data.has("username")) {
                username = (String) data.get("username");
                if (username.equals("")) {
                    errorE(nbc, "Username cannot be blank");
                }
            }
            if (data.has("id")) {
                user_id = (int) data.get("id");
            }
            String timezone = (String) user.timezone;
            int disabled = 0;
            String status = "";
            int ban_status = 0;
            String last_seen = "";
            String last_seen2 = "";
            String registered = "";
            String pic = "";
            int nsfw = 0;
            int customs = 0;
            ArrayList<JSONObject> social = new ArrayList<JSONObject>();
            String profile_txt = "";
            int views = 0;
            int html = 0;
            String gender = "";
            String orientation = "";
            int distance = 0;
            String region = "";
            String country = "";
            String location = "";
            String languages = "";
            String song_url = "";
            String song_original_url = "";
            String song_filename = "";
            String song_name = "";
            String song_author = "";
            JSONObject song = new JSONObject();
            int song_active = 0;
            int song_size = 0;
            int allow_comments = 1;
            int allow_comments_friends_only = 0;
            int require_comment_approval = 0;
            int message_friends_only = 0;
            int show_location = 1;
            int show_distance = 1;
            int match_rating = 100;
            int match_experience = 0;
            int single_rating = 100;
            int single_experience = 0;
            int match_wins = 0;
            int single_wins = 0;
            int match_losses = 0;
            int single_losses = 0;
            int match_draws = 0;
            int single_draws = 0;
            String facebook = "";
            String youtube = "";
            String twitter = "";
            String instagram = "";
            String skype = "";
            String discord = "";
            String snapchat = "";
            String kik = "";
            String tumblr = "";
            String twitch = "";
            String steam = "";
            String duel_links = "";
            ArrayList<JSONObject> friends = new ArrayList<JSONObject>();
            int duel_id = 0;
            Boolean watch_password = false;
            ArrayList<String> pics = new ArrayList<String>();
            ArrayList<Integer> ids = new ArrayList<Integer>();
            ArrayList<Integer> nsfws = new ArrayList<Integer>();
            ArrayList<JSONObject> comments = new ArrayList<JSONObject>();
            ArrayList<String> decks = new ArrayList<String>();
            int total_comments = 0;
            Boolean judge = false;
            User person = null;
            Boolean bot = false;
            System.out.println("7694");
            for (int i = 0; i < Users.size(); i++) {
                if (Users.get(i).username.equalsIgnoreCase(username) || Users.get(i).id == user_id || (Users.get(i).alt_username.equalsIgnoreCase(username) && !username.equals(""))) {
                    if (Users.get(i).nbc != null) {
                        person = Users.get(i);
                    }
                    else {
                        bot = true;
                    }
                    break;
                }
            }
            if (person != null) {
                user_id = person.id;
                username = person.username;
                ban_status = person.ban_status;
                disabled = 0;
                status = "Online";
                last_seen = "Now";
                registered = person.registered;
                pic = person.pic;
                nsfw = person.nsfw;   
                customs = person.customs;
                if (person.latitude != 0 && user.latitude != 0) {
                    distance = (int) Math.sqrt(Math.pow(69.1 * (person.latitude - user.latitude), 2) + Math.pow(69.1 * (user.longitude - person.longitude) * Math.cos(person.latitude / 57.3), 2));
                }
                region = person.region;
                country = person.country_code;
                location = person.region + ", " + person.country_code;
                facebook = person.facebook;
                youtube = person.youtube;
                twitter = person.twitter;
                instagram = person.instagram;
                skype = person.skype;
                discord = person.discord;
                snapchat = person.snapchat;
                kik = person.kik;
                tumblr = person.tumblr;
                twitch = person.twitch;
                steam = person.steam;
                duel_links = person.duel_links;
                if (!facebook.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "facebook");
                    site.put("link", facebook);
                    social.add(site);
                }
                if (!youtube.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "youtube");
                    site.put("link", youtube);
                    social.add(site);
                }
                if (!twitter.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "twitter");
                    site.put("link", twitter);
                    social.add(site);
                }
                if (!instagram.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "instagram");
                    site.put("link", instagram);
                    social.add(site);
                }
                if (!skype.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "skype");
                    site.put("link", skype);
                    social.add(site);
                }
                if (!discord.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "discord");
                    site.put("link", discord);
                    social.add(site);
                }
                if (!snapchat.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "snapchat");
                    site.put("link", snapchat);
                    social.add(site);
                }
                if (!kik.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "kik");
                    site.put("link", kik);
                    social.add(site);
                }
                if (!tumblr.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "tumblr");
                    site.put("link", tumblr);
                    social.add(site);
                }
                if (!twitch.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "twitch");
                    site.put("link", twitch);
                    social.add(site);
                }
                /*if (!steam.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "steam");
                    site.put("link", steam);
                    social.add(site);
                }*/
                if (!duel_links.equals("")) {
                    JSONObject site = new JSONObject();
                    site.put("site", "duel_links");
                    site.put("link", duel_links);
                    social.add(site);
                }
                profile_txt = person.profile_txt;
                views = person.views;
                html = person.html_txt;
                gender = person.gender;
                orientation = person.orientation;
                languages = person.languages;
                match_rating = person.match_rating;
                match_experience = person.match_experience;
                single_rating = person.single_rating;
                single_experience = person.single_experience;
                match_wins = person.match_wins;
                single_wins = person.single_wins;
                match_losses = person.match_losses;
                single_losses = person.single_losses;
                match_draws = person.match_draws;
                single_draws = person.single_draws;
                allow_comments = person.allow_comments;
                allow_comments_friends_only = person.allow_comments_friends_only;
                require_comment_approval = person.require_comment_approval;
                message_friends_only = person.message_friends_only;
                show_location = person.show_location;
                show_distance = person.show_distance;
                song = person.song;
                ids = person.ids;
                pics = person.pics;
                nsfws = person.nsfws;
                total_comments = person.total_comments;
                if (person.duel_id != 0 && person.duel != null && person.duel.active == true) {
                    if (person.duel.watching == true || user.admin > 0) {
                        duel_id = person.duel_id;
                        if (!person.duel.watch_password.equals("")) {
                            watch_password = true;
                        }
                    }
                }
            }
            else {
                try {
                    String query = "SELECT *, DATE_FORMAT(join_date,'%b %d, %Y, %l:%i %p') AS registered, SQRT(POW(69.1 * (latitude - " + user.latitude + "), 2) + POW(69.1 * (" + user.longitude + " - longitude) * COS(latitude / 57.3), 2)) AS distance FROM duelingbook_user db";
                    if (user_id == 0) {
                        query += " WHERE username = '" + escapeForSQL(username) + "' OR alt_username = '" + escapeForSQL(username) + "'";
                    }
                    else {
                        query += " WHERE id = " + user_id;
                    }
                    Statement st = getConnection().createStatement();
                    ResultSet rs = executeQuery(st, query);
                    if (rs.next()) {
                        user_id = rs.getInt("id");
                        username = rs.getString("username");
                        ban_status = rs.getInt("ban_status");
                        disabled = rs.getInt("is_disabled");
                        if (rs.getTimestamp("last_seen") == null) {
                            last_seen = "Never";
                            last_seen2 = "Never"; // ??
                            if (bot == true) {
                                last_seen = "Now";
                            }
                        }
                        else {
                            Timestamp timestamp = rs.getTimestamp("last_seen");
                            long time = timestamp.getTime();
                            last_seen = getDetailedTimeAgo(time);
                            last_seen2 = getTimeAgo(time);
                        }
                        registered = rs.getString("registered");
                        pic = rs.getString("pic");
                        nsfw = rs.getInt("nsfw");
                        customs = rs.getInt("customs");
                        if (user.latitude != 0) {
                            distance = (int) Math.floor(rs.getInt("distance"));
                        }
                        region = deAccent(rs.getString("region"));
                        country = deAccent(rs.getString("country_code"));
                        location = region + ", " + country;
                        if (!rs.getString("facebook").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "facebook");
                            site.put("link", rs.getString("facebook"));
                            social.add(site);
                        }
                        if (!rs.getString("youtube").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "youtube");
                            site.put("link", rs.getString("youtube"));
                            social.add(site);
                        }
                        if (!rs.getString("twitter").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "twitter");
                            site.put("link", rs.getString("twitter"));
                            social.add(site);
                        }
                        if (!rs.getString("instagram").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "instagram");
                            site.put("link", rs.getString("instagram"));
                            social.add(site);
                        }
                        if (!rs.getString("skype").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "skype");
                            site.put("link", rs.getString("skype"));
                            social.add(site);
                        }
                        if (!rs.getString("discord").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "discord");
                            site.put("link", rs.getString("discord"));
                            social.add(site);
                        }
                        if (!rs.getString("snapchat").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "snapchat");
                            site.put("link", rs.getString("snapchat"));
                            social.add(site);
                        }
                        if (!rs.getString("kik").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "kik");
                            site.put("link", rs.getString("kik"));
                            social.add(site);
                        }
                        if (!rs.getString("tumblr").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "tumblr");
                            site.put("link", rs.getString("tumblr"));
                            social.add(site);
                        }
                        if (!rs.getString("twitch").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "twitch");
                            site.put("link", rs.getString("twitch"));
                            social.add(site);
                        }
                        /*if (!rs.getString("steam").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "steam");
                            site.put("link", rs.getString("steam"));
                            social.add(site);
                        }*/
                        if (!rs.getString("duel_links").equals("")) {
                            JSONObject site = new JSONObject();
                            site.put("site", "duel_links");
                            site.put("link", rs.getString("duel_links"));
                            social.add(site);
                        }
                        profile_txt = rs.getString("profile_txt");
                        profile_txt = replaceReturns(profile_txt);
                        views = rs.getInt("profile_views");
                        html = rs.getInt("use_html_text");
                        gender = rs.getString("gender");
                        orientation = rs.getString("orientation");
                        languages = rs.getString("languages");
                        match_rating = rs.getInt("match_rating");
                        match_experience = rs.getInt("match_experience");
                        single_rating = rs.getInt("single_rating");
                        single_experience = rs.getInt("single_experience");
                        match_wins = rs.getInt("match_wins");
                        single_wins = rs.getInt("single_wins");
                        match_losses = rs.getInt("match_losses");
                        single_losses = rs.getInt("single_losses");
                        match_draws = rs.getInt("match_draws");
                        single_draws = rs.getInt("single_draws");

                        allow_comments = rs.getInt("allow_comments");
                        allow_comments_friends_only = rs.getInt("allow_comments_friends_only");
                        require_comment_approval = rs.getInt("require_comment_approval");
                        message_friends_only = rs.getInt("message_friends_only");
                        show_location = rs.getInt("show_location");
                        show_distance = rs.getInt("show_distance");
                        if (rs.getString("distance") == null) {
                            show_distance = 0;
                        }
                    }
                    else {
                        errorE(nbc, username + " is not a registered user");
                        return;
                    }
                    st.close();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                if (disabled == 1) {
                    errorE(nbc, username + "'s account was terminated");
                    return;
                }
                if (last_seen == null) {
                    last_seen = "Never";
                }
                status = "Offline";
            }
            if (username.indexOf("Resource Judge") >= 0) {
                judge = true;
            }
            String action = (String) data.get("action");
            System.out.println("action = " + action);
            result.put("action", action);
            result.put("user_id", user_id);
            result.put("username", username);
            result.put("judge", judge);
            result.put("status", status);
            result.put("ban_status", ban_status);
            result.put("last_seen", last_seen);
            result.put("registered", registered);
            result.put("pic", pic);
            result.put("nsfw", nsfw);
            result.put("social", social);
            result.put("profile_txt", profile_txt);
            result.put("html", html);
            //result.put("views", views);
            result.put("views", NumberFormat.getIntegerInstance().format(views));
            result.put("song", song);
            result.put("gender", gender);
            result.put("orientation", orientation);
            if (show_distance == 1 && distance != 0) {
                result.put("distance", Integer.toString(distance) + " miles away");
            }
            if (show_location == 1 && !location.equals(", ")) {
                result.put("location", location);
            }
            result.put("languages", languages);
            result.put("allow_comments", allow_comments);
            result.put("allow_comments_friends_only", allow_comments_friends_only);
            result.put("require_comment_approval", require_comment_approval);
            result.put("message_friends_only", message_friends_only);
            result.put("match_rating", match_rating);
            result.put("match_experience", match_experience);
            result.put("single_rating", single_rating);
            result.put("single_experience", single_experience);
            result.put("match_wins", match_wins);
            result.put("single_wins", single_wins);
            result.put("match_losses", match_losses);
            result.put("single_losses", single_losses);
            result.put("match_draws", match_draws);
            result.put("single_draws", single_draws);
            result.put("friends", friends);
            result.put("pics", pics);
            result.put("ids", ids);
            result.put("nsfws", nsfws);
            result.put("comments", comments);
            result.put("total_comments", total_comments);
            result.put("decks", decks);
            result.put("duel", duel_id);
            result.put("password", watch_password);
            write(nbc, result);
            
            // UPDATE VIEWS
            if (user.previous_views.indexOf(username) >= 10) {
                user.previous_views.remove(username);
            }
            if (!user.previous_views.contains(username)) {
                if (!user.lastViewedProfile.equals(username)) {
                    System.out.println("We ARE incrementing the views");
                    if (!username.equals(user.username)) {
                        try {
                            user.previous_views.add(username);
                            if (person != null) {
                                person.views++;
                            }
                            String query = "UPDATE duelingbook_user SET profile_views = profile_views + 1 WHERE id = " + user_id;
                            Statement st = getConnection().createStatement();
                            int numRowsChanged = executeUpdate(st, query);
                            st.close();
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
                user.lastViewedProfile = username;
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void calculateSingleRanking(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = (String) data.get("username");
            int ranking = 1;
            try {
                String query = "SELECT id, username, single_rating FROM duelingbook_user ORDER BY single_rating DESC, single_experience DESC, single_wins DESC, id";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    if (rs.getString("username").equals(username)) {
                        break;
                    }
                    ranking++;
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            JSONObject result = new JSONObject();
            result.put("action", "Calculate single ranking");
            result.put("ranking", ranking);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void calculateMatchRanking(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = (String) data.get("username");
            int ranking = 1;
            try {
                String query = "SELECT id, username, match_rating, match_experience, match_wins FROM duelingbook_user ORDER BY match_rating DESC, match_experience DESC, match_wins DESC, id";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    if (rs.getString("username").equals(username)) {
                        break;
                    }
                    ranking++;
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            JSONObject result = new JSONObject();
            result.put("action", "Calculate match ranking");
            result.put("ranking", ranking);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void loadMyProfile(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String time_remaining = "";
            JSONObject result = new JSONObject();
            result.put("action", "Load my profile");
            result.put("pic", user.pic);
            result.put("sleeve", user.sleeve);
            result.put("pics", user.pics);
            result.put("ids", user.ids);
            result.put("nsfws", user.nsfws);
            result.put("profile_txt", user.profile_txt);
            result.put("use_html_txt", user.html_txt);
            result.put("song", user.song);
            result.put("customs", user.customs);
            result.put("simple", user.simple);
            result.put("allow_messages", user.allow_messages);
            result.put("allow_offline_messages", user.allow_offline_messages);
            result.put("message_friends_only", user.message_friends_only);
            result.put("allow_comments", user.allow_comments);
            result.put("allow_comments_friends_only", user.allow_comments_friends_only);
            result.put("require_comment_approval", user.require_comment_approval);
            result.put("show_distance", user.show_distance);
            result.put("show_location", user.show_location);
            result.put("html", user.html);
            result.put("timezone", user.timezone);
            result.put("facebook", user.facebook);
            result.put("nsfw", user.nsfw);
            result.put("gender", user.gender);
            result.put("orientation", user.orientation);
            result.put("location", user.location);
            result.put("languages", user.languages);
            result.put("admin", user.admin_status);
            if (user.previous_calls.size() >= 200) {
                result.put("calls", String.format("%.2f", user.cpd));
            }
            if (!time_remaining.equals("")) {
                result.put("days", time_remaining);
            }
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void saveProfile(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String profile_txt = (String) data.get("profile_txt");
            int html = (int) data.get("html");
            int pic_id = (int) data.get("pic");
            int nsfw = (int) data.get("nsfw");
            int sleeve_id = (int) data.get("sleeve");
            String pic = "";
            String sleeve = "";
            if (user.html == 0 && user.customs == 0) {
                html = 0;
            }
            user.profile_txt = profile_txt;
            user.html_txt = html;
            if (html == 1) {
                //profile_txt = makeSSL(profile_txt);
            }
            System.out.println("pic_id = " + pic_id);
            if (pic_id != 0) {
                System.out.println("a");
                if (pic_id > 10000 && user.customs < 1 && user.id > 125) {
                    System.out.println("b");
                    System.out.println("user.customs = " + user.customs);
                    System.out.println("user.id = " + user.id);
                    invalidRequest(nbc);
                    return;
                }
                else {
                    if (pic_id < 10000) {
                        pic = Integer.toString(pic_id) + ".jpg";
                        nsfw = 0;
                        
                        
                        
                        
                        int index = -1;
                        for (int i = 0; i < Avatars.length; i++) {
                            if (pic_id == Avatars[i]) {
                                index = i;
                                break;
                            }
                        }
                        if (index >= 0) {
                            if (user.single_wins + user.match_wins < WinsNeededArr[index]) {
                                if (user.customs < 1) {
                                    invalidRequest(nbc);
                                    return;
                                }
                            }
                            else {
                                user.default_pic = pic;
                            }
                        }
                    }
                    else {
                        try {
                            String query = "SELECT pic_url FROM profile_pictures WHERE id = " + pic_id;
                            Statement st = getConnection().createStatement();
                            ResultSet rs = executeQuery(st, query);
                            if (rs.next()) {
                                pic = rs.getString("pic_url");
                            }
                            st.close();
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    if (pic_id != user.pic_id) {
                        try {
                            //String query = "UPDATE duelingbook_user SET pic = '" + escapeForSQL(pic) + "', default_pic = IF (" + pic_id + " < 10000, '" + escapeForSQL(pic) + "', default_pic), nsfw = " + nsfw + " WHERE id = " + user.id;
                            String query = "UPDATE duelingbook_user SET pic = '" + escapeForSQL(pic) + "', default_pic = '" + escapeForSQL(user.default_pic) + "', nsfw = " + nsfw + " WHERE id = " + user.id;
                            Statement st = getConnection().createStatement();
                            int numRowsChanged = executeUpdate(st, query);
                            st.close();
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        user.pic = pic;
                        user.pic_id = pic_id;
                        user.nsfw = nsfw;
                        updatePic(user);
                    }
                }
            }
            if (sleeve_id != 0) {
                if (user.customs < 1) {
                    invalidRequest(nbc);
                    return;
                }
                else {
                    try {
                        String query = "UPDATE duelingbook_user SET sleeve = (SELECT pic_url FROM sleeves WHERE id = " + sleeve_id + ") WHERE id = " + user.id;
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    user.sleeve = sleeve_id + ".jpg";
                }
            }
            try {
                String query = "UPDATE duelingbook_user SET profile_txt = '" + escapeForSQL(profile_txt) + "', use_html_text = " + html + " WHERE id = " + user.id;
                Statement st = getConnection().createStatement();
                int numRowsChanged = executeUpdate(st, query);
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            messageE(nbc, "Profile has been saved");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void updatePic(User user) {
        for (int i = 0; i < StatusUpdates.size(); i++) {
            if (StatusUpdates.get(i).username.equals(user.username)) {
                StatusUpdates.get(i).pic = user.pic;
                StatusUpdates.get(i).nsfw = user.nsfw;
            }
        }
        for (int i = 0; i < Users.size(); i++) {
            for (int j = 0; j < Users.get(i).comments.size(); j++) {
                if (Users.get(i).comments.get(j).user_id == user.id) {
                    Users.get(i).comments.get(j).pic = user.pic;
                    Users.get(i).comments.get(j).nsfw = user.nsfw;
                }
            }
        }
        for (int i = 0; i < Users.size(); i++) {
            for (int j = 0; j < Users.get(i).friends_arr.size(); j++) {
                if (Users.get(i).friends_arr.get(j).user_id == user.id) {
                    Users.get(i).friends_arr.get(j).pic = user.pic;
                    Users.get(i).friends_arr.get(j).nsfw = user.nsfw;
                }
            }
        }
        for (int i = 0; i < UserStates.size(); i++) {
            for (int j = 0; j < UserStates.get(i).comments.size(); j++) {
                if (UserStates.get(i).comments.get(j).user_id == user.id) {
                    UserStates.get(i).comments.get(j).pic = user.pic;
                    UserStates.get(i).comments.get(j).nsfw = user.nsfw;
                }
            }
        }
        for (int i = 0; i < UserStates.size(); i++) {
            for (int j = 0; j < UserStates.get(i).friends_arr.size(); j++) {
                if (UserStates.get(i).friends_arr.get(j).user_id == user.id) {
                    UserStates.get(i).friends_arr.get(j).pic = user.pic;
                    UserStates.get(i).friends_arr.get(j).nsfw = user.nsfw;
                }
            }
        }
    }
    
    public static Boolean isFriend(User user, String username) {
        for (int i = 0; i < user.friends.size(); i++) {
            if (user.friends.get(i).equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public static void rankingByRating(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            Boolean matches = (Boolean) data.get("matches");
            String location = (String) data.get("location");
            String rating = "single_rating";
            String experience = "single_experience";
            String wins = "single_wins";
            String losses = "single_losses";
            String draws = "single_draws";
            if (matches == true) {
                rating = "match_rating";
                experience = "match_experience";
                wins = "match_wins";
                losses = "match_losses";
                draws = "match_draws";
            }
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> results = new ArrayList<JSONObject>();
            try {
                String query = "SELECT username, " + rating + ", " + experience + ", " + wins + ", " + losses + ", " + draws + " FROM duelingbook_user WHERE is_disabled = 0 ";
                if (!location.equals("")) {
                    query += " AND country LIKE '%" + escapeForSQL(location) + "%'";
                }
                query += " ORDER BY " + rating + " DESC LIMIT 200";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    JSONObject entry = new JSONObject();
                    entry.put("entry1", rs.getString("username"));
                    entry.put("entry2", rs.getString(rating));
                    entry.put("entry3", rs.getString(wins));
                    entry.put("entry4", rs.getString(losses));
                    entry.put("entry5", rs.getString(draws));
                    entry.put("entry6", rs.getString(experience));
                    results.add(entry);
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "Ranking by rating");
            result.put("results", results);
            result.put("matches", matches);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void rankingByWins(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            Boolean matches = (Boolean) data.get("matches");
            String rating = "single_rating";
            String experience = "single_experience";
            String wins = "single_wins";
            String losses = "single_losses";
            String draws = "single_draws";
            if (matches == true) {
                rating = "match_rating";
                experience = "match_experience";
                wins = "match_wins";
                losses = "match_losses";
                draws = "match_draws";
            }
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> results = new ArrayList<JSONObject>();
            try {
                String query = "SELECT username, " + rating + ", " + experience + ", " + wins + ", " + losses + ", " + draws + " FROM duelingbook_user WHERE is_disabled = 0 ORDER BY " + wins + " DESC LIMIT 200";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    JSONObject entry = new JSONObject();
                    entry.put("entry1", rs.getString("username"));
                    entry.put("entry2", rs.getString(wins));
                    entry.put("entry3", rs.getString(losses));
                    entry.put("entry4", rs.getString(draws));
                    entry.put("entry5", rs.getString(rating));
                    entry.put("entry6", rs.getString(experience));
                    results.add(entry);
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "Ranking by wins");
            result.put("results", results);
            result.put("matches", matches);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void rankingByRep(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            Boolean matches = (Boolean) data.get("matches");
            String rating = "single_rating";
            String experience = "single_experience";
            String wins = "single_wins";
            String losses = "single_losses";
            String draws = "single_draws";
            if (matches == true) {
                rating = "match_rating";
                experience = "match_experience";
                wins = "match_wins";
                losses = "match_losses";
                draws = "match_draws";
            }
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> results = new ArrayList<JSONObject>();
            try {
                String query = "SELECT username, " + rating + ", " + experience + ", " + wins + ", " + losses + ", " + draws + " FROM duelingbook_user WHERE is_disabled = 0 ORDER BY " + experience + " DESC LIMIT 200";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    JSONObject entry = new JSONObject();
                    entry.put("entry1", rs.getString("username"));
                    entry.put("entry2", rs.getString(experience));
                    entry.put("entry3", rs.getString(rating));
                    entry.put("entry4", rs.getString(wins));
                    entry.put("entry5", rs.getString(losses));
                    entry.put("entry6", rs.getString(draws));
                    results.add(entry);
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "Ranking by rep");
            result.put("results", results);
            result.put("matches", matches);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void rankingByTotalRep(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            Boolean matches = (Boolean) data.get("matches");
            JSONObject result = new JSONObject();
            ArrayList<JSONObject> results = new ArrayList<JSONObject>();
            try {
                String query = "SELECT username, (single_experience + match_experience) AS total_experience, single_experience, match_experience, single_rating, match_rating "
                            + "FROM duelingbook_user WHERE is_disabled = 0 ORDER BY total_experience DESC, match_experience DESC, single_experience DESC, username LIMIT 200";
                Statement st = getConnection().createStatement();
                ResultSet rs = executeQuery(st, query);
                while (rs.next()) {
                    JSONObject entry = new JSONObject();
                    entry.put("entry1", rs.getString("username"));
                    entry.put("entry2", rs.getString("total_experience"));
                    entry.put("entry3", rs.getString("single_experience"));
                    entry.put("entry4", rs.getString("match_experience"));
                    entry.put("entry5", rs.getString("single_rating"));
                    entry.put("entry6", rs.getString("match_rating"));
                    results.add(entry);
                }
                st.close();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
            result.put("action", "Ranking by total rep");
            result.put("results", results);
            result.put("matches", matches);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void refreshCards(ChannelHandlerContext nbc, JSONObject data, User user) {
        if (user.admin < 3) {
            invalidRequest(nbc);
            return;
        }
        loadCards();
        messageE(nbc, "Cards have been refreshed");
    }
    
    public static void loadCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        try {
            String query = "SELECT * FROM card_library ORDER BY id";
            Statement st = getConnection().createStatement();
            ResultSet rs = executeQuery(st, query);
            int i = 1;
            while (rs.next()) {
                if (rs.getInt("id") != i) {
                    while (i != rs.getInt("id")) {
                        Card card = new Card();
                        card.name = "";
                        cards.add(card);
                        i++;
                    }
                }
                Card card = new Card();
                card.card_id = rs.getInt("id");
                card.name = rs.getString("name");
                card.treated_as = rs.getString("treated_as");
                card.effect = rs.getString("effect");
                card.pendulum_effect = rs.getString("pendulum_effect");
                card.card_type = rs.getString("card_type");
                card.monster_color = rs.getString("monster_color");
                card.is_effect = rs.getInt("is_effect");
                card.type = rs.getString("type");
                card.attribute = rs.getString("attribute");
                card.level = rs.getInt("level");
                card.ability = rs.getString("ability");
                card.flip = rs.getInt("flip");
                card.pendulum = rs.getInt("pendulum");
                card.scale_left = rs.getInt("scale_left");
                card.scale_right = rs.getInt("scale_right");
                card.arrows = rs.getString("arrows");
                card.atk = rs.getString("atk");
                card.def = rs.getString("def");
                card.tcg_limit = rs.getInt("tcg_limit");
                card.ocg_limit = rs.getInt("ocg_limit");
                card.serial_number = getSerialNumber(rs.getInt("serial_number"));
                card.tcg = rs.getInt("tcg");
                card.ocg = rs.getInt("ocg");
                card.pic = rs.getString("pic");
                card.timestamp = rs.getTimestamp("date_posted");
                if (card.atk == null) {
                    card.atk = "?";
                }
                if (card.def == null) {
                    card.def = "?";
                }
                card.hidden = rs.getInt("hidden");
                cards.add(card);
                i++;
            }
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("cards.size() = " + cards.size());
        Cards = cards;
        CardObjects = new ArrayList<JSONObject>();
        for (int i = 0; i < Cards.size(); i++) {
            if (Cards.get(i).name == null) {
                continue;
            }
            if (Cards.get(i).name.equals("")) {
                continue;
            }
            if (Cards.get(i).hidden == 1) {
                continue;
            }
            CardObjects.add(cardToObject(Cards.get(i)));
        }
        GoatFormatCards = new ArrayList<String>();
        GoatFormatLimits = new ArrayList<Integer>();
        try {
            String query = "SELECT cl.treated_as, gf.restriction FROM goat_format gf INNER JOIN card_library cl ON gf.id = cl.id GROUP BY treated_as";
            Statement st = getConnection().createStatement();       
            ResultSet rs = executeQuery(st, query);
            while (rs.next()) {
                GoatFormatCards.add(rs.getString("treated_as"));
                GoatFormatLimits.add(rs.getInt("restriction"));
            }
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        DuelLinksCards = new ArrayList<String>();
        DuelLinksLimits = new ArrayList<Integer>();
        try {
            String query = "SELECT name, card_limit, SUM(amount) AS amount FROM duel_links GROUP BY name ORDER BY name";
            Statement st = getConnection().createStatement();     
            ResultSet rs = executeQuery(st, query);
            while (rs.next()) {
                DuelLinksCards.add(rs.getString("name"));
                if (rs.getInt("card_limit") < 3) {
                    DuelLinksLimits.add(rs.getInt("card_limit"));
                }
                else {
                    int amount = rs.getInt("amount");
                    if (amount > 3) {
                        amount = 3;
                    }
                    DuelLinksLimits.add(amount);
                }
            }
            st.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void addValuesToArray(ArrayList arr1, ArrayList arr2) {
        for (int i = 0; i < arr2.size(); i++) {
            if (!arr1.contains(arr2.get(i))) {
                arr1.add(arr2.get(i));
            }
        }
    }
    
    public static ArrayList<Integer> getFieldCards(Player player) {
        ArrayList<Integer> cards = new ArrayList<Integer>();
        if (player.m1 != null) {
            cards.add(player.m1.id);
        }
        if (player.m2 != null) {
            cards.add(player.m2.id);
        }
        if (player.m3 != null) {
            cards.add(player.m3.id);
        }
        if (player.m4 != null) {
            cards.add(player.m4.id);
        }
        if (player.m5 != null) {
            cards.add(player.m5.id);
        }
        if (player.s1 != null) {
            cards.add(player.s1.id);
        }
        if (player.s2 != null) {
            cards.add(player.s2.id);
        }
        if (player.s3 != null) {
            cards.add(player.s3.id);
        }
        if (player.s4 != null) {
            cards.add(player.s4.id);
        }
        if (player.s5 != null) {
            cards.add(player.s5.id);
        }
        if (player.fieldSpell != null) {
            cards.add(player.fieldSpell.id);
        }
        if (player.pendulumLeft != null) {
            cards.add(player.pendulumLeft.id);
        }
        if (player.pendulumRight != null) {
            cards.add(player.pendulumRight.id);
        }
        if (player.duel.linkLeft != null) {
            cards.add(player.duel.linkLeft.id);
        }
        if (player.duel.linkRight != null) {
            cards.add(player.duel.linkRight.id);
        }
        return cards;
    }
    
    public static Boolean ableToDuel(ChannelHandlerContext nbc, JSONObject data, User user, Boolean rated) {
        if (caution == true) {
            JSONObject result = new JSONObject();
            result.put("action", "Caution");
            write(nbc, result);
            return false;
        }
        if (user.frozen == true) {
            errorE(nbc, "You are frozen");
            return false;
        }
        if (user.admin > 0 || user.adjudicator > 0) {
            errorE(nbc, "You cannot duel right now");
            return false;
        }
        if (rated == true && PoolEnabled == false) {
            errorE(nbc, "Advanced (Rated) is currently disabled");
            return false;
        }
        int deck = (int) data.get("deck");
        String type;
        String rules;
        String format = "ar";
        if (data.has("rules") && data.has("type")) {
            type = (String) data.get("type");
            rules = (String) data.get("rules");
            if (data.has("format")) {
                format = (String) data.get("format");
            }
        }
        else {
            String username = (String) data.get("username");
            User opp = getUser(username);
            type = opp.duel_type;
            rules = opp.duel_rules;
            format = opp.duel_format;
        }
        if (!rules.equals("TCG") && !rules.equals("OCG") && !rules.equals("*")) {
            invalidRequest(nbc);
            return false;
        }
        if (rated == true && rules.equals("*")) {
            invalidRequest(nbc);
            return false;
        }
        if (!type.equals("m") && !type.equals("s") && !type.equals("n")) {
            invalidRequest(nbc);
            return false;
        }
        if (rated == true && type.equals("n")) {
            invalidRequest(nbc);
            return false;
        }
        if (rated == true && user.beginner == 1) {
            errorE(nbc, "You cannot play in rated games right now");
            return false;
        }
        if (rated == true) {
            for (int i = 0; i < user.decks.size(); i++) {
                if ((int) user.decks.get(i).get("id") == deck) {
                    System.out.println(user.decks.get(i));
                    user.deck_legality = (String) user.decks.get(i).get("legality");
                    if (!user.decks.get(i).get("legality").equals("Advanced")) {
                        errorE(nbc, "This deck is not legal for Advanced play");
                        return false;
                    }
                    if ((int) user.decks.get(i).get("tcg") == 0 && rules.equals("TCG")) {
                        errorE(nbc, "This deck is not TCG");
                        return false;
                    }
                    if ((int) user.decks.get(i).get("ocg") == 0 && rules.equals("OCG")) {
                        errorE(nbc, "This deck is not OCG");
                        return false;
                    }
                    break;
                }
                if (i == user.decks.size() - 1) {
                    errorE(nbc, "Failed to load your deck");
                    return false;
                }
            }
        }
        else {
            if (format.equals("ju") && user.expert < 1) {
                errorE(nbc, "You must pass the Duelingbook exam in order to play in this format");
                return false;
            }
            for (int i = 0; i < user.decks.size(); i++) {
                if ((int) user.decks.get(i).get("id") == deck) {
                    user.deck_legality = (String) user.decks.get(i).get("legality");
                    if (user.decks.get(i).get("legality").equals("Empty")) {
                        errorE(nbc, "Your deck does not contain any cards yet");
                        return false;
                    }
                    if (user.decks.get(i).get("legality").equals("Illegal") && format.equals("uu")) {
                        errorE(nbc, "Your deck must have a minimum of 20 cards");
                        return false;
                    }
                    if (user.decks.get(i).get("legality").equals("Illegal") && !format.equals("uu")) {
                        errorE(nbc, "Deck contains under 40 cards");
                        return false;
                    }
                    if (user.decks.get(i).get("legality").equals("Small") && !format.equals("uu")) {
                        errorE(nbc, "Deck contains under 40 cards");
                        return false;
                    }
                    if ((int) user.decks.get(i).get("tcg") == 0 && rules.equals("TCG")) {
                        errorE(nbc, "This deck is not TCG");
                        return false;
                    }
                    if ((int) user.decks.get(i).get("ocg") == 0 && rules.equals("OCG")) {
                        errorE(nbc, "This deck is not OCG");
                        return false;
                    }
                    if (format.equals("au") && !user.decks.get(i).get("legality").equals("Advanced")) {
                        errorE(nbc, "This deck is not legal for Advanced play");
                        return false;
                    }
                    break;
                }
                if (i == user.decks.size() - 1) {
                    errorE(nbc, "Failed to load your deck");
                    return false;
                }
            }
        }
        if (user.duel_id != 0) {
            return false;
        }
        return true;
    }
    
    public static void joinPool(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (ableToDuel(nbc, data, user, true) == false) {
                return;
            }
            for (int i = 0; i < Pool.size(); i++) {
                if (Pool.get(i).user_username.equalsIgnoreCase(user.user_username)) {
                    return;
                }
            }
            int deck = (int) data.get("deck");
            String rules = (String) data.get("rules");
            String type = (String) data.get("type");
            String format = "ar";
            if (data.has("format")) {
                format = (String) data.get("format");
            }
            user.duel_note = "";
            user.duel_format = format;
            user.watch_note = "";
            user.links = true;
            user.has_watch_password = false;
            user.allow_watching = true;
            user.duel_password = "";
            user.watch_password = "";
            user.watch_note = "";
            user.inPool = true;
            user.deck = deck;
            user.duel_type = type;
            user.duel_rules = rules;
            user.rating = user.single_rating;
            user.experience = user.single_experience;
            if (type.equals("m")) {
                user.rating = user.match_rating;
                user.experience = user.match_experience;
            }
            user.waiting = false;
            user.standby = true;
            user.leeway = 0;
            user.ready = false;
            user.pool_seconds = 0;
            Pool.add(user);
            JSONObject result = new JSONObject();
            result.put("action", "Join pool");
            result.put("seconds", getPoolSeconds(user));
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void hostDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            JSONObject result = new JSONObject();
            if (ableToDuel(nbc, data, user, false) == false) {
                return;
            }
            cancelDuel(nbc, data, user);
            int deck = (int) data.get("deck");
            String rules = (String) data.get("rules");
            int rating = user.single_rating;
            int experience = user.single_experience;
            int friends = user.friends.size();
            String format = (String) data.get("format");
            String type = (String) data.get("type");
            Boolean links = (Boolean) data.get("links");
            String note = (String) data.get("note");
            Boolean watching = (Boolean) data.get("watching");
            String watch_note = (String) data.get("watch_note");
            String watch_password = (String) data.get("watch_password");
            String password = (String) data.get("password");
            Boolean has_duel_password = false;
            Boolean has_watch_password = false;
            String word = "";
            String deck_name = "";
            for (int i = 0; i < user.decks.size(); i++) {
                if ((int) user.decks.get(i).get("id") == deck) {
                    deck_name = (String) user.decks.get(i).get("name");
                }
            }
            user.duel_type = type;
            if (type.equals("m")) {
                rating = user.match_rating;
                experience = user.match_experience;
                word = "Match";
            }
            else {
                word = "Single";
            }
            if (!password.equals("")) {
                has_duel_password = true;
            }
            if (!watch_password.equals("")) {
                has_watch_password = true;
            }
            if (Hosts.contains(user)) {
                return;
            }
            if (format.equals("gu") || format.equals("dl")) {
                links = false;
            }
            user.duel_type = type;
            user.duel_rules = rules;
            user.duel_note = note;
            user.duel_format = format;
            user.watch_note = watch_note;
            user.links = links;
            user.has_watch_password = has_watch_password;
            user.allow_watching = watching;
            user.duel_password = password;
            user.watch_password = watch_password;
            user.watch_note = watch_note;
            user.rating = rating;
            user.experience = experience;
            user.joiners = new ArrayList<User>();
            user.deck = deck;
            user.deck_name = deck_name;
            user.pool_seconds = 0;
            Hosts.add(user);
            
            result.put("action", "Host duel");
            result.put("username", user.username);
            result.put("format", format);
            result.put("rules", rules);
            result.put("type", type);
            result.put("note", note);
            result.put("links", links);
            result.put("password", has_duel_password);
            result.put("watching", watching);
            result.put("watch_note", watch_note);
            result.put("rating", rating);
            result.put("experience", experience);
            result.put("friends", friends);
            result.put("legality", user.deck_legality);
            for (int i = 0; i < DuelRoom.size(); i++) {
                write(DuelRoom.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Boolean passesPoolFilter(User user1, User user2, Boolean logging) {
        if (!user1.duel_format.equals(user2.duel_format)) {
            return false;
        }
        if (user1.username.equals(user2.username)) {
            return false;
        }
        if (user1.id == user2.id) {
            return false;
        }
        if (!user1.duel_type.equals(user2.duel_type)) {
            return false;
        }
        if (!user1.duel_rules.equals(user2.duel_rules)) {
            return false;
        }
        if (!user1.duel_language.equals(user2.duel_language)) {
            return false;
        }
        if (InsertingMessages == true && (user1.duel_format.equals("ar") || user2.duel_format.equals("ar"))) {
            if (user1.ip_address.equals(user2.ip_address)) {
                return false;
            }
            if (user1.nbc_address.equals(user2.nbc_address)) {
                return false;
            }
            if (user1.db_id.equals(user2.db_id)) {
                return false;
            }
            if (user1.previous_opponents.contains(user2.username) || user2.previous_opponents.contains(user1.username)) {
                return false;
            }
            if (user1.previous_ips.contains(user2.ip_address) || user2.previous_ips.contains(user1.ip_address)) {
                return false;
            }
            if (user1.previous_nbc_ips.contains(user2.nbc_address) || user2.previous_nbc_ips.contains(user1.nbc_address)) {
                return false;
            }
            if (user1.previous_db_ids.contains(user2.db_id) || user2.previous_db_ids.contains(user1.db_id)) {
                return false;
            }
            if (user1.rating > user2.rating + 900) {
                return false;
            }
            if (user2.rating > user1.rating + 900) {
                return false;
            }
        }
        return true;
    }
    
    public static void poolEvent() {
        try {
            for (int i = 0; i < Pool.size(); i++) {
                if (Pool.get(i).duel_id != 0) {
                    Pool.remove(Pool.get(i));
                    i--;
                    continue;
                }
                Pool.get(i).leeway += 25;
                for (int j = 0; j < Pool.size(); j++) {
                    if (Pool.get(i).standby == false || Pool.get(j).standby == false) {
                        continue;
                    }
                    if (Pool.get(j).rating > (Pool.get(i).rating + Pool.get(i).leeway)) {
                        continue;
                    }
                    if (Pool.get(j).rating < (Pool.get(i).rating - Pool.get(i).leeway)) {
                        continue;
                    }           
                    if (passesPoolFilter(Pool.get(i), Pool.get(j), false) == false) {
                        continue;
                    }
                    if (Pool.get(i).rating - Pool.get(j).rating < 200 || Pool.get(i).rating - Pool.get(j).rating > 200) {
                        Pool.get(i).good_choices.add(Pool.get(j));
                    }
                }
            }
            for (int i = 0; i < Pool.size(); i++) {
                if (Pool.get(i).standby == true) {
                    if (Pool.get(i).good_choices.size() > 0) { 
                        int j = (int) Math.floor(Math.random() * Pool.get(i).good_choices.size());
                        if (Pool.get(i).good_choices.get(j).standby == true) {
                            assignPair(Pool.get(i), Pool.get(i).good_choices.get(j));
                            Pool.get(i).good_choices = new ArrayList<User>();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void assignPair(User host, User opponent) {      
        try {
            host.waiting = true;
            host.standby = false;
            host.ready = false;
            host.opponent = opponent;
            host.assignedAt = host.pool_seconds;
            opponent.waiting = true;
            opponent.standby = false;
            opponent.ready = false;
            opponent.opponent = host;
            opponent.assignedAt = host.pool_seconds;
            JSONObject result = new JSONObject();
            result.put("action", "Partner found");
            write(host.nbc, result);
            write(opponent.nbc, result);
            System.out.println(host.username + " was paired with " + opponent.username);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void leavePool(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            user.inPool = false;
            user.ready = false;
            if (Pool.contains(user)) {
                Pool.remove(user);
                if (user.opponent != null) {
                    if (user.opponent.inPool == true && user.opponent.waiting == true) {
                        JSONObject result = new JSONObject();
                        result.put("action", "Join pool");
                        result.put("seconds", getPoolSeconds(user.opponent));
                        write(user.opponent.nbc, result);
                        user.opponent.waiting = false;
                        user.opponent.standby = true;
                        user.opponent.leeway = 0;
                        user.opponent.ready = false;
                        if (user.opponent.good_choices.contains(user)) {
                            user.opponent.good_choices.remove(user);
                        }
                    }
                    user.opponent.opponent = null;
                }
                user.opponent = null;
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static int getPoolSeconds(User user) {
        int seconds = 45;
        int num = 0;
        for (int i = 0; i < Pool.size(); i++) {
            if (Pool.get(i).id != user.id) {
                if (Pool.get(i).duel_format.equals(user.duel_format)) {
                    if (Pool.get(i).waiting == false) {
                        if (Pool.get(i).rating < user.rating + 50 && user.rating - 50 < Pool.get(i).rating) {
                            num++;
                        }
                    }
                }
            }
        }
        if (num != 0) {
            seconds = 3 * num;
        }
        return seconds;
    }
    
    public static void readyE(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.duel_id != 0) {
                return;
            }
            user.ready = true;
            if (user.opponent.ready == true) {
                user.inPool = false;
                user.opponent.inPool = false;
                Boolean rated = true;
                if (!user.duel_format.equals("ar")) {
                    rated = false;
                }
                if (user.username.compareToIgnoreCase(user.opponent.username) < 0) {
                    startDuel(user, user.opponent, rated);
                }
                else {
                    startDuel(user.opponent, user, rated);
                }
            }
            else {
                JSONObject result = new JSONObject();
                result.put("action", "Ready");
                write(nbc, result);
                System.out.println("Nope. " + user.opponent.username + " is not ready");
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void duelError(User p1, User p2, Boolean b) {
        try {
            if (b == true) {
                p1.duel_id = 0;
                p2.duel_id = 0;
            }
            exitDuelRoomB(p1.nbc, null, p1);
            exitDuelRoomB(p2.nbc, null, p2);
            JSONObject result = new JSONObject();
            result.put("action", "Duel error");
            result.put("message", "There was a problem. Try joining the pool again.");
            write(p1.nbc, result);
            write(p2.nbc, result);
            return;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void startDuel(User p1, User p2, Boolean rated) {
        try {
            if (p1.duel_id != 0 || p2.duel_id != 0) {
                duelError(p1, p2, false);
                return;
            }
            if (rated == true) {
                if (passesPoolFilter(p1, p2, true) == false) {
                    duelError(p1, p2, true);
                    return;
                }
                if (p1.id == p2.id || p1.username.equals(p2.username)) {
                    duelError(p1, p2, true);
                    return;
                }
                if (!p1.duel_type.equals(p2.duel_type)) {
                    duelError(p1, p2, true);
                    return;
                }
                if (!p1.duel_rules.equals(p2.duel_rules)) {
                    duelError(p1, p2, true);
                    return;
                }
            }
            DuelIds++;
            int duel_id = DuelIds;
            for (int i = 0; i < Duels.size(); i++) {
                if (Duels.get(i).id == DuelIds) {
                    DuelIds++;
                    i = 0;
                }
            }
            TotalDuels++;
            p1.duel_id = duel_id;
            p2.duel_id = duel_id;
            
            String type = "Single";
            String detailed_type = "Single (with siding)";
            if (p1.duel_type.equals("m")) {
                type = "Match";
                detailed_type = "2 out of 3 Match";
            }
            else if (p1.duel_type.equals("n")) {
                detailed_type = "Single (no siding)";
            }
            Boolean has_watch_password = p1.has_watch_password;
            String watch_password = p1.watch_password;
            Boolean watching = p1.allow_watching;
            String note = p1.watch_note;
            Boolean links = p1.links;
            String format = "";
            switch (p1.duel_format) {
                case "ar":
                    format = "Advanced (Rated)";
                    break;
                case "au":
                    format = "Advanced (Unrated)";
                    break;
                case "tu":
                    format = "Traditional (Unrated)";
                    break;
                case "gu":
                    format = "Goat Format (Unrated)";
                    break;
                case "ju":
                    format = "Expert Room (Unrated)";
                    break;
                case "uu":
                    format = "Unlimited (Unrated)";
                    break;
            }
            String entry = p1.username + " hosted " + detailed_type + " in " + format;
            if (!p1.duel_note.equals("")) {
                entry += " with duel note \"" + p1.duel_note + "\"";
            }
            if (!p1.watch_note.equals("")) {
                entry += " and watch note \"" + p1.watch_note + "\"";
            }
            if (rated == true) {
                has_watch_password = false;
                watch_password = "";
                watching = true;
                note = "";
                links = true;
                entry = p1.username + " joined pool (" + p1.duel_language + ", " + p1.duel_rules + ", " + type + ")";
            }
            
            JSONObject action = new JSONObject();
            action.put("action", "START DUEL");
            action.put("message", p1.username + " vs " + p2.username);
            action.put("time", System.currentTimeMillis());

            p1.previous_opponents.add(p2.username);
            p1.previous_ips.add(p2.ip_address);
            p1.previous_nbc_ips.add(p2.nbc_address);
            p1.previous_db_ids.add(p2.db_id);
            p1.actions.add(action);

            p2.previous_opponents.add(p1.username);
            p2.previous_ips.add(p1.ip_address);
            p2.previous_nbc_ips.add(p1.nbc_address);
            p2.previous_db_ids.add(p1.db_id);
            p2.actions.add(action);

            Player player1 = newPlayer();
            player1.user = p1;
            player1.username = p1.username;
            player1.user_id = p1.id;
            player1.nbc = p1.nbc;
            player1.deck = loadDeck(p1.id, p1.deck, p1.default_deck);
            player1.main = (ArrayList<JSONObject>) player1.deck[2];
            player1.side = (ArrayList<JSONObject>) player1.deck[3];
            player1.extra = (ArrayList<JSONObject>) player1.deck[4];
            player1.token = (int) player1.deck[6];
            player1.sleeve = p1.sleeve;
            player1.deck_id = p1.deck;
            player1.pic = p1.pic;
            player1.default_pic = p1.default_pic;
            player1.rating = p1.rating;
            player1.experience = p1.experience;
            player1.prev_rating = p1.rating;
            player1.prev_experience = p1.experience;
            player1.nsfw = p1.nsfw;
            player1.friends = p1.friends.size();
            player1.ip_address = p1.ip_address;
            if (p1.gender.equals("Female")) {
                player1.pronoun = "her";
            }

            Player player2 = newPlayer();
            player2.user = p2;
            player2.username = p2.username;
            player2.user_id = p2.id;
            player2.nbc = p2.nbc;
            player2.deck = loadDeck(p2.id, p2.deck, p2.default_deck);
            player2.main = (ArrayList<JSONObject>) player2.deck[2];
            player2.side = (ArrayList<JSONObject>) player2.deck[3];
            player2.extra = (ArrayList<JSONObject>) player2.deck[4];
            player2.token = (int) player2.deck[6];
            player2.sleeve = p2.sleeve;
            player2.deck_id = p2.deck;
            player2.pic = p2.pic;
            player2.default_pic = p2.default_pic;
            player2.rating = p2.rating;
            player2.experience = p2.experience;
            player2.prev_rating = p2.rating;
            player2.prev_experience = p2.experience;
            player2.nsfw = p2.nsfw;
            player2.friends = p2.friends.size();
            player2.ip_address = p2.ip_address;
            if (p2.gender.equals("Female")) {
                player2.pronoun = "her";
            }

            player1.opponent = player2;
            player2.opponent = player1;
            
            if (!p1.duel_format.equals("uu")) {
                if (player1.main.size() == 0 || player2.main.size() == 0) {
                    duelError(p1, p2, true);
                    player1.user.decks = loadDecklists(player1.user.id, player1.user.default_deck);
                    player2.user.decks = loadDecklists(player2.user.id, player2.user.default_deck);
                    return;
                }
            }
            if (player1.user_id == player2.user_id || player1.user.id == player2.user.id || player1.username.equals(player2.username) || player1.user.username.equals(player2.user.username)) {
                duelError(p1, p2, true);
                return;
            }

            Duel duel = newDuel();
            duel.id = duel_id;
            duel.player1 = player1;
            duel.player2 = player2;
            duel.username1 = player1.username;
            duel.username2 = player2.username;
            duel.type = p1.duel_type;
            duel.note = note;
            duel.format = p1.duel_format;
            duel.rules = p1.duel_rules;
            duel.rated = rated;
            duel.links = links;
            duel.watching = watching;
            duel.watch_note = p1.watch_note;
            duel.watch_password = watch_password;
            duel.has_watch_password = has_watch_password;
            duel.language = p1.duel_language;
            if (duel.player1.rating <= duel.player2.rating) {
                duel.rating_low = duel.player1.rating;
                duel.rating_high = duel.player2.rating;
            }
            else {
                duel.rating_low = duel.player2.rating;
                duel.rating_high = duel.player1.rating;
            }
            if (duel.player1.experience <= duel.player2.experience) {
                duel.experience_low = duel.player1.experience;
                duel.experience_high = duel.player2.experience;
            }
            else {
                duel.experience_low = duel.player2.experience;
                duel.experience_high = duel.player1.experience;
            }
            duel.friends1 = player1.friends;
            duel.friends2 = player2.friends;
            player1.duel = duel;
            player2.duel = duel;
            p1.duel = duel;
            p2.duel = duel;

            JSONObject log = new JSONObject();
            log.put("seconds", duel.seconds);
            log.put("username", duel.player1.username);
            log.put("type", "game");
            log.put("public_log", entry);
            duel.addLog(log);

            duel.seconds = p1.assignedAt;
            entry = "Accepted " + duel.player2.username + " into duel";
            if (rated == true) {
                entry = duel.player2.username + " automatically paired via pool";
            }
            log = new JSONObject();
            log.put("seconds", duel.seconds);
            log.put("username", duel.player2.username);
            log.put("type", "game");
            log.put("public_log", entry);
            duel.addLog(log);
            

            Duels.add(duel);

            duel.player1.resetDeck();
            duel.player1.setDeck();
            duel.player1.randomizeDeck(101);

            duel.player2.resetDeck();
            duel.player2.setDeck();
            duel.player2.randomizeDeck(201);

            JSONObject player_1 = new JSONObject();
            player_1.put("username", player1.username);
            player_1.put("user_id", player1.user_id);
            player_1.put("pic", player1.pic);
            player_1.put("default_pic", player1.default_pic);
            player_1.put("nsfw", player1.nsfw);
            player_1.put("sleeve", player1.sleeve);
            player_1.put("rating", player1.rating);
            player_1.put("experience", player1.experience);
            player_1.put("token", player1.token);
            player_1.put("main_total", player1.main.size());
            player_1.put("extra_total", player1.extra.size());
            player_1.put("side_total", player1.side.size());
            player_1.put("start", 101);
            player_1.put("main", player1.cardsToIds(player1.main_arr));
            player_1.put("side", player1.cardsToIds(player1.side_arr));
            player_1.put("extra", player1.cardsToIds(player1.extra_arr));
            player_1.put("legality", player1.user.deck_legality);
            player_1.put("key", player1.key);
            player_1.put("db_id", player1.user.db_id);
            //player_1.put("deck", player1.getDeckIds());

            JSONObject player_2 = new JSONObject();
            player_2.put("username", player2.username);
            player_2.put("user_id", player2.user_id);
            player_2.put("pic", player2.pic);
            player_2.put("default_pic", player2.default_pic);
            player_2.put("nsfw", player2.nsfw);
            player_2.put("sleeve", player2.sleeve);
            player_2.put("rating", player2.rating);
            player_2.put("experience", player2.experience);
            player_2.put("token", player2.token);
            player_2.put("main_total", player2.main.size());
            player_2.put("extra_total", player2.extra.size());
            player_2.put("side_total", player2.side.size());
            player_2.put("start", 201);
            player_2.put("main", player2.cardsToIds(player2.main_arr));
            player_2.put("side", player2.cardsToIds(player2.side_arr));
            player_2.put("extra", player2.cardsToIds(player2.extra_arr));
            player_2.put("legality", player2.user.deck_legality);
            player_2.put("key", player2.key);
            player_2.put("db_id", player2.user.db_id);
            //player_2.put("deck", player2.getDeckIds());

            JSONObject result = new JSONObject();
            result.put("action", "Accept user");
            result.put("player1", player_2);
            result.put("player2", player_1);
            result.put("rated", duel.rated);
            result.put("links", duel.links);
            result.put("type", duel.type);
            result.put("format", duel.format);
            result.put("id", duel.id);
            result.put("logs", duel.log);
            if (player2.active == true) {
                write(player2.nbc, result);
            }

            result = new JSONObject();
            result.put("action", "Accept user");
            result.put("player1", player_1);
            result.put("player2", player_2);
            result.put("rated", duel.rated);
            result.put("links", duel.links);
            result.put("type", duel.type);
            result.put("format", duel.format);
            result.put("id", duel.id);
            result.put("logs", duel.log);
            if (player1.active == true) {
                write(player1.nbc, result);
            }

            if (p1.joiners.size() > 1) {
                JSONObject result2 = new JSONObject();
                result2.put("action", "Accept other user");
                result2.put("username", p1.username);
                result2.put("id", duel.id);
                result2.put("watching", p1.allow_watching);
                if (!duel.watch_password.equals("")) {
                    result2.put("password", true);
                }
                for (int j = 0; j < p1.joiners.size(); j++) {
                    if (!p1.joiners.get(j).username.equals(p2.username)) {
                        write(p1.joiners.get(j).nbc, result2);
                    }
                }
            }
            p1.joiners = new ArrayList<User>();
            p2.joiners = new ArrayList<User>();
            
            exitDuelRoom(p1.nbc, null, p1);
            exitDuelRoom(p2.nbc, null, p2);
            
            addWatchButton(duel);
            
            ((JSONObject) result.get("player1")).put("deck", player1.getDeckIds());
            ((JSONObject) result.get("player2")).put("deck", player2.getDeckIds());
            ((JSONObject) result.get("player1")).put("deck_id", player1.deck_id);
            ((JSONObject) result.get("player2")).put("deck_id", player2.deck_id);
            duel.initReplayData(new JSONObject(result, JSONObject.getNames(result)));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void cancelDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (Hosts.contains(user)) {
                Hosts.remove(user);
                JSONObject result = new JSONObject();
                result.put("action", "Cancel duel");
                result.put("username", user.username);
                for (int j = 0; j < DuelRoom.size(); j++) {
                    write(DuelRoom.get(j).nbc, result);
                }
                user.joiners = new ArrayList<User>();
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void joinDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (ableToDuel(nbc, data, user, false) == false) {
                return;
            }
            String username = (String) data.get("username");
            user.deck = (int) data.get("deck");
            user.duel_password = (String) data.get("password");
            user.rating = user.single_rating;
            user.experience = user.single_experience;
            JSONObject result = new JSONObject();
            for (int i = 0; i < Hosts.size(); i++) {
                if (Hosts.get(i).username.equals(username)) {
                    if (!Hosts.get(i).duel_password.equals("")) {
                        if (!Hosts.get(i).duel_password.toLowerCase().equals(user.duel_password.toLowerCase())) {
                            result.put("action", "Incorrect duel password");
                            result.put("message", "Incorrect password");
                            write(nbc, result);
                            return;
                        }
                    }
                    String rating = " (" + user.single_rating + "/" + user.single_experience + ")";
                    if (Hosts.get(i).duel_type.equals("m")) {
                        rating = " (" + user.match_rating + "/" + user.match_experience + ")";
                        user.rating = user.match_rating;
                        user.experience = user.match_experience;
                    }
                    if (user.deck_legality.equals("Illegal") || user.deck_legality.equals("") || user.deck_legality.equals("Small")) {
                        rating += " (Deck contains under 40 cards)";
                    }
                    Hosts.get(i).joiners.add(user);
                    result.put("action", "Join duel");
                    result.put("username", user.username);
                    result.put("rating", rating);
                    result.put("legality", user.deck_legality);
                    write(Hosts.get(i).nbc, result);
                    return;
                }
            }
            errorE(nbc, "Duel is no longer available");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void leaveDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Leave duel");
            result.put("username", user.username);
            for (int i = 0; i < Hosts.size(); i++) {
                for (int j = 0; j < Hosts.get(i).joiners.size(); j++) {
                    if (Hosts.get(i).joiners.get(j).username.equals(user.username)) {
                        write(Hosts.get(i).nbc, result);
                        Hosts.get(i).joiners.remove(j);
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void rejectUser(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = (String) data.get("username");
            JSONObject result = new JSONObject();
            result.put("action", "Reject user");
            result.put("username", user.username);
            for (int i = 0; i < Hosts.size(); i++) {
                if (Hosts.get(i).username.equals(user.username)) {
                    for (int j = 0; j < Hosts.get(i).joiners.size(); j++) {
                        if (Hosts.get(i).joiners.get(j).username.equals(username)) {
                            write(Hosts.get(i).joiners.get(j).nbc, result);
                            Hosts.get(i).joiners.remove(j);
                            return;
                        }
                    }
                    break;
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void loadHosting(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (!DuelRoom.contains(user)) {
                DuelRoom.add(user);
            }
            ArrayList<JSONObject> hosts = new ArrayList<JSONObject>();
            for (int i = 0; i < Hosts.size(); i++) {
                String note = Hosts.get(i).duel_note;
                if (user.moderator >= 2) {
                    note = Hosts.get(i).deck_name;
                    if (!Hosts.get(i).duel_note.equals("")) {
                        note += " | " + Hosts.get(i).duel_note;
                    }
                }
                Boolean require_password = false;
                if (!Hosts.get(i).duel_password.equals("")) {
                    require_password = true;
                }
                JSONObject host = new JSONObject();
                host.put("username", Hosts.get(i).username);
                host.put("type", Hosts.get(i).duel_type);
                host.put("note", note);
                host.put("format", Hosts.get(i).duel_format);
                host.put("rules", Hosts.get(i).duel_rules);
                host.put("password", require_password);
                host.put("watching", Hosts.get(i).allow_watching);
                host.put("rating", Hosts.get(i).rating);
                host.put("experience", Hosts.get(i).experience);
                host.put("friends", Hosts.get(i).friends);
                host.put("links", Hosts.get(i).links);
                host.put("legality", Hosts.get(i).deck_legality);
                hosts.add(host);
            }
            JSONObject result = new JSONObject();
            result.put("action", "Load duels");
            result.put("duels", hosts);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void addWatchButton(Duel duel) {
        try {
            if (duel.watching == true) {
                JSONObject watchButton = new JSONObject();
                watchButton.put("heading", duel.player1.username + " (" + duel.player1.rating + "/" + duel.player1.experience + ") | " + duel.player2.username + " (" + duel.player2.rating + "/" + duel.player2.experience + ")");
                watchButton.put("username1", duel.player1.username);
                watchButton.put("username2", duel.player2.username);
                watchButton.put("note", duel.note);
                watchButton.put("type", duel.type);
                watchButton.put("format", duel.format);
                watchButton.put("note", duel.note);
                watchButton.put("password", duel.has_watch_password);
                watchButton.put("id", duel.id);
                watchButton.put("rating_low", duel.rating_low);
                watchButton.put("rating_high", duel.rating_high);
                watchButton.put("experience_low", duel.experience_low);
                watchButton.put("experience_high", duel.experience_high);
                watchButton.put("friends1", duel.friends1);
                watchButton.put("friends2", duel.friends2);
                watchButton.put("links", duel.links);
                watchButton.put("watching", duel.watching);
                watchButton.put("rules", duel.rules);
                JSONObject result = new JSONObject();
                result.put("action", "Add duel");
                result.put("duel", watchButton);
                for (int i = 0; i < DuelRoom.size(); i++) {
                    write(DuelRoom.get(i).nbc, result);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void loadWatching(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (!DuelRoom.contains(user)) {
                DuelRoom.add(user);
            }
            String card = "";
            if (data.has("card")) {
                card = (String) data.get("card");
            }
            ArrayList<JSONObject> duels = new ArrayList<JSONObject>();
            for (int i = 0; i < Duels.size(); i++) {
                if (Duels.get(i) == null) {
                    System.out.println("Found a null duel");
                    continue;
                }
                if (Duels.get(i).player1 == null) {
                    System.out.println("Found a null player A");
                    continue;
                }
                if (Duels.get(i).player2 == null) {
                    System.out.println("Found a null player B");
                    continue;
                }
                if (Duels.get(i).player1.username == null) {
                    System.out.println("Found a null player A username");
                    continue;
                }
                if (Duels.get(i).player2.username == null) {
                    System.out.println("Found a null player B username");
                    continue;
                }
                if (Duels.get(i).active == false) {
                    continue;
                }
                if (user.moderator < 2) {
                    if (Duels.get(i).watching == false) {
                        continue;
                    }
                }
                if (!card.equals("")) {
                    if (containsCard(Duels.get(i), card.toLowerCase()) == false) {
                        continue;
                    }
                }
                JSONObject duel = new JSONObject();
                duel.put("heading", Duels.get(i).player1.username + " (" + Duels.get(i).player1.rating + "/" + Duels.get(i).player1.experience + ") | " + Duels.get(i).player2.username + " (" + Duels.get(i).player2.rating + "/" + Duels.get(i).player2.experience + ")");
                duel.put("username1", Duels.get(i).username1);
                duel.put("username2", Duels.get(i).username2);
                duel.put("note", Duels.get(i).note);
                duel.put("type", Duels.get(i).type);
                duel.put("format", Duels.get(i).format);
                duel.put("watching", Duels.get(i).watching);
                duel.put("password", Duels.get(i).has_watch_password);
                duel.put("id", Duels.get(i).id);
                duel.put("rating_low", Duels.get(i).rating_low);
                duel.put("rating_high", Duels.get(i).rating_high);
                duel.put("experience_low", Duels.get(i).experience_low);
                duel.put("experience_high", Duels.get(i).experience_high);
                duel.put("friends1", Duels.get(i).friends1);
                duel.put("friends2", Duels.get(i).friends2);
                duel.put("links", Duels.get(i).links);
                duel.put("rules", Duels.get(i).rules);
                duels.add(duel);
            }
            JSONObject result = new JSONObject();
            result.put("action", "Load duels");
            result.put("duels", duels);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Boolean containsCard(Duel duel, String name) {
        try {
            for (int i = 0; i < duel.player1.grave_arr.size(); i++) {
                if (duel.player1.grave_arr.get(i).name.toLowerCase().indexOf(name) >= 0) {
                    return true;
                }
            }
            for (int i = 0; i < duel.player1.banished_arr.size(); i++) {
                if (duel.player1.banished_arr.get(i).name.toLowerCase().indexOf(name) >= 0 && duel.player1.banished_arr.get(i).face_down == false) {
                    return true;
                }
            }
            if (onField(duel.player1, name) == true) {
                return true;
            }
            for (int i = 0; i < duel.player2.grave_arr.size(); i++) {
                if (duel.player2.grave_arr.get(i).name.toLowerCase().indexOf(name) >= 0) {
                    return true;
                }
            }
            for (int i = 0; i < duel.player2.banished_arr.size(); i++) {
                if (duel.player2.banished_arr.get(i).name.toLowerCase().indexOf(name) >= 0 && duel.player2.banished_arr.get(i).face_down == false) {
                    return true;
                }
            }
            if (onField(duel.player2, name) == true) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("containsCard failed");
            return false;
        }
    }
    
    public static Boolean onField(Player player, String name) {
        if (player.m1 != null) {
            if (player.m1.name.toLowerCase().indexOf(name) >= 0 && player.m1.face_down == false) {
                return true;
            }
        }
        if (player.m2 != null) {
            if (player.m2.name.toLowerCase().indexOf(name) >= 0 && player.m2.face_down == false) {
                return true;
            }
        }
        if (player.m3 != null) {
            if (player.m3.name.toLowerCase().indexOf(name) >= 0 && player.m3.face_down == false) {
                return true;
            }
        }
        if (player.m4 != null) {
            if (player.m4.name.toLowerCase().indexOf(name) >= 0 && player.m4.face_down == false) {
                return true;
            }
        }
        if (player.m5 != null) {
            if (player.m5.name.toLowerCase().indexOf(name) >= 0 && player.m5.face_down == false) {
                return true;
            }
        }
        if (player.s1 != null) {
            if (player.s1.name.toLowerCase().indexOf(name) >= 0 && player.s1.face_down == false) {
                return true;
            }
        }
        if (player.s2 != null) {
            if (player.s2.name.toLowerCase().indexOf(name) >= 0 && player.s2.face_down == false) {
                return true;
            }
        }
        if (player.s3 != null) {
            if (player.s3.name.toLowerCase().indexOf(name) >= 0 && player.s3.face_down == false) {
                return true;
            }
        }
        if (player.s4 != null) {
            if (player.s4.name.toLowerCase().indexOf(name) >= 0 && player.s4.face_down == false) {
                return true;
            }
        }
        if (player.s5 != null) {
            if (player.s5.name.toLowerCase().indexOf(name) >= 0 && player.s5.face_down == false) {
                return true;
            }
        }
        if (player.fieldSpell != null) {
            if (player.fieldSpell.name.toLowerCase().indexOf(name) >= 0 && player.fieldSpell.face_down == false) {
                return true;
            }
        }
        if (player.pendulumLeft != null) {
            if (player.pendulumLeft.name.toLowerCase().indexOf(name) >= 0 && player.pendulumLeft.face_down == false) {
                return true;
            }
        }
        if (player.pendulumRight != null) {
            if (player.pendulumRight.name.toLowerCase().indexOf(name) >= 0 && player.pendulumRight.face_down == false) {
                return true;
            }
        }
        if (player.duel.linkLeft != null) {
            if (player.duel.linkLeft.name.toLowerCase().indexOf(name) >= 0 && player.duel.linkLeft.face_down == false) {
                return true;
            }
        }
        if (player.duel.linkRight != null) {
            if (player.duel.linkRight.name.toLowerCase().indexOf(name) >= 0 && player.duel.linkRight.face_down == false) {
                return true;
            }
        }
        return false;
    }
    
    public static void exitDuelRoom(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            for (int i = 0; i < DuelRoom.size(); i++) {
                if (DuelRoom.get(i).username.equals(user.username)) {
                    DuelRoom.remove(i);
                    break;
                }
            }
            exitDuelRoomB(nbc, data, user);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void exitDuelRoomB(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            leavePool(nbc, data, user);
            cancelDuel(nbc, data, user);
            leaveDuel(nbc, data, user);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void cancelGame(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            System.out.println("cancelGame entered");
            if (user.duel.active == false) {
                return;
            }
            Duel duel = user.duel;
            String username = "";
            JSONObject result = new JSONObject();
            if (data.has("username")) {
                username = (String) data.get("username");
                User person = getUser(username);
                if (person != null) {
                    duel = person.duel;
                    if (!duel.player1.username.equals(person.username) && !duel.player2.username.equals(person.username)) {
                        result = new JSONObject();
                        errorE(nbc, username + " is not dueling.");
                        return;
                    }
                }
                else {
                    errorE(nbc, username + " is not online");
                    return;
                }
            }
            if (user.admin < 1) {
                if (user.duel.rated == true || user.adjudicator == 0) {
                    invalidRequest(nbc);
                    return;
                }
            }
            String word = "";
            String type = "";
            if (data.has("type")) {
                type = (String) data.get("type");
                if (type.equals("Duel")) {
                    word = "duel";
                }
                else if (type.equals("Match")) {
                    word = "match";
                }
                else {
                    invalidRequest(nbc);
                    return;
                }
            }
            else {
                if (duel.type.equals("m")) {
                    word = "match";
                    type = "Match";
                }
                else {
                    word = "duel";
                    type = "Duel";
                }
            }
            String public_entry = "A judge cancelled the " + word;
            JSONObject log = new JSONObject();
            log.put("seconds", duel.seconds);
            //log.put("timestamp", duel.timestamp);
            log.put("username", user.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", duel.player1.rating);
            player1.put("experience", duel.player1.experience);
            player1.put("points", duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", duel.player2.rating);
            player2.put("experience", duel.player2.experience);
            player2.put("points", duel.player2.points);
            
            result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Cancel game");
                result.put("id", duel.id);
            result.put("type", word);
            result.put("username", user.username);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            duelInactive(duel);
            if (duel.type.equals("m")) {
                if (type.equals("Duel")) {
                    beginSiding(duel);
                }
                else {
                    System.out.println("No, type = " + type);
                }
            }
            updateDuelEndTime(duel);
            if (!username.equals("")) {
                messageE(nbc, username + "'s " + word + " has been cancelled");
                return;
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void pauseGame(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (user.admin < 1 && user.adjudicator < 1) {
                invalidRequest(nbc);
                return;
            }
            String public_entry = "A judge paused the game";
            JSONObject result = new JSONObject();
            if (user.duel.paused == true) {
                user.duel.paused = false;
                result.put("play", "Resume game");
                public_entry = "A judge resumed the game";
            }
            else {
                user.duel.paused = true;
                result.put("play", "Pause game");
            }
            result.put("username", user.username);
            publicGameResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void gameloss(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.duel.active == false) {
                return;
            }
            if (user.duel.status.equals("Siding")) {
                if (user.duel.log.get(user.duel.log.size() - 1).has("play")) {
                    if (user.duel.log.get(user.duel.log.size() - 1).get("play").equals("Admit defeat")) {
                        return;
                    }
                }
            }
            if (user.admin < 1) {
                if (user.duel.rated == true || user.adjudicator == 0) {
                    invalidRequest(nbc);
                    return;
                }
            }
            Boolean over = false;
            Player winner = null;
            Player loser = null;
            String type = (String) data.get("type");
            String word = "";
            String username = "";
            if (data.get("action").equals("Host loss")) {
                winner = user.duel.player2;
                loser = user.duel.player1;
            }
            else if (data.get("action").equals("Opponent loss")) {
                winner = user.duel.player1;
                loser = user.duel.player2;
            }
            else {
                invalidRequest(nbc);
                return;
            }
            if (type.equals("Game loss")) {
                word = "game";
            }
            else if (type.equals("Match loss")) {
                word = "match";
                user.duel.winner = winner;
                winner.result = 2;
                user.duel.loser = loser;
                loser.result = 0;
                over = true;
            }
            else {
                invalidRequest(nbc);
                return;
            }
            winner.wins++;
            loser.losses++;
            winner.recorded_wins++;
            loser.recorded_losses++;
            user.duel.rpsWinner = loser.username;
            if (user.duel.type.equals("m")) {
                if (user.duel.games >= 2 && winner.wins > 1) {
                    if (winner.wins > loser.wins) {
                        user.duel.winner = winner;
                        winner.result = 2;
                        user.duel.loser = loser;
                        loser.result = 0;
                        over = true;
                    }
                }
            }
            else {
                user.duel.winner = winner;
                winner.result = 2;
                user.duel.loser = loser;
                loser.result = 0;
                over = true;
            }
            if (user.duel.rated == true) {
                if (user.duel.winner != null) {
                    updateRatings(user.duel);
                }
            }
            String public_entry = "Received a " + word + " loss by a judge";
            JSONObject log = new JSONObject();
            //log.put("timestamp", user.duel.timestamp);
            log.put("username", loser.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            user.duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", user.duel.player1.rating);
            player1.put("experience", user.duel.player1.experience);
            player1.put("points", user.duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", user.duel.player2.rating);
            player2.put("experience", user.duel.player2.experience);
            player2.put("points", user.duel.player2.points);
            
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", user.duel.seconds);
            result.put("play", (String) data.get("type"));
                result.put("id", user.duel.id);
            result.put("over", over);
            result.put("type", word);
            result.put("username", loser.username);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (user.duel.player2.active == true) {
                write(user.duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (user.duel.player1.active == true) {
                write(user.duel.player1.nbc, result);
            }
            user.duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < user.duel.watchers.size(); i++) {
                write(user.duel.watchers.get(i).nbc, result);
            }
            duelInactive(user.duel);
            if (user.duel.type.equals("m")) {
                if (user.duel.winner == null) {
                    beginSiding(user.duel);
                }
            }
            if (user.duel.winner != null) {
                updateDuelEndTime(user.duel);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void autoLoss(User user, String reason) {
        try {
            if (user.duel.active == false) {
                return;
            }
            if (user.duel.awaiting_admin == true || user.duel.turn_player == null || user.duel.currentPhase == null) {
                user.duel.player1.resetAFKTimer();
                user.duel.player2.resetAFKTimer();
                return;
            }
            System.out.println("afkLoss entered");
            Player you = null;
            Player opp = null;
            Duel duel = user.duel;
            if (duel.player1.username.equals(user.username)) {
                you = duel.player1;
                opp = duel.player2;
            }
            else if (duel.player2.username.equals(user.username)) {
                you = duel.player2;
                opp = duel.player1;
            }
            String word = "";
            String loss_type = "";
            if (duel.type.equals("m")) {
                word = "match";
            }
            else {
                word = "game";
            }
            duel.winner = opp;
            opp.result = 2;
            duel.loser = you;
            you.result = 0;
            duel.winner.wins++;
            duel.loser.losses++;
            duel.winner.recorded_wins++;
            duel.loser.recorded_losses++;
            duel.rpsWinner = duel.loser.username;
            if (duel.rated == true) {
                updateRatings(duel);
            }
            //String public_entry = "Received a loss due to being AFK";
            String public_entry = "Received a loss due to " + reason;
            JSONObject log = new JSONObject();
            //log.put("timestamp", user.duel.timestamp);
            log.put("username", duel.loser.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", duel.player1.rating);
            player1.put("experience", duel.player1.experience);
            player1.put("points", duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", duel.player2.rating);
            player2.put("experience", duel.player2.experience);
            player2.put("points", duel.player2.points);
            
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Loss");
            result.put("reason", reason);
            result.put("over", true);
            result.put("type", word);
            result.put("username", duel.loser.username);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (user.duel.player2.active == true) {
                write(user.duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (user.duel.player1.active == true) {
                write(user.duel.player1.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < user.duel.watchers.size(); i++) {
                write(user.duel.watchers.get(i).nbc, result);
            }
            duelInactive(user.duel);
            /*if (user.duel.type.equals("m")) {
                if (user.duel.winner == null) {
                    beginSiding(user.duel);
                }
            }*/
            if (user.duel.winner != null) {
                updateDuelEndTime(user.duel);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void acceptDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = (String) data.get("username");
            for (int i = 0; i < user.joiners.size(); i++) {
                if (user.joiners.get(i).username.equals(username)) {
                    startDuel(user, user.joiners.get(i), false);
                    return;
                }
            }
            errorE(nbc, "User has left");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Duel getDuelByID(int duel_id) {
        for (int i = 0; i < Duels.size(); i++) {
            if (Duels.get(i).id == duel_id) {
                return Duels.get(i);
            }
        }
        return null;
    }
    
    public static void duelTypeError(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void getDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            Player you = null;
            Player opp = null;
            Duel duel = user.duel;
            if (duel.player1.username.equals(user.username)) {
                you = duel.player1;
                opp = duel.player2;
            }
            else if (duel.player2.username.equals(user.username)) {
                you = duel.player2;
                opp = duel.player1;
            }
            else if (user.admin > 0 || user.moderator > 1 || user.adjudicator > 0) {
                // YOU ARE THE ADMIN
                you = duel.player1;
                opp = duel.player2;
            }
            else {
                System.out.println("LINE 9445 ENTERED. Illegal duel action attempted");
                return;
            }
            you.resetAFKTimer();
            if (duel.turn_player != null && duel.currentPhase != null) {
                if (duel.turn_player.equals(user.username) && !duel.currentPhase.equals("")) {
                    you.startAFKTimer();
                }
                else if (!duel.turn_player.equals(user.username) && duel.currentPhase.equals("")) {
                    you.startAFKTimer();
                }
            }
            if (user.plays.size() > 10) {
                user.plays.remove(0);
            }
            user.plays.add((String) data.get("play"));
            if (duel.player1.ready == true && duel.player2.ready == true) {
                //if (user.lastPlay.equals(data.toString()) && ArrayUtils.indexOf(GeneralPlays, (String) data.get("play")) < 0 && System.currentTimeMillis() - user.lastPlayTimestamp.getTime() < 500) {
                if (user.lastPlay.equals(data.toString()) && GeneralPlays.indexOf((String) data.get("play")) < 0 && System.currentTimeMillis() - user.lastPlayTimestamp.getTime() < 500) {
                    System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
                    System.out.println("user.lastPlayTimestamp.getTime() = " + user.lastPlayTimestamp.getTime());
                    return;
                }
                user.lastPlay = data.toString();
                user.lastPlayTimestamp = new Timestamp(System.currentTimeMillis());
            }
            switch((String) data.get("play")) {
                case "Ready":
                    playerReadyE(nbc, data, user, duel, you, opp);
                    break;
                case "RPS":
                    chooseRPS(nbc, data, user, duel, you, opp);
                    break;
                case "Duel message":
                    duelChat(nbc, data, user, duel, you, opp);
                    break;
                case "Swap cards":
                    swapCards(nbc, data, user, duel, you, opp);
                    break;
                case "Reset deck":
                    resetDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Pause game":
                    pauseGame(nbc, data, user, duel, you, opp);
                    break;
                case "Call admin":
                    callAdmin(nbc, data, user, duel, you, opp);
                    break;
                case "Cancel call":
                    cancelCall(nbc, data, user, duel, you, opp);
                    break;
                case "Admit defeat":
                    admitDefeat(nbc, data, user, duel, you, opp);
                    break;
                case "Quit duel":
                    quitDuel(nbc, data, user, duel, you, opp);
                    break;
                case "Offer draw":
                    offerDrawE(nbc, data, user, duel, you, opp);
                    break;
                case "Revoke draw":
                    revokeDrawE(nbc, data, user, duel, you, opp);
                    break;
                case "Accept draw":
                    acceptDrawE(nbc, data, user, duel, you, opp);
                    break;
                case "Offer rematch":
                    offerRematchE(nbc, data, user, duel, you, opp);
                    break;
                case "Revoke rematch":
                    revokeRematchE(nbc, data, user, duel, you, opp);
                    break;
                case "Accept rematch":
                    acceptRematchE(nbc, data, user, duel, you, opp);
                    break;
                case "Done siding":
                    doneSiding(nbc, data, user, duel, you, opp);
                    break;
            }
            if (opp.ready == false) {
                return;
            }
            switch((String) data.get("play")) {
                case "Pick first":
                    pickFirst(nbc, data, user, duel, you, opp);
                    break;
                case "Target":
                    targetCard(nbc, data, user, duel, you, opp);
                    break;
                case "Stop viewing":
                    stopViewing(nbc, data, user, duel, you, opp);
                    break;
            }
            if (duel.paused == true) {
                return;
            }
            if (data.has("granted")) {
                permissionGranted(nbc, data, user, duel, you, opp);
            }
            switch((String) data.get("play")) {
                case "Draw card":
                    drawCard(nbc, data, user, duel, you, opp);
                    break;
                case "Mill":
                    millCard(nbc, data, user, duel, you, opp);
                    break;
                case "Mill difference":
                    data.put("amount", you.main_arr.size() - opp.main_arr.size());
                    millCard(nbc, data, user, duel, you, opp);
                    break;
                case "Shuffle deck":
                    shuffleDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Shuffle hand":
                    shuffleHand(nbc, data, user, duel, you, opp);
                    break;
                case "Banish top card of deck":
                    banishTopCardOfDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Banish top card of deck FD":
                    banishTopCardOfDeckFD(nbc, data, user, duel, you, opp);
                    break;
                case "Banish top 10 cards of deck FD":
                    data.put("amount", 10);
                    banishTopCardOfDeckFD(nbc, data, user, duel, you, opp);
                    break;
                case "Banish 3 cards from top of deck":
                    data.put("amount", 3);
                    banishTopCardOfDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Reveal from hand":
                    revealFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Summon token":
                    summonToken(nbc, data, user, duel, you, opp);
                    break;
                case "View deck":
                    viewDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Show deck":
                    showDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Show hand":
                    showHand(nbc, data, user, duel, you, opp);
                    break;
                case "To hand from deck":
                    toHandFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "To grave from deck":
                    toGraveFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from deck":
                    banishFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Banish FD from deck":
                    banishFDFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "To ST from deck":
                    toSTFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "To ST from hand":
                    toSTFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Normal Summon":
                    normalSummon(nbc, data, user, duel, you, opp);
                    break;
                case "Set monster from hand":
                    setMonsterFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Spell from hand":
                    activateSpellFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Set ST from hand":
                    setSTFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "To grave from hand":
                    toGraveFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from hand":
                    banishFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from hand FD":
                    banishFromHandFD(nbc, data, user, duel, you, opp);
                    break;
                case "Set monster to ST from hand":
                    setMonsterToSTFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Activate ST":
                    activateST(nbc, data, user, duel, you, opp);
                    break;
                case "Set ST":
                    setST(nbc, data, user, duel, you, opp);
                    break;
                case "Turn face-down":
                    turnFaceDown(nbc, data, user, duel, you, opp);
                    break;
                case "Set monster":
                    setMonster(nbc, data, user, duel, you, opp);
                    break;
                case "Flip monster":
                    flipMonster(nbc, data, user, duel, you, opp);
                    break;
                case "Flip Summon":
                    flipSummon(nbc, data, user, duel, you, opp);
                    break;
                case "To ATK":
                    toATK(nbc, data, user, duel, you, opp);
                    break;
                case "To DEF":
                    toDEF(nbc, data, user, duel, you, opp);
                    break;
                case "To grave from field":
                    toGraveFromField(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from field":
                    banishFromField(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from field FD":
                    banishFromFieldFD(nbc, data, user, duel, you, opp);
                    break;
                case "Remove Token":
                    removeToken(nbc, data, user, duel, you, opp);
                    break;
                case "Change control":
                    changeControl(nbc, data, user, duel, you, opp);
                    break;
                case "To hand from field":
                    toHandFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To opposing hand from field":
                    toOpposingHandFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck from field":
                    toTopOfDeckFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To bottom of deck from field":
                    toBottomOfDeckFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck FU from field":
                    toTopOfDeckFUFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To top of opponent's deck from field":
                    toTopOfOpponentDeckFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To top of opponent's deck from opponent's deck":
                    toTopOfOpponentDeckFromOpponentDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Turn top card of deck face-up":
                    turnTopCardOfDeckFU(nbc, data, user, duel, you, opp);
                    break;
                case "Flip deck":
                    flipDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Flip deck back":
                    flipDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Spyral event":
                    spyralEvent(nbc, data, user, duel, you, opp);
                    break;
                case "Cynet Storm":
                    cynetStorm(nbc, data, user, duel, you, opp);
                    break;
                case "Random extra event":
                    randomExtraEvent(nbc, data, user, duel, you, opp);
                    break;
                case "Show top 3 cards":
                    showTop3Cards(nbc, data, user, duel, you, opp);
                    break;
                case "Exchange event":
                    exchangeEvent(nbc, data, user, duel, you, opp);
                    break;
                case "Exchange":
                    exchange(nbc, data, user, duel, you, opp);
                    break;
                case "Permission denied":
                    permissionDenied(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Pendulum Right from hand":
                    activatePendulumRightFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Pendulum Left from hand":
                    activatePendulumLeftFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Pendulum Right from field":
                    activatePendulumRightFromField(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Pendulum Left from field":
                    activatePendulumLeftFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra from field":
                    toExtraFromField(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra FU from field":
                    toExtraFUFromField(nbc, data, user, duel, you, opp);
                    break;
                case "Move":
                    move(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Field Spell from hand":
                    activateFieldSpellFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Set Field Spell from hand":
                    setFieldSpellFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "Set Field Spell from deck":
                    setFieldSpellFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "Set Field Spell from deck to opponent's field":
                    setFieldSpellFromDeckToOpponent(nbc, data, user, duel, you, opp);
                    break;
                case "Activate Field Spell":
                    activateFieldSpell(nbc, data, user, duel, you, opp);
                    break;
                case "Set Field Spell":
                    setFieldSpell(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from hand":
                    ssATKFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from hand":
                    ssDEFFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck from hand":
                    toTopOfDeckFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "To bottom of deck from hand":
                    toBottomOfDeckFromHand(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck from Extra":
                    toTopOfDeckFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Overlay":
                    overlay(nbc, data, user, duel, you, opp);
                    break;
                case "Detach":
                    detach(nbc, data, user, duel, you, opp);
                    break;
                case "Attack":
                    attack(nbc, data, user, duel, you, opp);
                    break;
                case "Attack directly":
                    attackDirectly(nbc, data, user, duel, you, opp);
                    break;
                case "Banish Xyz Material":
                    banishXyzMaterial(nbc, data, user, duel, you, opp);
                    break;
                case "View Extra Deck":
                    viewExtraDeck(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from deck":
                    ssATKFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from deck":
                    ssDEFFromDeck(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from Extra":
                    ssATKFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from Extra":
                    ssDEFFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "OL ATK from Extra":
                    olATKFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "OL DEF from Extra":
                    olDEFFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "To Grave from Extra":
                    toGraveFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from Extra":
                    banishFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from Extra FD":
                    banishFromExtraFD(nbc, data, user, duel, you, opp);
                    break;
                case "Banish random card from extra":
                    banishRandomCardFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Reveal from Extra":
                    revealFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Show Extra Deck":
                    showExtraDeck(nbc, data, user, duel, you, opp);
                    break;
                //case "View Host's Public Extra Deck":
                //    viewHostExtraDeck(nbc, data, user, duel, you, opp);
                //    break;
                case "View Opponent's Public Extra Deck":
                    viewOpponentExtraDeck(nbc, data, user, duel, you, opp);
                    break;
                case "View Graveyard":
                    viewGraveyard(nbc, data, user, duel, you, opp);
                    break;
                case "View Banished":
                    viewBanished(nbc, data, user, duel, you, opp);
                    break;
                case "View Opponent's Graveyard":
                    viewOpponentGraveyard(nbc, data, user, duel, you, opp);
                    break;
                case "View Opponent's Banished":
                    viewOpponentBanished(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from grave":
                    ssATKFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from grave":
                    ssDEFFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra from grave":
                    toExtraFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra FU from grave":
                    toExtraFUFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra from banished":
                    toExtraFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To Extra FU from banished":
                    toExtraFUFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To hand from grave":
                    toHandFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To hand from Extra":
                    toHandFromExtra(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from grave":
                    banishFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from grave FD":
                    banishFromGraveFD(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck from grave":
                    toTopOfDeckFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To bottom of deck from grave":
                    toBottomOfDeckFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "To ST from grave":
                    toSTFromGrave(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from banished":
                    ssATKFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from banished":
                    ssDEFFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To hand from banished":
                    toHandFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To grave from banished":
                    toGraveFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To top of deck from banished":
                    toTopOfDeckFromBanished(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from opponent's grave":
                    ssATKFromOpponentGrave(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from opponent's grave":
                    ssDEFFromOpponentGrave(nbc, data, user, duel, you, opp);
                    break;
                case "Banish from opponent's grave":
                    banishFromOpponentGrave(nbc, data, user, duel, you, opp);
                    break;
                case "SS ATK from opponent's banished":
                    ssATKFromOpponentBanished(nbc, data, user, duel, you, opp);
                    break;
                case "SS DEF from opponent's banished":
                    ssDEFFromOpponentBanished(nbc, data, user, duel, you, opp);
                    break;
                case "To grave from opponent's banished":
                    toGraveFromOpponentBanished(nbc, data, user, duel, you, opp);
                    break;
                case "Life points":
                    updateLifePoints(nbc, data, user, duel, you, opp);
                    break;
                case "Enter DP":
                    enterDP(nbc, data, user, duel, you, opp);
                    break;
                case "Enter SP":
                    enterSP(nbc, data, user, duel, you, opp);
                    break;
                case "Enter M1":
                    enterM1(nbc, data, user, duel, you, opp);
                    break;
                case "Enter BP":
                    enterBP(nbc, data, user, duel, you, opp);
                    break;
                case "Enter M2":
                    enterM2(nbc, data, user, duel, you, opp);
                    break;
                case "Enter EP":
                    enterEP(nbc, data, user, duel, you, opp);
                    break;
                case "End turn":
                    endTurn(nbc, data, user, duel, you, opp);
                    break;
                case "Start turn":
                    startTurn(nbc, data, user, duel, you, opp);
                    break;
                case "Add counter":
                    addCounter(nbc, data, user, duel, you, opp);
                    break;
                case "Remove counter":
                    removeCounter(nbc, data, user, duel, you, opp);
                    break;
                case "Die":
                    dieRoll(nbc, data, user, duel, you, opp);
                    break;
                case "Coin":
                    coinFlip(nbc, data, user, duel, you, opp);
                    break;
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void chooseRPS(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.status.equals("RPS")) {
                return;
            }
            String choice = (String) data.get("choice");
            String oppChoice = opp.rpsChoice;
            String winnerUsername = "";
            String winnerChoice = "";
            String loserChoice = "";
            Boolean tie = false;
            you.rpsChoice = choice;
            you.resetAFKTimer();
            JSONObject result;
            if (!oppChoice.equals("")) {
                if (choice.equals("Rock")) {
                    if (oppChoice.equals("Paper")) {
                        duel.rpsWinner = opp.username;
                    }
                    else if (oppChoice.equals("Scissors")) {
                        duel.rpsWinner = you.username;
                    }
                    else {
                        tie = true;
                    }
                }   
                else if (choice.equals("Paper")) {
                    if (oppChoice.equals("Scissors")) {
                        duel.rpsWinner = opp.username;
                    }
                    else if (oppChoice.equals("Rock")) {
                        duel.rpsWinner = you.username;
                    }
                    else {
                        tie = true;
                    }
                }
                else {
                    if (oppChoice.equals("Rock")) {
                        duel.rpsWinner = opp.username;
                    }
                    else if (oppChoice.equals("Paper")) {
                        duel.rpsWinner = you.username;
                    }
                    else {
                        tie = true;
                    }
                }
                if (tie) {
                    result = new JSONObject();
                    result.put("action", "Duel");
                    result.put("seconds", duel.seconds);
                    result.put("play", "RPS");
                    result.put("winner", "");
                    result.put("player1", duel.player1.username);
                    result.put("player2", duel.player2.username);
                    result.put("player1_choice", duel.player1.rpsChoice);
                    result.put("player2_choice", duel.player2.rpsChoice);
                    duel.player1.startAFKTimer();
                    duel.player2.startAFKTimer();
                }
                else {
                    you.ready = false;
                    opp.ready = false;
                    duel.status = "Pick first";
                    String entry = "Won Rock-Paper-Scissors";
                    JSONObject log = new JSONObject();
                    //log.put("timestamp", duel.timestamp);
                    log.put("username", duel.rpsWinner);
                    log.put("type", "game");
                    log.put("public_log", entry);
                    //log.put("private_log", entry);
                    duel.addLog(log);
                    
                    result = new JSONObject();
                    result.put("action", "Duel");
                    result.put("seconds", duel.seconds);
                    result.put("play", "RPS");
                    result.put("winner", duel.rpsWinner);
                    result.put("player1", duel.player1.username);
                    result.put("player2", duel.player2.username);
                    result.put("player1_choice", duel.player1.rpsChoice);
                    result.put("player2_choice", duel.player2.rpsChoice);
                    result.put("log", log);
                    if (duel.rpsWinner.equals(duel.player1.username)) {
                        duel.player1.startAFKTimer();
                    }
                    else if (duel.rpsWinner.equals(duel.player2.username)) {
                        duel.player2.startAFKTimer();
                    }
                }
                if (you.active == true) {
                    write(you.nbc, result);
                }
                if (opp.active == true) {
                    write(opp.nbc, result);
                }
                for (int i = 0; i < duel.watchers.size(); i++) {
                    write(duel.watchers.get(i).nbc, result);
                }
                you.rpsChoice = "";
                opp.rpsChoice = "";
                duel.addReplay(result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void watchDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            user.greeted = false;
            exitDuel(nbc, data, user);
            exitDuelRoom(nbc, data, user);
            Duel duel = null;
            int id = (int) data.get("id");
            if (data.has("username")) {
                String username = (String) data.get("username");
                for (int i = 0; i < Duels.size(); i++) {
                    if (Duels.get(i) == null) {
                        System.out.println("Duel is null on line 17846");
                        continue;
                    }
                    if (Duels.get(i).id == id) {
                        duel = Duels.get(i);
                        if (!duel.player1.username.equals(username) && !duel.player2.username.equals(username) && !duel.watchers.contains(username)) {
                            messageE(nbc, "Duel is no longer available!");
                            return;
                        }
                        break;
                    }
                }
            }
            if (data.has("password")) {
                user.watch_password = (String) data.get("password");
            }
            JSONObject result = new JSONObject();
            for (int i = 0; i < Duels.size(); i++) {
                if (Duels.get(i) == null) {
                    System.out.println("Duel is null on line 17883");
                    continue;
                }
                if (Duels.get(i).id == id) {
                    duel = Duels.get(i);
                    if (user.adjudicator > 0) {
                        if (duel.rated == true || duel.player1.user.nbc_address.equals(user.nbc_address) || duel.player2.user.nbc_address.equals(user.nbc_address)) {
                            errorE(nbc, "You cannot watch this duel");
                            return;
                        }
                    }
                    if (!duel.watch_password.equals("")) {
                        if (!user.watch_password.toLowerCase().equals(duel.watch_password.toLowerCase()) && user.admin < 1 && user.moderator < 2) {
                            errorE(nbc, "Incorrect watch password");
                            return;
                        }
                    }
                    if (duel.watching == false && user.admin < 1 && user.moderator < 2) {
                        errorE(nbc, "Watching is disabled for this game");
                        return;
                    }
                    Player player1 = duel.player1;
                    Player player2 = duel.player2;

                    JSONObject player_1 = new JSONObject();
                    player_1.put("username", duel.player1.username);
                    player_1.put("pic", duel.player1.pic);
                    player_1.put("rating", duel.player1.rating);
                    player_1.put("experience", duel.player1.experience);
                    player_1.put("nsfw", duel.player1.nsfw);
                    player_1.put("sleeve", duel.player1.sleeve);
                    player_1.put("lifepoints", duel.player1.lifepoints);
                    player_1.put("token", duel.player1.token);
                    player_1.put("main_total", duel.player1.main.size());
                    player_1.put("extra_total", duel.player1.extra.size());
                    player_1.put("deck_face_up", duel.player1.deck_face_up);
                    player_1.put("viewing", duel.player1.viewing);
                    player_1.put("done_siding", duel.player1.done_siding);
                    player_1.put("start", 101);
                    
                    JSONObject player_2 = new JSONObject();
                    player_2.put("username", duel.player2.username);
                    player_2.put("pic", duel.player2.pic);
                    player_2.put("rating", duel.player2.rating);
                    player_2.put("experience", duel.player2.experience);
                    player_2.put("nsfw", duel.player2.nsfw);
                    player_2.put("sleeve", duel.player2.sleeve);
                    player_2.put("lifepoints", duel.player2.lifepoints);
                    player_2.put("token", duel.player2.token);
                    player_2.put("main_total", duel.player2.main.size());
                    player_2.put("extra_total", duel.player2.extra.size());
                    player_2.put("deck_face_up", duel.player2.deck_face_up);
                    player_2.put("viewing", duel.player2.viewing);
                    player_2.put("done_siding", duel.player2.done_siding);
                    player_2.put("start", 201);
                    
                    ArrayList<JSONObject> main_arr = new ArrayList<JSONObject>();
                    ArrayList<JSONObject> grave_arr = new ArrayList<JSONObject>();
                    ArrayList<JSONObject> banished_arr = new ArrayList<JSONObject>();
                    ArrayList<JSONObject> extra_arr = new ArrayList<JSONObject>();
                    ArrayList<JSONObject> hand_arr = new ArrayList<JSONObject>();
                    ArrayList<JSONObject> field_arr = new ArrayList<JSONObject>();
                    JSONObject m1 = new JSONObject();
                    JSONObject m2 = new JSONObject();
                    JSONObject m3 = new JSONObject();
                    JSONObject m4 = new JSONObject();
                    JSONObject m5 = new JSONObject();
                    JSONObject s1 = new JSONObject();
                    JSONObject s2 = new JSONObject();
                    JSONObject s3 = new JSONObject();
                    JSONObject s4 = new JSONObject();
                    JSONObject s5 = new JSONObject();
                    JSONObject leftPendulum = new JSONObject();
                    JSONObject rightPendulum = new JSONObject();
                    JSONObject fieldSpell = new JSONObject();
                    field_arr = new ArrayList<JSONObject>();
                    field_arr.add(m1);
                    field_arr.add(m2);
                    field_arr.add(m3);
                    field_arr.add(m4);
                    field_arr.add(m5);
                    field_arr.add(s1);
                    field_arr.add(s2);
                    field_arr.add(s3);
                    field_arr.add(s4);
                    field_arr.add(s5);
                    field_arr.add(leftPendulum);
                    field_arr.add(rightPendulum);
                    field_arr.add(fieldSpell);
                    ArrayList<Card> player1_field_arr = new ArrayList<Card>();
                    player1_field_arr.add(player1.m1);
                    player1_field_arr.add(player1.m2);
                    player1_field_arr.add(player1.m3);
                    player1_field_arr.add(player1.m4);
                    player1_field_arr.add(player1.m5);
                    player1_field_arr.add(player1.s1);
                    player1_field_arr.add(player1.s2);
                    player1_field_arr.add(player1.s3);
                    player1_field_arr.add(player1.s4);
                    player1_field_arr.add(player1.s5);
                    player1_field_arr.add(player1.pendulumLeft);
                    player1_field_arr.add(player1.pendulumRight);
                    player1_field_arr.add(player1.fieldSpell);
                    player1_field_arr.add(player1.linkLeft);
                    player1_field_arr.add(player1.linkRight);
                    getPlayerCards(player1, player_1, main_arr, "main", player1.main_arr);
                    getPlayerCards(player1, player_1, grave_arr, "grave", player1.grave_arr);
                    getPlayerCards(player1, player_1, banished_arr, "banished", player1.banished_arr);
                    getPlayerCards(player1, player_1, extra_arr, "extra", player1.extra_arr);
                    getPlayerCards(player1, player_1, hand_arr, "hand", player1.hand_arr);
                    getPlayerCards(player1, player_1, field_arr, "field", player1_field_arr);
                    
                    main_arr = new ArrayList<JSONObject>();
                    grave_arr = new ArrayList<JSONObject>();
                    banished_arr = new ArrayList<JSONObject>();
                    extra_arr = new ArrayList<JSONObject>();
                    hand_arr = new ArrayList<JSONObject>();
                    field_arr = new ArrayList<JSONObject>();
                    m1 = new JSONObject();
                    m2 = new JSONObject();
                    m3 = new JSONObject();
                    m4 = new JSONObject();
                    m5 = new JSONObject();
                    s1 = new JSONObject();
                    s2 = new JSONObject();
                    s3 = new JSONObject();
                    s4 = new JSONObject();
                    s5 = new JSONObject();
                    leftPendulum = new JSONObject();
                    rightPendulum = new JSONObject();
                    fieldSpell = new JSONObject();
                    field_arr = new ArrayList<JSONObject>();
                    field_arr.add(m1);
                    field_arr.add(m2);
                    field_arr.add(m3);
                    field_arr.add(m4);
                    field_arr.add(m5);
                    field_arr.add(s1);
                    field_arr.add(s2);
                    field_arr.add(s3);
                    field_arr.add(s4);
                    field_arr.add(s5);
                    field_arr.add(leftPendulum);
                    field_arr.add(rightPendulum);
                    field_arr.add(fieldSpell);
                    ArrayList<Card> player2_field_arr = new ArrayList<Card>();
                    player2_field_arr.add(player2.m1);
                    player2_field_arr.add(player2.m2);
                    player2_field_arr.add(player2.m3);
                    player2_field_arr.add(player2.m4);
                    player2_field_arr.add(player2.m5);
                    player2_field_arr.add(player2.s1);
                    player2_field_arr.add(player2.s2);
                    player2_field_arr.add(player2.s3);
                    player2_field_arr.add(player2.s4);
                    player2_field_arr.add(player2.s5);
                    player2_field_arr.add(player2.pendulumLeft);
                    player2_field_arr.add(player2.pendulumRight);
                    player2_field_arr.add(player2.fieldSpell);
                    player2_field_arr.add(player2.linkLeft);
                    player2_field_arr.add(player2.linkRight);
                    getPlayerCards(player2, player_2, main_arr, "main", player2.main_arr);
                    getPlayerCards(player2, player_2, grave_arr, "grave", player2.grave_arr);
                    getPlayerCards(player2, player_2, banished_arr, "banished", player2.banished_arr);
                    getPlayerCards(player2, player_2, extra_arr, "extra", player2.extra_arr);
                    getPlayerCards(player2, player_2, hand_arr, "hand", player2.hand_arr);
                    getPlayerCards(player2, player_2, field_arr, "field", player2_field_arr);

                    result.put("action", "Duel");
                    result.put("play", "Add watcher");
                    result.put("seconds", duel.seconds);
                    result.put("username", user.username);
                    result.put("admin", user.admin);
                    result.put("adjudicator", user.adjudicator);
                    result.put("id", user.id);
                    if (user.donator == 2) {
                        result.put("donator", user.donator);
                    }
                    if (duel.player1.active == true) {
                        write(duel.player1.nbc, result);
                    }
                    if (duel.player1.active == true) {
                        write(duel.player2.nbc, result);
                    }
                    for (int j = 0; j < duel.watchers.size(); j++) {
                        write(duel.watchers.get(j).nbc, result);
                    }
                    duel.addReplay(result);
                    Watcher watcher = newWatcher();
                    watcher.nbc = nbc;
                    watcher.username = user.username;
                    watcher.user_id = user.id;
                    watcher.admin = user.admin;
                    watcher.adjudicator = user.adjudicator;
                    watcher.donator = user.donator;
                    if (isWatching(duel, user.username) == false) {
                        duel.watchers.add(watcher);
                    }
                    System.out.println("in the duel " + user.username + " just entered, duel.watchers.size() is now " + duel.watchers.size());
                    
                    ArrayList<JSONObject> watchers = new ArrayList<JSONObject>();
                    for (int j = 0; j < duel.watchers.size(); j++) {
                        JSONObject watcher2 = new JSONObject();
                        watcher2.put("username", duel.watchers.get(j).username);
                        watcher2.put("id", duel.watchers.get(j).user_id);
                        watcher2.put("admin", duel.watchers.get(j).admin);
                        watcher2.put("adjudicator", duel.watchers.get(j).adjudicator);
                        if (duel.watchers.get(j).donator == 2) {
                            watcher2.put("donator", duel.watchers.get(j).donator);
                        }
                        watchers.add(watcher2);
                    }
                    result = new JSONObject();
                    result.put("action", "Watch duel");
                    result.put("player1", player_1);
                    result.put("player2", player_2);
                    result.put("status", duel.status);
                    result.put("turn_player", duel.turn_player);
                    result.put("phase", duel.currentPhase);
                    result.put("paused", duel.paused);
                    result.put("watchers", watchers);
                    result.put("rated", duel.rated);
                    result.put("links", duel.links);
                    result.put("links_chosen", duel.links);
                    result.put("id", duel.id);
                    result.put("type", duel.type);
                    result.put("format", duel.format);
                    if (user.admin > 0 || user.adjudicator > 0) {
                        ArrayList<JSONObject> log = new ArrayList<JSONObject>();
                        for (int j = 0; j < duel.log.size(); j++) {
                            log.add(duel.log.get(j));
                        }
                        result.put("log", log);
                    }
                    user.duel = duel;
                    user.duel_id = duel.id;
                    user.last_duel_id = duel.id;
                    write(nbc, result);
                    break;
                }
            }
            if (duel == null) {
                errorE(nbc, "Duel no longer exists");
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            errorE(nbc, e.toString());
        }
    }
    
    public static Boolean isWatching(Duel duel, String username) {
        for (int i = 0; i < duel.watchers.size(); i++) {
            if (duel.watchers.get(i).username.equals(username)) {
                return true;
            }
        }
        return false;
    }
               
    public static void getPlayerCards(Player player, JSONObject json_player, ArrayList<JSONObject> json_arr, String name, ArrayList<Card> player_arr) {
        json_arr = new ArrayList<JSONObject>();
        for (int i = 0; i < player_arr.size(); i++) {
            if (player_arr.get(i) != null) {
                JSONObject card = new JSONObject();
                card.put("id", player_arr.get(i).id);
                card.put("face_up", player_arr.get(i).face_up);
                card.put("face_down", player_arr.get(i).face_down);
                card.put("inATK", player_arr.get(i).inATK);
                card.put("inDEF", player_arr.get(i).inDEF);
                card.put("counters", player_arr.get(i).counters);
                card.put("controller", player_arr.get(i).controller.username);
                card.put("owner", player_arr.get(i).owner.username);
                ArrayList<JSONObject> xyz_arr = new ArrayList<JSONObject>();
                for (int j = 0; j < player_arr.get(i).xyz_arr.size(); j++) {
                    JSONObject material = new JSONObject();
                    material.put("id", player_arr.get(i).xyz_arr.get(j).id);
                    material.put("face_up", player_arr.get(i).xyz_arr.get(j).face_up);
                    material.put("face_down", player_arr.get(i).xyz_arr.get(j).face_down);
                    material.put("inATK", player_arr.get(i).xyz_arr.get(j).inATK);
                    material.put("inDEF", player_arr.get(i).xyz_arr.get(j).inDEF);
                    material.put("counters", player_arr.get(i).xyz_arr.get(j).counters);
                    material.put("controller", player_arr.get(i).xyz_arr.get(j).controller.username);
                    material.put("owner", player_arr.get(i).xyz_arr.get(j).owner.username);
                    material.put("xyz_arr", new ArrayList<JSONObject>());
                    material.put("data", player_arr.get(i).xyz_arr.get(j).data);
                    xyz_arr.add(material);
                }
                card.put("xyz_arr", xyz_arr);
                if (player_arr.equals(player.main_arr)) {
                    if (player_arr.get(i).face_up == true || player.deck_face_up == true) {
                        card.put("data", player_arr.get(i).data);
                    }
                }
                else if (player_arr.equals(player.grave_arr)) {
                    card.put("data", player_arr.get(i).data);
                }
                else if (player_arr.equals(player.hand_arr)) {
                    //
                }
                else if (player_arr.get(i).face_down == false) {
                    card.put("data", player_arr.get(i).data);
                }
                json_arr.add(card);
            }
            else {
                json_arr.add(null);
            }
        }
        json_player.put(name, json_arr);
    }
    
    public static void exitDuel(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.duel == null) {
                return;
            }
            if (user.duel_id == 0) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Remove watcher");
            result.put("seconds", user.duel.seconds);
            result.put("username", user.username);
            result.put("admin", user.admin);
            Boolean unpause = true;
            if (user.duel.player1.username.equals(user.username)) {
                user.duel.player1.active = false;
                duelInactive(user.duel);
            }
            else if (user.duel.player2.username.equals(user.username)) {
                user.duel.player2.active = false;
                duelInactive(user.duel);
            }
            else {
                for (int i = 0; i < user.duel.watchers.size(); i++) {
                    if (user.duel.watchers.get(i).username.equals(user.username)) {
                        recycleWatcher(user.duel.watchers.remove(i));
                        i--;
                    }
                }
            }
            if (user.duel.player1.active == true) {
                write(user.duel.player1.nbc, result);
            }
            if (user.duel.player2.active == true) {
                write(user.duel.player2.nbc, result);
            }
            for (int i = 0; i < user.duel.watchers.size(); i++) {
                write(user.duel.watchers.get(i).nbc, result);
                if (user.duel.watchers.get(i).admin > 0) {
                    unpause = false;
                }
            }
            user.duel.addReplay(result);
            if (unpause == true) {
                user.duel.paused = false;
            }
            if (user.duel.player1.active == false && user.duel.player2.active == false && user.duel.watchers.size() == 0) {
                recycleDuel(user.duel);
            }
            user.duel_id = 0;
            user.duel = null;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
            
    public static void playerReadyE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        you.ready = true;
        you.status = (String) data.get("status");
    }
    
    public static Card newCard(int id, Player player) {
        Card card = new Card();
        card.owner = player;
        card.controller = player;
        player.all_cards_arr.add(card);
        
        card.card_id = Cards.get(id).card_id;
        card.name = Cards.get(id).name;
        card.treated_as = Cards.get(id).treated_as;
        card.effect = Cards.get(id).effect;
        card.pendulum_effect = Cards.get(id).pendulum_effect;
        card.card_type = Cards.get(id).card_type;
        card.monster_color = Cards.get(id).monster_color;
        card.is_effect = Cards.get(id).is_effect;
        card.type = Cards.get(id).type;
        card.attribute = Cards.get(id).attribute;
        card.level = Cards.get(id).level;
        card.ability = Cards.get(id).ability;
        card.flip = Cards.get(id).flip;
        card.pendulum = Cards.get(id).pendulum;
        card.scale_left = Cards.get(id).scale_left;
        card.scale_right = Cards.get(id).scale_right;
        card.arrows = Cards.get(id).arrows;
        card.atk = Cards.get(id).atk;
        card.def = Cards.get(id).def;
        card.tcg_limit = Cards.get(id).tcg_limit;
        card.ocg_limit = Cards.get(id).ocg_limit;
        card.serial_number = Cards.get(id).serial_number;
        card.tcg = Cards.get(id).tcg;
        card.ocg = Cards.get(id).ocg;
        card.pic = Cards.get(id).pic;
        
        card.data.put("id", card.card_id);
        card.data.put("name", card.name);
        card.data.put("treated_as", card.treated_as);
        card.data.put("effect", card.effect);
        card.data.put("pendulum_effect", card.pendulum_effect);
        card.data.put("card_type", card.card_type);
        card.data.put("monster_color", card.monster_color);
        card.data.put("is_effect", card.is_effect);
        card.data.put("type", card.type);
        card.data.put("attribute", card.attribute);
        card.data.put("level", card.level);
        card.data.put("ability", card.ability);
        card.data.put("flip", card.flip);
        card.data.put("pendulum", card.pendulum);
        card.data.put("scale_left", card.scale_left);
        card.data.put("scale_right", card.scale_right);
        card.data.put("arrows", card.arrows);
        card.data.put("atk", card.atk);
        card.data.put("def", card.def);
        card.data.put("tcg_limit", card.tcg_limit);
        card.data.put("ocg_limit", card.ocg_limit);
        card.data.put("serial_number", card.serial_number);
        card.data.put("tcg", card.tcg);
        card.data.put("ocg", card.ocg);
        card.data.put("pic", card.pic);
        
        return card;
    }
    
    public static void pickFirst(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.ready == false) {
                return;
            }
            if (!duel.status.equals("Pick first")) {
                System.out.println("Returning becaues duel.status = " + duel.status);
                return;
            }
            System.out.println("pickFirst entered");
            you.resetAFKTimer();
            String word = "";
            duel.status = "Dueling";
            you.ready = false;
            opp.ready = false;
            
            if (!duel.rpsWinner.equals(user.username)) {
                System.out.println("5772");
                return;
            }
            String choice = (String) data.get("choice");
            String firstPlayer = "";
            if (choice.equals("First")) {
                firstPlayer = you.username;
                you.startAFKTimer();
                word = "first";
            }
            else {
                firstPlayer = opp.username;
                opp.startAFKTimer();
                word = "second";
            }
            
            duel.currentPhase = "DP";
            duel.turn_player = firstPlayer;
            
            ArrayList<JSONObject> player1_cards = new ArrayList<JSONObject>();
            ArrayList<JSONObject> player1_log = new ArrayList<JSONObject>();
            ArrayList<JSONObject> player2_log = new ArrayList<JSONObject>();
            
            ArrayList<JSONObject> private_log = new ArrayList<JSONObject>();
            ArrayList<JSONObject> private_cards = new ArrayList<JSONObject>();
            
            String public_entry = "Chose to go " + word;
            String private_entry = "Chose to go " + word;
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            log.put("private_log", private_entry);
            
            if (duel.replay_arr.get(duel.replay_arr.size() - 1).get("play").equals("Pick first")) {
                InfoLog += "Line 19814 entered in duel #" + duel.id + "\n";
                return; // added 7/28
            }
            
            duel.addLog(log);
            player1_log.add(log);
            player2_log.add(log);
            private_log.add(log);

            int num = 5;
            if (duel.player1.main_arr.size() < 5) {
                num = duel.player1.main_arr.size();
            }
            for (int i = 0; i < num; i++) {
                JSONObject card = duel.player1.main_arr.get(0).data;
                duel.player1.hand_arr.add(duel.player1.main_arr.get(0));
                duel.player1.main_arr.remove(0);
                player1_cards.add(card);
                private_cards.add(card);
                        
                JSONObject entry = new JSONObject();
                    //entry.put("timestamp", duel.timestamp);
                entry.put("username", duel.player1.username);
                entry.put("type", "duel");
                entry.put("public_log", "Drew a card");
                entry.put("private_log", "Drew \"" + card.get("name") + "\"");
                player1_log.add(entry);
                private_log.add(entry);
                //duel.log.add(entry);
                duel.addLog(entry);
                
                JSONObject entry2 = new JSONObject();
                    //entry2.put("timestamp", duel.timestamp);
                entry2.put("username", duel.player1.username);
                entry2.put("type", "duel");
                entry2.put("public_log", "Drew a card");
                //entry2.put("private_log", "Drew a card");
                player2_log.add(entry2);
            }
            ArrayList<JSONObject> player2_cards = new ArrayList<JSONObject>();
           num = 5;
            if (duel.player2.main_arr.size() < 5) {
                num = duel.player2.main_arr.size();
            }
            for (int i = 0; i < num; i++) {
                JSONObject card = duel.player2.main_arr.get(0).data;
                duel.player2.hand_arr.add(duel.player2.main_arr.get(0));
                duel.player2.main_arr.remove(0);
                player2_cards.add(card);
                private_cards.add(card);
                
                JSONObject entry = new JSONObject();
                    //entry.put("timestamp", duel.timestamp);
                entry.put("username", duel.player2.username);
                entry.put("type", "duel");
                entry.put("public_log", "Drew a card");
                //entry.put("private_log", "Drew a card");
                player1_log.add(entry);
                
                JSONObject entry2 = new JSONObject();
                    //entry2.put("timestamp", duel.timestamp);
                entry2.put("username", duel.player2.username);
                entry2.put("type", "duel");
                entry2.put("public_log", "Drew a card");
                entry2.put("private_log", "Drew \"" + card.get("name") + "\"");
                player2_log.add(entry2);
                private_log.add(entry2);
                //duel.log.add(entry2);
                duel.addLog(entry2);
            }
            
            System.out.println("player1_log = " + player1_log);

            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Pick first");
            result.put("username", firstPlayer);
            result.put("cards", player1_cards);
            result.put("log", player1_log);
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Pick first");
            result.put("username", firstPlayer);
            result.put("cards", player2_cards);
            result.put("log", player2_log);
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Pick first");
            result.put("username", firstPlayer);
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            result.put("cards", private_cards);
            result.put("log", private_log);
            duel.addReplay(result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void drawCard(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.viewing.equals("Deck") || opp.viewing.equals("Opponent's Deck")) {
                return;
            }
            int amount = 1;
            if (data.has("amount")) {
                amount = (int) data.get("amount");
            }
            if (amount > you.main_arr.size()) {
                amount = you.main_arr.size();
            }
            for (int i = 0; i < amount; i++) {
                Card card = you.main_arr.get(0);
                if (card == null) {
                    return;
                }
                card.face_up = false;
                you.hand_arr.add(card);
                you.main_arr.remove(0);
                String public_entry = "Drew a card";
                String private_entry = "Drew \"" + card.name + "\"";
                JSONObject result = new JSONObject();
                result.put("play", "Draw card");
                result.put("card", card.data);
                privateDuelResult(result, duel, public_entry, private_entry, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void millCard(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.viewing.equals("Deck") || opp.viewing.equals("Opponent's Deck")) {
                return;
            }
            int amount = 1;
            if (data.has("amount")) {
                amount = (int) data.get("amount");
            }
            if (amount > you.main_arr.size()) {
                amount = you.main_arr.size();
            }
            for (int i = 0; i < amount; i++) {
                Card card = you.main_arr.get(0);
                if (card == null) {
                    return;
                }
                card.face_up = false;
                you.main_arr.remove(0);
                card.owner.grave_arr.add(0, card);
                JSONObject result = new JSONObject();
                result.put("play", "Mill");
                result.put("card", card.data);
                String public_entry = "Milled \"" + card.name + "\" from top of deck";
                publicDuelResult(result, duel, public_entry, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishTopCardOfDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.viewing.equals("Deck") || opp.viewing.equals("Opponent's Deck")) {
                return;
            }
            int amount = 1;
            if (data.has("amount")) {
                amount = (int) data.get("amount");
            }
            if (amount > you.main_arr.size()) {
                amount = you.main_arr.size();
            }
            for (int i = 0; i < amount; i++) {
                Card card = you.main_arr.get(0);
                if (card == null) {
                    return;
                }
                card.face_down = false;
                you.main_arr.remove(0);
                card.owner.banished_arr.add(0, card);
                JSONObject result = new JSONObject();
                result.put("play", "Banish top card of deck");
                result.put("card", card.data);
                String public_entry = "Banished \"" + card.name + "\" from top of deck";
                publicDuelResult(result, duel, public_entry, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishTopCardOfDeckFD(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.viewing.equals("Deck") || opp.viewing.equals("Opponent's Deck")) {
                return;
            }
            int amount = 1;
            if (data.has("amount")) {
                amount = (int) data.get("amount");
            }
            if (amount > you.main_arr.size()) {
                amount = you.main_arr.size();
            }
            for (int i = 0; i < amount; i++) {
                Card card = you.main_arr.get(0);
                if (card == null) {
                    return;
                }
                card.face_up = false;
                card.face_down = true;
                you.main_arr.remove(0);
                card.owner.banished_arr.add(0, card);
                JSONObject result = new JSONObject();
                result.put("play", "Banish top card of deck FD");
                result.put("card", card.data);
                String public_entry = "Banished a card from top of deck face-down";
                String private_entry = "Banished \"" + card.name + "\" from top of deck face-down";
                privateDuelResult(result, duel, public_entry, private_entry, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void revealFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            int index = 0;
            Card card = new Card();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                if (you.hand_arr.get(i).id == card_id) {
                    card = you.hand_arr.get(i);
                    index = i;
                    break;
                }
            }
            //if (card == null) {
            if (card.id == 0) {
                return;
            }
            shuffle(you.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                hand_ids.add(you.hand_arr.get(i).id);
            }
            String public_entry = "Revealed \"" + card.name + "\" from hand (" + (index + 1) + "/" + you.hand_arr.size() + ")";
            JSONObject result = new JSONObject();
            result.put("play", "Reveal card from hand");
            result.put("card", card.data);
            result.put("hand", hand_ids);
            result.put("id", card_id);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void shuffle(ArrayList<Card> arr){
        ArrayList<Card> shuffle_arr = new ArrayList<Card>();
        int index = 0;
        int times = 0;
        while (arr.size() > 0) {
            times++;
            index = (int) Math.floor(Math.random() * arr.size());
            shuffle_arr.add(arr.get(index));
            arr.remove(index);
        }
        for (int i = 0; i < shuffle_arr.size(); i++) {
            arr.add(shuffle_arr.get(i));
        }
        if (times > 1000) {
            System.out.println("TIMES = " + times);
        }
        // NEW
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).name.equals(arr.get(i - 1).name)) {
                arr.add(arr.get(i));
                arr.remove(i);
            }
        }
    }
    
    public static void summonToken(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.m1 != null && you.m2 != null && you.m3 != null && you.m4 != null && you.m5 != null) {
                return;
            }
            Card token = newToken(duel, you);
            token.inATK = false;
            token.inDEF = true;
            token.face_down = false;
            String zone = getNextMonsterZone(token, you, data);
            if (zone == null) {
                return;
            }
            System.out.println("6059");
            JSONObject result = new JSONObject();
            result.put("play", "Summon token");
            result.put("id", token.id);
            String public_entry = "Summoned a token in " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void getXyzMaterial(Card card, int card_id) {
        if (card.xyz_arr.size() > 0) {
            for (int i = 0; i < card.xyz_arr.size(); i++) {
                if (card.xyz_arr.get(i).id == card_id) {

                }
            }
        }
    }
    
    public static Object[] getLocation(Player player, int card_id) {
        Card card = new Card();
        String public_location = "";
        String private_location = "";
        String zone = "";
        
        for (int i = 0; i < player.hand_arr.size(); i++) {
            if (player.hand_arr.get(i).id == card_id) {
                card = player.hand_arr.get(i);
                public_location = "card in opponent's hand (" + (i + 1) + "/" + player.hand_arr.size() + ")";
                private_location = "\"" + player.hand_arr.get(i).name + "\" in opponent's hand (" + (i + 1) + "/" + player.hand_arr.size() + ")";
                if (player.opponent.viewing.equals("Opponent's Hand")) {
                    public_location = private_location;
                }
            }
        }
        if (card.id == 0) {
            if (player.m1 != null) {
                if (player.m1.id == card_id) {
                    card = player.m1;
                    zone = "M-1";
                }
                //getXyzMaterial(player.m1, card_id);
            }
            if (player.m2 != null) {
                if (player.m2.id == card_id) {
                    card = player.m2;
                    zone = "M-2";
                }
            }
            if (player.m3 != null) {
                if (player.m3.id == card_id) {
                    card = player.m3;
                    zone = "M-3";
                }
            }
            if (player.m4 != null) {
                if (player.m4.id == card_id) {
                    card = player.m4;
                    zone = "M-4";
                }
            }
            if (player.m5 != null) {
                if (player.m5.id == card_id) {
                    card = player.m5;
                    zone = "M-5";
                }
            }
            if (player.s1 != null) {
                if (player.s1.id == card_id) {
                    card = player.s1;
                    zone = "S-1";
                }
            }
            if (player.s2 != null) {
                if (player.s2.id == card_id) {
                    card = player.s2;
                    zone = "S-2";
                }
            }
            if (player.s3 != null) {
                if (player.s3.id == card_id) {
                    card = player.s3;
                    zone = "S-3";
                }
            }
            if (player.s4 != null) {
                if (player.s4.id == card_id) {
                    card = player.s4;
                    zone = "S-4";
                }
            }
            if (player.s5 != null) {
                if (player.s5.id == card_id) {
                    card = player.s5;
                    zone = "S-5";
                }
            }
            if (player.fieldSpell != null) {
                if (player.fieldSpell.id == card_id) {
                    card = player.fieldSpell;
                    zone = "Field Spell Zone";
                }
            }
            if (player.pendulumLeft != null) {
                if (player.pendulumLeft.id == card_id) {
                    card = player.pendulumLeft;
                    zone = "Left Pendulum Zone";
                }
            }
            if (player.pendulumRight != null) {
                if (player.pendulumRight.id == card_id) {
                    card = player.pendulumRight;
                    zone = "Right Pendulum Zone";
                }
            }
            if (player.duel.linkLeft != null) {
                if (player.duel.linkLeft.id == card_id) {
                    card = player.duel.linkLeft;
                    if (player.equals(player.duel.player1)) {
                        zone = "Left Extra Monster Zone";
                    }
                    else {
                        zone = "Right Extra Monster Zone";
                    }
                }
            }
            if (player.duel.linkRight != null) {
                if (player.duel.linkRight.id == card_id) {
                    card = player.duel.linkRight;
                    if (player.equals(player.duel.player1)) {
                        zone = "Right Extra Monster Zone";
                    }
                    else {
                        zone = "Left Extra Monster Zone";
                    }
                }
            }
            if (card.face_down == true) {
                public_location = "Set card in " + zone;
                private_location = "Set \"" + card.name + "\" in " + zone;
            }
            else {
                public_location = "\"" + card.name + "\" in " + zone;
                private_location = "\"" + card.name + "\" in " + zone;
            }
        }
        if (card.id == 0) {
            card = null;
        }
        Object[] locations = {public_location,  private_location, card};
        return locations;
    }
    
    public static Object[] removeFromField(Player player, int card_id, Boolean keep_stats) {
        //Card card = new Card();
        Card card = null;
        String public_location = "";
        String private_location = "";
        String zone = "";
        String set = "";
        if (player.m1 != null) {
            if (player.m1.id == card_id) {
                card = player.m1;
                zone = "M-1";
                player.m1 = null;
            }
        }
        if (player.m2 != null) {
            if (player.m2.id == card_id) {
                card = player.m2;
                zone = "M-2";
                player.m2 = null;
            }
        }
        if (player.m3 != null) {
            if (player.m3.id == card_id) {
                card = player.m3;
                zone = "M-3";
                player.m3 = null;
            }
        }
        if (player.m4 != null) {
            if (player.m4.id == card_id) {
                card = player.m4;
                zone = "M-4";
                player.m4 = null;
            }
        }
        if (player.m5 != null) {
            if (player.m5.id == card_id) {
                card = player.m5;
                zone = "M-5";
                player.m5 = null;
            }
        }
        if (player.s1 != null) {
            if (player.s1.id == card_id) {
                card = player.s1;
                zone = "S-1";
                player.s1 = null;
            }
        }
        if (player.s2 != null) {
            if (player.s2.id == card_id) {
                card = player.s2;
                zone = "S-2";
                player.s2 = null;
            }
        }
        if (player.s3 != null) {
            if (player.s3.id == card_id) {
                card = player.s3;
                zone = "S-3";
                player.s3 = null;
            }
        }
        if (player.s4 != null) {
            if (player.s4.id == card_id) {
                card = player.s4;
                zone = "S-4";
                player.s4 = null;
            }
        }
        if (player.s5 != null) {
            if (player.s5.id == card_id) {
                card = player.s5;
                zone = "S-5";
                player.s5 = null;
            }
        }
        if (player.fieldSpell != null) {
            if (player.fieldSpell.id == card_id) {
                card = player.fieldSpell;
                zone = "Field Spell Zone";
                player.fieldSpell = null;
            }
        }
        if (player.pendulumLeft != null) {
            if (player.pendulumLeft.id == card_id) {
                card = player.pendulumLeft;
                zone = "Left Pendulum Zone";
                player.pendulumLeft = null;
            }
        }
        if (player.pendulumRight != null) {
            if (player.pendulumRight.id == card_id) {
                card = player.pendulumRight;
                zone = "Right Pendulum Zone";
                player.pendulumRight = null;
            }
        }
        if (player.duel.linkLeft != null) {
            if (player.duel.linkLeft.id == card_id) {
                card = player.duel.linkLeft;
                player.duel.linkLeft = null;
                player.linkLeft = null;
                if (player.equals(player.duel.player1)) {
                    zone = "Left Extra Monster Zone";
                }
                else {
                    zone = "Right Extra Monster Zone";
                }
            }
        }
        if (player.duel.linkRight != null) {
            if (player.duel.linkRight.id == card_id) {
                card = player.duel.linkRight;
                player.duel.linkRight = null;
                player.linkRight = null;
                if (player.equals(player.duel.player1)) {
                    zone = "Right Extra Monster Zone";
                }
                else {
                    zone = "Left Extra Monster Zone";
                }
            }
        }
        if (card.face_down == true) {
            set = "Set ";
            public_location = set + "card from " + zone;
        }
        else {
            public_location = "\"" + card.name + "\" from " + zone;
        }
        private_location = set + "\"" + card.name + "\" from " + zone;
        if (!card.controller.equals(card.owner)) {
            card.controller = card.owner;
        }
        if (keep_stats == false) {
            card.inATK = false;
            card.inDEF = false;
            card.face_down = false;
            card.counters = 0;
            detachAllMaterials(card);
        }
        if (card.id == 0) {
            card = null;
        }
        Object[] locations = {card, public_location, private_location};
        return locations;
    }
    
    public static void detachAllMaterials(Card card) {
        while (card.xyz_arr.size() > 0) {
            Card material = card.xyz_arr.get(0);
            card.xyz_arr.remove(0);
            material.owner.grave_arr.add(0, material);
        }
    }
    
    public static void targetCardAdmin(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = new Card();
            Object location1[] = getLocation(opp, card_id);
            Object location2[] = getLocation(you, card_id);

            JSONObject result = new JSONObject();
            result.put("play", "Target card");
            result.put("id", card_id);
            String public_entry = "";
            String private_entry = "";

            if (location1[2] != null) {
                public_entry = "Pointed at " + location1[0];
                private_entry = "Pointed at " + location1[1];
            }
            else if (location2[2] != null) {
                public_entry = "Pointed at " + location2[0];
                private_entry = "Pointed at " + location2[1];
            }
            else {
                System.out.println("ADMIN COULD NOT FIND TARGETED CARD");
                //return;
            }

            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", user.username);
            log.put("type", "duel");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);

            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("username", user.username);
            result.put("log", log);
            duel.addReplay(result);

            if (location1[2] != null) {
                if (you.active == true) {
                    write(you.nbc, result);
                }
                result.remove("log");
                for (int i = 0; i < duel.watchers.size(); i++) {
                    write(duel.watchers.get(i).nbc, result);
                }
                result.put("log", log);
                //((JSONObject) result.get("log")).remove("private_log");
                ((JSONObject) result.get("log")).put("private_log", private_entry);
                if (opp.active == true) {
                    write(opp.nbc, result);
                }
                duel.addLog(log);
            }
            else {
                if (opp.active == true) {
                    write(opp.nbc, result);
                }
                result.remove("log");
                for (int i = 0; i < duel.watchers.size(); i++) {
                    write(duel.watchers.get(i).nbc, result);
                }
                result.put("log", log);
                //((JSONObject) result.get("log")).remove("private_log");
                ((JSONObject) result.get("log")).put("private_log", private_entry);
                if (you.active == true) {
                    write(you.nbc, result);
                }
                duel.addLog(log);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public static void targetCard(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (user.admin > 0) {
                targetCardAdmin(nbc, data, user, duel, you, opp);
                return;
            }
            int card_id = (int) data.get("card");
            Card card = new Card();
            Object location[] = getLocation(opp, card_id);
            if (location[2] == null) {
                System.out.println("COULD NOT FIND TARGETED CARD");
                return;
            }
            JSONObject result = new JSONObject();
            if (you.exchanging == true) {
                you.exchanging = false;
                you.exchanged_card = card_id;
                you.viewing = "";
                
                Object[] locations = removeFromHand(opp, card_id);
                card = (Card) locations[0];
                you.hand_arr.add(card);
                
                shuffle(you.hand_arr);
                ArrayList<Integer> hand_ids = new ArrayList<Integer>();
                for (int i = 0; i < you.hand_arr.size(); i++) {
                    hand_ids.add(you.hand_arr.get(i).id);
                }
                
                result = new JSONObject();
                result.put("play", "Exchange cards");
                result.put("id", card_id);
                result.put("hand", hand_ids);
                String public_entry = "Chose \"" + card.name + "\"";
                publicDuelResult(result, duel, public_entry, you, opp);
            }
            else {
                result = new JSONObject();
                result.put("play", "Target card");
                result.put("id", card_id);
                String public_entry = "Pointed at " + location[0];
                String private_entry = "Pointed at " + location[1];

                JSONObject log = new JSONObject();
                //log.put("timestamp", duel.timestamp);
                log.put("username", you.username);
                log.put("type", "duel");
                log.put("public_log", public_entry);
                //log.put("private_log", public_entry);

                result.put("action", "Duel");
                result.put("seconds", duel.seconds);
                result.put("username", you.username);
                result.put("log", log);
                //duel.addReplay(result);
                if (you.active == true) {
                    write(you.nbc, result);
                }
                result.remove("log");
                for (int i = 0; i < duel.watchers.size(); i++) {
                    write(duel.watchers.get(i).nbc, result);
                }
                result.put("log", log);
                //((JSONObject) result.get("log")).remove("private_log");
                ((JSONObject) result.get("log")).put("private_log", private_entry);
                if (opp.active == true) {
                    write(opp.nbc, result);
                }
                duel.addLog(log);
                duel.addReplay(result); // moved to here from up there ^^
                // ^^ THE OPPONENT RECEIVES THE PRIVATE INFO ABOVE
                //privateDuelResult(result, duel, public_entry, private_entry, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Card newToken(Duel duel, Player player) {
        Card token = new Card();
        
        token.id = duel.tokens;
        token.card_id = 0;
        token.name = "Token";
        token.treated_as = "Token";
        token.effect = "This card can be used as any Token";
        token.pendulum_effect = null;
        token.card_type = "Monster";
        token.monster_color = "Token";
        token.is_effect = 0;
        token.type = "";
        token.attribute = null;
        token.level = 0;
        token.ability = null;
        token.flip = 0;
        token.pendulum = 0;
        token.scale_left = 0;
        token.scale_right = 0;
        token.arrows = "";
        token.atk = null;
        token.def = null;
        //token.restriction = 0;
        token.tcg_limit = 3;
        token.ocg_limit = 3;
        token.serial_number = "";
        token.tcg = 0;
        token.ocg = 0;
        token.pic = "https://www.duelingbook.com/images/tokens/" + player.token + ".jpg";
        token.controller = player;
        token.owner = player;

        token.data.put("id", token.card_id);
        token.data.put("name", token.name);
        token.data.put("treated_as", token.treated_as);
        token.data.put("effect", token.effect);
        token.data.put("pendulum_effect", token.pendulum_effect);
        token.data.put("card_type", token.card_type);
        token.data.put("monster_color", token.monster_color);
        token.data.put("is_effect", token.is_effect);
        token.data.put("type", token.type);
        token.data.put("attribute", token.attribute);
        token.data.put("level", token.level);
        token.data.put("ability", token.ability);
        token.data.put("flip", token.flip);
        token.data.put("pendulum", token.pendulum);
        token.data.put("scale_left", token.scale_left);
        token.data.put("scale_right", token.scale_right);
        token.data.put("arrows", token.arrows);
        token.data.put("atk", token.atk);
        token.data.put("def", token.def);
        //token.data.put("restriction", token.restriction);
        //token.data.put("restriction", token.tcg_limit);
        token.data.put("tcg_limit", token.tcg_limit);
        token.data.put("ocg_limit", token.ocg_limit);
        token.data.put("serial_number", token.serial_number);
        token.data.put("tcg", token.tcg);
        token.data.put("ocg", token.ocg);
        token.data.put("pic", token.pic);

        duel.tokens++;
        return token;
    }
    
    public static Object getZoneByName(Player player, String zone) {
        if (zone.equals("M-1")) {
            return player.m1;
        }
        if (zone.equals("M-2")) {
            return player.m2;
        }
        if (zone.equals("M-3")) {
            return player.m3;
        }
        if (zone.equals("M-4")) {
            return player.m4;
        }
        if (zone.equals("M-5")) {
            return player.m5;
        }
        if (zone.equals("S-1")) {
            return player.s1;
        }
        if (zone.equals("S-2")) {
            return player.s2;
        }
        if (zone.equals("S-3")) {
            return player.s3;
        }
        if (zone.equals("S-4")) {
            return player.s4;
        }
        if (zone.equals("S-5")) {
            return player.s5;
        }
        return null;
    }
    
    public static String getNextMonsterZone(Card card, Player you, JSONObject data) {
        if (data.has("zone")) {
            if (getZoneByName(you, (String) data.get("zone")) == null) {
                switch ((String) data.get("zone")) {
                    case "M-1":
                        you.m1 = card;
                        break;
                    case "M-2":
                        you.m2 = card;
                        break;
                    case "M-3":
                        you.m3 = card;
                        break;
                    case "M-4":
                        you.m4 = card;
                        break;
                    case "M-5":
                        you.m5 = card;
                        break;
                    case "Left Extra Monster Zone":
                        if (you.equals(you.duel.player1)) {
                            you.duel.linkLeft = card;
                            you.linkLeft = card; // for watchDuel
                        }
                        else {
                            you.duel.linkRight = card;
                            you.linkRight = card;
                        }
                        break;
                    case "Right Extra Monster Zone":
                        if (you.equals(you.duel.player1)) {
                            you.duel.linkRight = card;
                            you.linkRight = card;
                        }
                        else {
                            you.duel.linkLeft = card;
                            you.linkLeft = card;
                        }
                        break;
                }
                you.zone = (String) data.get("zone");
                you.lastZone = (String) data.get("zone");
                return (String) data.get("zone");
            }
            else {
                you.lastZone = null;
                return null;
            }
        }
        if (you.m3 == null) {
            you.m3 = card;
            you.lastZone = "M-3";
        }
        else if (you.m4 == null) {
            you.m4 = card;
            you.lastZone = "M-4";
        }
        else if (you.m2 == null) {
            you.m2 = card;
            you.lastZone = "M-2";
        }
        else if (you.m5 == null) {
            you.m5 = card;
            you.lastZone = "M-5";
        }
        else if (you.m1 == null) {
            you.m1 = card;
            you.lastZone = "M-1";
        }
        else {
            you.lastZone = null;
            System.out.println("you.m1.name = " + you.m1.name);
            System.out.println("you.m2.name = " + you.m2.name);
            System.out.println("you.m3.name = " + you.m3.name);
            System.out.println("you.m4.name = " + you.m4.name);
            System.out.println("you.m5.name = " + you.m5.name);
        }
        return you.lastZone;
    }
    
    public static String getNextSTZone(Card card, Player you, JSONObject data) {
        if (data.has("zone")) {
            if (getZoneByName(you, (String) data.get("zone")) == null) {
                switch ((String) data.get("zone")) {
                    case "S-1":
                        you.s1 = card;
                        break;
                    case "S-2":
                        you.s2 = card;
                        break;
                    case "S-3":
                        you.s3 = card;
                        break;
                    case "S-4":
                        you.s4 = card;
                        break;
                    case "S-5":
                        you.s5 = card;
                        break;
                }
                you.zone = (String) data.get("zone");
                you.lastZone = (String) data.get("zone");
                return (String) data.get("zone");
            }
            else {
                you.lastZone = null;
                return null;
            }
        }
        if (you.s3 == null) {
            you.s3 = card;
            you.lastZone = "S-3";
        }
        else if (you.s4 == null) {
            you.s4 = card;
            you.lastZone = "S-4";
        }
        else if (you.s2 == null) {
            you.s2 = card;
            you.lastZone = "S-2";
        }
        else if (you.s5 == null) {
            you.s5 = card;
            you.lastZone = "S-5";
        }
        else if (you.s1 == null) {
            you.s1 = card;
            you.lastZone = "S-1";
        }
        else {
            you.lastZone = null;
        }
        return you.lastZone;
    }

    public static void shuffleDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.viewing.equals("Opponent's Deck")) {
                return;
            }
            shuffle(you.main_arr);
            ArrayList<Integer> deck_ids = new ArrayList<Integer>();
            for (int i = 0; i < you.main_arr.size(); i++) {
                deck_ids.add(you.main_arr.get(i).id);
            }
            JSONObject result = new JSONObject();
            result.put("play", "Shuffle deck");
            result.put("deck", deck_ids);
            result.put("message", you.username + " shuffled " + you.pronoun + " deck");
            String public_entry = "Shuffled deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                System.out.println("aa " + you.viewing);
                return;
            }
            if (opp.viewing.equals("Opponent's Deck")) {
                System.out.println("bb " + opp.viewing);
                return;
            }
            //Boolean seen = (Boolean) data.get("seen");
            you.viewing = "Deck";
            JSONObject result = new JSONObject();
            result.put("play", "View deck");
            result.put("viewing", "Deck");
            if (you.seen_deck == false) {
            //if (seen == false) {
                ArrayList<JSONObject> deck_arr = new ArrayList<JSONObject>();
                for (int i = 0; i < you.main_arr.size(); i++) {
                    deck_arr.add(you.main_arr.get(i).data);
                }
                result.put("deck", deck_arr);
            }
            you.seen_deck = true;
            String public_entry = "Viewed deck";
            String private_entry = "Viewed deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewGraveyard(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            you.viewing = "Graveyard";
            JSONObject result = new JSONObject();
            result.put("play", "View Graveyard");
            result.put("viewing", "Graveyard");
            String public_entry = "Viewed Graveyard";
            String private_entry = "Viewed Graveyard";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            you.viewing = "Banished";
            JSONObject result = new JSONObject();
            result.put("play", "View Banished");
            result.put("viewing", "Banished");
            String public_entry = "Viewed Banished";
            String private_entry = "Viewed Banished";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewOpponentGraveyard(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            you.viewing = "Opponent's Graveyard";
            String public_entry = "Viewed Opponent's Graveyard";
            String private_entry = "Viewed Opponent's Graveyard";
            JSONObject result = new JSONObject();
            result.put("play", "View Opponent's Graveyard");
            result.put("viewing", "Opponent's Graveyard");
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewOpponentBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            you.viewing = "Opponent's Banished";
            String public_entry = "Viewed Opponent's Banished";
            String private_entry = "Viewed Opponent's Banished";
            JSONObject result = new JSONObject();
            result.put("play", "View Opponent's Banished");
            result.put("viewing", "Opponent's Banished");
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void showTop3Cards(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.active == false) {
                return;
            }
            if (!opp.viewing.equals("")) {
                return;
            }
            if (you.main_arr.size() < 3) {
                return;
            }
            ArrayList<JSONObject> deck_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < 3; i++) {
                deck_arr.add(you.main_arr.get(i).data);
            }
            opp.viewing = "Opponent's Deck (partial)";
            String public_entry = "Showed the top 3 cards";
            JSONObject result = new JSONObject();
            result.put("play", "Show deck");
            result.put("viewing", "Opponent's Deck (partial)");
            result.put("deck", deck_arr);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void showDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.active == false) {
                return;
            }
            if (!opp.viewing.equals("")) {
                return;
            }
            ArrayList<JSONObject> deck_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < you.main_arr.size(); i++) {
                deck_arr.add(you.main_arr.get(i).data);
            }
            opp.viewing = "Opponent's Deck";
            String public_entry = "Showed deck";
            JSONObject result = new JSONObject();
            result.put("play", "Show deck");
            result.put("viewing", "Opponent's Deck");
            result.put("deck", deck_arr);
            publicDuelResult(result, duel, public_entry, you, opp);
            shuffle(you.main_arr);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void showHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.active == false) {
                return;
            }
            if (!opp.viewing.equals("")) {
                return;
            }
            ArrayList<JSONObject> hand_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                hand_arr.add(you.hand_arr.get(i).data);
            }
            opp.viewing = "Opponent's Hand";
            String public_entry = "Showed hand";
            JSONObject result = new JSONObject();
            result.put("play", "Show hand");
            result.put("viewing", "Opponent's Hand");
            result.put("deck", hand_arr);
            System.out.println("result = " + result.toString());
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewExtraDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            /*if (opp.viewing.equals("Opponent's Extra Deck")) {
                return; // added 8/11
            }
            else {
                System.out.println("opp.viewing = " + opp.viewing);
            }*/
            //Boolean seen = (Boolean) data.get("seen");
            you.viewing = "Extra Deck";
            String public_entry = "Viewed Extra Deck";
            String private_entry = "Viewed Extra Deck";
            JSONObject result = new JSONObject();
            result.put("play", "View Extra Deck");
            result.put("viewing", "Extra Deck");
            if (you.seen_extra == false) {
            //if (seen == false) {
                ArrayList<JSONObject> extra_arr = new ArrayList<JSONObject>();
                for (int i = 0; i < you.extra_arr.size(); i++) {
                    extra_arr.add(you.extra_arr.get(i).data);
                }
                result.put("deck", extra_arr);
            }
            you.seen_extra = true;
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void showExtraDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.active == false) {
                return;
            }
            if (!opp.viewing.equals("")) {
                return;
            }
            ArrayList<JSONObject> extra_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < you.extra_arr.size(); i++) {
                extra_arr.add(you.extra_arr.get(i).data);
            }
            opp.viewing = "Opponent's Extra Deck";
            String public_entry = "Showed Extra Deck";
            JSONObject result = new JSONObject();
            result.put("play", "Show Extra Deck");
            result.put("viewing", "Opponent's Extra Deck");
            result.put("deck", extra_arr);
            result.put("permission", true);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void viewOpponentExtraDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!you.viewing.equals("")) {
                return;
            }
            you.viewing = "Opponent's Public Extra Deck";
            String public_entry = "Viewed Opponent's Extra Deck";
            String private_entry = "Viewed Opponent's Extra Deck";
            JSONObject result = new JSONObject();
            result.put("play", "View Opponent's Public Extra Deck");
            result.put("viewing", "Opponent's Public Extra Deck");
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void stopViewing(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            String location = (String) data.get("viewing");
            String public_entry = "Stopped viewing " + location;
            JSONObject result = new JSONObject();
            result.put("play", "Stop viewing");
            result.put("viewing", you.viewing);
            publicDuelResult(result, duel, public_entry, you, opp);
            you.viewing = "";
            you.exchanging = false;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toHandFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            you.hand_arr.add(card);
            shuffle(you.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                hand_ids.add(you.hand_arr.get(i).id);
            }
            String public_entry = "Added \"" + card.name + "\" from Deck to hand";
            JSONObject result = new JSONObject();
            result.put("play", "To hand from deck");
            result.put("card", card.data);
            result.put("hand", hand_ids);
            result.put("id", card_id);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void shuffleHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.viewing.equals("Opponent's Hand")) {
                return;
            }
            if (you.hand_arr.size() < 2) {
                return;
            }
            shuffle(you.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                hand_ids.add(you.hand_arr.get(i).id);
            }
            String public_entry = "Shuffled hand";
            JSONObject result = new JSONObject();
            result.put("play", "Shuffle hand");
            result.put("hand", hand_ids);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Object[] removeFromHand(Player player, int card_id) {
        //Card card = new Card();
        Card card = null;
        int index = 0;
        String location = "";
        int i = 0;
        for (i = 0; i < player.hand_arr.size(); i++) {
            if (player.hand_arr.get(i).id == card_id) {
                index = i + 1;
                card = player.hand_arr.get(i);
                player.hand_arr.remove(i);
                break;
            }
        }
        if (i == player.hand_arr.size() && card != null) {
            for (int j = 0; j < player.hand_arr.size(); j++) {
                System.out.println("player.hand_arr.get(" + j + ").name = " + player.hand_arr.get(j).name + ", id = " + player.hand_arr.get(j).id);
            }
        }
        location = "(" + index + "/" + (player.hand_arr.size() + 1) + ")";
        Object[] locations = {card, location};
        return locations;
    }
    
    public static void normalSummon(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] locations = removeFromHand(you, card_id);
            Card card = (Card) locations[0];
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Normal Summon");
            result.put("card", card.data);
            result.put("id", card.id);
            String public_entry = "Normal Summoned \"" + card.name + "\" " + locations[1] + " to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setMonsterFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] locations = removeFromHand(you, card_id);
            Card card = (Card) locations[0];
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = true;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Set monster from hand");
            //result.put("card", card.data);
            result.put("id", card.id);
            String public_entry = "Set card from hand " + locations[1] + " to " + zone;
            String private_entry = "Set \"" + card.name + "\" " + locations[1] + " to " + zone;
            
            System.out.println("in setMonsterFromHand, private_entry = " + private_entry);
            
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
            
    public static void turnFaceDown(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = true;
            JSONObject result = new JSONObject();
            result.put("play", "Turn face-down");
            result.put("id", card.id);
            String public_entry = "Turned \"" + card.name + "\" in " + location[0] + " face-down";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setMonster(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.inDEF = true;
            card.inATK = false;
            card.counters = 0;
            JSONObject result = new JSONObject();
            result.put("play", "Set monster");
            result.put("id", card.id);
            String public_entry = "Set " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void flipMonster(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.inDEF = true;
            card.inATK = false;
            JSONObject result = new JSONObject();
            result.put("play", "Flip monster");
            result.put("card", card.data);
            result.put("id", card.id);
            String public_entry = "Flipped " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void flipSummon(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.inDEF = false;
            card.inATK = true;
            JSONObject result = new JSONObject();
            result.put("play", "Flip Summon");
            result.put("card", card.data);
            result.put("id", card.id);
            String public_entry = "Flip Summoned " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toATK(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.inDEF = false;
            card.inATK = true;
            JSONObject result = new JSONObject();
            result.put("play", "To ATK");
            result.put("id", card.id);
            String public_entry = "Changed " + location[0] + " to ATK";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void addCounter(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.counters++;
            JSONObject result = new JSONObject();
            result.put("play", "Add counter");
            result.put("id", card.id);
            result.put("total", card.counters);
            //String public_entry = "Placed a counter on \"" + card.name + "\" in " + location[0] + " (now " + card.counters + ")";
            String public_entry = "Placed a counter on " + location[0] + " (now " + card.counters + ")";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void removeCounter(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.counters--;
            if (card.counters < 0) {
                card.counters = 0;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Remove counter");
            result.put("id", card.id);
            result.put("total", card.counters);
            //String public_entry = "Removed a counter from \"" + card.name + "\" in " + location[0] + " (now " + card.counters + ")";
            String public_entry = "Removed a counter from " + location[0] + " (now " + card.counters + ")";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toDEF(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.inDEF = true;
            card.inATK = false;
            JSONObject result = new JSONObject();
            result.put("play", "To DEF");
            result.put("id", card.id);
            String public_entry = "Changed " + location[0] + " to DEF";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toGraveFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To grave from field");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Sent " + location[2] + " to Graveyard";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from field");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished " + location[2];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromFieldFD(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from field FD");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished " + location[1] + " face-down";
            String private_entry = "Banished " + location[2] + " face-down";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toGraveFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To grave from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Sent \"" + card.name + "\" to Graveyard from Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toGraveFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To Grave from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Sent \"" + card.name + "\" to Graveyard from Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toGraveFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, true);
            if (card == null) {
                return;
            }
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To grave from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned \"" + card.name + "\" to Graveyard from Banished";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toGraveFromOpponentBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(opp, card_id, true);
            if (card == null) {
                return;
            }
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To grave from opponent's banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned \"" + card.name + "\" to opponent's Graveyard from Banished";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" from Graveyard";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromGraveFD(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from grave FD");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" from Graveyard face-down";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromOpponentGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(opp, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from opponent's grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" opponent's from Graveyard";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" from Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFDFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish FD from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished a card from Deck face-down";
            String private_entry = "Banished \"" + card.name + "\" from Deck face-down";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" from Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromExtraFD(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from Extra FD");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished a card from Extra Deck face-down";
            String private_entry = "Banished \"" + card.name + "\" from Extra Deck face-down";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishRandomCardFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int i = (int) Math.floor(Math.random() * you.extra_arr.size());
            int card_id = you.extra_arr.get(i).id;
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            result.put("line", you.username + " randomly banished \"" + card.name + "\" from " + you.pronoun + " Extra Deck");
            String public_entry = "Randomly banished \"" + card.name + "\" from Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void revealFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            you.extra_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "Reveal from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Revealed \"" + card.name + "\" from Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void removeToken(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Remove Token");
            result.put("id", card.id);
            String public_entry = "Removed " + location[2];
            publicDuelResult(result, duel, public_entry, you, opp);
            card = null;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void changeControl(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            if (opp.hasAvailableMonsterZone() == false) {
                return;
            }
            Object location[] = removeFromField(you, card_id, true);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            String zone = getNextMonsterZone(card, opp, data);
            JSONObject result = new JSONObject();
            result.put("play", "Change control");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Changed control of " + location[1] + " to opponent (" + zone + ")";
            String private_entry = "Changed control of " + location[2] + " to opponent (" + zone + ")";
            
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("type", "duel");
            log.put("public_log", public_entry);
            log.put("private_log", private_entry);
            
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("username", you.username);
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            result.put("log", log);
            if (you.active == true) {
                write(you.nbc, result);
            }
            if (opp.active == true) {
                write(opp.nbc, result);
            }
            duel.addLog(log);
            duel.addReplay(result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toHandFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.owner.hand_arr.add(card);
            shuffle(card.owner.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < card.owner.hand_arr.size(); i++) {
                hand_ids.add(card.owner.hand_arr.get(i).id);
            }
            
            JSONObject result = new JSONObject();
            result.put("play", "To hand from field");
            result.put("id", card.id);
            result.put("hand", hand_ids);
            String public_entry = "Returned " + location[1] + " to hand";
            String private_entry = "Returned " + location[2] + " to hand";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toHandFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.hand_arr.add(card);
            shuffle(card.owner.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < card.owner.hand_arr.size(); i++) {
                hand_ids.add(card.owner.hand_arr.get(i).id);
            }
            JSONObject result = new JSONObject();
            result.put("play", "To hand from grave");
            result.put("id", card.id);
            result.put("hand", hand_ids);
            String public_entry = "Returned \"" + card.name + "\" from Graveyard to hand";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toHandFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.hand_arr.add(card);
            shuffle(card.owner.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < card.owner.hand_arr.size(); i++) {
                hand_ids.add(card.owner.hand_arr.get(i).id);
            }
            JSONObject result = new JSONObject();
            result.put("play", "To hand from Extra");
            result.put("id", card.id);
            result.put("hand", hand_ids);
            String public_entry = "Returned \"" + card.name + "\" from Extra Deck to hand";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toHandFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, false);
            if (card == null) {
                return;
            }
            card.owner.hand_arr.add(card);
            shuffle(card.owner.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < card.owner.hand_arr.size(); i++) {
                hand_ids.add(card.owner.hand_arr.get(i).id);
            }
            JSONObject result = new JSONObject();
            result.put("play", "To hand from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            result.put("hand", hand_ids);
            String public_entry = "Returned \"" + card.name + "\" from Banished to hand";
            String private_entry = "Returned \"" + card.name + "\" from Banished to hand";
            if (card.face_down == true) {
                public_entry = "Returned face-down card from Banished to hand";
                private_entry = "Returned face-down \"" + card.name + "\" from Banished to hand";
                card.face_down = false;
            }
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    // NOT USED YET
    public static void toOpposingHandFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.controller = card.owner.opponent;
            card.controller.hand_arr.add(card);
            shuffle(card.controller.hand_arr);
            ArrayList<Integer> hand_ids = new ArrayList<Integer>();
            for (int i = 0; i < card.controller.hand_arr.size(); i++) {
                hand_ids.add(card.controller.hand_arr.get(i).id);
            }
            
            JSONObject result = new JSONObject();
            result.put("play", "To opposing hand from field");
            result.put("id", card.id);
            result.put("hand", hand_ids);
            String public_entry = "Returned " + location[2] + " to hand";
            String private_entry = "Returned " + location[2] + " to hand";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck from field");
            result.put("id", card.id);
            String public_entry = "Returned " + location[1] + " to top of Deck";
            String private_entry = "Returned " + location[2] + " to top of Deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFUFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_up = true;
            you.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck FU from field");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned " + location[1] + " to top of Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void toTopOfOpponentDeckFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_up = true;
            you.opponent.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of opponent's deck from field");
            result.put("id", card.id);
            String public_entry = "Returned " + location[1] + " to top of opponent's Deck";
            String private_entry = "Returned " + location[2] + " to top of opponent's Deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfOpponentDeckFromOpponentDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(opp, card_id);
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of opponent's deck from opponent's deck");
            result.put("id", card.id);
            String public_entry = "Returned card to top of Deck";
            String private_entry = "Returned \"" + card.name + "\" to top of Deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void turnTopCardOfDeckFU(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            Card card = you.main_arr.get(0);
            if (card == null) {
                return;
            }
            card.face_up = true;
            JSONObject result = new JSONObject();
            result.put("play", "Turn top card of deck face-up");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Turned top card of deck face-up";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void flipDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            ArrayList<JSONObject> deck_arr = new ArrayList<JSONObject>();
            ArrayList<Integer> deck_ids = new ArrayList<Integer>();
            Collections.reverse(you.main_arr);
            for (int i = 0; i < you.main_arr.size(); i++) {
                deck_ids.add(you.main_arr.get(i).id);
                deck_arr.add(you.main_arr.get(i).data);
            }
            String public_entry = "Flipped deck upside down";
            you.deck_face_up = true;
            if (data.get("play").equals("Flip deck back")) {
                public_entry = "Flipped deck back";
                you.deck_face_up = false;
            }
            JSONObject result = new JSONObject();
            result.put("play", data.get("play"));
            result.put("deck", deck_arr);
            result.put("ids", deck_ids);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void cynetStorm(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            ArrayList<Card> face_up_arr = new ArrayList<Card>();
            ArrayList<Card> face_down_arr = new ArrayList<Card>();
            for (int i = 0; i < you.extra_arr.size(); i++) {
                if (you.extra_arr.get(i).face_down == true) {
                    face_down_arr.add(you.extra_arr.get(i));
                }
                else {
                    face_up_arr.add(you.extra_arr.get(i));
                }
            }
            shuffle(face_down_arr);
            int card_id = face_down_arr.get(0).id;
            you.extra_arr = face_down_arr;
            for (int i = 0; i < face_up_arr.size(); i++) {
                you.extra_arr.add(face_up_arr.get(i));
            }
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            ArrayList<Integer> deck_ids = new ArrayList<Integer>();
            for (int i = 0; i < you.extra_arr.size(); i++) {
                deck_ids.add(you.extra_arr.get(i).id);
            }
            JSONObject result = new JSONObject();
            result.put("play", "Cynet Storm");
            result.put("deck", deck_ids);
            result.put("message", you.username + " resolved Cynet Storm's effect");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Resolved Cynet Storm's effect";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void spyralEvent(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            JSONObject result = new JSONObject();
            result.put("play", "Permission event");
            result.put("title", "SPYRAL GEAR - Drone");
            result.put("message", "Are you ready to resolve the effect of SPYRAL GEAR - Drone?");
            result.put("callback", "Show top 3 cards");
            String public_entry = "Requested permission to resolve SPYRAL GEAR - Drone";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void randomExtraEvent(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Permission event");
            result.put("title", card.name);
            result.put("message", "Are you ready to resolve the effect of " + card.name + "?");
            result.put("callback", "Banish random card from extra");
            String public_entry = "Requested permission to resolve " + card.name;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void exchangeEvent(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            JSONObject result = new JSONObject();
            result.put("play", "Permission event");
            result.put("title", "Exchange");
            result.put("message", "Are you ready to resolve the effect of Exchange?");
            result.put("callback", "Exchange");
            String public_entry = "Requested permission to resolve Exchange";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void exchange(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            ArrayList<JSONObject> your_hand_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < you.hand_arr.size(); i++) {
                your_hand_arr.add(you.hand_arr.get(i).data);
            }
            ArrayList<JSONObject> opp_hand_arr = new ArrayList<JSONObject>();
            for (int i = 0; i < opp.hand_arr.size(); i++) {
                opp_hand_arr.add(opp.hand_arr.get(i).data);
            }
            you.viewing = "Opponent's Hand";
            opp.viewing = "Opponent's Hand";
            you.exchanging = true;
            opp.exchanging = true;

            /*String public_entry = "Showed hands";
            JSONObject log = new JSONObject();
            log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("public_log", public_entry);
            log.put("private_log", public_entry);
            log.put("type", "duel");
            
            JSONObject result = new JSONObject();
            result.put("log", log);
            
            result.put("action", "Duel");
            result.put("play", "Show hand");
            result.put("deck", your_hand_arr);
            result.put("username", you.username);
            opp.nbc.write(result + "\0");
            
            result.remove("deck");
            result.put("deck", opp_hand_arr);
            result.remove("username");
            result.put("username", opp.username);
            
            result.put("action", "Duel");
            result.put("play", "Show hand");
            result.put("deck", opp_hand_arr);
            you.nbc.write(result + "\0");
            
            result.remove("log");
            result.remove("deck");
            for (int i = 0; i < duel.watchers.size(); i++) {
                duel.watchers.get(i).nbc.write(result + "\0");
            }*/
            
            String public_entry = "Showed hand";
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            log.put("type", "duel");
            
            JSONObject result = new JSONObject();
            result.put("log", log);
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Show hand");
            result.put("viewing", "Opponent's Hand");
            result.put("deck", your_hand_arr);
            result.put("username", you.username);
            write(opp.nbc, result);
            write(you.nbc, result);
            result.remove("log");
            result.remove("deck");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            
            log.remove("username");
            log.put("username", opp.username);
            
            result = new JSONObject();
            result.put("log", log);
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Show hand");
            result.put("viewing", "Opponent's Hand");
            result.put("deck", opp_hand_arr);
            result.put("username", opp.username);
            write(opp.nbc, result);
            write(you.nbc, result);
            duel.addLog(log); // 9/24 added this cuz i didnt see it up above
            duel.addReplay(result); // dont get this
            result.remove("log");
            result.remove("deck");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void permissionDenied(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            JSONObject result = new JSONObject();
            result.put("play", "Permission denied");
            String public_entry = "Denied permission to use the effect";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned \"" + card.name + "\" from Graveyard to top of Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, false);
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned \"" + card.name + "\" from Banished to top of Deck";
            String private_entry = "Returned \"" + card.name + "\" from Banished to top of Deck";
            if (card.face_down == true) {
                public_entry = "Returned face-down card from Banished to top of Deck";
                private_entry = "Returned face-down \"" + card.name + "\" from Banished to top of Deck";
            }
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public static void toBottomOfDeckFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To bottom of deck from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned \"" + card.name + "\" from Graveyard to bottom of Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    
    public static void toBottomOfDeckFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.owner.main_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To bottom of deck from field");
            result.put("id", card.id);
            String public_entry = "Returned " + location[1] + " to bottom of Deck";
            String private_entry = "Returned " + location[2] + " to bottom of Deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activatePendulumRightFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.pendulumRight != null) {
                return;
            }
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.pendulumRight = card;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Pendulum Right from field");
            result.put("id", card.id);
            String public_entry = "Activated " + location[1] + " to Right Pendulum Zone";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activatePendulumLeftFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.pendulumLeft != null) {
                return;
            }
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.pendulumLeft = card;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Pendulum Left from field");
            result.put("id", card.id);
            String public_entry = "Activated " + location[1] + " to Right Left Zone";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activatePendulumRightFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.pendulumRight != null) {
                return;
            }
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.pendulumRight = card;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Pendulum Right from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Activated \"" + card.name + "\" " + location[1] + " to Right Pendulum Zone";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activatePendulumLeftFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.pendulumLeft != null) {
                return;
            }
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.pendulumLeft = card;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Pendulum Left from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Activated \"" + card.name + "\" " + location[1] + " to Left Pendulum Zone";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activateFieldSpellFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.fieldSpell = card;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Field Spell from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Activated Field Spell \"" + card.name + "\" from hand " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
            if (!card.type.equals("Field")) {
                CardError = user.username + " activated " + card.name + " in the field spell zone in duel #" + duel.id + ". The card id is " + card.id + ". The possible field spells are ";
                for (int i = 0; i < you.all_cards_arr.size(); i++) {
                    if (you.all_cards_arr.get(i).type.equals("Field")) {
                        CardError += you.all_cards_arr.get(i).name + " (" + you.all_cards_arr.get(i).id + "), ";
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setFieldSpellFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = true;
            you.fieldSpell = card;
            JSONObject result = new JSONObject();
            result.put("play", "Set Field Spell from hand");
            result.put("id", card.id);
            String public_entry = "Set Field Spell from hand " + location[1];
            String private_entry = "Set Field Spell \"" + card.name + "\" from hand " + location[1];
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setFieldSpellFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            if (you.fieldSpell != null) {
                return;
            }
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            you.fieldSpell = card;
            JSONObject result = new JSONObject();
            result.put("play", "Set Field Spell from deck");
            result.put("id", card.id);
            String public_entry = "Set Field Spell from deck";
            String private_entry = "Set Field Spell \"" + card.name + "\" from deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setFieldSpellFromDeckToOpponent(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            if (opp.fieldSpell != null) {
                return;
            }
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.controller = opp;
            opp.fieldSpell = card;
            JSONObject result = new JSONObject();
            result.put("play", "Set Field Spell from deck to opponent's field");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Set Field Spell from deck to opponent's field";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activateFieldSpell(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            JSONObject result = new JSONObject();
            result.put("play", "Activate Field Spell");
            result.put("id", card.id);
            result.put("card", card.data);
            //String public_entry = "Activated Set \"" + card.name + "\" in " + location[2];
            String public_entry = "Activated set Field Spell \"" + card.name + "\"";
            publicDuelResult(result, duel, public_entry, you, opp);
            if (!card.type.equals("Field")) {
                CardError = user.username + " activated " + card.name + " in the field spell zone. The card id is " + card.id + ". The possible field spells are ";
                for (int i = 0; i < you.all_cards_arr.size(); i++) {
                    if (you.all_cards_arr.get(i).type.equals("Field")) {
                        CardError += you.all_cards_arr.get(i).name + " (" + you.all_cards_arr.get(i).id + "), ";
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void setFieldSpell(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = true;
            JSONObject result = new JSONObject();
            result.put("play", "Set Field Spell");
            result.put("id", card.id);
            result.put("card", card.data);
            //String public_entry = "Changed \"" + card.name + "\" in " + location[0] + " to face-down";
            String public_entry = "Changed Field Spell \"" + card.name + "\" to face-down";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void stopAndShuffle(Player player) {
        try {
            if (player.active == true) {
                JSONObject result = new JSONObject();
                result.put("action", "Stop and shuffle");
                write(player.nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }     
    }
    
    public static void activateSpellFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.inDEF = false;
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Activate Spell from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Activated \"" + card.name + "\" from hand " + location[1] + " to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void activateST(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = false;
            JSONObject result = new JSONObject();
            result.put("play", "Activate ST");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Activated " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void setST(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            card.face_down = true;
            JSONObject result = new JSONObject();
            result.put("play", "Set ST");
            result.put("id", card.id);
            String public_entry = "Set " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    
    
    
    
    public static void setSTFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = true;
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Set ST from hand");
            result.put("id", card.id);
            String public_entry = "Set a card from hand " + location[1] + " to " + zone;
            String private_entry = "Set \"" + card.name + "\" from hand " + location[1] + " to " + zone;
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from hand " + location[1] + " to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from hand " + location[1] + " to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.main_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from Deck to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.main_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from Deck to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.extra_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from Extra Deck to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void olATKFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int start_id = (int) data.get("start_card");
            int end_id = (int) data.get("end_card");
            Card start_card = removeFromExtra(you, start_id);
            if (start_card == null) {
                return;
            }
            String start_name = start_card.name;
            Object end_location[] = getLocation(you, end_id);
            Card end_card = (Card) end_location[2];
            //if (end_card == null) { // !!
            //    return;
            //}
            String end_name = (String) end_location[1];
            String zone = (String) end_location[0];
            start_card.xyz_arr.add(end_card);
            while (end_card.xyz_arr.size() > 0) {
                start_card.xyz_arr.add(end_card.xyz_arr.get(0));
                end_card.xyz_arr.remove(0);
            }
            start_card.inATK = true;
            start_card.inDEF = false;
            start_card.face_down = false;
            replaceCard(you, start_card, end_card);
            JSONObject result = new JSONObject();
            result.put("play", "OL ATK from Extra");
            result.put("start_id", start_id);
            result.put("end_id", end_id);
            result.put("card", start_card.data);
            String public_entry = "Special Summoned \"" + start_card.name + "\" (ATK) onto " + end_name;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void olDEFFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int start_id = (int) data.get("start_card");
            int end_id = (int) data.get("end_card");
            Card start_card = removeFromExtra(you, start_id);
            if (start_card == null) {
                return;
            }
            String start_name = start_card.name;
            Object end_location[] = getLocation(you, end_id);
            Card end_card = (Card) end_location[2];
            //if (end_card == null) { // !!
            //    return;
            //}
            String end_name = (String) end_location[1];
            String zone = (String) end_location[0];
            start_card.xyz_arr.add(end_card);
            while (end_card.xyz_arr.size() > 0) {
                start_card.xyz_arr.add(end_card.xyz_arr.get(0));
                end_card.xyz_arr.remove(0);
            }
            start_card.inATK = false;
            start_card.inDEF = true;
            start_card.face_down = false;
            replaceCard(you, start_card, end_card);
            JSONObject result = new JSONObject();
            result.put("play", "OL DEF from Extra");
            result.put("start_id", start_id);
            result.put("end_id", end_id);
            result.put("card", start_card.data);
            String public_entry = "Special Summoned \"" + start_card.name + "\" (DEF) onto " + end_name;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.extra_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from Extra Deck to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.grave_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from Graveyard to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.grave_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from Graveyard to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromOpponentGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(opp, card_id);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                opp.grave_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from opponent's grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from opponent's Graveyard to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromOpponentGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(opp, card_id);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                opp.grave_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from opponent's grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from opponent's Graveyard to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssATKFromOpponentBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(opp, card_id, true);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                opp.banished_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from opponent's banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from opponent's Banished to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromOpponentBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(opp, card_id, true);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                opp.banished_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from opponent's banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from opponent's Banished to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public static void ssATKFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, true);
            if (card == null) {
                return;
            }
            card.inATK = true;
            card.inDEF = false;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.banished_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS ATK from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (ATK) from Banished to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void ssDEFFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, true);
            if (card == null) {
                return;
            }
            card.inATK = false;
            card.inDEF = true;
            card.face_down = false;
            String zone = getNextMonsterZone(card, you, data);
            if (zone == null) {
                you.banished_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "SS DEF from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Special Summoned \"" + card.name + "\" (DEF) from Banished to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void updateLifePoints(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int amount = (int) data.get("amount");
            if (amount == 0) {
                return;
            }
            if (amount < 0 && -amount > you.lifepoints) {
                amount = -you.lifepoints;
            }
            you.lifepoints += amount;
            if (you.lifepoints > 999999) {
                you.lifepoints = 999999;
            }
            else if (you.lifepoints < 0) {
                you.lifepoints = 0;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Life points");
            result.put("life", you.lifepoints);
            result.put("amount", amount);
            String word = "Gained ";
            String public_entry = "";
            String message = "";
            if (amount < 0) {
                word = "Lost ";
                result.put("points", -amount);
                result.put("word", "decreased");
                public_entry = "Lost " + -amount + " LP";
                message = user.username + " has lost " + -amount + " life points";
            }
            else {
                 result.put("points", amount);
                 result.put("word", "increased");
                 public_entry = "Gained " + amount + " LP";
                 message = user.username + " has gained " + amount + " life points";
            }
            result.put("message", message);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void permissionGranted(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            JSONObject result = new JSONObject();
            result.put("play", "Permission granted");
            String public_entry = "Granted permission";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterDP(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("DP")) {
                return;
            }
            duel.currentPhase = "DP";
            JSONObject result = new JSONObject();
            result.put("play", "Enter DP");
            String public_entry = "Entered Draw Phase";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterSP(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("SP")) {
                return;
            }
            duel.currentPhase = "SP";
            JSONObject result = new JSONObject();
            result.put("play", "Enter SP");
            String public_entry = "Entered Standby Phase";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterM1(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("M1")) {
                return;
            }
            duel.currentPhase = "M1";
            JSONObject result = new JSONObject();
            result.put("play", "Enter M1");
            String public_entry = "Entered Main Phase 1";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterBP(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("BP")) {
                return;
            }
            duel.currentPhase = "BP";
            JSONObject result = new JSONObject();
            result.put("play", "Enter BP");
            String public_entry = "Entered Battle Phase";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterM2(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("M2")) {
                return;
            }
            duel.currentPhase = "M2";
            JSONObject result = new JSONObject();
            result.put("play", "Enter M2");
            String public_entry = "Entered Main Phase 2";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void enterEP(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                return;
            }
            if (duel.currentPhase.equals("EP")) {
                return;
            }
            duel.currentPhase = "EP";
            JSONObject result = new JSONObject();
            result.put("play", "Enter EP");
            String public_entry = "Entered End Phase";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void endTurn(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.turn_player.equals(user.username)) {
                System.out.println("No, duel.turn_player = " + duel.turn_player);
                return;
            }
            if (duel.currentPhase.equals("")) {
                System.out.println("No, duel.currentPhase = " + duel.currentPhase);
                return;
            }
            you.resetAFKTimer();
            opp.startAFKTimer();
            duel.currentPhase = "";
            JSONObject result = new JSONObject();
            result.put("play", "End turn");
            String public_entry = "Ended turn";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void startTurn(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.turn_player.equals(you.username) && duel.turnCount > 0) {
                return;
            }
            Boolean draw = (Boolean) data.get("draw");
            duel.currentPhase = "DP";
            duel.turn_player = you.username;
            duel.turnCount++;
            JSONObject result = new JSONObject();
            result.put("play", "Start turn");
            result.put("username", duel.turn_player);
            //String public_entry = "--------------------------------------------";
            String public_entry = "Entered Draw Phase";
            publicDuelResult(result, duel, public_entry, you, opp);
            if (draw == true) {
                drawCard(nbc, data, user, duel, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void dieRoll(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int number = (int) Math.ceil(Math.random() * 6);
            JSONObject result = new JSONObject();
            result.put("play", "Die");
            result.put("result", number);
            String public_entry = "Rolled a die (" + number + ")";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void coinFlip(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            String outcome = "heads";
            int number = (int) Math.round(Math.random());
            if (number == 1) {
                outcome = "heads";
            }
            else {
                outcome = "tails";
            }
            JSONObject result = new JSONObject();
            result.put("play", "Coin");
            result.put("result", outcome);
            String public_entry = "Flipped a coin (" + outcome + ")";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void callAdmin(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.active == false) {
                return;
            }
            if (duel.awaiting_admin == true) {
                return;
            }
            String issue = (String) data.get("issue");
            System.out.println("23175");
            Call call = new Call();
            System.out.println("23177");
            call.id = duel.id;
            call.duel = duel;
            call.issue = issue;
            call.caller_id = you.user_id;
            call.caller = you.username;
            call.opponent_id = opp.user_id;
            call.opponent = opp.username;
            duel.call = call;
            duel.awaiting_admin = true;
            Calls.add(duel.call);
            TotalCalls++;
            JSONObject result = new JSONObject();
            result.put("play", "Call admin");
            result.put("username", user.username);
            String public_entry = "Called a judge for " + issue;
            publicGameResult(result, duel, public_entry, you, opp);
            
            result = new JSONObject();
            result.put("action", "Call admin");
            result.put("id", duel.id);
            result.put("issue", issue);
            result.put("caller", you.username);
            result.put("opponent", opp.username);
            for (int i = 0; i < Admins.size(); i++) {
                write(Admins.get(i).nbc, result);
            }
            
            ArrayList<JSONObject> log = new ArrayList<JSONObject>();
            String str = issue + ": " + you.username + " | " + opp.username + "\n";
            str += "Total Calls: " + Calls.size() + "\n";
            if (duel.log.size() > 6) {
                str += "```";
                for (int i = duel.log.size() - 6; i < duel.log.size(); i++) {
                    log.add(duel.log.get(i));
                    str += getDuelTimestamp((int) duel.log.get(i).get("seconds")) + " " + duel.log.get(i).get("username") + ": " + duel.log.get(i).get("public_log") + "\n";
                }
                str += "```\n";
            }
            System.out.println("str = " + str);
            try {
                InetAddress address = InetAddress.getByName("localhost");
                Socket socket = new Socket(address, 3000);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                result = new JSONObject();
                result.put("action", "Call admin");
                result.put("message", str);
                result.put("total", Calls.size());
                result.put("log", log);
                result.put("caller", call.caller);
                result.put("log", call.opponent);
                result.put("issue", call.issue);
                result.put("total", TotalCalls);
                bw.write(result.toString());
                bw.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static String getDuelTimestamp(int seconds) {
        int m = (int) Math.floor(seconds / 60);
        String s = String.valueOf(seconds % 60);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return "[" + m + ":" + s + "]";
    }
    
    public static void cancelCall(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            Calls.remove(duel.call);
            duel.call = null;
            duel.awaiting_admin = false;
            JSONObject result = new JSONObject();
            result.put("play", "Cancel call");
            result.put("username", user.username);
            //String public_entry = "Cancelled the admin call";
            String public_entry = "Cancelled the judge call";
            publicGameResult(result, duel, public_entry, you, opp);
            
            result = new JSONObject();
            result.put("action", "Cancel call");
            result.put("id", duel.id);
            for (int i = 0; i < Admins.size(); i++) {
                write(Admins.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void duelInactive(Duel duel) {
        removeDuelFromCalls(duel);
        removeDuelFromRoom(duel);
        duel.active = false;
        duel.player1.resetAFKTimer();
        duel.player2.resetAFKTimer();
    }
    
    public static void admitDefeat(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.active == false) {
                return;
            }
            if (duel.status.equals("Siding")) {
                return;
            }
            //duel.active = false;
            Boolean over = false;
            opp.wins++;
            you.losses++;
            opp.recorded_wins++;
            you.recorded_losses++;
            duel.rpsWinner = you.username;
            if (duel.type.equals("m")) {
                if (duel.games >= 2) {
                    if (opp.wins > you.wins && opp.wins > 1) {
                        duel.winner = opp;
                        opp.result = 2;
                        duel.loser = you;
                        you.result = 0;
                        over = true;
                    }
                }
            }
            else {
                duel.winner = opp;
                opp.result = 2;
                duel.loser = you;
                you.result = 0;
                over = true;
            }
            if (duel.rated == true) {
                you.experience += 2;
                opp.experience += 1;
                if (duel.type.equals("m")) {
                    you.user.match_experience += 2;
                    opp.user.match_experience += 1;
                    try {
                        String query = "UPDATE duelingbook_user SET match_experience = match_experience + 2 WHERE id = " + you.user_id;
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    try {
                        String query = "UPDATE duelingbook_user SET match_experience = match_experience + 1 WHERE id = " + opp.user_id;
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                else {
                    you.user.single_experience += 2;
                    opp.user.single_experience += 1;
                    try {
                        String query = "UPDATE duelingbook_user SET single_experience = single_experience + 2 WHERE id = " + you.user_id;
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    try {
                        String query = "UPDATE duelingbook_user SET single_experience = single_experience + 1 WHERE id = " + opp.user_id;
                        Statement st = getConnection().createStatement();
                        int numRowsChanged = executeUpdate(st, query);
                        st.close();
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                if (duel.winner != null) {
                    updateRatings(duel);
                }
            }
            String public_entry = "Admitted defeat";
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", duel.player1.rating);
            player1.put("experience", duel.player1.experience);
            player1.put("points", duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", duel.player2.rating);
            player2.put("experience", duel.player2.experience);
            player2.put("points", duel.player2.points);
            
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Admit defeat");
            result.put("over", over);
            result.put("username", you.username);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            duelInactive(user.duel);
            if (duel.type.equals("m")) {
                if (duel.winner == null) {
                    beginSiding(duel);
                }
            }
            if (duel.winner != null) {
                updateDuelEndTime(duel);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void quitDuel(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.winner == null && duel.active == true) {
            //if (duel.winner == null) {
                opp.wins++;
                opp.recorded_wins++;
                you.losses++;
                you.recorded_losses++;
                duel.rpsWinner = you.username;
                duel.winner = opp;
                opp.result = 2;
                duel.loser = you;
                you.result = 0;
                if (duel.rated == true) {
                    updateRatings(duel);
                }
            }
            if (duel.active == true) {
                updateDuelEndTime(duel);
                duelInactive(user.duel);
            }
            you.active = false;
            you.agree_to_rematch = false;
            String public_entry = "Left duel";
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", duel.player1.rating);
            player1.put("experience", duel.player1.experience);
            player1.put("points", duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", duel.player2.rating);
            player2.put("experience", duel.player2.experience);
            player2.put("points", duel.player2.points);

            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Quit duel");
            result.put("username", you.username);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            if (user.duel.player1.active == false && user.duel.player2.active == false && user.duel.watchers.size() == 0) {
                recycleDuel(user.duel);
            }
            System.out.println("This is " + user.username);
            user.duel_id = 0;
            user.duel = null;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void removeDuelFromCalls(Duel duel) {
        try {
            for (int i = 0; i < Calls.size(); i++) {
                if (Calls.get(i).duel.equals(duel)) {
                    JSONObject result = new JSONObject();
                    result.put("action", "Cancel call");
                    result.put("id", Calls.get(i).id);
                    for (int j = 0; j < Admins.size(); j++) {
                        write(Admins.get(j).nbc, result);
                    }
                    Calls.remove(i);
                    break;
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void removeDuelFromRoom(Duel duel) {
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Duel over");
            result.put("id", duel.id);
            for (int i = 0; i < DuelRoom.size(); i++) {
                write(DuelRoom.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void updateDuelEndTime(Duel duel) {
        duel.endTimestamp = new Timestamp(System.currentTimeMillis());
        int winner_id = 0;
        if (duel.winner != null) {
            winner_id = duel.winner.user_id;
        }
        int rated = 0;
        int draw = 0;
        if (duel.rated == true) {
            rated = 1;
        }
        if (duel.draw == true) {
            draw = 1;
        }
        if (UpdatingRatings == true) {
            addToDuelEntries(duel.id, duel.language, duel.rules, duel.type, duel.format, rated, duel.player1.user_id, duel.player1.username, duel.player2.user_id, duel.player2.username, winner_id, draw, duel.player1.deck_id, duel.player2.deck_id, duel.player1.prev_rating, duel.player1.rating, duel.player1.prev_experience, duel.player2.prev_rating, duel.player2.rating, duel.player2.prev_experience, duel.player1.experience, duel.player2.experience, duel.player1.points, duel.player2.points, duel.startTimestamp, duel.endTimestamp, duel.player1.key, duel.player2.key);
        }
    }
    
    public static void addToDuelEntries(int duel_id, String language, String rules, String type, String format, int rated, int player1_id, String player1_username, int player2_id, String player2_username, int winner_id, int draw, int host_deck, int opp_deck, int host_prev_rating, int host_new_rating, int host_prev_exp, int opp_prev_rating, int opp_new_rating, int opp_prev_exp, int host_new_exp, int opp_new_exp, int player1_points, int player2_points, Timestamp startTimestamp, Timestamp endTimestamp, String host_key, String opp_key) {
        System.out.println("addToDuelEntries entered");
        DuelEntry entry = newDuelEntry();
        entry.duel_id = duel_id;
        entry.language = language;
        entry.rules = rules;
        entry.type = type;
        entry.format = format;
        entry.rated = rated;
        entry.player1_id = player1_id;
        entry.player1_username = player1_username;
        entry.player2_id = player2_id;
        entry.player2_username = player2_username;
        entry.winner_id = winner_id;
        entry.draw = draw;
        entry.host_deck = host_deck;
        entry.opp_deck = opp_deck;
        entry.host_prev_rating = host_prev_rating;
        entry.host_new_rating = host_new_rating;
        entry.host_prev_exp = host_prev_exp;
        entry.host_new_exp = host_new_exp;
        entry.opp_prev_rating = opp_prev_rating;
        entry.opp_new_rating = opp_new_rating;
        entry.opp_prev_exp = opp_prev_exp;
        entry.opp_new_exp = opp_new_exp;
        entry.player1_points = player1_points;
        entry.player2_points = player2_points;
        entry.startTimestamp = startTimestamp;
        entry.endTimestamp = endTimestamp;
        entry.host_key = host_key;
        entry.opp_key = opp_key;
        DuelEntries.add(entry);
    }
    
    public static void beginSiding(Duel duel) {
        try {
            duel.active = true;
            duel.paused = false;
            duel.player1.resetDeck();
            duel.player1.setDeck();
            duel.player1.startAFKTimer();
            duel.player1.done_siding = false;
            duel.player2.resetDeck();
            duel.player2.setDeck();
            duel.player2.startAFKTimer();
            duel.player2.done_siding = false;
            duel.status = "Siding";
            duel.currentPhase = "";
            duel.turn_player = "";
            duel.awaiting_admin = false;

            // SETUP FOR NEXT DUEL
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Siding");
            result.put("seconds", duel.seconds);
            result.put("main", duel.player1.cardsToObjects(duel.player1.main_arr));
            result.put("side", duel.player1.cardsToObjects(duel.player1.side_arr));
            result.put("extra", duel.player1.cardsToObjects(duel.player1.extra_arr));
            result.put("message", "Siding for the next duel has begun (" + duel.player1.wins + "-" + duel.player2.wins + "-" + duel.player1.draws + ")");
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            
            result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Siding");
            result.put("seconds", duel.seconds);
            result.put("main", duel.player2.cardsToObjects(duel.player2.main_arr));
            result.put("side", duel.player2.cardsToObjects(duel.player2.side_arr));
            result.put("extra", duel.player2.cardsToObjects(duel.player2.extra_arr));
            result.put("message", "Siding for the next duel has begun (" + duel.player2.wins + "-" + duel.player1.wins + "-" + duel.player2.draws + ")");
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }

            result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Siding");
            result.put("seconds", duel.seconds);
            result.put("message", "Siding for the next duel has begun (" + duel.player1.wins + "-" + duel.player2.wins + "-" + duel.player1.draws + ")");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            duel.addReplay(result); // not tested
            addWatchButton(duel);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void offerDrawE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (opp.agree_to_draw == true) {
                return;
            }
            if (duel.status.equals("Siding")) {
                return;
            }
            you.agree_to_draw = true;
            JSONObject result = new JSONObject();
            result.put("play", "Offer draw");
            String public_entry = "Offered a draw";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void revokeDrawE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            you.agree_to_draw = false;
            JSONObject result = new JSONObject();
            result.put("play", "Revoke draw");
            String public_entry = "Revoked the draw offer";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void acceptDrawE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.active == false) {
                return;
            }
            if (opp.agree_to_draw == false) {
                return;
            }
            you.agree_to_draw = true;
            you.draws++;
            opp.draws++;
            you.recorded_draws++;
            opp.recorded_draws++;
            if (duel.rpsWinner != null) {
                duel.rpsWinner = null;
            }
            Boolean over = false;
            if (duel.rated == true) {
                if (!duel.type.equals("m")) {
                    duel.draw = true;
                    you.result = 1;
                    opp.result = 1;
                    updateRatings(duel);
                    over = true;
                }
            }
            String public_entry = "Accepted the Draw Offer";
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", you.username);
            log.put("type", "game");
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            duel.addLog(log);
            
            JSONObject player1 = new JSONObject();
            player1.put("rating", duel.player1.rating);
            player1.put("experience", duel.player1.experience);
            player1.put("points", duel.player1.points);
            
            JSONObject player2 = new JSONObject();
            player2.put("rating", duel.player2.rating);
            player2.put("experience", duel.player2.experience);
            player2.put("points", duel.player2.points);
            
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("play", "Accept draw");
            result.put("username", you.username);
            result.put("over", over);
            result.put("log", log);
            result.put("player1", player2);
            result.put("player2", player1);
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            result.remove("player1");
            result.remove("player2");
            result.put("player1", player1);
            result.put("player2", player2);
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            duelInactive(user.duel);
            if (duel.type.equals("m")) {
                if (duel.winner == null) {
                    beginSiding(user.duel);
                }
            }
            if (duel.winner != null || duel.draw == true) {
                System.out.println("Draw, and updateDuelEndTime");
                updateDuelEndTime(user.duel);
            }
            else {
                System.out.println("No, duel.draw = " + duel.draw);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void updateRatings(Duel duel) {
        
    }

    public static void offerRematchE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (duel.active == true) {
                return;
            }
            if (you.active == false) {
                return;
            }
            if (opp.active == false) {
                return;
            }
            JSONObject result = new JSONObject();
            if (duel.rated == true && duel.times_rematched > 0) {
                errorE(nbc, "You are only permitted to rematch this opponent once");
                return;
            }
            if (duel.rated == true && (you.suspicious == 1 || you.suspicious == 1)) {
                errorE(nbc, "You cannot offer a rematch right now");
                return;
            }
            if (PoolEnabled == false && duel.rated == true) {
                errorE(nbc, "Advanced games are currently disabled");
                return;
            }
            if (opp.agree_to_rematch == true) {
                return;
            }
            you.agree_to_rematch = true;
            result = new JSONObject();
            result.put("play", "Offer rematch");
            result.put("username", you.username);
            String public_entry = "Offered a rematch";
            publicGameResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void revokeRematchE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            you.agree_to_rematch = false;
            opp.agree_to_rematch = false;
            JSONObject result = new JSONObject();
            result.put("play", "Revoke rematch");
            result.put("username", you.username);
            String public_entry = "Revoked the rematch offer";
            publicGameResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void acceptRematchE(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.agree_to_rematch == true) {
                return;
            }
            if (opp.agree_to_rematch == false) {
                return;
            }
            duel.times_rematched++;
            //you.agree_to_rematch = true;
            JSONObject result = new JSONObject();
            result.put("play", "Accept rematch");
            result.put("username", you.username);
            String public_entry = "Accepted the rematch offer";
            publicGameResult(result, duel, public_entry, you, opp);
            duel.winner = null;
            duel.player1.prev_rating = duel.player1.rating;
            duel.player1.prev_experience = duel.player1.experience;
            duel.player2.prev_rating = duel.player2.rating;
            duel.player2.prev_experience = duel.player2.experience;
            System.out.println("duel.type = " + duel.type);
            if (duel.type.equals("n")) {
                duel.player1.resetDeck();
                duel.player1.setDeck();
                duel.player2.resetDeck();
                duel.player2.setDeck();
                beginNextDuel(nbc, data, user, duel, you, opp);
                addWatchButton(duel);
            }
            else if (duel.type.equals("m")) {
                // idk, probably redundant
                duel.rpsWinner = null;
                you.wins = 0;
                you.losses = 0; // is this used?
                you.draws = 0;
                opp.wins = 0;
                opp.losses = 0; // is this used?
                opp.draws = 0;
                duel.games = 0;

                System.out.println("Reverting to original");
                duel.player1.revertToOriginal();
                duel.player1.resetDeck();
                duel.player1.setDeck();
                duel.player2.revertToOriginal();
                duel.player2.resetDeck();
                duel.player2.setDeck();
                beginNextDuel(nbc, data, user, duel, you, opp);
                addWatchButton(duel);
            }
            else {
                beginSiding(duel);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void swapCards(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            JSONArray main0 = (JSONArray) ((JSONObject) data.get("cards")).get("main");
            JSONArray side0 = (JSONArray) ((JSONObject) data.get("cards")).get("side");
            JSONArray extra0 = (JSONArray) ((JSONObject) data.get("cards")).get("extra");
            ArrayList<Integer> main = new ArrayList<Integer>();
            ArrayList<Integer> side = new ArrayList<Integer>();
            ArrayList<Integer> extra = new ArrayList<Integer>();
            System.out.println("a");
            for (Object obj: main0) {
                main.add((int) obj);
            }
            System.out.println("aa");
            for (Object obj: side0) {
                side.add((int) obj);
            }
            System.out.println("aaa");
            for (Object obj: extra0) {
                extra.add((int) obj);
            }
            System.out.println("b");
            ArrayList<Integer> main_indexes = new ArrayList<Integer>();
            ArrayList<Integer> side_indexes = new ArrayList<Integer>();
            ArrayList<Integer> extra_indexes = new ArrayList<Integer>();
            System.out.println("c");
            for (int i = 0; i < you.main_arr.size(); i++) {
                if (main.contains(you.main_arr.get(i).id)) {
                    System.out.println("Adding " + you.main_arr.get(i).name + "'s id " + you.main_arr.get(i).id + " to main_indexes. i = " + i);
                    main_indexes.add(i);
                }
            }
            System.out.println("e");
            for (int i = 0; i < you.side_arr.size(); i++) {
                if (side.contains(you.side_arr.get(i).id)) {
                    System.out.println("Adding " + you.side_arr.get(i).name + "'s id " + you.side_arr.get(i).id + " to side_indexes. i = " + i);
                    side_indexes.add(i);
                }
            }
            System.out.println("f");
            for (int i = 0; i < you.extra_arr.size(); i++) {
                if (extra.contains(you.extra_arr.get(i).id)) {
                    System.out.println("Adding " + you.extra_arr.get(i).name + "'s id " + you.extra_arr.get(i).id + " to extra_indexes. i = " + i);
                    extra_indexes.add(i);
                }
            }
            System.out.println("g");
            for (int i = 0; i < side.size(); i++) {
                for (int j = 0; j < you.side_arr.size(); j++) {
                    if (side.size() < i) {
                        System.out.println("a4");
                        return;
                    }
                    if (side.get(i) == you.side_arr.get(j).id) {
                        System.out.println("h");
                        System.out.println("you.side_arr.size() = " + you.side_arr.size());
                        System.out.println("j = " + j);
                        Card swappedCard = you.side_arr.get(j);
                        System.out.println("hh");
                        System.out.println("swappedCard.name = " + swappedCard.name);
                        you.side_arr.remove(swappedCard);
                        //you.side_arr.remove(j);
                        System.out.println("i");
                        if (swappedCard.data.has("monster_color") && (swappedCard.data.get("monster_color").equals("Fusion") || swappedCard.data.get("monster_color").equals("Link") || swappedCard.data.get("monster_color").equals("Synchro") || swappedCard.data.get("monster_color").equals("Xyz"))) {
                            System.out.println("j");
                            Card swappedCard2 = you.extra_arr.get(extra_indexes.get(0));
                            you.extra_arr.remove(swappedCard2);
                            //you.extra_arr.remove(extra_indexes.get(0));
                            you.side_arr.add(side_indexes.get(0), swappedCard2);
                            you.extra_arr.add(extra_indexes.get(0), swappedCard);
                            System.out.println("You removed " + swappedCard2.name + " from extra at " + extra_indexes.get(0) + " and inserted it in side at " + side_indexes.get(0));
                            System.out.println("You removed " + swappedCard.name + " from side at " + j + " and inserted it in extra at " + extra_indexes.get(0));
                            extra_indexes.remove(0);
                            side_indexes.remove(0);
                            System.out.println("k");
                        }
                        else {
                            System.out.println("l");
                            Card swappedCard2 = you.main_arr.get(main_indexes.get(0));
                            you.main_arr.remove(swappedCard2);
                            //you.main_arr.remove(main_indexes.get(0));
                            you.side_arr.add(side_indexes.get(0), swappedCard2);
                            you.main_arr.add(main_indexes.get(0), swappedCard);
                            System.out.println("You removed " + swappedCard2.name + " from main at " + main_indexes.get(0) + " and inserted it in side at " + side_indexes.get(0));
                            System.out.println("You removed " + swappedCard.name + " from side at " + j + " and inserted it in main at " + main_indexes.get(0));
                            main_indexes.remove(0);
                            side_indexes.remove(0);
                            System.out.println("m");
                        }
                    }
                }
            }
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Swap cards");
            result.put("seconds", duel.seconds);
            result.put("main", you.cardsToIds(you.main_arr));
            result.put("side", you.cardsToIds(you.side_arr));
            result.put("extra", you.cardsToIds(you.extra_arr));
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("line 24577 entered");
        }
    }
    
    public static void resetDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            you.revertToOriginal();
            you.resetDeck();
            you.setDeck();
            if (data.has("error")) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", "Reset deck");
            result.put("main", you.cardsToIds(you.main_arr));
            result.put("side", you.cardsToIds(you.side_arr));
            result.put("extra", you.cardsToIds(you.extra_arr));
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void doneSiding(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (you.done_siding == true) {
                return;
            }
            you.done_siding = true;
            you.resetAFKTimer();
            you.alterDeck();
            JSONObject result = new JSONObject();
            result.put("play", "Done siding");
            result.put("username", you.username);
            String public_entry = "Finished siding";
            publicGameResult(result, duel, public_entry, you, opp);
            if (opp.done_siding == true) {
                beginNextDuel(nbc, data, user, duel, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void beginNextDuel(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            duel.active = true;
            duel.paused = false;
            duel.player1.ready = false;
            duel.player2.ready = false;
            duel.player1.done_siding = false;
            duel.player2.done_siding = false;
            duel.player1.viewing = "";
            duel.player2.viewing = "";
            duel.games++;
            Boolean starting = true;
            if (you.agree_to_draw == true && opp.agree_to_draw == true) {
                starting = false;
            }
            duel.player1.refreshPlayer();
            duel.player2.refreshPlayer();
            duel.player1.startAFKTimer();
            duel.player2.startAFKTimer();
            duel.player1.randomizeDeck(101);
            duel.player2.randomizeDeck(201);
            duel.status = "Pick first";
            duel.currentPhase = "";
            duel.turn_player = "";
            duel.awaiting_admin = false;
            String action = "Begin next duel";
            String message = "";
            if (duel.type.equals("n") || duel.rpsWinner == null) {
                action = "Back to RPS";
                duel.status = "RPS";
            }
            duel.draw = false;

            JSONObject player1 = new JSONObject();
            player1.put("username", duel.player1.username);
            player1.put("main_total", duel.player1.main.size());
            player1.put("extra_total", duel.player1.extra.size());
            player1.put("token", duel.player1.token);
            player1.put("start", 101);

            JSONObject player2 = new JSONObject();
            player2.put("username", duel.player2.username);
            player2.put("main_total", duel.player2.main.size());
            player2.put("extra_total", duel.player2.extra.size());
            player2.put("token", duel.player2.token);
            player2.put("start", 201);

            /*JSONObject result = new JSONObject();
            result = new JSONObject();
            result.put("action", action);
            result.put("username", duel.rpsWinner);
            result.put("player1", player1);
            result.put("player2", player2);
            result.put("starting", starting);
            result.put("score", "(" + duel.player1.recorded_wins + "-" + duel.player2.recorded_wins + "-" + duel.player1.recorded_draws + ")");
            write(duel.player1.nbc, result);*/

            JSONObject result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", action);
            result.put("seconds", duel.seconds);
            result.put("username", duel.rpsWinner);
            result.put("player1", player2);
            result.put("player2", player1);
            result.put("starting", starting);
            result.put("score", "(" + duel.player2.recorded_wins + "-" + duel.player1.recorded_wins + "-" + duel.player2.recorded_draws + ")");
            write(duel.player2.nbc, result);

            result = new JSONObject();
            result.put("action", "Duel");
            result.put("play", action);
            result.put("seconds", duel.seconds);
            result.put("username", duel.rpsWinner);
            result.put("player1", player1);
            result.put("player2", player2);
            result.put("starting", starting);
            result.put("score", "(" + duel.player1.recorded_wins + "-" + duel.player2.recorded_wins + "-" + duel.player1.recorded_draws + ")");
            write(duel.player1.nbc, result);
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            duel.addReplay(result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void duelChat(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            String message = (String) data.get("message");
            int html = (int) data.get("html");
            JSONObject result = new JSONObject();
            if (user.duel.status.equals("Dueling")) {
                if (user.duel.player1.username.equals(user.username) || user.duel.player2.username.equals(user.username)) {
                    if (message.toLowerCase().indexOf("/draw") == 0) {
                        if (Integer.parseInt(message.substring(5, message.length()).trim()) >= 0 && Integer.parseInt(message.substring(5, message.length()).trim()) <= 10) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", Integer.parseInt(message.substring(5, message.length()).trim()));
                            drawCard(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                    else if (message.toLowerCase().indexOf("/mill") == 0) {
                        if (Integer.parseInt(message.substring(5, message.length()).trim()) >= 0 && Integer.parseInt(message.substring(5, message.length()).trim()) <= 10) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", Integer.parseInt(message.substring(5, message.length()).trim()));
                            millCard(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                    else if (message.toLowerCase().indexOf("/discardhand") == 0) {
                        if (opp.ready == false) {
                            return;
                        }
                        discardHand(nbc, data, user, duel, you, opp);
                        return;
                    }
                    else if (message.toLowerCase().indexOf("/banishhand") == 0) {
                        if (opp.ready == false) {
                            return;
                        }
                        banishHand(nbc, data, user, duel, you, opp);
                        return;
                    }
                    else if (message.toLowerCase().indexOf("/banishfd") == 0) {
                        if (Integer.parseInt(message.substring(9, message.length()).trim()) >= 0 && Integer.parseInt(message.substring(9, message.length()).trim()) <= 10) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", Integer.parseInt(message.substring(9, message.length()).trim()));
                            banishTopCardOfDeckFD(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                    else if (message.toLowerCase().indexOf("/banish") == 0) {
                        if (Integer.parseInt(message.substring(7, message.length()).trim()) >= 0 && Integer.parseInt(message.substring(7, message.length()).trim()) <= 10) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", Integer.parseInt(message.substring(7, message.length()).trim()));
                            banishTopCardOfDeck(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                    else if (message.toLowerCase().indexOf("/add") == 0) {
                        if (Integer.parseInt(message.substring(4, message.length()).trim()) >= 0) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", Integer.parseInt(message.substring(4, message.length()).trim()));
                            updateLifePoints(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                    else if (message.toLowerCase().indexOf("/sub") == 0) {
                        if (Integer.parseInt(message.substring(4, message.length()).trim()) >= 0) {
                            if (opp.ready == false) {
                                return;
                            }
                            data.put("amount", -Integer.parseInt(message.substring(4, message.length()).trim()));
                            updateLifePoints(nbc, data, user, duel, you, opp);
                            return;
                        }
                    }
                }
            }
            if (user.html < 1) {
                html = 0;
            }
            if (user.judge > 0) {
                if (user.greeted == false) {
                    if (message.toLowerCase().equals("problem?") || message.toLowerCase().equals("issue?") || message.toLowerCase().equals("ruling?") || message.toLowerCase().equals("glitch?") || message.toLowerCase().equals("issue ?")) {
                        errorE(nbc, "Remember to greet the players before anything else");
                        return;
                    }
                    else if (message.toLowerCase().equals("yo") || message.toLowerCase().indexOf("yo.") == 0) {
                    //else if (message.toLowerCase().indexOf("yo") == 0) {
                        message = "Hello";
                    }
                }
                user.greeted = true;
            }
            String color = "0000FF";
            switch (user.admin) {
                case 0:
                    color = "0000FF";
                    break;
                case 1:
                    color = "009900";
                    break;
                case 2:
                    color = "707070";
                    break;
                case 3:
                case 4:
                    color = "CC9900";
                    break;
            }
            if (user.adjudicator == 1) {
                //color = "00AA99";
                //color = "AA06B0";
                color = "660099";
            }
            result.put("message", message);
            String public_entry = "\"" + message + "\"";
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", user.username);
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            log.put("type", "chat");
            duel.addLog(log);
            
            Message duelMessage = newMessage();
            duelMessage.duel_id = duel.id;
            duelMessage.user_id = user.id;
            duelMessage.username = user.username;
            duelMessage.message = message;
            duelMessage.admin = user.admin;
            duelMessage.color = color;
            duelMessage.html = html;
            duel.DuelMessages.add(duelMessage);
            
            result.put("action", "Duel");
            result.put("play", "Duel message");
            result.put("seconds", duel.seconds);
            result.put("username", user.username);
            result.put("log", log);
            result.put("color", color);
            result.put("html", html);
            
            if (duel.player1.active == true) {
                write(duel.player1.nbc, result);
            }
            if (duel.player2.active == true) {
                write(duel.player2.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void watcherMessage(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String message = (String) data.get("message");
            int html = (int) data.get("html");
            String color = "0000FF";
            Duel duel = user.duel;
            JSONObject result = new JSONObject();
            if (user.frozen == true) {
                result.put("action", "Error");
                result.put("message", "Invalid request");
                result.put("text", message);
                write(nbc, result);
                return;
            }     
            if (user.html == 0) {
                html = 0;
            }
            if (user.muted == true) {
                invalidRequest(nbc);
                return;
            }
            if (user.username.equals(duel.player1.username) || user.username.equals(duel.player2.username)) {
                color = "660099";
            }
            if (user.admin > 0) {
                switch (user.admin) {
                    case 0:
                        color = "0000FF";
                        break;
                    case 1:
                        color = "009900";
                        break;
                    case 2:
                        color = "707070";
                        break;
                    case 3:
                    case 4:
                        color = "CC9900";
                        break;
                }
            }
            if (user.adjudicator == 1) {
                //color = "00AAFF";
                //color = "AA06B0";
                color = "660099";
            }
            //WatchMessage watchMessage = new WatchMessage();
            Message watchMessage = newMessage();
            watchMessage.duel_id = duel.id;
            watchMessage.user_id = user.id;
            watchMessage.username = user.username;
            watchMessage.color = color;
            watchMessage.message = message;
            watchMessage.html = html;
            watchMessage.hidden = user.ignored;
            WatchMessages.add(watchMessage);
            duel.WatchMessages.add(watchMessage);
            
            System.out.println("inserting into " + user.duel_id);
            System.out.println("inserting into " + duel.id);
            
            result.put("action", "Duel");
            result.put("play", "Watcher message");
            result.put("seconds", duel.seconds);
            result.put("message", message);
            result.put("html", html);
            result.put("username", user.username);
            result.put("color", color);
            if (user.ignored == 1) {
                write(nbc, result);
            }
            else {
                if (duel.player1.active == true) {
                    write(duel.player1.nbc, result);
                }
                if (duel.player2.active == true) {
                    write(duel.player2.nbc, result);
                }
                for (int i = 0; i < duel.watchers.size(); i++) {
                    write(duel.watchers.get(i).nbc, result);
                }
            }
            duel.addReplay(result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Card removeFromDeck(Player player, int card_id) {
        Card card = null;
        int i = 0;
        for (i = 0; i < player.main_arr.size(); i++) {
            if (player.main_arr.get(i).id == card_id) {
                card = player.main_arr.get(i);
                card.face_up = false;
                player.main_arr.remove(i);
                return card;
            }
        }
        if (i == player.main_arr.size()) {
            for (int j = 0; j < player.main_arr.size(); j++) {
                System.out.println("player.main_arr.get(" + j + ").name = " + player.main_arr.get(j).name);
            }
            return null;
        }
        return card;
    }
    
    public static Card removeFromExtra(Player player, int card_id) {
        //Card card = new Card();
        Card card = null;
        int i = 0;
        for (i = 0; i < player.extra_arr.size(); i++) {
            if (player.extra_arr.get(i).id == card_id) {
                card = player.extra_arr.get(i);
                player.extra_arr.remove(i);
                return card;
            }
        }
        if (i == player.extra_arr.size()) {
            for (int j = 0; j < player.extra_arr.size(); j++) {
                System.out.println("player.extra_arr.get(" + j + ").id = " + player.extra_arr.get(j).id);
            }
            return null;
        }
        return card;
    }
    
    public static Card removeFromGrave(Player player, int card_id) {
        //Card card = new Card();
        Card card = null;
        int i = 0;
        for (i = 0; i < player.grave_arr.size(); i++) {
            if (player.grave_arr.get(i).id == card_id) {
                card = player.grave_arr.get(i);
                player.grave_arr.remove(i);
                return card;
            }
        }
        if (i == player.grave_arr.size()) {
            for (int j = 0; j < player.grave_arr.size(); j++) {
                System.out.println("player.grave_arr.get(" + j + ").name = " + player.grave_arr.get(j).name);
            }
            return null;
        }
        return card;
    }
    
    public static Card removeFromBanished(Player player, int card_id, Boolean reset_face_down) {
        Card card;
        int i = 0;
        for (i = 0; i < player.banished_arr.size(); i++) {
            if (player.banished_arr.get(i).id == card_id) {
                card = player.banished_arr.get(i);
                if (reset_face_down == true) {
                    card.face_down = false;
                }
                player.banished_arr.remove(i);
                return card;
            }
        }
        return null;
    }
    
    public static void toGraveFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To grave from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Sent \"" + card.name + "\" from hand " + location[1] + " to Graveyard";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void discardHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            while (you.hand_arr.size() > 0) {
                JSONObject obj = new JSONObject();
                obj.put("card", you.hand_arr.get(0).id);
                toGraveFromHand(nbc, obj, user, duel, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            while (you.hand_arr.size() > 0) {
                JSONObject obj = new JSONObject();
                obj.put("card", you.hand_arr.get(0).id);
                banishFromHand(nbc, obj, user, duel, you, opp);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            System.out.println("banishFromHand entered");
            System.out.println("(int) data.get(\"card\") = " + (int) data.get("card"));
            int card_id = (int) data.get("card");
            System.out.println("d");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished \"" + card.name + "\" from hand " + location[1];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishFromHandFD(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish from hand FD");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Banished a card from hand " + location[1] + " face-down";
            String private_entry = "Banished \"" + card.name + "\" from hand " + location[1] + " face-down";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck from hand");
            result.put("id", card.id);
            String public_entry = "Placed card from hand " + location[1] + " to top of deck";
            String private_entry = "Placed \"" + card.name + "\" from hand " + location[1] + " to top of deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toBottomOfDeckFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            you.main_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To bottom of deck from hand");
            result.put("id", card.id);
            String public_entry = "Placed card from hand " + location[1] + " to bottom of deck";
            String private_entry = "Placed \"" + card.name + "\" from hand " + location[1] + " to bottom of deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toTopOfDeckFromExtra(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromExtra(you, card_id);
            if (card == null) {
                return;
            }
            you.main_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To top of deck from Extra");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned " + card.name + " to top of deck from Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void setMonsterToSTFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = true;
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Set monster to ST from hand");
            result.put("id", card.id);
            String public_entry = "Set a card from hand " + location[1] + " to " + zone;
            String private_entry = "Set \"" + card.name + "\" from hand " + location[1] + " to " + zone;
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void overlay(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int start_id = (int) data.get("start_card");
            int end_id = (int) data.get("end_card");
            Object[] start_location = getLocation(you, start_id);
            Card start_card = (Card) start_location[2];
            if (start_card == null) {
                return;
            }
            String start_name = (String) start_location[1];
            Object end_location[] = getLocation(you, end_id);
            Card end_card = (Card) end_location[2];
            String end_name = (String) end_location[1];
            start_card.xyz_arr.add(end_card);
            while (end_card.xyz_arr.size() > 0) {
                start_card.xyz_arr.add(end_card.xyz_arr.get(0));
                end_card.xyz_arr.remove(0);
            }
            removeFromField(you, start_id, true);
            replaceCard(you, start_card, end_card);
            JSONObject result = new JSONObject();
            result.put("play", "Overlay");
            result.put("start_id", start_id);
            result.put("end_id", end_id);
            String public_entry = "Overlayed " + start_location[1] + " onto " + end_name;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void detach(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeXyzMaterial(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.owner.grave_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Detach");
            result.put("id", card.id);
            String public_entry = "Detached \"" + card.name + "\" from " + location[1];
            String private_entry = "Detached \"" + card.name + "\" from " + location[2];
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void attack(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.currentPhase.equals("BP")) {
                return;
            }
            int attacking_id = (int) data.get("attacking_card");
            int attacked_id = (int) data.get("attacked_card");
            Object[] attacking_location = getLocation(you, attacking_id);
            Card attacking_card = (Card) attacking_location[2];
            if (attacking_card == null) {
                return;
            }
            String attacking_name = (String) attacking_location[1];
            Object attacked_location[] = getLocation(opp, attacked_id);
            Card attacked_card = (Card) attacked_location[2];
            if (attacked_card == null) {
                return;
            }
            //String attacked_name = (String) attacked_location[1];
            JSONObject result = new JSONObject();
            result.put("play", "Attack");
            result.put("attacking_id", attacking_id);
            result.put("attacked_id", attacked_id);
            String public_entry = "Attacked " + attacked_location[0] + " with " + attacking_location[1];
            //String private_entry = "Attacked " + attacked_location[1] + " with " + attacking_location[1];
            //privateDuelResult(result, duel, public_entry, private_entry, you, opp);
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void attackDirectly(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            if (!duel.currentPhase.equals("BP")) {
                return;
            }
            int card_id = (int) data.get("card");
            Object location[] = getLocation(you, card_id);
            Card card = (Card) location[2];
            if (card == null) {
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "Attack directly");
            result.put("id", card.id);
            String public_entry = "Attacked directly with " + location[0];
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void banishXyzMaterial(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeXyzMaterial(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.banished_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "Banish Xyz Material");
            result.put("id", card.id);
            String public_entry = "Banished \"" + card.name + "\" as Xyz Material from " + location[1];
            String private_entry = "Banished \"" + card.name + "\" as Xyz Material from " + location[2];
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void replaceCard(Player player, Card start_card, Card end_card) {
        if (player.m1 != null) {
            if (player.m1.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.m1 = start_card;
                return;
            }
        }
        if (player.m2 != null) {
            if (player.m2.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.m2 = start_card;
                return;
            }
        }
        if (player.m3 != null) {
            if (player.m3.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.m3 = start_card;
                return;
            }
        }
        if (player.m4 != null) {
            if (player.m4.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.m4 = start_card;
                return;
            }
        }
        if (player.m5 != null) {
            if (player.m5.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.m5 = start_card;
                return;
            }
        }
        if (player.duel.linkLeft != null) {
            if (player.duel.linkLeft.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.duel.linkLeft = start_card;
                player.linkLeft = start_card;
                return;
            }
        }
        if (player.duel.linkRight != null) {
            if (player.duel.linkRight.equals(end_card)) {
                removeFromField(player, end_card.id, true);
                player.duel.linkRight = start_card;
                player.linkRight = start_card;
                return;
            }
        }
        System.out.println("NONE");
    }
    
    public static Object[] removeXyzMaterial(Player player, int card_id) {
        Card card = null;
        Card root_monster = new Card();
        String name = "";
        String zone = "";
        String public_location = "";
        String private_location = "";
        
        if (player.m1 != null) {
            for (int i = 0; i < player.m1.xyz_arr.size(); i++) {
                if (player.m1.xyz_arr.get(i).id == card_id) {
                    card = player.m1.xyz_arr.get(i);
                    root_monster = player.m1;
                    zone = "M-1";
                    player.m1.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.m2 != null) {
            for (int i = 0; i < player.m2.xyz_arr.size(); i++) {
                if (player.m2.xyz_arr.get(i).id == card_id) {
                    card = player.m2.xyz_arr.get(i);
                    root_monster = player.m2;
                    zone = "M-2";
                    player.m2.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.m3 != null) {
            for (int i = 0; i < player.m3.xyz_arr.size(); i++) {
                if (player.m3.xyz_arr.get(i).id == card_id) {
                    card = player.m3.xyz_arr.get(i);
                    root_monster = player.m3;
                    zone = "M-3";
                    player.m3.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.m4 != null) {
            for (int i = 0; i < player.m4.xyz_arr.size(); i++) {
                if (player.m4.xyz_arr.get(i).id == card_id) {
                    card = player.m4.xyz_arr.get(i);
                    root_monster = player.m4;
                    zone = "M-5";
                    player.m4.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.m5 != null) {
            for (int i = 0; i < player.m5.xyz_arr.size(); i++) {
                if (player.m5.xyz_arr.get(i).id == card_id) {
                    card = player.m5.xyz_arr.get(i);
                    root_monster = player.m5;
                    zone = "M-5";
                    player.m5.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.duel.linkLeft != null) {
            for (int i = 0; i < player.duel.linkLeft.xyz_arr.size(); i++) {
                if (player.duel.linkLeft.xyz_arr.get(i).id == card_id) {
                    card = player.duel.linkLeft.xyz_arr.get(i);
                    root_monster = player.duel.linkLeft;
                    if (player.equals(player.duel.player1)) {
                        zone = "Left Extra Monster Zone";
                    }
                    else {
                        zone = "Right Extra Monster Zone";
                    }
                    player.duel.linkLeft.xyz_arr.remove(i);
                    break;
                }
            }
        }
        if (player.duel.linkRight != null) {
            for (int i = 0; i < player.duel.linkRight.xyz_arr.size(); i++) {
                if (player.duel.linkRight.xyz_arr.get(i).id == card_id) {
                    card = player.duel.linkRight.xyz_arr.get(i);
                    root_monster = player.duel.linkRight;
                    if (player.equals(player.duel.player1)) {
                        zone = "Right Extra Monster Zone";
                    }
                    else {
                        zone = "Left Extra Monster Zone";
                    }
                    player.duel.linkRight.xyz_arr.remove(i);
                    break;
                }
            }
        }
        //if (root_monster.inDEF == true) {
        if (root_monster.face_down == true) {
            public_location = "Set card in " + zone;
        }
        else {
            public_location = "\"" + root_monster.name + "\" in " + zone;
        }
        private_location = "\"" + root_monster.name + "\" in " + zone;
        if (card.id == 0) {
            card = null;
            System.out.println("Detach problem in " + player.username + "'s duel");
        }
        Object[] location = {card, public_location, private_location};
        return location;
    }
    
    public static void toSTFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.grave_arr.add(card);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "To ST from grave");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Moved " + card.name + " from Graveyard to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toSTFromDeck(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromDeck(you, card_id);
            if (card == null) {
                return;
            }
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.main_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "To ST from deck");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Moved " + card.name + " from Deck to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toSTFromHand(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromHand(you, card_id);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            String zone = getNextSTZone(card, you, data);
            if (zone == null) {
                you.hand_arr.add(card);
                stopAndShuffle(you);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("play", "To ST from hand");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Placed " + card.name + " from hand " + location[1] + " to " + zone;
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toExtraFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            card.face_down = true;
            card.owner.extra_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra from field");
            result.put("id", card.id);
            String public_entry = "Returned " + location[1] + " to Extra Deck";
            String private_entry = "Returned " + location[2] + " to Extra Deck";
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toExtraFUFromField(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Object[] location = removeFromField(you, card_id, false);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.extra_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra FU from field");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned " + location[1] + " to Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void move(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            String zone = (String) data.get("zone");
            Object[] location = removeFromField(you, card_id, true);
            Card card = (Card) location[0];
            if (card == null) {
                return;
            }
            if (zone.indexOf("S-") >= 0) {
                card.inATK = true;
                card.inDEF = false;
                detachAllMaterials(card);
                zone = getNextSTZone(card, you, data);
            }
            else {
                zone = getNextMonsterZone(card, you, data);
            }
            JSONObject result = new JSONObject();
            result.put("play", "Move");
            result.put("id", card.id);
            String public_entry = "Moved " + location[1] + " to " + zone;
            String private_entry = "Moved " + location[2] + " to " + zone;
            privateDuelResult(result, duel, public_entry, private_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toExtraFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.extra_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra from grave");
            result.put("id", card.id);
            String public_entry = "Returned " + card.name + " from Graveyard to Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toExtraFUFromGrave(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromGrave(you, card_id);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.extra_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra FU from grave");
            result.put("id", card.id);
            String public_entry = "Returned " + card.name + " from Graveyard to Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void toExtraFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, true);
            if (card == null) {
                return;
            }
            card.face_down = true;
            card.owner.extra_arr.add(card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned " + card.name + " from Banished to Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toExtraFUFromBanished(ChannelHandlerContext nbc, JSONObject data, User user, Duel duel, Player you, Player opp) {
        try {
            int card_id = (int) data.get("card");
            Card card = removeFromBanished(you, card_id, true);
            if (card == null) {
                return;
            }
            card.face_down = false;
            card.owner.extra_arr.add(0, card);
            JSONObject result = new JSONObject();
            result.put("play", "To Extra FU from banished");
            result.put("id", card.id);
            result.put("card", card.data);
            String public_entry = "Returned " + card.name + " from Banished to Extra Deck";
            publicDuelResult(result, duel, public_entry, you, opp);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void publicDuelResult(JSONObject result, Duel duel, String public_entry, Player you, Player opp) {
        try {
            String username = you.username;
            if (result.has("username")) {
                username = (String) result.get("username");
            }
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", username);
            log.put("public_log", public_entry);
            log.put("type", "duel");
            duel.addLog(log);
            
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("username", username);
            result.put("log", log);
            if (you.zone != null) {
                result.put("zone", you.zone);
                you.zone = null;
            }
            if (you.active == true && you.duel.id == duel.id) { // ??
                write(you.nbc, result);
            }
            if (opp.active == true) {
                write(opp.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void privateDuelResult(JSONObject result, Duel duel, String public_entry, String private_entry, Player you, Player opp) {
        try {
            Object card = null;
            Object deck = null;
            String username = you.username;
            if (result.has("username")) {
                username = (String) result.get("username");
            }
            
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", username);
            log.put("type", "duel");
            log.put("public_log", public_entry);
            log.put("private_log", public_entry);
            log.put("type", "duel");
            
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            result.put("username", username);
            if (you.zone != null) {
                result.put("zone", you.zone);
                you.zone = null;
            }
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
            result.put("log", log);
            if (result.has("card")) {
                card = result.get("card");
                result.remove("card");
            }
            if (result.has("deck")) {
                deck = result.get("deck");
                result.remove("deck");
            }
            if (opp.active == true) {
                write(opp.nbc, result);
            }
            if (card != null) {
                result.put("card", card);
            }
            if (deck != null) {
                result.put("deck", deck);
            }
            ((JSONObject) result.get("log")).remove("private_log");
            ((JSONObject) result.get("log")).put("private_log", private_entry);
            if (you.active == true) {
                write(you.nbc, result);
            }
            duel.addReplay(result);
            duel.addLog(log);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void publicGameResult(JSONObject result, Duel duel, String public_entry, Player you, Player opp) {
        try {
            result.put("action", "Duel");
            result.put("seconds", duel.seconds);
            if (!result.has("username")) {
                result.put("username", you.username);
            }
            JSONObject log = new JSONObject();
            //log.put("timestamp", duel.timestamp);
            log.put("username", result.get("username"));
            log.put("public_log", public_entry);
            //log.put("private_log", public_entry);
            log.put("type", "game");
            duel.addLog(log);
            
            result.put("log", log);
            if (you.active == true) {
                write(you.nbc, result);
            }
            if (opp.active == true) {
                write(opp.nbc, result);
            }
            duel.addReplay(result);
            result.remove("log");
            for (int i = 0; i < duel.watchers.size(); i++) {
                write(duel.watchers.get(i).nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void recycleDuel(Duel duel) {
        Duels.remove(duel);
        if (duel == null) {
            System.out.println("Duel was null");
            return;
        }
        if (UpdatingRatings == true) {
            duel.saveReplayToDB();
        }
        recycleMessages(duel.DuelMessages);
        recycleMessages(duel.WatchMessages);
        duel.player1.init();
        duel.player2.init();
        RecycledPlayers.add(duel.player1);
        RecycledPlayers.add(duel.player2);
        duel.init();
        if (!RecycledDuels.contains(duel)) {
            RecycledDuels.add(duel);
        }
        else {
            System.out.println("RecycledDuels already contains duel");
        }
    }
    
    public static Duel newDuel() {
        if (RecycledDuels.size() > 0) {
            Duel duel = RecycledDuels.remove(0);
            duel.init();
            System.out.println("Getting recycled duel");
            return duel;
        }
        TotalDuelObjects++;
        return new Duel();
    }
    
    public static Player newPlayer() {
        if (RecycledPlayers.size() > 0) {
            return RecycledPlayers.remove(0);
        }
        TotalPlayers++;
        return new Player();
    }
    
    public static Message newMessage() {
        if (RecycledMessages.size() > 0) {
            Message message = RecycledMessages.remove(0);
            message.init();
            return message;
        }
        TotalMessages++;
        return new Message();
    }
    
    public static Status newStatus() {
        if (RecycledStatuses.size() > 0) {
            Status status = RecycledStatuses.remove(0);
            status.init();
            return status;
        }
        TotalStatuses++;
        return new Status();
    }
    
    public static JournalEntry newJournalEntry() {
        if (RecycledJournalEntries.size() > 0) {
            JournalEntry je = RecycledJournalEntries.remove(0);
            je.init();
            return je;
        }
        TotalJournalEntries++;
        return new JournalEntry();
    }
    
    public static DuelEntry newDuelEntry() {
        if (RecycledDuelEntries.size() > 0) {
            DuelEntry de = RecycledDuelEntries.remove(0);
            de.init();
            return de;
        }
        TotalDuelEntries++;
        return new DuelEntry();
    }
    
    public static Friend newFriend() {
        if (RecycledFriends.size() > 0) {
            System.out.println("recycling friend");
            Friend friend = RecycledFriends.remove(0);
            friend.init();
            return friend;
        }
        TotalFriends++;
        return new Friend();
    }
    
    public static Watcher newWatcher() {
        if (RecycledWatchers.size() > 0) {
            return RecycledWatchers.remove(0);
        }
        TotalWatchers++;
        return new Watcher();
    }
    
    public static void invalidRequest(ChannelHandlerContext nbc) {
        errorE(nbc, "Invalid request");
    }
    
    public static void timedOut(ChannelHandlerContext nbc) {
        System.out.println("timedOut entered for " + getUserByNbc(nbc).username);
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Timed out");
            if (nbc != null) {
                write(nbc, result);
                offlineUser(nbc, true);
                // if instead of offlineUser(nbc) you say nbc.close(), websocket users will stay online somehow
            }
        }
        catch (Exception e) {
            System.out.println("15137");
            System.err.println(e.getMessage());
        }
    }
    
    public static void lostConnection(ChannelHandlerContext nbc, User user, Boolean do_write) {
        try {
            System.out.println("lostConnection entered for " + user.username);
            if (ReportingLostConnections == true) {
                InfoLog += user.username + " has lost connection\n";
            }
            user.times_lost_connection++;
            user.lost_connection = true;
            user.lost_connection_timestamp = new Timestamp(System.currentTimeMillis());
            if (do_write == true) {
                JSONObject result = new JSONObject();
                result.put("action", "Lost connection");
                write(nbc, result);
            }
            offlineUser(nbc, do_write);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("20783");
    }
    
    public static Boolean isOnline(String username) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i) != null) {
                if (Users.get(i).username != null) {
                    if (Users.get(i).username.equalsIgnoreCase(username)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static Boolean isPuffinIP(String str) {
        if (str == null) {
            return false;
        }
        if (str.indexOf("45.33.") == 0 || str.indexOf("107.178") == 0) {
            return true;
        }
        return false;
    }
    
    public static String getDaysLeft(long then) {
        long now = System.currentTimeMillis();
        long difference = then - now;
        long days = difference / 86400000;
        if (days == 0) {
            return " less than 1 day";
        }
        return " " + (days + 1) + " days";
    }
    
    public static int getDaysLeftInt(long then) {
        long now = System.currentTimeMillis();
        long difference = then - now;
        long days = difference / 86400000;
        if (days == 0) {
            return 1;
        }
        return (int) days + 1;
    }
    
    public static String getDetailedTimeAgo(long then) {
        long now = new java.util.Date().getTime();
        long difference = now - then;
        String ago = "";
        if (difference < 60000) {
            long seconds = difference / 1000;
            if (seconds == 1) {
                ago += seconds + " second ago";
            }
            else {
                ago += seconds + " seconds ago";
            }
        }
        else if (difference < 3600000) {
            long minutes = difference / 60000;
            if (minutes == 1) {
                ago = minutes + " minute, ";
            }
            else {
                ago = minutes + " minutes, ";
            }
            long seconds = difference % 60000 / 1000;
            if (seconds == 1) {
                ago += seconds + " second ago";
            }
            else {
                ago += seconds + " seconds ago";
            }
        }
        else if (difference < 86400000) {
            long hours = difference / 3600000;
            if (hours == 1) {
                ago += hours + " hour, ";
            }
            else {
                ago += hours + " hours, ";
            }
            long minutes = difference % 3600000 / 60000;
            if (minutes == 1) {
                ago += minutes + " minute ago";
            }
            else {
                ago += minutes + " minutes ago";
            }
        }
        else if (difference < 604800000) {
            long days = difference / 86400000;
            if (days == 1) {
                ago += days + " day, ";
            }
            else {
                ago += days + " days, ";
            }
            long hours = difference % 86400000 / 3600000;
            if (hours == 1) {
                ago += hours + " hour ago";
            }
            else {
                ago += hours + " hours ago";
            }
        }
        else if (difference < 2592000000L) {
            long weeks = difference / 604800000;
            if (weeks == 1) {
                ago += weeks + " week, ";
            }
            else {
                ago += weeks + " weeks, ";
            }
            long days = difference % 604800000 / 86400000;
            if (days == 1) {
                ago += days + " day ago";
            }
            else {
                ago += days + " days ago";
            }
        }
        else if (difference < 31536000000L) {
            long months = difference / 2592000000L;
            if (months == 1) {
                ago += months + " month, ";
            }
            else {
                ago += months + " months, ";
            }
            long weeks = difference % 2592000000L / 604800000;
            if (weeks == 1) {
                ago += weeks + " week ago";
            }
            else {
                ago += weeks + " weeks ago";
            }
        }
        else {
            long years = difference / 31536000000L;
            if (years == 1) {
                ago += years + " year, ";
            }
            else {
                ago += years + " years, ";
            }
            long months = difference % 31536000000L / 2592000000L;
            if (months == 1) {
                ago += months + " month ago";
            }
            else {
                ago += months + " months ago";
            }
        }
        return ago;
    }
    
    public static String getTimeRemaining(long then) {
        String str = getDetailedTimeRemaining(then);
        if (str.indexOf(",") >= 0) {
            str = str.substring(0, str.indexOf(","));
        }
        return str;
    }
    
    public static String getDetailedTimeRemaining(long then) {
        long now = new java.util.Date().getTime();
        long difference = then - now;
        String ago = "";
        if (difference < 60000) {
            long seconds = difference / 1000;
            if (seconds == 1) {
                ago += seconds + " second";
            }
            else {
                ago += seconds + " seconds";
            }
        }
        else if (difference < 3600000) {
            long minutes = difference / 60000;
            if (minutes == 1) {
                ago = minutes + " minute, ";
            }
            else {
                ago = minutes + " minutes, ";
            }
            long seconds = difference % 60000 / 1000;
            if (seconds == 1) {
                ago += seconds + " second";
            }
            else {
                ago += seconds + " seconds";
            }
        }
        else if (difference < 86400000) {
            long hours = difference / 3600000;
            if (hours == 1) {
                ago += hours + " hour, ";
            }
            else {
                ago += hours + " hours, ";
            }
            long minutes = difference % 3600000 / 60000;
            if (minutes == 1) {
                ago += minutes + " minute";
            }
            else {
                ago += minutes + " minutes";
            }
        }
        else if (difference < 604800000) {
            long days = difference / 86400000;
            if (days == 1) {
                ago += days + " day, ";
            }
            else {
                ago += days + " days, ";
            }
            long hours = difference % 86400000 / 3600000;
            if (hours == 1) {
                ago += hours + " hour";
            }
            else {
                ago += hours + " hours";
            }
        }
        else if (difference < 2592000000L) {
            long weeks = difference / 604800000;
            if (weeks == 1) {
                ago += weeks + " week, ";
            }
            else {
                ago += weeks + " weeks, ";
            }
            long days = difference % 604800000 / 86400000;
            if (days == 1) {
                ago += days + " day";
            }
            else {
                ago += days + " days";
            }
        }
        else if (difference < 31536000000L) {
            long months = difference / 2592000000L;
            if (months == 1) {
                ago += months + " month, ";
            }
            else {
                ago += months + " months, ";
            }
            long weeks = difference % 2592000000L / 604800000;
            if (weeks == 1) {
                ago += weeks + " week";
            }
            else {
                ago += weeks + " weeks";
            }
        }
        else {
            long years = difference / 31536000000L;
            if (years == 1) {
                ago += years + " year, ";
            }
            else {
                ago += years + " years, ";
            }
            long months = difference % 31536000000L / 2592000000L;
            if (months == 1) {
                ago += months + " month";
            }
            else {
                ago += months + " months";
            }
        }
        return ago;
    }
    
    public static String removeAgo(String str) {
        if (str.indexOf(" ago") >= 0) {
            return str.substring(0, str.indexOf(" ago"));
        }
        return str;
    }
    
    public static String getDetailedDaysLeft(long then) {
        long now = new java.util.Date().getTime();
        long difference = then - now;
        String ago = "";
        if (difference < 60000) {
            long seconds = difference / 1000;
            if (seconds == 1) {
                ago += seconds + " second";
            }
            else {
                ago += seconds + " seconds";
            }
        }
        else if (difference < 3600000) {
            long minutes = difference / 60000;
            if (minutes == 1) {
                ago = minutes + " minute, ";
            }
            else {
                ago = minutes + " minutes, ";
            }
            long seconds = difference % 60000 / 1000;
            if (seconds == 1) {
                ago += seconds + " second";
            }
            else {
                ago += seconds + " seconds";
            }
        }
        else if (difference < 86400000) {
            long hours = difference / 3600000;
            if (hours == 1) {
                ago += hours + " hour, ";
            }
            else {
                ago += hours + " hours, ";
            }
            long minutes = difference % 3600000 / 60000;
            if (minutes == 1) {
                ago += minutes + " minute";
            }
            else {
                ago += minutes + " minutes";
            }
        }
        else {
            long days = difference / 86400000;
            if (days == 1) {
                ago += days + " day, ";
            }
            else {
                ago += days + " days, ";
            }
            long hours = difference % 86400000 / 3600000;
            if (hours == 1) {
                ago += hours + " hour";
            }
            else {
                ago += hours + " hours";
            }
        }
        return ago;
    }
    
    public static String getTimeAgo(long then) {
        long now = new java.util.Date().getTime();
        long difference = now - then;
        String ago = "";
        if (difference < 60000) {
            long seconds = difference / 1000;
            if (seconds == 1) {
                ago = seconds + " second ago";
            }
            else {
                if (seconds <= 0) {
                    ago = "Just now";
                }
                else {
                    ago = seconds + " seconds ago";
                }
            }
        }
        else if (difference < 3600000) {
            long minutes = difference / 60000;
            if (minutes == 1) {
                ago = minutes + " minute ago";
            }
            else {
                ago = minutes + " minutes ago";
            }
        }
        else if (difference < 86400000) {
            long hours = difference / 3600000;
            if (hours == 1) {
                ago = hours + " hour ago";
            }
            else {
                ago = hours + " hours ago";
            }
        }
        else if (difference < 604800000) {
            long days = difference / 86400000;
            if (days == 1) {
                ago += days + " day ago";
            }
            else {
                ago += days + " days ago";
            }
        }
        else if (difference < 2592000000L) {
            long weeks = difference / 604800000;
            if (weeks == 1) {
                ago += weeks + " week ago";
            }
            else {
                ago += weeks + " weeks ago";
            }
        }
        else if (difference < 31536000000L) {
            long months = difference / 2592000000L;
            if (months == 1) {
                ago += months + " month ago";
            }
            else {
                ago += months + " months ago";
            }
        }
        else {
            long years = difference / 31536000000L;
            if (years == 1) {
                ago += years + " year ago";
            }
            else {
                ago += years + " years ago";
            }
        }
        return ago;
    }
    
    public static int getSecondsAgo(long then) {
        long now = new java.util.Date().getTime();
        long difference = now - then;
        String ago = "";
        long seconds = difference / 1000;
        return (int) seconds;
    }
    
    public static String getSuffix(String day_string) {
        int day_int = Integer.parseInt(day_string);
        return Suffixes[day_int];
    }
    
    public static User getFromUserStates(String username, String password, String ip_address, String db_id) {
        User user = null;
        try {
            for (int i = 0; i < UserStates.size(); i++) {
                if (UserStates.get(i) != null) {
                    if (UserStates.get(i).username != null && UserStates.get(i).username.equalsIgnoreCase(username)) {
                        user = UserStates.get(i);
                        if (user.password == null || !user.password.equals(password)) {
                            try {
                                String query = "SELECT * FROM login_table WHERE username = '" + escapeForSQL(username) + "' AND password = '" + escapeForSQL(password) + "'";
                                Statement st = getConnection().createStatement();
                                ResultSet rs = executeQuery(st, query);
                                if (rs.next()) {
                                    st.close();
                                    UserStates.remove(user);
                                    return user;
                                }
                                else {
                                    st.close();
                                    return null;
                                }
                            }
                            catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                        else {
                            user.login_complete = true;
                            UserStates.remove(user);
                            return user;
                        }
                    }
                }
                else {
                    System.out.println("USER IS NULL");
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (UserStates.size() > 3000) {
            user = UserStates.get(UserStates.size() - 1);
            UserStates.remove(user);
            recycleFriends(user.friends_arr);
            user.init();
            return user;
        }
        TotalUsers++;
        return new User();
    }
    
    public static String stripSpaces(String str) {
	while (str.indexOf("\r\r\r") >= 0) {
            str = str.replace("\r\r\r", "\r\r");
	}
	while (str.indexOf("  ") >= 0) {
            str = str.replace("  ", " ");
	}
	return str;
    }
    
    public static String replaceReturns(String str) {
	while (str.indexOf("\r") >= 0) {
            str = str.replace("\r", "\n");
	}
	return str;
    }
    
    public static String escapeForSQL(String str) {
        if (str == null) {
            return "NULL";
        }
        str = str.replaceAll("'", "''");
        str = str.replace("\\", "\\\\");
        return str;
    }
    
    public static String escapeHTML(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }
    
    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        return NormailizePattern.matcher(nfdNormalizedString).replaceAll("");
    }
    
    public static Map<String, String> getQueryMap(String query) {  
        String[] params = query.split("&");  
        Map<String, String> map = new HashMap<String, String>();  
        for (String param : params) {  
            String name = param.split("=")[0];  
            String value = param.split("=")[1];  
            map.put(name, value);  
        }  
        return map;
    }  
    
    public static void cautionE(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.moderator < 1) {
                invalidRequest(nbc);
                return;
            }
            caution = false;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void toggleRated(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 4 && user.moderator < 2) {
                invalidRequest(nbc);
                return;
            }
            String message = "Advanced (Rated) is now enabled";
            if (PoolEnabled == true) {
                PoolEnabled = false;
                message = "Advanced (Rated) is now disabled";
            }
            else {
                PoolEnabled = true;
            }
            messageE(nbc, message);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void getActions(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 1 && user.moderator < 2 && user.adjudicator < 1) {
                invalidRequest(nbc);
                return;
            }
            String username = (String) data.get("username");
            JSONObject result = new JSONObject();
            User person = getUser(username);
            if (person == null) {
                errorE(nbc, username + " is not online");
                return;
            }
            if (person.username == null) {
                errorE(nbc, username + "'s username is null");
                return;
            }
            if (user.moderator < 2) {
                if (person.duel_id != user.duel_id) {
                    errorE(nbc, "You must be in the same duel as this user to use this tool");
                    return;
                }
            }
            String actions = escapeHTML(person.username) + "<br><br>";
            for (int i = 0; i < person.actions.size(); i++) {
                if (person.actions.get(i).get("action").equals("START DUEL")) {
                    actions += "<font color=\"#FF0000\">Duel started (" + escapeHTML((String) person.actions.get(i).get("message")) + ")</font>";
                }
                else {
                    actions += person.actions.get(i).get("action");
                }
                if (person.actions.get(i).has("play")) {
                    actions += " | " + person.actions.get(i).get("play");
                }
                actions += "<font color=\"#AAAAAA\"> - " + getDetailedTimeAgo((long) person.actions.get(i).get("time")) + "</font><br>";
            }
            if (person.actions.size() > 1 && getSecondsAgo((long) person.actions.get(person.actions.size() - 1).get("time")) > 32) {
                actions += "<font color=\"#FF0000\">User may have lost connection</font><br>";
            }
            result.put("action", "Long message");
            result.put("message", actions);
            result.put("html", true);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void getScreenshot(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 1 && user.moderator < 1 && user.adjudicator < 1) {
                invalidRequest(nbc);
                return;
            }
            String username = (String) data.get("username");
            JSONObject result = new JSONObject();
            User person = getUser(username);
            if (person != null) {
                if (user.moderator < 1) {
                    if (user.duel_id == 0) {
                        invalidRequest(nbc);
                        return;
                    }
                    if (person.duel_id == 0) {
                        errorE(nbc, person.username + " is not dueling");
                        return;
                    }
                }
            }
            else {
                errorE(nbc, username + " is not online");
                return;
            }
            result.put("action", "Get screenshot");
            result.put("username", user.username);
            write(person.nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void sendScreenshot(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            String username = (String) data.get("username");
            String link = (String) data.get("link");
            JSONObject result = new JSONObject();
            if (username.equals("Duelingbook")) {
                InfoLog += user.username + "'s screenshot: " + link + "\n";
                return;
            }
            User person = getUser(username);
            if (person != null) {
                if (person.admin < 1 && person.moderator < 1 && person.adjudicator < 1) {
                    return;
                }
                if (link.indexOf("https://imgur.com/") != 0 && link.indexOf("https://i.imgur.com") != 0) {
                    return;
                }
                result.put("action", "Send screenshot");
                result.put("link", link);
                write(person.nbc, result);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void checkDecks(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 1 && user.moderator < 1) {
                invalidRequest(nbc);
                return;
            }
            String username = (String) data.get("username");
            JSONObject result = new JSONObject();
            User person = getUser(username);
            if (person == null) {
                errorE(nbc, username + " is not online");
                return;
            }
            username = person.username;
            String message = escapeHTML(person.user_username) + "\nDefault Deck: " + escapeHTML(person.default_deck) + "\n";
            message += "Beginner: ";
            if (person.beginner == 1) {
                message += "Yes\n\n";
            }
            else {
                message += "No\n\n";
            }
            for (int i = 0; i < person.decks.size(); i++) {
                if (person.decks.get(i).get("legality").equals("Unlimited") && (int) person.decks.get(i).get("goat") == 0) {
                    message += "<u><a href=\"https://www.duelingbook.com/deck?id=" + person.decks.get(i).get("id") + "\" target=\"_blank\"><font color=\"#0000FF\">" + escapeHTML((String) person.decks.get(i).get("name")) + "</font></a></u> ";
                }
                else if (person.decks.get(i).get("legality").equals("Advanced") && (int) person.decks.get(i).get("tcg") == 0 && (int) person.decks.get(i).get("ocg") == 0) {
                    message += "<u><a href=\"https://www.duelingbook.com/deck?id=" + person.decks.get(i).get("id") + "\" target=\"_blank\"><font color=\"#0000FF\">" + escapeHTML((String) person.decks.get(i).get("name")) + "</font></a></u> ";
                }
                else {
                    message += person.decks.get(i).get("name") + " ";
                }
                if (person.decks.get(i).get("legality").equals("Advanced")) {
                    message += "<font color=\"#009900\">Advanced</font>";
                }
                else if (person.decks.get(i).get("legality").equals("")) {
                    message += "<font color=\"#777777\">Unknown</font>";
                }
                else if (person.decks.get(i).get("legality").equals("Illegal")) {
                    message += "<font color=\"#777777\">Under 40 cards</font>";
                }
                else {
                    message += "<font color=\"#777777\">" + person.decks.get(i).get("legality") + "</font>";
                }
                if ((int) person.decks.get(i).get("tcg") == 1) {
                    message += ", <font color=\"#777777\">TCG</font>";
                }
                if ((int) person.decks.get(i).get("ocg") == 1) {
                    message += ", <font color=\"#777777\">OCG</font>";
                }
                if ((int) person.decks.get(i).get("goat") == 1) {
                    message += ", <font color=\"#777777\">GOAT</font>";
                }
                message += "\n";
            }
            result.put("action", "Long message");
            result.put("message", message);
            result.put("html", true);
            write(nbc, result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static String getDuelFormat(String str) {
	switch (str) {
            case "ar":
                return "Advanced (Rated)";
            case "au":
                return "Advanced (Unrated)";
            case "tu":
                return "Traditional";
            case "gu":
                return "Goat Format";
            case "ju":
                return "Expert";
            case "uu":
                return "Unlimited";
            case "dr":
                return "Duelroulette";
            case "tg":
                return "Tag Duels";
            case "cu":
                return "Custom Cards";
	}
        return "";
    }
    
    public static String getDuelType(String str) {
	switch (str) {
            case "s":
                return "Single (with siding)";
            case "n":
                return "Single (no siding)";
            case "m":
                return "2 out of 3 match";
	}
        return "";
    }
    
    public static void resetDecks(ChannelHandlerContext nbc, JSONObject data, User user) {
        try {
            if (user.admin < 3) {
                invalidRequest(nbc);
                return;
            }
            JSONObject result = new JSONObject();
            result.put("action", "Reset decks");
            broadcastE(result);
            messageE(nbc, "Decks have been updated");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void purgeImage(JSONObject data) {
        try {
            JSONObject result = new JSONObject();
            result.put("action", "Purge image");
            result.put("url", data.get("url"));
            broadcastE(result);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void startQPSTimer() {
        qpsTimer.scheduleAtFixedRate(qpsTimeoutTask,1000,1000);
    }
    
    public static Connection getConnection() {
        QPS++;
        ConnectionIndex++;
        if (ConnectionIndex >= Connections.size()) {
            ConnectionIndex = 0;
        }
        return Connections.get(ConnectionIndex);
    }
    
    public static ResultSet executeQuery(Statement st, String query) {
        try {
            if (ReportingQueries == true) {
                System.out.println("query = " + query);
            }
            return st.executeQuery(query);
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public static int executeUpdate(Statement st, String query) {
        try {
            if (ReportingQueries == true) {
                System.out.println("query = " + query);
            }
            return st.executeUpdate(query);
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }
    
    public static void createConnection() {
        try {
            Connections.add(newConnection());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Connection newConnection() {
        Connection connection = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://" + DB_URL + ":3306/" + DB_DATABASE + "?autoReconnect=true&failOverReadOnly=false&useUnicode=yes&CharSet=utf8mb4&character_set_server=utf8mb4&noAccessToProcedureBodies=true";
            Class.forName(myDriver);
            connection = DriverManager.getConnection(myUrl, DB_USERNAME, DB_PASSWORD);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }
    
    public static void errorE(ChannelHandlerContext nbc, String message) {
        JSONObject result = new JSONObject();
        result.put("action", "Error");
        result.put("message", message);
        write(nbc, result);
    }
    
    public static void messageE(ChannelHandlerContext nbc, String message) {
        JSONObject result = new JSONObject();
        result.put("action", "Message");
        result.put("message", message);
        write(nbc, result);
    }
    
    public static void broadcastE(Object result) {
        for (int i = 0; i < Users.size(); i++) {
            write(Users.get(i).nbc, result);
        }
    }
    
    public static Boolean write(final ChannelHandlerContext ctx, final Object result) {
        try {
            if (ctx == null) { 
                return true;
            }
            byte[] response = null;
            if (ctx.pipeline().names().contains("flashHandler")) {
                response = new String(result.toString() + "\0").getBytes();
            }
            else if (ctx.pipeline().names().contains("websocketHandler")) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame(result.toString()));
                return true;
            }
            else {
                response = new String(result.toString()).getBytes();
            }
            ByteBuf buf = ctx.alloc().heapBuffer(response.length);
            ByteBuf encoded = buf.writeBytes(response);
            ctx.writeAndFlush(encoded);
            return true;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public static String hex2bin(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        return bin;
    }
    
    public static String getUsername(ChannelHandlerContext nbc) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i) != null) {
                if (Users.get(i).nbc != null) {
                    if (Users.get(i).nbc.equals(nbc)) {
                        return Users.get(i).username;
                    }
                }
            }
        }
        return "";
    }
    
    public static User getUserByNbc(ChannelHandlerContext nbc) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i) != null) {
                if (Users.get(i).nbc != null) {
                    if (Users.get(i).nbc.equals(nbc)) {
                        return Users.get(i);
                    }
                }
            }
        }
        return null;
    }
    
    public static User getUser(String username) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i) != null) {
                if (Users.get(i).username.equalsIgnoreCase(username)) {
                    return Users.get(i);
                }
            }
        }
        return null;
    }
    
    public static User getUserByID(int user_id) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i) != null) {
                if (Users.get(i).id == user_id) {
                    return Users.get(i);
                }
            }
        }
        return null;
    }
    
    public static Boolean sameUser(User person, User user) {
        if (person.ip_address.equals(user.ip_address)) {
            return true;
        }
        if (person.nbc_address.equals(user.nbc_address)) {
            return true;
        }
        if (person.db_id.equals(user.db_id)) {
            return true;
        }
        return false;
    }
    
    public static String randomHex(int num) {
        Object[] arr = {"a","b","c","d","e","f","g","h","i","j","k","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
        String str = "";
        for (int i = 0; i < num; i++) {
            str += arr[(int) Math.floor(Math.random() * arr.length)];
        }
        return str;
    }
}