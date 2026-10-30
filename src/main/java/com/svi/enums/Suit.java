package com.svi.enums;

/**
 * Represents the Suit of a Card.
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public enum Suit {
    C(1), S(2), H(3), D(4);

    private final int value;

    /**
     * Constructs a Suit with an associated numeric value.
     *
     * @param value the numeric value of the card
     */
    Suit(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of this Suit.
     *
     * @return the integer value
     */
    public int getValue() {
        return this.value;
    }
}
