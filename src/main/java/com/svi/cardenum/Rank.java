package com.svi.cardenum;

/**
 * Represents the Rank of a Card.
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public enum Rank {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), J(11), Q(12), K(13), A(14);

    private final int value;

    /**
     * Constructs a Rank with an associated numeric value.
     *
     * @param value the numeric value of the card
     */
    Rank(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of this Rank.
     *
     * @return the integer value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Constructs a Rank associated with a given numeric value.
     *
     * @param val given numeric value
     * @return Rank with the given value
     */
    public static Rank fromInt(int val) {
        for (Rank rank: Rank.values()) {
            if (rank.value == val) {
                return rank;
            }
        }
        return null;
    }
}
