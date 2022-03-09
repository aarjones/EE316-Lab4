package gui;

import com.sun.deploy.security.SelectableSecurityManager;
import comports.ComInterface;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sun.applet.Main;
import sun.security.x509.OtherName;
import test.TestMainWindow;
import test.TestNewGameWindow;

public class NewGameWindow extends Application {
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
     * The number of bad guesses for this set of games
     */
    private int numBadGuesses;
    /**
     * The number of games played so far
     */
    private int numGames;
    /**
     * The number of wins so far
     */
    private int numWins;
    /**
     * If the previous game was a victory
     */
    private boolean victory;

    /**
     * Generates a NewGameWindow and opens a COM port
     *
     * @param portDescriptor The port descriptor of which COM port to use
     * @param baud The baud rate to use for the COM port
     * @param numGames The number of games played so far
     * @param numWins The number of wins so far
     * @param numBadGuesses The maximum number of bad guesses allowed in a game
     * @param victory Was the last game a victory?
     */
    public NewGameWindow(String portDescriptor, int baud, int numGames, int numWins, int numBadGuesses, boolean victory) {
        this.comPort = new ComInterface(portDescriptor, baud);
        this.numBadGuesses = numBadGuesses;
        this.numGames = numGames;
        this.numWins = numWins;
        this.victory = victory;
    }

    /**
     * Generates a NewGameWindow with a pre-initialized COM port
     *
     * @param comPort A ComInterface for an open COM port.
     * @param numGames The number of games played so far
     * @param numWins The number of wins so far
     * @param numBadGuesses The maximum number of bad guesses allowed in a game
     * @param victory Was the last game a victory?
     */
    public NewGameWindow(ComInterface comPort, int numGames, int numWins, int numBadGuesses, boolean victory) {
        this.comPort = comPort;
        this.numBadGuesses = numBadGuesses;
        this.numGames = numGames;
        this.numWins = numWins;
        this.victory = victory;
    }

    /**
     * Allows this window to be run individually.  Customize these values to test the logic used to display victory messages, etc.
     */
    public NewGameWindow() {
        this.comPort = null;
        this.victory = false;
        this.numBadGuesses = 6;
        this.numGames = 0;
        this.numWins = 0;
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
        if(this.numGames != 0) {
            if (this.victory)
                vBox.getChildren().add(makeLabel("You win!"));
            else
                vBox.getChildren().add(makeLabel("You Lose!"));
            vBox.getChildren().add(makeLabel("You have solved " + this.numWins + " puzzles out of " + this.numGames));
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

        //new Thread(new TestNewGameWindow(this)).start();
    }

    /**
     * Builds a Label
     *
     * @param label The text to show on the Label
     * @return A new Label with the given parameters
     */
    private static Label makeLabel(String label) {
        Label toReturn = new Label(label);
        toReturn.setAlignment(Pos.TOP_CENTER);
        return toReturn;
    }

    /**
     * A command key has been pressed
     *
     * @param c The character pressed.
     */
    public void commandPressed(char c) {
        if(c == 'Y') {
            MainWindow window = new MainWindow(this.comPort, this.numBadGuesses, this.numWins, this.numGames);
            try {
                window.start(new Stage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            this.primaryStage.close();
        } else if(c == 'N') {
            GameOverWindow window = new GameOverWindow(this.comPort, this.numBadGuesses, this.numWins, this.numGames);
            try {
                window.start(new Stage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
