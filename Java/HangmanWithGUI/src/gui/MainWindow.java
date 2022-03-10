package gui;

import comports.ComInterface;
import exceptions.CharacterAlreadyGuessedException;
import exceptions.GameOverException;
import hangman.Hangman;
import hangman.HangmanStats;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import test.TestMainWindow;

public class MainWindow extends Window {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 500;
    /**
     * The default width of the window
     */
    public static final int DEFAULT_HEIGHT = 800;

    /* **************** PRIVATE VARS **************** */
    /**
     * The current height of the window.
     */
    private int windowHeight = DEFAULT_HEIGHT;
    /**
     * The width of the Window
     */
    private int windowWidth = DEFAULT_WIDTH;
    /**
     * The Hangman object on which this gui operates
     */
    private Hangman hangman;
    /**
     * The SerialPort through which the game will communicate
     */
    private ComInterface comPort;
    /**
     * The Stage for this Window
     */
    private Stage stage;
    /**
     * A TextField where the current text will be shown.
     */
    private TextField currentText;
    /**
     * A TextField where the bad guesses will be shown.
     */
    private TextField badGuesses;
    /**
     * A TextField where the number of remaining guesses will be shown.
     */
    private TextField remainingGuesses;
    /**
     * A placeholder for the hangman to be shown
     */
    private ImageView hangmanImageView;
    /**
     * The VBox where the Image will be placed
     */
    private StackPane hangmanBox;
    /**
     * The HangmanStats for this session
     */
    private HangmanStats gameStats;

    /**
     * Create a new MainWindow, maintaining all COM parameters and game statistics.
     *
     * @param portDescriptor The portDescriptor of the COM port to use in this game
     * @param baud The baud rate to use for the COM port
     * @param gameStats The HangmanStats for the current session.
     */
    public MainWindow(String portDescriptor, int baud, HangmanStats gameStats) {
        this.comPort = new ComInterface(portDescriptor, baud);
        this.hangman = new Hangman(gameStats.getNewKey(), gameStats.getMaxGuesses());
        this.gameStats = gameStats;
    }

    /**
     * Create a new MainWindow, maintaining all COM parameters and game statistics
     *
     * @param comPort The ComInterface used by this game
     * @param gameStats The HangmanStats for the current session
     */
    public MainWindow(ComInterface comPort, HangmanStats gameStats) {
        this.comPort = comPort;
        this.hangman = new Hangman(gameStats.getNewKey(), gameStats.getMaxGuesses());
        this.gameStats = gameStats;
    }

    /**
     * Allows us to run just this file.
     */
    public MainWindow() {
        this.comPort = new ComInterface(); //runs on System.in and System.out
        this.gameStats = new HangmanStats(6, 0, 0, false, "");
        this.hangman = new Hangman(this.gameStats.getNewKey(), 6);

        this.comPort.updateWindow(this);
        new Thread(this.comPort).start();
    }

    /**
     * Starts the Hangman GUI
     *
     * @param primaryStage The stage on which to launch the program
     * @throws Exception Exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        //Set the size of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final int SCREEN_WIDTH = (int)screenBounds.getWidth();
        final int SCREEN_HEIGHT = (int)screenBounds.getHeight();

        //Set up the sizing of the GUI's grid
        int colCount = 4;
        int rowCount = 6;
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / colCount);

        //Make a grid to place the other items
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setBackground(new Background(new BackgroundFill(
                new LinearGradient(
                        0, 0, 0, 0.4, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("222222")), new Stop(1, Color.web("1b1b1b"))),
                CornerRadii.EMPTY, Insets.EMPTY
        )));

        //Set up the bad guesses field
        this.badGuesses = makeTextField("Bad characters...", false);

        //Set up the current word field
        this.currentText = makeTextField("Current word...", false);

        //Set up the remaining guesses field
        this.remainingGuesses = makeTextField("# Guesses", false);

        //Set up the tmp hangman photo
        /*
        this.hangmanImageView = new ImageView(new Image("file:./res/HangmanFull.png"));
        this.hangmanImageView.setTranslateY(30);
        this.hangmanImageView.setTranslateX(-90);
        this.hangmanImageView.setPreserveRatio(false);
        this.hangmanImageView.setFitHeight(windowHeight * 3/7d);
         */

