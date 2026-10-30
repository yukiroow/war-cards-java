package com.svi.models;

import module java.base;
import com.svi.utils.GameUtils;

/**
 * Represents an instance of the War Card Game.
 *
 * <p>
 * The game attributes (players, decks, round) are held in this Class, as well as the main game logic.
 * </p>
 *
 * @author Harry Dominguez
 * @since 1.0
 */
public class Game {
    private final ArrayList<Player> players;
    private final Deck parsedDeck;
    private int rounds;
    private boolean winner;

    /**
     * Construct an instance of the War Card Game.
     *
     * <p>
     * This loads and instantiates the input file to {@code parsedDeck}. This also instantiates the player list as an empty object, initializes {@code rounds} to {@code 1}, and the {@code winner} to false.
     * </p>
     */
    public Game() {
        parsedDeck = GameUtils.loadDeckFile(Game.class.getResourceAsStream("/input.txt"));
        players = new ArrayList<>();
        rounds = 1;
        winner = false;
    }

    /**
     * Initialize the game state.
     *
     * <p>
     * The method performs the following operations:
     * </p>
     * <ul>
     *     <li>Reads the required game settings from user input.</li>
     *     <li>Shuffles the original deck using the Faro shuffle algorithm.</li>
     *     <li>Splits the shuffled deck and creates the corresponding {@code Player} objects.</li>
     *     <li>Displays the original and shuffled decks in the console.</li>
     * </ul>
     *
     * @param keyInput the Scanner object for user input
     */
    public void initializeGame(Scanner keyInput) {
        ArrayList<Deck> decks;
        int qtyPlayer, qtyShuffle;
        Deck shuffledDeck = new Deck(parsedDeck.cards());

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Welcome to Ward Cards!");
        System.out.println("-------------------------------------------------------------------------------");
        qtyPlayer = GameUtils.readGameSetting("Enter number of players (2-8): ", 1, keyInput);
        qtyShuffle = GameUtils.readGameSetting("Enter number of shuffles: ", 2, keyInput);
        System.out.println("-------------------------------------------------------------------------------");

        for (int i = 0; i < qtyShuffle % 8; i++) {
            shuffledDeck = Deck.perfectShuffleDeck(shuffledDeck);
        }
        decks = Deck.splitDeck(shuffledDeck, qtyPlayer);
        for (int i = 0; i < decks.size(); i++) {
            players.add(new Player((i + 1), decks.get(i)));
        }

        GameUtils.displayDeck("Original Deck", parsedDeck);
        GameUtils.displayDeck("Shuffled Deck", shuffledDeck);
        System.out.println("-------------------------------------------------------------------------------");
    }

    /**
     * This starts the War Cards Game Loop.
     *
     * <p>
     * Each iteration of the loop represents a Round in the Game. The loop will end once {@code winner} boolean is set to {@code true}. In each iteration:
     * </p>
     * <ul>
     *     <li>An empty map to store the round pool (Players and their played Cards) is instantiated.</li>
     *     <li>An empty list to store the cards to be collected by the round winner is instantiated.</li>
     *     <li>Displays the round number and starts the game flow.</li>
     *     <li>Cleans up eliminated players and updates the {@code rounds} counter if needed.</li>
     * </ul>
     */
    public void startGameLoop() {
        while (!winner) {
            Map<Player, Card> roundPool = new LinkedHashMap<>();
            List<Card> collectableCards = new ArrayList<>();

            System.out.println("[[ ROUND " + rounds + " ]]");
            playCards(roundPool, collectableCards);
            doRoundCleanup();

            rounds++;
        }
    }

    /**
     * This runs the Game's flow and updates associated instance attributes.
     *
     * <p>
     * The Game Flow runs as follows:
     * </p>
     * <ul>
     *     <li>The top card of each player is put into the {@code roundPool}</li>
     *     <li>The content of {@code roundPool} is displayed</li>
     *     <li>The winner of the round is evaluated and displayed, along with the winning Card</li>
     *     <li>The cards are collected in round-robin fashion, starting from the winning Player's Card</li>
     *     <li>The updated Deck of the winning Player is displayed</li>
     * </ul>
     *
     * @param roundPool        the current round's Card pool
     * @param collectableCards the current round's Card pool to be collected by round winner
     */
    private void playCards(Map<Player, Card> roundPool, List<Card> collectableCards) {
        for (Player player : players) {
            roundPool.put(player, player.playCard());
        }
        GameUtils.displayRoundPool(roundPool);

        Map.Entry<Player, Card> roundWinner = evaluateWinner(roundPool);
        System.out.println("Round Winner is Player " + (roundWinner.getKey().playerNo()) + " with Card " + roundWinner.getValue());

        int winnerIndex = players.indexOf(roundWinner.getKey());
        int qtyRemainingPlayer = players.size();

        for (int i = 0; i < qtyRemainingPlayer; i++) {
            Player player = players.get((winnerIndex + i) % qtyRemainingPlayer);
            collectableCards.add(roundPool.get(player));
        }

        roundWinner.getKey().collectCards(collectableCards);

        GameUtils.displayDeck("Player " + (roundWinner.getKey().playerNo()) + "'s updated deck: ", roundWinner.getKey().deck());
    }

    /**
     * This removes eliminated players and determines if there is already a Game winner.
     *
     * <p>
     * Players in {@code players} with no Cards left in their Deck are considered as eliminated.
     * </p>
     */
    private void doRoundCleanup() {
        players.removeIf(player -> {
            if (player.deck().cards().isEmpty()) {
                System.out.println("Player " + player.playerNo() + " has been ELIMINATED");
                return true;
            }
            return false;
        });

        if (players.size() < 2) {
            System.out.println("Player " + players.getFirst().playerNo() + " has WON the game");
            winner = true;
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    /**
     * This returns the Map Entry of the winner in the round pool Map object.
     *
     * <p>
     * The winning entry is determined by finding the entry with the highest card value in the round pool. Card values are compared using the {@link Card#compare(Card, Card)} method.
     * </p>
     *
     * @param roundPool the round pool
     * @return the Map Entry of the winner
     */
    private Map.Entry<Player, Card> evaluateWinner(Map<Player, Card> roundPool) {
        if (roundPool == null || roundPool.isEmpty()) {
            return null;
        }

        return roundPool.entrySet().stream().max((cardA, cardB) -> Card.compare(cardA.getValue(), cardB.getValue())).orElse(null);
    }
}
