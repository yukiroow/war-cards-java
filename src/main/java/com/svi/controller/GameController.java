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



}
