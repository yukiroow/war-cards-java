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
        parsedDeck = GameUtils.parseFileToDeck(Game.class.getResourceAsStream("/input.txt"));
        players = new ArrayList<>();
        rounds = 1;
        winner = false;
    }

    void initializeGame() {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;
        Scanner keyInput = new Scanner(System.in);
        Deck sortedDeck = new Deck(parsedDeck.cards());

        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------");
        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8): ", 1, keyInput);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles: ", 2, keyInput);
        System.out.println("-------------------------------------------------------");
//        Deck.shuffleDeck(parsedDeck, qtyShuffle);
        System.out.println("Original Deck: \n" + parsedDeck.cards());
        for (int i = 0; i < qtyShuffle; i++) {
            sortedDeck = Deck.perfectShuffleDeck(sortedDeck);
        }
        System.out.println("Shuffled Deck: \n" + sortedDeck.cards());
        decks = Deck.splitDeck(sortedDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }
        System.out.println("-------------------------------------------------------");
    }

    void startGameLoop() {
        while (!winner) {
            Map<Player, Card> roundPool = new HashMap<>();
            ArrayList<Card> collectableCards = new ArrayList<>();

            System.out.println("Round " + rounds);
            playCards(roundPool, collectableCards);
            doRoundCleanup();

            rounds++;
        }
    }

    void playCards(Map<Player, Card> roundPool, ArrayList<Card> collectableCards) {
        for (Player player : players) {
            roundPool.put(player, player.playCard());
        }
        System.out.println("Laid Cards: " + roundPool.values());

        Map.Entry<Player, Card> roundWinner = GameUtils.evaluateWinner(roundPool);
        System.out.println("Round Winner is Player " + (roundWinner.getKey().playerNo()) + " with Card " + roundWinner.getValue());

        collectableCards.addAll(roundPool.values());
        collectableCards.sort(Collections.reverseOrder(Card::compare));

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
}
