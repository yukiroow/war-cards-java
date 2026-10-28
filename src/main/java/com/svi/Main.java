package com.svi;

import com.svi.controller.GameController;
import com.svi.model.Game;
import com.svi.view.GameFrame;

import javax.swing.*;

public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController();
        });
    }
}
