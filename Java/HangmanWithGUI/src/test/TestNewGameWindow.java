package test;

import gui.NewGameWindow;

public class TestNewGameWindow implements Runnable {
    private static final int TIME = 2000;

    private NewGameWindow window;

    public TestNewGameWindow(NewGameWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIME);
            this.window.commandPressed('Y');
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
