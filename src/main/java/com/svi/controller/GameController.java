package com.svi.controller;

import com.svi.model.Game;
import com.svi.view.GameFrame;

import java.util.*;

public class GameController {
    private final GameFrame gameView;
    private final Game gameModel;

    public GameController() {
        gameView = new GameFrame();
        gameModel = new Game();
        gameModel.initializeGame();
        gameModel.startGameLoop();
    }

    public void startGame() {
        gameView.loadMainPanel(gameModel.getGameHistory(), gameModel.getOriginalDeck(), gameModel.getShuffledDeck());
        gameView.setVisible(true);
    }
}