        ImageView hangmanGallows = new ImageView(new Image("file:./res/gallows.png"));
        hangmanGallows.setPreserveRatio(false);
        hangmanGallows.setTranslateX(60);
        hangmanGallows.setFitWidth(windowWidth * 5/6d);
        hangmanGallows.setFitHeight(windowHeight * 4/7d);

        //Set up StackPane for hangman
        this.hangmanBox = new StackPane();
        this.hangmanBox.getChildren().add(hangmanGallows);
        this.hangmanBox.setAlignment(Pos.CENTER_LEFT);
        this.hangmanBox.setPrefHeight(SCREEN_HEIGHT * 4/6d);
        this.hangmanBox.setPrefWidth(SCREEN_WIDTH);

        //Add constraints to the grid
        for(int i = 0; i < rowCount; i++)
            grid.getRowConstraints().add(rc);
        for(int i = 0; i < colCount; i++)
            grid.getColumnConstraints().add(cc);

        //Add things to the grid
        grid.add(this.badGuesses, 0, 0, 3, 1);
        grid.add(this.remainingGuesses, 3, 0, 1, 1);
        grid.add(this.hangmanBox, 0, 1, colCount, 4);
        grid.add(this.currentText, 0, 5, 4, 1);

        //Set up the scene
        Scene scene = new Scene(grid, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowHeight = newValue.intValue();
                hangmanImageView.setFitHeight(windowHeight * 3/6d);
                hangmanGallows.setFitHeight(windowHeight * 4/7d);
            }
        });
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowWidth = newValue.intValue();
                hangmanGallows.setFitWidth(windowWidth * 5/6d);
            }
        });

        scene.getStylesheets().add("file:./src/resources/main-style.css");

        //Show the program
        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        updateFields();

        //Handle closing
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (comPort != null)
                    comPort.closePort();
                System.exit(0);
            }
        });

        /*
        Run the test script on a second thread
        This is very similar to how I expect to get input from the FPGA--start a second thread which uses MainWindow's guessLetter() function
        */
        //new Thread(new TestMainWindow(this)).start();
    }

    /**
     * A key has been pressed.  Used to guess letters using the Hangman object.
     *
     * @param c The letter to guess
     */
    public void keyPressed(char c) {
        try {
            this.hangman.checkLetter(c);
            updateFields();
        } catch(CharacterAlreadyGuessedException cag) {
            //ignore this character/do nothing
        } catch (GameOverException goe) {
            updateFields();
        }
    }

    /**
     * Update all of MainWindow's fields
     */
     private void updateFields() {
        //Update each TextField
        this.remainingGuesses.setText(Integer.toString(this.hangman.getRemainingGuesses()));
        this.badGuesses.setText(this.hangman.getBadGuesses());
        this.currentText.setText(this.hangman.getCurrentText());

        //hang the man here
        switch(this.hangman.getRemainingGuesses()) {
            case 0:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-right-leg.png", DEFAULT_HEIGHT));
                break;
            case 1:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-left-leg.png", DEFAULT_HEIGHT));
                break;
            case 2:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-right-arm.png", DEFAULT_HEIGHT));
                break;
            case 3:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-left-arm.png", DEFAULT_HEIGHT));
                break;
            case 4:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-torso.png", DEFAULT_HEIGHT));
                break;
            case 5:
                this.hangmanBox.getChildren().add(buildImageView("file:./res/hangman-head.png", DEFAULT_HEIGHT));
                break;
            default:
                break;
        }

        //Handle if the game is over
        checkGameOver();

     }

     public void checkGameOver() {
         if(this.hangman.gameOver()) {
             this.gameStats.gameEnded(this.hangman.isSolved(), this.hangman.getKey());
             Window window = new NewGameWindow(this.comPort, this.gameStats, this.stage);
             this.comPort.updateWindow(window);
             try {
                 window.start(new Stage());
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }

    /**
     * Starts the GUI
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
