package com.svi;

import com.svi.controller.GameController;

import javax.swing.*;

public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController();
            gameController.startGame();
        });
    }
}
