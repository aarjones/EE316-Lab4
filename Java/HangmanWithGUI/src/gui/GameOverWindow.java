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

public class GameOverWindow extends Window {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 350;
    /**
     * The default width of the window
     */
    public static final int DEFAULT_HEIGHT = 100;

    /* **************** PRIVATE VARS **************** */
    /**
     * The ComInterface used by this window
     */
    private ComInterface comPort;
    /**
     * The HangmanStats for this session
     */
    private HangmanStats gameStats;

    /**
     * Generates a NewGameWindow and opens a COM port
     *
     * @param portDescriptor The port descriptor of which COM port to use
     * @param baud The baud rate to use for the COM port
     * @param gameStats The HangmanStats for the current session
     */
    public GameOverWindow(String portDescriptor, int baud, HangmanStats gameStats) {
        this.comPort = new ComInterface(portDescriptor, baud);
        this.gameStats = gameStats;
    }

    /**
     * Generates a NewGameWindow with a pre-initialized COM port
     *
     * @param comPort A ComInterface for an open COM port.
     * @param gameStats The HangmanStats for the current session.
     */
    public GameOverWindow(ComInterface comPort, HangmanStats gameStats) {
        this.comPort = comPort;
        this.gameStats = gameStats;
    }

    /**
     * Allows this window to be run individually.  Customize these values to test the logic used to display victory messages, etc.
     */
    public GameOverWindow() {
        this.comPort = null;
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
        vBox.getChildren().add(makeLabel("GAME OVER!"));
        vBox.getChildren().add(makeLabel("Final Score: " + this.gameStats.getNumWins() + " correct out of " + this.gameStats.getNumGames() + " puzzles"));

        //Set up the scene
        Scene scene = new Scene(vBox, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add("file:./src/resources/game-over-style.css");

        //Show the program
        primaryStage.setTitle("Hangman: Game Over");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        LcdController.updateLCDGameOverWindow(this.comPort, this.gameStats);

        //Close the COM port
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (comPort != null)
                    comPort.closePort();
                System.exit(0);
            }
        });
    }

    /**
     * Placeholder.
     *
     * @param c The character received.
     */
    @Override
    public void keyPressed(char c) {
        //do nothing
    }

    public static void main(String[] args) {
        launch(args);
    }
}
