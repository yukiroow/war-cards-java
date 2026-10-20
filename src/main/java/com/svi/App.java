package com.svi;

import module java.base;
import com.svi.enums.Rank;
import com.svi.enums.Suit;
import com.svi.models.Card;
import com.svi.models.Deck;
import com.svi.models.Player;

public class App {
    static void main() {
        int qtyPlayer, qtyShuffle;
        ArrayList<Player> players = new ArrayList<>();
        Deck deck;
        int round = 1;
        boolean winner = false;

        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------");

        deck = parseFileToObj(App.class.getResourceAsStream("/input.txt"));

        qtyPlayer = readGameSetting("Enter number of players (2-8): ", 1);
        qtyShuffle = readGameSetting("Enter number of shuffles: ", 2);
        System.out.println("-------------------------------------------------------");

        Deck.shuffleDeck(deck, qtyShuffle);
        ArrayList<Deck> decks = Deck.splitDeck(deck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }

        while (!winner) {
            Map<Integer, Card> roundPool = new HashMap<>();
            ArrayList<Card> collectableCards = new ArrayList<>();

            System.out.println("\n-------------------------------------------------------");
            System.out.println("Round " + round);
            for (int i = 0; i < players.size(); i++) {
                roundPool.put(i, players.get(i).playCard());
            }
            System.out.println("Laid Cards: " + roundPool.values());

            Map.Entry<Integer, Card> roundWinner = evaluateWinner(roundPool);
            System.out.println("Round Winner is Player " + (roundWinner.getKey() + 1) + " with Card " + roundWinner.getValue());

            roundPool.entrySet()
                    .stream()
                    .toList()
                    .forEach(entry -> collectableCards.add(entry.getValue()));

            collectableCards.sort(Collections.reverseOrder(Card::compare));

            players.get(roundWinner.getKey()).collectCards(collectableCards);

            System.out.println("Player " + (roundWinner.getKey() + 1) + "'s Deck: " + players.get(roundWinner.getKey()).deck().cards());

            players.removeIf(player -> {
                if (player.deck().cards().isEmpty()) {
                    System.out.println("Player " + player.playerNo() + " has been eliminated");
                    return true;
                }
                return false;
            });

            if (players.size() < 2) {
                System.out.println("Player " + players.getFirst().playerNo() + " has won the game.");
                winner = true;
            }

            round++;
        }
    }

    static Map.Entry<Integer, Card> evaluateWinner(Map<Integer, Card> laidCards) {
        if (laidCards == null || laidCards.isEmpty()) {
            return null;
        }

        return laidCards.entrySet().stream()
                .max((cardA, cardB) -> Card.compare(cardA.getValue(), cardB.getValue()))
                .orElse(null);
    }

    static int readGameSetting(String prompt, int type) {
        Scanner keyInput = new Scanner(System.in);
        int qty;
        while (true) {
            IO.print(prompt);
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

    static Deck parseFileToObj(InputStream inputFile) {
        Scanner scanner;
        ArrayList<Card> parsedCards = new ArrayList<>();
        String rawFile = "";
        try {
            scanner = new Scanner(inputFile);
            rawFile = scanner.nextLine();
        } catch (IllegalStateException ise) {
            System.out.println("File input.txt was not found. Please verify resources directory.");
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
