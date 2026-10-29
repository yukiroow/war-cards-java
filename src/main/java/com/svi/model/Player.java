package com.svi.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int playerNo;
    private final Deck deck;

    public Player(int playerNo, Deck deck) {
        this.playerNo = playerNo;
        this.deck = deck;
    }

    public Player(Player player) {
        this.playerNo = player.getPlayerNo();
        this.deck = new Deck(
                new ArrayList<>(player.getDeck().cards())
        );
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card playCard() {
        return deck.getTopCard();
    }

    public void collectCards(List<Card> cards) {
        deck.addToBottom(cards);
    }
}
