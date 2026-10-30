package com.svi.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Deck of Cards in the War Cards game.
 *
 * @param cards the cards to be saved in this Deck
 * @author Harry Dominguez
 * @since 1.0
 */
public record Deck(ArrayList<Card> cards) {
    /**
     * Returns a new instance of a shuffled Deck from the given Deck.
     *
     * <p>
     * The deck is shuffled should through Faro shuffle (Perfect shuffling), where the deck is split into halves and a card is taken from each split alternately.
     * </p>
     *
     * @param deck the given Deck to be shuffled.
     * @return the shuffled Deck.
     */
    public static Deck perfectShuffleDeck(Deck deck) {
        int mid = deck.cards().size() / 2;
        ArrayList<Card> newDeck = new ArrayList<>();
        ArrayList<Card> firstHalf = new ArrayList<>(deck.cards().subList(0, mid));
        ArrayList<Card> secondHalf = new ArrayList<>(deck.cards().subList(mid, deck.cards().size()));
        for (int i = 0; i < mid; i++) {
            newDeck.add(firstHalf.get(i));
            newDeck.add(secondHalf.get(i));
        }
        return new Deck(newDeck);
    }

    /**
     * Returns a list of Decks to be assigned to the Game Players.
     *
     * <p>
     * The deck is split into {@code qtyPlayers} items, each populated through round-robin addition from the given Deck.
     * </p>
     *
     * @param deck the given deck
     * @param qtyPlayers the amount of decks to be generated
     * @return the list of Decks
     */
    public static ArrayList<Deck> splitDeck(Deck deck, int qtyPlayers) {
        int totalCards = deck.cards().size();
        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 0; i < qtyPlayers; i++) {
            decks.add(new Deck(new ArrayList<>()));
        }

        for (int i = 0; i < totalCards; i++) {
            int playerIndex = i % qtyPlayers;
            decks.get(playerIndex).cards().add(deck.cards().get(i));
        }

        return decks;
    }

    /**
     * Return and remove the first Card from this deck.
     *
     * @return the removed card
     */
    public Card getTopCard() {
        return cards.removeFirst();
    }

    /**
     * Add the given list of Cards to the end of this deck.
     *
     * @param cards the list of Cards to be added
     */
    public void addToBottom(List<Card> cards) {
        this.cards.addAll(cards);
    }
}
