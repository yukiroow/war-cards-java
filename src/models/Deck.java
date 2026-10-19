package models;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public static void shuffleDeck(Deck deck, int qtyShuffle) {
        System.out.println("Original Deck: \n" + deck.getCards());
        for (int i = 0; i < qtyShuffle - 1; i++) {
            Collections.shuffle(deck.getCards());
        }
        System.out.println("Shuffled Deck: \n" + deck.getCards());
    }

    public static ArrayList<Deck> splitDeck(Deck deck, int qtyPlayers) {
        ArrayList<Deck> splitDecks = new ArrayList<>();
        int deckSize = deck.getCards().size() / qtyPlayers;
        int currCard = 0;
        for (int i = 0; i < qtyPlayers - 1; i++) {
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int j = 0; j < deckSize - 1; j++) {
                tempDeck.add(deck.getCards().get(currCard));
                currCard++;
            }
            splitDecks.add(new Deck(tempDeck));
        }
        return splitDecks;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card getTopCard() {
        return cards.removeFirst();
    }

    public void addToBottom(ArrayList<Card> cards) {
        for (Card card : cards) {
            cards.addFirst(card);
        }
    }
}
