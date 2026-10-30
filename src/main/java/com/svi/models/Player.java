package com.svi.models;

import java.util.List;

/**
 * Represents a Player in the War Cards Game.
 *
 * @param playerNo the numeric identifier of this Player
 * @param deck     the Deck of Cards of this Player
 */
public record Player(int playerNo, Deck deck) {

    /**
     * Remove and return the top Card of this Player's Deck.
     *
     * @return the top Card
     */
    public Card playCard() {
        return deck.getTopCard();
    }

    /**
     * Add the given list of Cards to this Player's Deck.
     *
     * @param cards the list of Cards
     */
    public void collectCards(List<Card> cards) {
        deck.addToBottom(cards);
    }
}
