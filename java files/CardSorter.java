package nettysocketserver;

import java.util.Comparator;

class CardSorter implements Comparator<Card> {
    @Override
    public int compare(Card a, Card b) {
        if (a == null) {
            return 0;
        }
        if (b == null) {
            return 0;
        }
        if (a.name == null) {
            return 0;
        }
        if (b.name == null) {
            return 0;
        }
        return a.name.toLowerCase().compareTo(b.name.toLowerCase());
    }
}