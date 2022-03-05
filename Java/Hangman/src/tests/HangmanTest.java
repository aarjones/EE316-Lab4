package tests;

import exceptions.CharacterAlreadyGuessedException;
import hangman.Hangman;

import java.util.Random;

public class HangmanTest {

    public static void main(String[] args) {
        //Make a hangman1 object with a chosen word
        Hangman hangman1 = new Hangman("bettermoron", 8);
        Hangman hangman2 = new Hangman("moron");
        //RNG for guessing characters
        Random random = new Random();

        //Print initial state of the text to display
        System.out.println("Current Text: " + hangman1.getCurrentText());
        System.out.println();

        //Keep guessing until the game is over
        while(!hangman1.gameOver()) {
            char toGuess = (char) (random.nextInt(26) + 'a'); //generate a random guess
            System.out.println("Guessing: " + toGuess); //print it
            try {
                hangman1.checkLetter(toGuess); //Guess the letter
                System.out.println("Current Text: " + hangman1.getCurrentText());
                System.out.println();
            } catch (CharacterAlreadyGuessedException cag) {
                System.err.println(cag.getMessage());
            }
        }

        try {
            hangman2.checkLetter('m');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }
        try {
            hangman2.checkLetter('r');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }
        try {
            hangman2.checkLetter('o');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }
        try {
            hangman2.checkLetter('a');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }
        try {
            hangman2.checkLetter('b');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }
        try {
            hangman2.checkLetter('n');
            System.out.println("Current Text: " + hangman2.getCurrentText());
            System.out.println();
        } catch(CharacterAlreadyGuessedException cag) {
            System.out.println(cag.getMessage());
        }

        System.out.println("Result of hangman1: " + hangman1.isSolved());
        System.out.println("Current Text hangman1: " + hangman1.getCurrentText());
        System.out.println("Bad Guesses hangman1: " + hangman1.getBadGuesses());
        System.out.println();

        System.out.println("Result of hangman2: " + hangman2.isSolved());
        System.out.println("Current Text hangman2: " + hangman2.getCurrentText());
        System.out.println("Bad Guesses hangman2: " + hangman2.getBadGuesses());
        System.out.println();
    }
}
