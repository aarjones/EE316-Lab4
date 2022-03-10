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
            this.window.keyPressed('A');
            Thread.sleep(TIME);
            this.window.keyPressed('T');
            Thread.sleep(TIME);
            this.window.keyPressed('B');
            Thread.sleep(TIME);
            this.window.keyPressed('E');
            Thread.sleep(TIME);
            this.window.keyPressed('P');
            Thread.sleep(TIME);
            this.window.keyPressed('M');
            Thread.sleep(TIME);
            this.window.keyPressed('C');
            Thread.sleep(TIME);
            this.window.keyPressed('K');
            Thread.sleep(TIME);
            this.window.keyPressed('Y');
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
