package com.svi.model;

import module java.base;
import com.svi.util.GameUtils;

public class Game {
    private final ArrayList<Player> players;
    private final ArrayList<Round> gameHistory;
    private final Deck originalDeck;
    private Deck shuffledDeck;
    private boolean gameOver;

    public Game() {
        gameHistory = new ArrayList<>();
        originalDeck = GameUtils.loadDeckFile(Game.class.getResourceAsStream("/input.txt"));
        shuffledDeck = new Deck(originalDeck.cards());
        players = new ArrayList<>();
        gameOver = false;
    }

    public void initializeGame() {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;

        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8)", 1);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles", 2);

        for (int i = 0; i < qtyShuffle; i++) {
            shuffledDeck = Deck.perfectShuffleDeck(shuffledDeck);
        }
        decks = Deck.splitDeck(shuffledDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }
    }

    public void startGameLoop() {
        int rounds = 1;
        while (!gameOver) {
            Round round = new Round(rounds);
            playCards(round);
            doRoundCleanup();
            gameHistory.add(round);
            rounds++;
        }
    }

    public Deck getOriginalDeck() {
        return originalDeck;
    }

    public Deck getShuffledDeck() {
        return shuffledDeck;
    }

    public ArrayList<Round> getGameHistory() {
        return gameHistory;
    }

    private void playCards(Round round) {
        List<Card> collectableCards = new ArrayList<>();
        for (Player player : players) {
            round.addToRoundPool(player, player.playCard());
        }
        Map.Entry<Player, Card> roundWinner = evaluateWinner(round.getRoundPool());
        int winnerIndex = players.indexOf(roundWinner.getKey());
        int qtyRemainingPlayer = players.size();
        for (int i = 0; i < qtyRemainingPlayer; i++) {
            Player player = players.get((winnerIndex + i) % qtyRemainingPlayer);
            collectableCards.add(round.getRoundPool().get(player));
        }
        roundWinner.getKey().collectCards(collectableCards);
        round.declareRoundWinner(new Player(roundWinner.getKey()));
    }

    public void doRoundCleanup() {
        players.removeIf(player -> player.getDeck().cards().isEmpty());

        if (players.size() < 2) {
            gameOver = true;
        }
    }

    public Map.Entry<Player, Card> evaluateWinner(Map<Player, Card> laidCards) {
        if (laidCards == null || laidCards.isEmpty()) {
            return null;
        }

        return laidCards.entrySet().stream()
                .max((cardA, cardB) -> Card.compare(cardA.getValue(), cardB.getValue()))
                .orElse(null);
    }
}
