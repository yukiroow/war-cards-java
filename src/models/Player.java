package models;

import java.util.ArrayList;

public record Player(int playerNo, Deck deck) {

    public Card playCard() {
        return deck.getTopCard();
    }

    public void collectCards(ArrayList<Card> cards) {
        deck.addToBottom(cards);
    }
}
