import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TODO
 * 1. File Reading
 * 2. Basic Application Flow (User Inputs -> Game Loop)
 * 3. Objects (Player, Card, Deck) and Enums (Rank, Suit)
 * 4. Static Methods (File loading, Deck Shuffling, Deck Splitting)
 * 5. Game Methods
 */
public class App {
    static void main() {
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
