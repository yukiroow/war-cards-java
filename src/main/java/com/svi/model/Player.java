package com.svi.model;

import java.util.List;

public class Player {
    private int playerNo;
    private Deck deck;

    public Player(int playerNo, Deck deck) {
        this.playerNo = playerNo;
        this.deck = deck;
    }

    public Player(Player player) {
        this.playerNo = player.getPlayerNo();
        this.deck = player.getDeck();
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
