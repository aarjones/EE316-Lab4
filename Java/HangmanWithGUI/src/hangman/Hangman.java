package hangman;

import exceptions.CharacterAlreadyGuessedException;
import exceptions.GameOverException;

import java.util.ArrayList;
import java.util.Arrays;

public class Hangman {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default number of bad guesses
     */
    public static final int DEFAULT_MAX_GUESSES = 6;
    /**
     * The blank character for currentText
     */
    public static final char BLANK_CHARACTER = '_';

    /* **************** PRIVATE VARS **************** */
    /**
     * The word we're guessing
     */
    private String key;
    /**
     * The current text to display.
     */
    private char[] currentText;
    /**
     * A list of any characters which have been guessed
     */
    private ArrayList<Character> allGuesses;
    /**
     * A list of characters which have been chosen incorrectly
     */
    private ArrayList<Character> badCharacters;
    /**
     * The number of incorrect guesses so far
     */
    private int incorrectGuesses;
    /**
     * How many characters have we found
     */
    private int foundChars;
    /**
     * The maximum number of incorrect guesses before the game ends
     */
    private int maxIncGuesses;
    /**
     * Has the puzzle been solved?
     */
    private boolean solved;
    /**
     * Should the game end?
     */
    private boolean endGame;

    /**
     * Constructor for Hangman.  Allows the custom choice of maximum number of guesses
     *
     * @param key The word to be guessed
     * @param maxIncGuesses The maximum number of guesses allowed
     * @throws IllegalArgumentException Thrown when key is > 16 characters long, after trim().
     */
    public Hangman(String key, int maxIncGuesses) throws IllegalArgumentException {
        key = key.trim(); //remove spaces
        if(key.length() > 16) throw new IllegalArgumentException("Word must be less than 16 characters");

        this.key = key;
        this.currentText = new char[key.length()];
        Arrays.fill(this.currentText, BLANK_CHARACTER);

        this.maxIncGuesses = maxIncGuesses;
        this.incorrectGuesses = 0;
        this.foundChars = 0;
        this.endGame = false;
        this.solved = false;

        this.allGuesses = new ArrayList<Character>();
        this.badCharacters = new ArrayList<Character>();
    }

    /**
     * Constructor for Hangman.  Uses DEFAULT_MAX_GUESSES.
     *
     * @param key The word to be guessed
     */
    public Hangman(String key) {
        this(key, DEFAULT_MAX_GUESSES);
    }

    /**
     * Check if a character is contained in this.key.  If it is, update this.currentText with the newly found letter.  Keeps track of all characters guessed, and updates this.solved and this.endGame.
     *
     * @param c The character to search for in this.key
     * @return Was the character found?
     * @throws CharacterAlreadyGuessedException Thrown when a character is guessed for a second time.
     */
    public boolean checkLetter(char c) throws CharacterAlreadyGuessedException, GameOverException {
        if(this.allGuesses.contains(c)) throw new CharacterAlreadyGuessedException(c + " has already been guessed!");
        if(this.endGame) throw new GameOverException("The game is already over!");

        //check if the character is in the key
        if(this.key.indexOf(c) != -1) {
            int index = this.key.indexOf(c);
            while(index != -1) {
                this.currentText[index] = this.key.charAt(index); //and every occurrence of it is added to the currentText
                this.foundChars++;
                index = this.key.indexOf(c, index + 1); //check for the next one
            }
            //Check if the game is over
            if(this.foundChars == this.key.length()) {
                this.solved = true;
                this.endGame = true;
            }
            //Key track of everything that has been found
            this.allGuesses.add(c);
            return true;

        } else {
            //Key track of bad letters
            this.badCharacters.add(c);
            this.allGuesses.add(c);

            //Another incorrect guess, check if game is over
            incorrectGuesses++;
            if(incorrectGuesses == maxIncGuesses) endGame = true;

            return false;
        }
    }

    /**
     * Get the currentText to display
     *
     * @return The currently guessed form of the word
     */
    public String getCurrentText() {
        return new String(this.currentText);
    }

    /**
     * Gets a list of all the bad characters guessed
     *
     * @return an ArrayList with every bad character guessed
     */
    public String getBadGuesses() {
        StringBuilder toReturn = new StringBuilder();
        for(Character c : this.badCharacters)
            toReturn.append(c).append(" ");

        return toReturn.toString();
    }

    public int getRemainingGuesses() {
        return this.maxIncGuesses - this.incorrectGuesses;
    }

    /**
     * Is the game over?
     *
     * @return this.endGame
     */
    public boolean gameOver() {
        return this.endGame;
    }

    /**
     * Has the puzzle been correctly solved?
     *
     * @return this.solved
     */
    public boolean isSolved() {
        return this.solved;
    }

}
