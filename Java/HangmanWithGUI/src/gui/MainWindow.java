package gui;

import com.fazecast.jSerialComm.SerialPort;
import comports.comInterface;
import hangman.Hangman;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class MainWindow extends Application {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 400;
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
    private comInterface comPort;
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
     * Used to import Hangman paramters
     */
    public MainWindow(String portDescriptor, int baud, int numBadGuesses) {
        this.comPort = new comInterface(portDescriptor, baud);

        this.hangman = new Hangman("TMP_KEY", numBadGuesses);
    }

    /**
     * Starts the Hangman GUI
     *
     * @param primaryStage The stage on which to launch the program
     * @throws Exception Exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        this.badGuesses = new TextField();
        this.badGuesses.setPrefHeight(SCREEN_HEIGHT / 6d);
        this.badGuesses.setPrefWidth(SCREEN_WIDTH * 3/4d);
        this.badGuesses.setEditable(false);
        this.badGuesses.setText("Bad characters...");
        this.badGuesses.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white; -fx-font-size: 15pt;");

        //Set up the current word field
        this.currentText = new TextField();
        this.currentText.setPrefHeight(SCREEN_HEIGHT / 6d);
        this.currentText.setPrefWidth(SCREEN_WIDTH * 3/4d);
        this.currentText.setEditable(false);
        this.currentText.setText("Current word...");
        this.currentText.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white; -fx-font-size: 15pt;");

        //Set up the remaining guesses field
        this.remainingGuesses = new TextField();
        this.remainingGuesses.setPrefHeight(SCREEN_HEIGHT / 6d);
        this.remainingGuesses.setPrefWidth(SCREEN_WIDTH / 4d);
        this.remainingGuesses.setEditable(false);
        this.remainingGuesses.setText("# guesses");
        this.remainingGuesses.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white; -fx-font-size: 15pt;");

        //Set up the tmp hangman photo
        this.hangmanImageView = new ImageView(new Image("file:./res/hangman_tmp.png"));
        this.hangmanImageView.setTranslateY(30);
        this.hangmanImageView.setPreserveRatio(false);
        this.hangmanImageView.setFitHeight(windowHeight * 3/7d);

        ImageView hangmanGallows = new ImageView(new Image("file:./res/gallows.png"));
        hangmanGallows.setPreserveRatio(false);
        hangmanGallows.setFitWidth(windowWidth * 5/6d);
        hangmanGallows.setFitHeight(windowHeight * 4/7d);

        //Set up StackPane for hangman
        this.hangmanBox = new StackPane();
        this.hangmanBox.getChildren().add(hangmanGallows);
        this.hangmanBox.getChildren().add(this.hangmanImageView);
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

        //Show the program
        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        //Handle closing
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                comPort.closePort();
            }
        });
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
