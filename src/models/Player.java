package models;

public class Player {
    private int playerNo;
    private Deck deck;

    Player(int playerNo, Deck deck) {
        this.playerNo = playerNo;
        this.deck = deck;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Deck getDeck() {
        return deck;
    }

    // TODO: Card playCard()
    // TODO: void collectCards(List<Card> cards)
}
