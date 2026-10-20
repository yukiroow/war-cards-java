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

    public static ArrayList<Deck> splitDeck(Deck deck, int qtyPlayers) {
        ArrayList<Deck> splitDecks = new ArrayList<>();
        int deckSize = deck.cards().size() / qtyPlayers;
        int currCard = 0;
        for (int i = 0; i < qtyPlayers; i++) {
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int j = 0; j < deckSize; j++) {
                tempDeck.add(deck.cards().get(currCard));
                currCard++;
            }
            splitDecks.add(new Deck(tempDeck));
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
