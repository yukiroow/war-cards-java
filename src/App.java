import enums.Rank;
import enums.Suit;
import models.Card;
import models.Deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * TODO
 * 1. Objects (Player, Card, Deck) and Enums (Rank, Suit)
 * 2. Static Methods (File loading, Deck Shuffling, Deck Splitting)
 * 3. Game Methods
 */
public class App {
    static void main() {
        int qtyPlayer, qtyShuffle, round = 1;
        boolean winner = false;
        Deck originalDeck;

        System.out.println("Welcome to Ward Cards!");

        originalDeck = parseFileToObj(new File("./src/resources/input.txt"));
        qtyPlayer = readGameParams("Enter number of players (2-8): ", 1);
        qtyShuffle = readGameParams("Enter number of shuffles: ", 2);

//        System.out.println("Settings: " + qtyPlayer + " Players and " + qtyShuffle + " Shuffles.");

        System.out.println("Original Deck: \n" + originalDeck.getCards());

        // TODO: Print Deck
        // TODO: Shuffle Deck
        // TODO: Print Shuffled Deck
        // TODO: Distribute Cards

        while (winner) {
            System.out.println("Round " + round);
        }
    }

    static int readGameParams(String prompt, int type) {
        Scanner keyInput = new Scanner(System.in);
        int qty;
        while (true) {
            System.out.print(prompt);
            try {
                qty = Integer.parseInt(keyInput.nextLine());
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

    static Deck parseFileToObj(File inputFile) {
        Scanner scanner;
        ArrayList<Card> parsedCards = new ArrayList<>();
        String rawFile = "";
        try {
            scanner = new Scanner(inputFile);
            rawFile = scanner.nextLine();
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
            System.exit(0);
        }
        StringTokenizer tokenizer = new StringTokenizer(rawFile, ",");
        while(tokenizer.hasMoreTokens()) {
            StringTokenizer innerTokenizer = new StringTokenizer(tokenizer.nextToken(), "-");
            Card tempCard;
            // Suit
            Suit suitVal = Suit.valueOf(innerTokenizer.nextToken());
            // Rank
            String tempRank = innerTokenizer.nextToken();
            try {
                Rank rankVal = Rank.fromInt(Integer.parseInt(tempRank));
                tempCard = new Card(rankVal, suitVal);
            } catch (NumberFormatException nfe) {
                tempCard = new Card(Rank.valueOf(tempRank), suitVal);
            }
            parsedCards.add(tempCard);
        }

        return new Deck(parsedCards);
    }
}
