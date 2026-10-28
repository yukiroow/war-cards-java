package com.svi.model;

import module java.base;
import com.svi.util.GameUtils;

public class Game {
    private final ArrayList<Player> players;
    private final ArrayList<Player> winnerHistory;
    private final ArrayList<Map<Player, Card>> roundHistory;
    private final Deck parsedDeck;
    private int rounds;
    private boolean gameOver;

    public Game() {
        winnerHistory = new ArrayList<>();
        roundHistory = new ArrayList<>();
        parsedDeck = GameUtils.loadDeckFile(Game.class.getResourceAsStream("/input.txt"));
        players = new ArrayList<>();
        rounds = 1;
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void endGame() {
        gameOver = true;
    }

    public Deck getParsedDeck() {
        return parsedDeck;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getRounds() {
        return rounds;
    }

    public void incrementRound() {
        rounds++;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getRemainingPlayerCount() {
        return players.size();
    }
}
