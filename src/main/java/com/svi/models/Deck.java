package com.svi.models;

import java.util.ArrayList;

public record Deck(ArrayList<Card> cards) {
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

    public Card getTopCard() {
        return cards.removeFirst();
    }

    public void addToBottom(ArrayList<Card> cards) {
        for (Card card : cards) {
            this.cards.addLast(card);
        }
    }
}
