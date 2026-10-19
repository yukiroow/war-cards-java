import enums.Rank;
import enums.Suit;
import models.Card;
import models.Deck;
import models.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * TODO
 * 1. Game Methods
 */
public class App {
    static void main() {
        int qtyPlayer, qtyShuffle;
        ArrayList<Player> players = new ArrayList<>();
        Deck deck;
        int round = 1;
        boolean winner = false;

        System.out.println("Welcome to Ward Cards!");

        deck = parseFileToObj(new File("./src/resources/input.txt"));

        qtyPlayer = readGameSetting("Enter number of players (2-8): ", 1);
        qtyShuffle = readGameSetting("Enter number of shuffles: ", 2);

        Deck.shuffleDeck(deck, qtyShuffle);

        ArrayList<Deck> decks = Deck.splitDeck(deck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }

        while (winner) {
            System.out.println("Round " + round);
        }
    }

    static int readGameSetting(String prompt, int type) {
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
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer innerTokenizer = new StringTokenizer(tokenizer.nextToken(), "-");
            Card tempCard;
            Suit suitVal = Suit.valueOf(innerTokenizer.nextToken());
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
