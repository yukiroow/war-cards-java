package com.svi.utils;

import com.svi.enums.Rank;
import com.svi.enums.Suit;
import com.svi.models.Card;
import com.svi.models.Deck;
import com.svi.models.Player;

import java.io.InputStream;
import java.util.*;

/**
 * This provides utility methods for essential game operations (User input, File Loading, Display Helpers)
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public class GameUtils {
    /**
     * Prevents instantiation of this utility class.
     */
    private GameUtils() {
    }

    /**
     * This reads and returns an integer value based on the condition of the given type.
     *
     * <p>Conditions:</p>
     * <ul>
     *      <li>A type {@code 1} setting's integer value must be at least 2 and at most 8.</li>
     *      <li>A type {@code 2} setting's integer value must be at least 1.</li>
     * </ul>
     *
     * @param prompt        the message prompt
     * @param type          the given type
     * @param keyboardInput the Scanner object for user input
     * @return the integer value
     */
    public static int readGameSetting(String prompt, int type, Scanner keyboardInput) {
        int inputNum;
        while (true) {
            System.out.print(prompt + "\n> ");
            try {
                inputNum = Integer.parseInt(keyboardInput.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entry must be a number. Please try again.");
                continue;
            }
            if (type == 1 && (inputNum < 2 || inputNum > 8)) {
                System.out.println("There should be at least 2 players and at most 8. Please try again.");
                continue;
            }
            if (type == 2 && inputNum < 1) {
                System.out.println("Entry must be at least 1. Please try again.");
                continue;
            }
            break;
        }
        return inputNum;
    }

    /**
     * This prompts the user to restart the game.
     *
     * @param keyboardInput the Scanner object for user input
     * @return {@code true} if input is {@code y} or {@code yes}
     * {@code false} if input is {@code n} or {@code no}
     */
    public static boolean readPlayAgain(Scanner keyboardInput) {
        while (true) {
            System.out.print("Do you want to play again (y/n)?\n> ");
            try {
                switch (keyboardInput.nextLine().trim().toLowerCase()) {
                    case "y", "yes" -> {
                        return true;
                    }
                    case "n", "no" -> {
                        return false;
                    }
                    default -> System.out.println("Entry must only be y/yes or n/no.");
                }
            } catch (NoSuchElementException nsee) {
                System.out.println("Entry must only be y/yes or n/no.");
            }
        }
    }

    /**
     * This displays the Players and their played Cards in the given round pool.
     *
     * <p>
     * There will be a max of 4 players per line. The following players are printed in the next line.
     * </p>
     *
     * @param roundPool the given round pool
     */
    public static void displayRoundPool(Map<Player, Card> roundPool) {
        System.out.print("ROUND POOL\n-> ");
        int i = 0;
        for (Map.Entry<Player, Card> roundPoolEntry : roundPool.entrySet()) {
            if (i++ == 4) System.out.print("\n   ");
            System.out.printf("P%d: %s  ", roundPoolEntry.getKey().playerNo(), roundPoolEntry.getValue().toString());
        }
        System.out.println("\n");
    }

    /**
     * This displays the given message and the content of the given Card Deck.
     *
     * <p>
     * There will be a max of 13 Card per row. Each Card is displayed evenly within 6 widths, padding is added in the empty spaces between each card.
     * </p>
     *
     * @param message the given message
     * @param deck    the given Card Deck
     */
    public static void displayDeck(String message, Deck deck) {
        System.out.println(message);

        for (int i = 0; i < deck.cards().size(); i++) {
            String card = deck.cards().get(i).toString();
            String visibleCard = card.replaceAll("\u001B\\[[;\\d]*m", "");
            System.out.print(card);
            System.out.print(" ".repeat(Math.max(0, 6 - visibleCard.length()))); // Padding

            if ((i + 1) % 13 == 0 || i == deck.cards().size() - 1) {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * This returns a Deck object parsed from the given input file.
     *
     * @param inputFile the InputStream of the input file
     * @return the Deck object parsed from the file
     */
    public static Deck loadDeckFile(InputStream inputFile) {
        Scanner fileScanner;
        String rawFile = "";
        try {
            fileScanner = new Scanner(inputFile);
            rawFile = fileScanner.nextLine();
        } catch (NoSuchElementException nsee) {
            System.out.println("File input.txt was not found.\nPlease verify resources directory.");
            System.exit(0);
        }

        return parseToDeck(rawFile);
    }

    /**
     * This parses the content of the given string to Card Objects and returns it all as a Deck object.
     *
     * <p>
     * {@code StringTokenizer} is used to separate the card values (,) as well as the Suit-Rank values (-)
     * </p>
     *
     * @param rawFile the given string
     * @return the Deck object parsed from the string
     */
    private static Deck parseToDeck(String rawFile) {
        ArrayList<Card> parsedCards = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(rawFile, ",");
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer innerTokenizer = new StringTokenizer(tokenizer.nextToken(), "-");
            Suit suitVal = Suit.valueOf(innerTokenizer.nextToken());
            String rawRank = innerTokenizer.nextToken();
            Card currCard = parseCard(suitVal, rawRank);
            parsedCards.add(currCard);
        }

        if (!validateLoadedDeck(parsedCards)) {
            System.out.println("There was a discrepancy in the amount of Cards loaded.\nPlease verify the input.txt file and reload.");
            System.exit(0);
        }

        return new Deck(parsedCards);
    }

    /**
     * Returns a Card object constructed from a given Suit value and rank String.
     *
     * @param suitVal the given Suit value
     * @param rawRank the given rank String
     * @return the constructed Card
     */
    private static Card parseCard(Suit suitVal, String rawRank) {
        Card tempCard = null;
        Rank rankVal;
        try {
            rankVal = Rank.fromInt(Integer.parseInt(rawRank));
            tempCard = new Card(rankVal, suitVal);
        } catch (NumberFormatException nfe) {
            rankVal = Rank.valueOf(rawRank);
            tempCard = new Card(rankVal, suitVal);
        } catch (IllegalArgumentException iae) {
            System.out.println("Illegal characters have been detected.\nPlease verify the input.txt file and reload.");
            System.exit(0);
        }
        return tempCard;
    }

    /**
     * This validates the given list of Cards.
     *
     * <p>
     * The given list of cards must have 52 unique Card objects.
     * </p>
     *
     * @param parsedCards the given list of Cards
     * @return {@code true} if the given list of Cards is valid
     * {@code false} if the given list of Cards is invalid
     */
    private static boolean validateLoadedDeck(ArrayList<Card> parsedCards) {
        if (parsedCards.size() != 52) {
            return false;
        }

        // Instantiating a HashSet from the list would eliminate duplicates
        return new HashSet<>(parsedCards).size() == 52;
    }
}
