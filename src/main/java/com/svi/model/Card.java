package com.svi.model;

import com.svi.model.cardenum.Rank;
import com.svi.model.cardenum.Suit;

public record Card(Rank rank, Suit suit) {

    public static int compare(Card a, Card b) {
        if (a.getRankValue() > b.getRankValue()) {
            return 1;
        } else if (a.getRankValue() < b.getRankValue()) {
            return -1;
        }

        if (a.getSuitValue() > b.getSuitValue()) {
            return 1;
        } else if (a.getSuitValue() < b.getSuitValue()) {
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
        return (suit.getValue() > 2 ? "\u001B[48;2;224;17;95m\u001B[30m" : "\u001B[40m\u001B[37m")
                + symbol + suit
                + "-"
                + (rank.getValue() < 11 ? rank.getValue() : rank)
                + "\u001B[0m";
    }

    public int getRankValue() {
        return rank.getValue();
    }

    public int getSuitValue() {
        return suit.getValue();
    }
}
