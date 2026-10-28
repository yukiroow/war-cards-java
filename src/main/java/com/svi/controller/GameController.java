package com.svi.controller;

import com.svi.model.Card;
import com.svi.model.Deck;
import com.svi.model.Game;
import com.svi.model.Player;
import com.svi.util.GameUtils;
import com.svi.view.GameFrame;

import java.util.*;

public class GameController {
    private final GameFrame gameView;
    private final Game gameModel;

    public GameController() {
        gameView = new GameFrame();
        gameModel = new Game();
    }

    public void initializeGame() {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;
        Deck shuffledDeck = new Deck(gameModel.getParsedDeck().cards());

        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------");
        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8): ", 1);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles: ", 2);
        System.out.println("-------------------------------------------------------");

        for (int i = 0; i < qtyShuffle; i++) {
            shuffledDeck = Deck.perfectShuffleDeck(shuffledDeck);
        }
        decks = Deck.splitDeck(shuffledDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            gameModel.addPlayer(new Player((i + 1), decks.get(i)));
        }

        System.out.println("Original Deck: \n" + gameModel.getParsedDeck().cards());
        System.out.println("Shuffled Deck: \n" + shuffledDeck.cards());
        System.out.println("-------------------------------------------------------");
    }

    public void startGameLoop() {
        while (!gameModel.isGameOver()) {
            Map<Player, Card> roundPool = new LinkedHashMap<>();
            List<Card> collectableCards = new ArrayList<>();

            System.out.println("Round " + gameModel.getRounds());
            playCards(roundPool, collectableCards);
            doRoundCleanup();

            gameModel.incrementRound();
        }
    }

    public void playCards(Map<Player, Card> roundPool, List<Card> collectableCards) {
        for (Player player : gameModel.getPlayers()) {
            roundPool.put(player, player.playCard());
        }
        System.out.println("Laid Cards: " + roundPool.values());

        Map.Entry<Player, Card> roundWinner = evaluateWinner(roundPool);
        System.out.println("Round Winner is Player " + (roundWinner.getKey().playerNo()) + " with Card " + roundWinner.getValue());

        int winnerIndex = gameModel.getPlayers().indexOf(roundWinner.getKey());
        int qtyRemainingPlayer = gameModel.getPlayers().size();

        for (int i = 0; i < qtyRemainingPlayer; i++) {
            Player player = gameModel.getPlayers().get((winnerIndex + i) % qtyRemainingPlayer);
            collectableCards.add(roundPool.get(player));
        }

        roundWinner.getKey().collectCards(collectableCards);

        System.out.println("Player " + (roundWinner.getKey().playerNo()) + "'s Deck: " + roundWinner.getKey().deck().cards());
    }

    public void doRoundCleanup() {
        gameModel.getPlayers().removeIf(player -> {
            if (player.deck().cards().isEmpty()) {
                System.out.println("Player " + player.playerNo() + " has been eliminated");
                return true;
            }
            return false;
        });

        if (gameModel.getRemainingPlayerCount() < 2) {
            System.out.println("Player " + gameModel.getPlayers().getFirst().playerNo() + " has won the game.");
            gameModel.endGame();
        }
        System.out.println("-------------------------------------------------------");
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
