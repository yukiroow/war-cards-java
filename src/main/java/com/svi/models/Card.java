package com.svi.models;

import com.svi.enums.Rank;
import com.svi.enums.Suit;

/**
 * Represents a Card in the War Cards game.
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    /**
     * Constructs a Card from a given Rank and Suit
     *
     * @param rank the given Rank
     * @param suit the given Suit
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Compares two Cards for order based on their rank and suit.
     *
     * <p>
     * The primary criteria is the card's rank. If the rank's are equal, they are compared by their suit.
     * </p>
     *
     * @param a the first Card to be compared
     * @param b the second Card to be compared
     * @return {@code 1} if Card {@code a} is greater than card {@code b};
     * {@code -1} if Card {@code a} is less than card {@code b};
     * {@code 0} if Card {@code a} is equal to card {@code b};
     */
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

    /**
     * Returns a colored and symboled string representation of this Card.
     *
     * <p>
     * The output includes terminal ANSI escape codes to dynamically style the card's
     * appearance based on its suit:
     * <ul>
     *   <li>Red suits are rendered with a crimson background and black text.</li>
     *   <li>Black suits are rendered with a dark background and white text.</li>
     * </ul>
     *
     * @return a rich-text ANSI string representing the card's suit symbol, name, and rank
     */
    @Override
    public String toString() {
        String symbol = switch (suit) {
            case Suit.D -> "♦";
            case Suit.H -> "♥";
            case Suit.S -> "♠";
            case Suit.C -> "♣";
        };
        return (suit.getValue() > 2 ? "\u001B[48;2;224;17;95m\u001B[30m" : "\u001B[40m\u001B[37m") + symbol + suit + "-" + (rank.getValue() < 11 ? rank.getValue() : rank) + "\u001B[0m";
    }

    /**
     * Returns the numeric value of this Card's Rank.
     *
     * @return the integer value of this Card's Rank
     */
    public int getRank() {
        return rank.getValue();
    }

    /**
     * Returns the numeric value of this Card's Suit.
     *
     * @return the integer value of this Card's Suit
     */
    public int getSuit() {
        return suit.getValue();
    }
}
