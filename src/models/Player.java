package models;

import java.util.ArrayList;

public class Player {
    private int playerNo;
    private Deck deck;

    public Player(int playerNo, Deck deck) {
        this.playerNo = playerNo;
        this.deck = deck;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Deck getDeck() {
        return deck;
    }

    Card playCard() {
        return deck.getTopCard();
    }

    void collectCards(ArrayList<Card> cards) {
        deck.addToBottom(cards);
    }
}
