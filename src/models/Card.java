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

    public int getRank() {
        return rank.getValue();
    }

    public int getSuit() {
        return suit.getValue();
    }
}
