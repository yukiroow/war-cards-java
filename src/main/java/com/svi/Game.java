package com.svi;

import module java.base;
import com.svi.models.Card;
import com.svi.models.Deck;
import com.svi.models.Player;
import com.svi.utils.GameUtils;

public class Game {
    private final ArrayList<Player> players;
    private final Deck parsedDeck;
    private int rounds;
    private boolean winner;

    Game() {
        parsedDeck = GameUtils.loadDeckFile(Game.class.getResourceAsStream("/input.txt"));
        players = new ArrayList<>();
        rounds = 1;
        winner = false;
    }

    void initializeGame(Scanner keyInput) {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;
        Deck shuffledDeck = new Deck(parsedDeck.cards());

        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------");
        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8): ", 1, keyInput);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles: ", 2, keyInput);
        System.out.println("-------------------------------------------------------");

        for (int i = 0; i < qtyShuffle; i++) {
            shuffledDeck = Deck.perfectShuffleDeck(shuffledDeck);
        }
        decks = Deck.splitDeck(shuffledDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }

        System.out.println("Original Deck: \n" + parsedDeck.cards());
        System.out.println("Shuffled Deck: \n" + shuffledDeck.cards());
        System.out.println("-------------------------------------------------------");
    }

    void startGameLoop() {
        while (!winner) {
            Map<Player, Card> roundPool = new LinkedHashMap<>();
            List<Card> collectableCards = new ArrayList<>();

            System.out.println("Round " + rounds);
            playCards(roundPool, collectableCards);
            doRoundCleanup();

            rounds++;
        }
    }

    void playCards(Map<Player, Card> roundPool, List<Card> collectableCards) {
        for (Player player : players) {
            roundPool.put(player, player.playCard());
        }
        System.out.println("Laid Cards: " + roundPool.values());

        Map.Entry<Player, Card> roundWinner = evaluateWinner(roundPool);
        System.out.println("Round Winner is Player " + (roundWinner.getKey().playerNo()) + " with Card " + roundWinner.getValue());

        int winnerIndex = players.indexOf(roundWinner.getKey());
        int qtyRemainingPlayer = players.size();

        for (int i = 0; i < qtyRemainingPlayer; i++) {
            Player player = players.get((winnerIndex + i) % qtyRemainingPlayer);
            collectableCards.add(roundPool.get(player));
        }

        roundWinner.getKey().collectCards(collectableCards);

        System.out.println("Player " + (roundWinner.getKey().playerNo()) + "'s Deck: " + roundWinner.getKey().deck().cards());
    }

    void doRoundCleanup() {
        players.removeIf(player -> {
            if (player.deck().cards().isEmpty()) {
                System.out.println("Player " + player.playerNo() + " has been eliminated");
                return true;
            }
            return false;
        });

        if (players.size() < 2) {
            System.out.println("Player " + players.getFirst().playerNo() + " has won the game.");
            winner = true;
        }
        System.out.println("-------------------------------------------------------");
    }

    Map.Entry<Player, Card> evaluateWinner(Map<Player, Card> laidCards) {
        if (laidCards == null || laidCards.isEmpty()) {
            return null;
        }

        return laidCards.entrySet().stream()
                .max((cardA, cardB) -> Card.compare(cardA.getValue(), cardB.getValue()))
                .orElse(null);
    }
}
