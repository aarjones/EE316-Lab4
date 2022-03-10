package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HangmanStats {
    /**
     * The maximum number of guesses allowed in a single game
     */
    private final int maxGuesses;
    /**
     * The number of games played so far
     */
    private int numGames;
    /**
     * The number of wins so far
     */
    private int numWins;
    /**
     * Was the previous game a victory?
     */
    private boolean previousGameVictory;
    /**
     * The key for the previous game
     */
    private String previousKey;
    /**
     * An ArrayList of currently available words.
     */
    private ArrayList<String> keyBank;

    /**
     * Make a HangmanStats object with given parameters.
     *
     * @param maxGuesses The maximum number of guesses allowed in a single game.
     * @param numGames The number of games played so far.
     * @param numWins The number of wins so far.
     * @param previousGameVictory Was the previous game a victory?
     * @param previousKey The correct word for the previous game.
     */
    public HangmanStats(int maxGuesses, int numGames, int numWins, boolean previousGameVictory, String previousKey) {
        this.maxGuesses = maxGuesses;
        this.numGames = numGames;
        this.numWins = numWins;
        this.previousGameVictory = previousGameVictory;
        this.previousKey = previousKey;
        importFiles();
    }

    public HangmanStats(int maxGuesses) {
        this.maxGuesses = maxGuesses;
        this.numGames = 0;
        this.numWins = 0;
        this.previousGameVictory = false;
        this.previousKey = "";
        importFiles();
    }

    /**
     * Update statistics after the end of a game.
     *
     * @param previousGameVictory Was the previous game a victory?
     * @param previousKey The correct word for the previous game.
     */
    public void gameEnded(boolean previousGameVictory, String previousKey) {
        this.numGames++;
        if(previousGameVictory)
            this.numWins++;
        this.previousGameVictory = previousGameVictory;
        this.previousKey = previousKey;
    }

    /**
     * Update statistics after the end of a game.
     *
     * @param previousGameVictory Was the previous game a victory?
     */
    public void gameEnded(boolean previousGameVictory) {
        this.numGames++;
        if(previousGameVictory)
            this.numWins++;
        this.previousGameVictory = previousGameVictory;
    }

    /**
     * Get a new random word, without duplicates. Also updates this.previousKey
     *
     * @return A new key.
     */
    public String getNewKey() {
        int index = ThreadLocalRandom.current().nextInt(0, this.keyBank.size());
        this.previousKey = this.keyBank.get(index).toUpperCase();
        this.keyBank.remove(index);
        return this.previousKey;
    }

    /**
     * Import the list of potential files to ArrayList
     */
    public void importFiles() {
        this.keyBank = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File("./res/words.txt"));
            while(sc.hasNext()) {
                this.keyBank.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Failure importing files: " + e.getMessage());
        }
    }

    /**
     * Increment the number of games played
     */
    public void incrementNumGames() {
        this.numGames++;
    }

    /**
     * Increment the number of wins
     */
    public void incrementNumWins() {
        this.numWins++;
     }

    /**
     * Set the win/loss status of the previous game.
     *
     * @param gameResult Was the previous game a victory?
     */
    public void setPreviousGameVictory(boolean gameResult) {
        this.previousGameVictory = gameResult;
     }

     public void setKey(String newKey) {
        this.previousKey = newKey;
     }

    /**
     * Get the maximum number of guesses allowed in a single game
     *
     * @return The maximum number of guesses in a single game
     */
    public int getMaxGuesses() {
        return this.maxGuesses;
    }

    /**
     * Get the number of games played so far
     *
     * @return The number of games played so far
     */
    public int getNumGames() {
        return this.numGames;
    }

    /**
     * Get the number of wins so far
     *
     * @return The number of wins
     */
    public int getNumWins() {
        return this.numWins;
    }

    /**
     * Get the win/loss status of the previous game
     *
     * @return True if the previous game was a victory, false otherwise.
     */
    public boolean isPreviousGameVictory() {
        return this.previousGameVictory;
    }

    /**
     * Get the correct word for the previous game.
     *
     * @return The previous key
     */
    public String getPreviousKey() {
        return this.previousKey;
    }
}
