package com.svi;

import com.svi.utils.GameUtils;

import java.util.Scanner;


public class Main {
    static void main() {
        try {
            Scanner keyInput = new Scanner(System.in);
            Game warCards;
            do {
                warCards = new Game();
                warCards.initializeGame(keyInput);
                warCards.startGameLoop();
            } while (GameUtils.readPlayAgain(keyInput));
        } catch (Exception e) {
            System.out.println("There was an unexpected problem. Please try again." + e);
        }
    }
}
