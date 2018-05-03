package nettysocketserver;

import java.util.Comparator;

class TimestampSorter implements Comparator<User> {
    @Override
    public int compare(User a, User b) {
        if (a == null) {
            return 0;
        }
        if (b == null) {
            return 0;
        }
        if (a.order == null) {
            System.out.println("a.order is null");
            return 0;
        }
        switch (a.order) {
            case "connect":
                if (a.connect_timestamp == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.connect_timestamp == null) {
                    //b.error = "true";
                    return 0;
                }
                return b.connect_timestamp.compareTo(a.connect_timestamp);
            case "connect2":
                if (a.connect_timestamp == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.connect_timestamp == null) {
                    //b.error = "true";
                    return 0;
                }
                return a.connect_timestamp.compareTo(b.connect_timestamp);
            case "connection":
                if (a.connection_timestamp == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.connection_timestamp == null) {
                    //b.error = "true";
                    return 0;
                }
                return b.connection_timestamp.compareTo(a.connection_timestamp);
            case "connection2":
                if (a.connection_timestamp == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.connection_timestamp == null) {
                    //b.error = "true";
                    return 0;
                }
                return a.connection_timestamp.compareTo(b.connection_timestamp);
            case "username":
                if (a.username == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.username == null) {
                    //b.error = "true";
                    return 0;
                }
                return a.username.compareTo(b.username);
            case "username2":
                if (a.username == null) {
                    //a.error = "true";
                    return 0;
                }
                if (b.username == null) {
                    //b.error = "true";
                    return 0;
                }
                return b.username.compareTo(a.username);
            case "lost":
                if (b.lost_connection == true) {
                    if (a.lost_connection == true) {
                        return b.lost_connection_timestamp.compareTo(a.lost_connection_timestamp);
                    }
                    return a.lost_connection.compareTo(b.lost_connection);
                }
                return 0;
            case "lost2":
                if (b.lost_connection == true) {
                    if (a.lost_connection == true) {
                        return a.lost_connection_timestamp.compareTo(b.lost_connection_timestamp);
                    }
                    return b.lost_connection.compareTo(a.lost_connection);
                }
                return 0;
            case "online":
                return a.online.compareTo(b.online);
            case "online2":
                return b.online.compareTo(a.online);
            case "times":
                return Integer.compare(b.times_lost_connection, a.times_lost_connection);
            case "times2":
                return Integer.compare(a.times_lost_connection, b.times_lost_connection);
        }
        return 0;
    }
}