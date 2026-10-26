package com.svi.models;

import com.svi.enums.Rank;
import com.svi.enums.Suit;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    public static int compare(Card a, Card b) {
        if (a.getRank() > b.getRank()) {
            return 1;
        } else if (a.getRank() < b.getRank()) {
            return -1;
        }

        if (a.getSuit() > b.getSuit()) {
            return 1;
        } else if (a.getSuit() < b.getSuit()) {
            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        String symbol = switch (suit) {
            case Suit.D -> "♦";
            case Suit.H -> "♥";
            case Suit.S -> "♠";
            case Suit.C -> "♣";
        };
        return (suit.getValue() > 2 ? "\u001B[48;2;224;17;95m\u001B[30m" : "\u001B[40m")
                + symbol
                + "-"
                + (rank.getValue() < 11 ? rank.getValue() : rank)
                + "\u001B[0m";
    }

    public int getRank() {
        return rank.getValue();
    }

    public int getSuit() {
        return suit.getValue();
    }
}
