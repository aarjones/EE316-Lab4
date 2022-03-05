package exceptions;

public class CharacterAlreadyGuessedException extends Exception {
    public CharacterAlreadyGuessedException(String message) {
        super(message);
    }
}
