package com.svi.util;

import com.svi.model.Card;
import com.svi.model.Deck;
import com.svi.model.cardenum.Rank;
import com.svi.model.cardenum.Suit;

import javax.swing.*;
import java.io.InputStream;
import java.util.*;

public class GameUtils {
    public static int readGameSetting(String prompt, int type) {
        int qty;
        while (true) {
            try {
                qty = Integer.parseInt(JOptionPane.showInputDialog(prompt).trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entry must be a number. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (type == 1 && (qty < 2 || qty > 8)) {
                JOptionPane.showMessageDialog(null, "There should be at least 2 players and at most 8. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (type == 2 && qty < 1) {
                JOptionPane.showMessageDialog(null, "Entry must be at least 1. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }
        return qty;
    }

    public static boolean readPlayAgain() {
        while (true) {
            try {
                switch (JOptionPane.showInputDialog("Do you want to play again (y/n)?").trim()) {
                    case "y", "yes" -> {
                        return true;
                    }
                    case "n", "no" -> {
                        return false;
                    }
                    default ->
                            JOptionPane.showMessageDialog(null, "Invalid entry.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NoSuchElementException nsee) {
                System.out.println("Invalid entry!");
            }
        }
    }

    public static Deck loadDeckFile(InputStream inputFile) {
        Scanner scanner;
        String rawFile = "";
        try {
            scanner = new Scanner(inputFile);
            rawFile = scanner.nextLine();
        } catch (NoSuchElementException nsee) {
            JOptionPane.showMessageDialog(null, "File input.txt was not found. Please verify resources directory.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return parseToDeck(rawFile);
    }

    private static Deck parseToDeck(String rawFile) {
        ArrayList<Card> parsedCards = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(rawFile, ",");
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer innerTokenizer = new StringTokenizer(tokenizer.nextToken(), "-");
            Card tempCard = null;
            Suit suitVal = Suit.valueOf(innerTokenizer.nextToken());
            String tempRank = innerTokenizer.nextToken();
            Rank rankVal;
            try {
                rankVal = Rank.fromInt(Integer.parseInt(tempRank));
                tempCard = new Card(rankVal, suitVal);
            } catch (NumberFormatException nfe) {
                rankVal = Rank.valueOf(tempRank);
                tempCard = new Card(rankVal, suitVal);
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(null, "Illegal Card detected. Please verify the input.txt file and reload.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            parsedCards.add(tempCard);
        }

        if (!validateLoadedDeck(parsedCards)) {
            JOptionPane.showMessageDialog(null, "There was a problem with the loaded deck. Please verify the input.txt file and reload.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return new Deck(parsedCards);
    }

    private static boolean validateLoadedDeck(ArrayList<Card> parsedCards) {
        if (parsedCards.size() != 52) {
            return false;
        }

        return new HashSet<>(parsedCards).size() == 52;
    }
}
