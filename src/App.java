import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("Welcome to Ward Cards!");
        qtyPlayer = readGameParams("Enter number of players (2-8): ", 1);
        qtyShuffle = readGameParams("Enter number of shuffles: ", 2);

        System.out.println("Settings: " + qtyPlayer + " Players and " + qtyShuffle + " Shuffles.");

        // TODO: Print Deck
        // TODO: Shuffle Deck
        // TODO: Print Shuffled Deck
        // TODO: Distribute Cards

        while(winner) {
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

    // TODO: Return Deck object
    static void parseFileToObj(File inputFile) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File("./src/resources/input.txt"));

            String rawFile = scanner.nextLine();

            ArrayList<String> splitRawFile = new ArrayList<>(List.of(rawFile.split(",")));

            System.out.println(splitRawFile);

            // TODO: Parse splitRawFile items to Card Objects
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
    }
}
