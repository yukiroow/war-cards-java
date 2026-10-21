package com.svi.models;

import java.util.ArrayList;
import java.util.Collections;

public record Deck(ArrayList<Card> cards) {
    public static void shuffleDeck(Deck deck, int qtyShuffle) {
        System.out.println("Original Deck: \n" + deck.cards());
        for (int i = 0; i < qtyShuffle; i++) {
            Collections.shuffle(deck.cards());
        }
        System.out.println("Shuffled Deck: \n" + deck.cards());
    }

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
        ArrayList<Deck> splitDecks = new ArrayList<>();

        for (int i = 0; i < qtyPlayers; i++) {
            splitDecks.add(new Deck(new ArrayList<>()));
        }

        for (int i = 0; i < deck.cards().size(); i++) {
            int playerIndex = i % qtyPlayers;
            splitDecks.get(playerIndex).cards().add(deck.cards().get(i));
        }

        return splitDecks;
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
