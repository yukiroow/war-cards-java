package com.svi;

import com.svi.utils.GameUtils;

import java.util.Scanner;

public class Main {
    static void main() {
        try {
            Scanner keyInput = new Scanner(System.in);
            Game warCards;
            boolean gameRunning = true;
            while(gameRunning) {
                warCards = new Game();
                warCards.initializeGame(keyInput);
                warCards.startGameLoop();
                gameRunning = GameUtils.readPlayAgain(keyInput);
            }
            System.out.println("Thank you for playing War Cards!");
        } catch (Exception e) {
            System.out.println("There was an unexpected problem. Please try again." + e);
        }
    }
}
