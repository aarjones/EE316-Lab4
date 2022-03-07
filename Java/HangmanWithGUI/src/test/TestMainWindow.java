package test;

import exceptions.CharacterAlreadyGuessedException;
import gui.MainWindow;

public class TestMainWindow implements Runnable {
    private static final int TIME = 2000;

    MainWindow window;

    public TestMainWindow(MainWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIME);
            this.window.guessLetter('A');
            Thread.sleep(TIME);
            this.window.guessLetter('T');
            Thread.sleep(TIME);
            this.window.guessLetter('B');
            Thread.sleep(TIME);
            this.window.guessLetter('E');
            Thread.sleep(TIME);
            this.window.guessLetter('P');
            Thread.sleep(TIME);
            this.window.guessLetter('M');
            Thread.sleep(TIME);
            this.window.guessLetter('C');
            Thread.sleep(TIME);
            this.window.guessLetter('K');
            Thread.sleep(TIME);
            this.window.guessLetter('Y');
            Thread.sleep(TIME);
        } catch (CharacterAlreadyGuessedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
