package com.svi.models;

import java.util.List;

public record Player(int playerNo, Deck deck) {

    public Card playCard() {
        return deck.getTopCard();
    }

    public void collectCards(List<Card> cards) {
        deck.addToBottom(cards);
    }
}
