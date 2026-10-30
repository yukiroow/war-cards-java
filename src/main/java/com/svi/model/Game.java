package com.svi.model;

import module java.base;
import com.svi.util.GameUtils;

public class Game {
    private final ArrayList<Player> players;
    private final Deck parsedDeck;
    private int rounds;
    private boolean winner;

    public Game() {
        parsedDeck = GameUtils.loadDeckFile(Game.class.getResourceAsStream("/input.txt"));
        players = new ArrayList<>();
        rounds = 1;
        winner = false;
    }

    public void initializeGame(Scanner keyInput) {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;
        Deck shuffledDeck = new Deck(parsedDeck.cards());

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------------------------------");
        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8): ", 1, keyInput);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles: ", 2, keyInput);
        System.out.println("-------------------------------------------------------------------------------");

        for (int i = 0; i < qtyShuffle % 8; i++) {
            shuffledDeck = Deck.perfectShuffleDeck(shuffledDeck);
        }
        decks = Deck.splitDeck(shuffledDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }

        GameUtils.displayDeck("Original Deck", parsedDeck);
        GameUtils.displayDeck("Shuffled Deck", shuffledDeck);
        System.out.println("-------------------------------------------------------------------------------");
    }

    public void startGameLoop() {
        while (!winner) {
            Map<Player, Card> roundPool = new LinkedHashMap<>();
            List<Card> collectableCards = new ArrayList<>();

            System.out.println("[[ ROUND " + rounds + " ]]");
            playCards(roundPool, collectableCards);
            doRoundCleanup();

            rounds++;
        }
    }

    private void playCards(Map<Player, Card> roundPool, List<Card> collectableCards) {
        for (Player player : players) {
            roundPool.put(player, player.playCard());
        }
        GameUtils.displayRoundPool(roundPool);

        Map.Entry<Player, Card> roundWinner = evaluateWinner(roundPool);
        System.out.println("Round Winner is Player " + (roundWinner.getKey().playerNo()) + " with Card " + roundWinner.getValue());

        int winnerIndex = players.indexOf(roundWinner.getKey());
        int qtyRemainingPlayer = players.size();

        for (int i = 0; i < qtyRemainingPlayer; i++) {
            Player player = players.get((winnerIndex + i) % qtyRemainingPlayer);
            collectableCards.add(roundPool.get(player));
        }

        roundWinner.getKey().collectCards(collectableCards);

        GameUtils.displayDeck("Player " + (roundWinner.getKey().playerNo()) + "'s updated deck: ", roundWinner.getKey().deck());
    }

    private void doRoundCleanup() {
        players.removeIf(player -> {
            if (player.deck().cards().isEmpty()) {
                System.out.println("Player " + player.playerNo() + " has been ELIMINATED");
                return true;
            }
            return false;
        });

        if (players.size() < 2) {
            System.out.println("Player " + players.getFirst().playerNo() + " has WON the game");
            winner = true;
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    private Map.Entry<Player, Card> evaluateWinner(Map<Player, Card> laidCards) {
        if (laidCards == null || laidCards.isEmpty()) {
            return null;
        }

        return laidCards.entrySet().stream()
                .max((cardA, cardB) -> Card.compare(cardA.getValue(), cardB.getValue()))
                .orElse(null);
    }
}
