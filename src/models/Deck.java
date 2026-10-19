package models;

import java.util.ArrayList;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards;

    Deck(Stack<Card> cards) {
        this.cards = cards;
    }

    public Stack<Card> getCards() {
        return cards;
    }

    public Card getTopCard() {
        return cards.pop();
    }

    public void addToBottom(ArrayList<Card> cards) {
        for (Card card: cards) {
            cards.addFirst(card);
        }
    }
}
