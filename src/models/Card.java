package models;

import enums.Rank;
import enums.Suit;

public class Card {
    private final Rank rank;
    private final Suit suit;

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    static int compare(Card a, Card b) {
        if (a.getRank() > b.getRank()) {
            return 1;
        } else if (a.getRank() < b.getRank()) {
            return -1;
        }

        if (a.getSuit() > b.getSuit()) {
            return 1;
        }
        return -1;
    }

    public int getRank() {
        return rank.getValue();
    }

    public int getSuit() {
        return suit.getValue();
    }
}
