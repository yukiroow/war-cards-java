package models;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cards;

    Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
