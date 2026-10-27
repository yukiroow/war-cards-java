# War Cards

## About

This repository contains my CLI implementation of the War Cards Game to be submitted as a deliverable for the Java Training Session.

This project is written with `Java JDK 25.0.4` and built using `Maven`.

## Running the Project

```java
java -jar harrydom-war-cards
```

## Original Specification

### General Instruction
Write a Java program that emulates a card game with n players. The player who gets all the cards of the deck in his hand wins. The player with the highest card wins the round.
There are 52 cards on the deck.

### Rules

1. The card values in descending order are:
   - suits: diamond, heart, spades, clubs
   - ranks: ace, king, queen, jack, ten, nine, eight, seven, six, five, four, three, two

2. The initial deck will be given in a table in memory.

> ~~3. Shuffling means cutting the card deck in half, then sequencing the cards from the half decks alternately to a new deck.~~

3. Shuffling means randomly sequencing the cards.

4. Distribute the cards to the players

5. Each player plays the top card of his deck on each round. 

6. The winner of the round is the player with the highest card. The winner gets the cards of the round and places them at the bottom of his deck. Note: winning card should be on top

7. The winner of the game is the player that gets all the cards. 

### Input 

1. The number of players for the game
2. The number of times the deck will be shuffled before the game starts
3. The table in memory that contains the initial deck of cards

### Output

1. Display the content of the deck before the shuffle.
2. Display the content of the deck after the shuffle but before the start of the game.
3. Display cards laid by players per round (round number included)
4. Display the content of the deck of the winning player (for every round). 

### Program-specific Instructions

1. Use Java Collection Framework for the data structure.
2. Use Enum to store or keep track of the constants.
3. Save the initial card sequence in an `input.txt` file and use Java I/O to read the content in your code.
4. Use StringTokenizer to parse the content of `input.txt` and initialize your cards out of it.
