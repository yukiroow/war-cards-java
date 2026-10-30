package com.svi;

import com.svi.model.Game;
import com.svi.util.GameUtils;

import java.util.Scanner;

/**
 * The {@code Main} class serves as the primary entry point for the Game.
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public class Main {
    /**
     * Starts the application.
     */
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
