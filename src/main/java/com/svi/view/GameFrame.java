package com.svi.view;

import com.svi.model.Deck;
import com.svi.model.Round;

import javax.swing.*;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    private JPanel mainPanel;
    public GameFrame() {
        setTitle("War Cards Java");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void loadMainPanel(ArrayList<Round> gameHistory, Deck originalDeck, Deck shuffledDeck) {
        mainPanel = new RoundViewerPanel(gameHistory, originalDeck, shuffledDeck);
        setContentPane(mainPanel);
    }

}
