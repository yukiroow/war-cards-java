package com.svi.util;

import com.svi.cardenum.Rank;
import com.svi.cardenum.Suit;
import com.svi.model.Card;
import com.svi.model.Deck;
import com.svi.model.Player;

import java.io.InputStream;
import java.util.*;

public class GameUtils {
    public static int readGameSetting(String prompt, int type, Scanner keyInput) {
        int qty;
        while (true) {
            System.out.print(prompt + "\n> ");
            try {
                qty = Integer.parseInt(keyInput.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entry must be a number. Please try again.");
                continue;
            }
            if (type == 1 && (qty < 2 || qty > 8)) {
                System.out.println("There should be at least 2 players and at most 8. Please try again.");
                continue;
            }
            if (type == 2 && qty < 1) {
                System.out.println("Entry must be at least 1. Please try again.");
                continue;
            }
            break;
        }
        return qty;
    }

    public static boolean readPlayAgain(Scanner keyInput) {
        while (true) {
            System.out.print("Do you want to play again (y/n)?\n> ");
            try {
                switch (keyInput.nextLine().trim().toLowerCase()) {
                    case "y", "yes" -> {
                        return true;
                    }
                    case "n", "no" -> {
                        return false;
                    }
                    default -> System.out.println("Invalid entry.");
                }
            } catch (NoSuchElementException nsee) {
                System.out.println("Invalid entry!");
            }
        }
    }

    public static void displayRoundPool(Map<Player, Card> roundPool) {
        System.out.print("ROUND POOL\n-> ");
        int i = 0;
        for (Map.Entry<Player, Card> entry : roundPool.entrySet()) {
            if (i++ == 4) System.out.print("\n   ");
            System.out.printf("P%d: %s  ", entry.getKey().playerNo(), entry.getValue().toString());
        }
        System.out.println("\n");
    }

    public static void displayDeck(String message, Deck deck) {
        System.out.println(message);

        for (int i = 0; i < deck.cards().size(); i++) {
            String card = deck.cards().get(i).toString();
            String visibleCard = card.replaceAll("\u001B\\[[;\\d]*m", "");
            System.out.print(card);
            System.out.print(" ".repeat(Math.max(0, 6 - visibleCard.length())));

            if ((i + 1) % 13 == 0 || i == deck.cards().size() - 1) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static Deck loadDeckFile(InputStream inputFile) {
        Scanner scanner;
        String rawFile = "";
        try {
            scanner = new Scanner(inputFile);
            rawFile = scanner.nextLine();
        } catch (NoSuchElementException nsee) {
            System.out.println("File input.txt was not found. Please verify resources directory.");
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
                System.out.println("Illegal Card detected. Please verify the input.txt file and reload.");
                System.exit(0);
            }
            parsedCards.add(tempCard);
        }

        if (!validateLoadedDeck(parsedCards)) {
            System.out.println("There was a problem with the loaded deck. Please verify the input.txt file and reload.");
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
