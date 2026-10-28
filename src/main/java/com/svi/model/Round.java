package com.svi.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Round {
    private final int roundNo;
    private Player winner;
    private final Map<Player, Card> roundPool;

    public Round(int roundNo) {
        this.roundNo = roundNo;
        roundPool = new LinkedHashMap<>();
    }

    public void addToRoundPool(Player player, Card card) {
        roundPool.put(player, card);
    }

    public void declareRoundWinner(Player player) {
        winner = new Player(player);
    }

    public Player getWinner() {
        return winner;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public Map<Player, Card> getRoundPool() {
        return roundPool;
    }
}
