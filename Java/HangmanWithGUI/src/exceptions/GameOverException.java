package exceptions;

import gui.GameOverWindow;

public class GameOverException extends Exception {
    public GameOverException(String message) {
        super(message);
    }
}
