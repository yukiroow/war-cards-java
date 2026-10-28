package com.svi.model.cardenum;

public enum Suit {
    C(1), S(2), H(3), D(4);

    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
