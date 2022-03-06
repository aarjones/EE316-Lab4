package gui;

import hangman.Hangman;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class MainWindow extends Application {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 1200;
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
     * A button used to reset this hangman object
     */
    private Button resetButton;

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
        int colCount = 6;
        int rowCount = 6;
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / colCount);

        //Make a grid to place the other items
        GridPane grid = new GridPane();
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
        this.badGuesses.setPrefHeight(SCREEN_HEIGHT * 0.5);
        this.badGuesses.setPrefWidth(SCREEN_WIDTH * 5/6d);
        this.badGuesses.setEditable(false);
        this.badGuesses.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white;");

        //Set up the current word field
        this.currentText = new TextField();
        this.currentText.setPrefHeight(SCREEN_HEIGHT * 0.5);
        this.currentText.setPrefWidth(SCREEN_WIDTH * 5/6d);
        this.currentText.setEditable(false);
        this.currentText.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white;");

        //Set up the remaining guesses field
        this.remainingGuesses = new TextField();
        this.remainingGuesses.setPrefHeight(SCREEN_HEIGHT / 6d);
        this.remainingGuesses.setPrefWidth(SCREEN_WIDTH / 6d);
        this.remainingGuesses.setEditable(false);
        this.remainingGuesses.setStyle("-fx-font-family: 'monospaced';-fx-control-inner-background: #222222; -fx-text-fill: white;");

        //Set up the tmp hangman photo
        this.hangmanImageView = new ImageView(new Image("file:./res/hangman_tmp.png"));
        this.hangmanImageView.setPreserveRatio(false);
        this.hangmanImageView.setFitHeight(windowHeight * 4/6d);
        this.hangmanImageView.setFitWidth(windowWidth / 6d);

        //Set up the reset button
        resetButton = new Button("Reset");
        resetButton.setPrefWidth(SCREEN_WIDTH / 6d);
        resetButton.setPrefHeight(SCREEN_HEIGHT / 6d);
        resetButton.setStyle("-fx-background-color: #222222; -fx-text-fill: white;");

        //Add constraints to the grid
        for(int i = 0; i < rowCount; i++)
            grid.getRowConstraints().add(rc);
        for(int i = 0; i < colCount; i++)
            grid.getColumnConstraints().add(cc);

        //Add things to the grid
        grid.add(this.currentText, 0, 3, 5, 3);
        grid.add(this.badGuesses, 0, 0, 5, 3);
        grid.add(this.remainingGuesses, 5, 0, 1, 1);
        grid.add(this.hangmanImageView, 5, 1, 1, 4);
        grid.add(this.resetButton, 5, 5, 1, 1);

        //Set up the scene
        Scene scene = new Scene(grid, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowWidth = newValue.intValue();
                hangmanImageView.setFitWidth(windowWidth / 6d);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowHeight = newValue.intValue();
                hangmanImageView.setFitHeight(windowHeight * 4/6d);
            }
        });

        //Show the program
        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
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
