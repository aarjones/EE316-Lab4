package gui;

import comports.ComInterface;
import comports.LcdController;
import hangman.HangmanStats;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Arrays;

public class NewGameWindow extends Window {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 300;
    /**
     * The default width of the window
     */
    public static final int DEFAULT_HEIGHT = 100;

    /* **************** PRIVATE VARS **************** */
    /**
     * The primaryStage used by this window
     */
    private Stage primaryStage;
    /**
     * The ComInterface used by this window
     */
    private ComInterface comPort;
    /**
     * The previous Stage
     */
    private Stage previousStage;
    /**
     * The HangmanStats for this session
     */
    private HangmanStats gameStats;

    /**
     * Generates a NewGameWindow and opens a COM port
     *
     * @param portDescriptor The port descriptor of which COM port to use
     * @param baud The baud rate to use for the COM port
     * @param gameStats The HangmanStats for this session
     */
    public NewGameWindow(String portDescriptor, int baud, HangmanStats gameStats) {
        this.comPort = new ComInterface(portDescriptor, baud);
        this.gameStats = gameStats;

        //Since this is a new ComInterface, start the ComInterface thread.
        new Thread(this.comPort).start();
    }

    /**
     * Generates a NewGameWindow with a pre-initialized COM port.  Keeps the previous window open until a new game is
     * started or this game is ended.
     *
     * @param comPort A ComInterface for an open COM port.
     * @param gameStats The HangmanStats for this session
     * @param previousStage The previous window.
     */
    public NewGameWindow(ComInterface comPort, HangmanStats gameStats, Stage previousStage) {
        this.comPort = comPort;
        this.previousStage = previousStage;
        this.gameStats = gameStats;
    }

    /**
     * Allows this window to be run individually.  Customize these values to test the logic used to display victory
     * messages, etc.
     */
    public NewGameWindow() {
        this.comPort = new ComInterface();
        this.gameStats = new HangmanStats(6, 0, 0, false, "");
    }

    /**
     * Launches the Login Window.
     *
     * @param primaryStage The stage on which to launch the LoginWindow
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        //Set the size of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final int SCREEN_WIDTH = (int)screenBounds.getWidth();
        final int SCREEN_HEIGHT = (int)screenBounds.getHeight();

        //Set up the sizing of the GUI's grid
        int colCount = 1;
        int rowCount = 5;
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / colCount);

        //Create the vBox
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getStyleClass().add("vbox");
        if(this.gameStats.getNumGames() != 0) {
            if (this.gameStats.isPreviousGameVictory())
                vBox.getChildren().add(makeLabel("You win!"));
            else {
                vBox.getChildren().add(makeLabel("Sorry!"));
                vBox.getChildren().add(makeLabel("The correct word was: " + this.gameStats.getPreviousKey()));
            }
            vBox.getChildren().add(makeLabel("You have solved " + this.gameStats.getNumWins() + " puzzles out of " + this.gameStats.getNumGames()));
        }
        vBox.getChildren().add(makeLabel("New Game? (Y/n)"));

        //Set up the scene
        Scene scene = new Scene(vBox, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add("file:./src/resources/new-game-style.css");

        //Show the program
        primaryStage.setTitle("Hangman: New Game");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        //update the text shown on the LCD
        updateLCD();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (comPort != null)
                    comPort.closePort();
                System.exit(0);
            }
        });
        //new Thread(new TestNewGameWindow(this)).start();
    }

    /**
     * Perform operations based on a key press being registered by the FPGA
     *
     * @param c The character pressed.
     */
    @Override
    public void keyPressed(char c) {
        if(c == 'Y') {
            this.previousStage.close();
            Window window = new MainWindow(this.comPort, this.gameStats);
            this.comPort.updateWindow(window);
            try {
                window.start(new Stage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            this.primaryStage.close();
        } else if(c == 'N') {
            this.previousStage.close();
            GameOverWindow window = new GameOverWindow(this.comPort, this.gameStats);
            this.comPort.updateWindow(window);
            try {
                window.start(new Stage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            this.primaryStage.close();
        }
    }

    private void updateLCD() {
        //send win/loss message
        String top, bottom = "";
        if(this.gameStats.isPreviousGameVictory())
            top = "Well Done! You have solved " + this.gameStats.getNumWins() + " puzzles out of " + this.gameStats.getNumGames();
        else
            top = "Sorry! The correct word was " + this.gameStats.getPreviousKey() + ". You have solved " + this.gameStats.getNumWins() + " puzzles out of " + this.gameStats.getNumGames();

        LcdController lcd = new LcdController(this.comPort, top, bottom);
        Thread lcdThread = new Thread(lcd);
        lcdThread.start();
        try {
            lcdThread.join();
            Thread.sleep(1000);
        } catch(InterruptedException ie) {
            System.err.println("Error in updateLCD(): " + ie.getMessage());
        }

        //send new game prompt
        lcd = new LcdController(this.comPort, "New Game? (y/n)", "");
        new Thread(lcd).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
