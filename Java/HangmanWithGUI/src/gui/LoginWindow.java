package gui;

import hangman.HangmanStats;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static comports.ComInterface.getComPorts;

public class LoginWindow extends Window {
    /* **************** PUBLIC VARS **************** */
    /**
     * The default height of the window
     */
    public static final int DEFAULT_WIDTH = 400;
    /**
     * The default width of the window
     */
    public static final int DEFAULT_HEIGHT = 600;

    /* **************** PRIVATE VARS **************** */
    /**
     * An array of VBoxes, one for each row
     */
    private VBox[] rows;
    /**
     * A Drop-Down menu from which the user can select which com port to use
     */
    private ComboBox<String> comPorts;
    /**
     * A button to start the game.
     */
    private Button startButton;
    /**
     * A TextField where the user can select a baud rate
     */
    private TextField baudField;
    /**
     * A TextField where the user can select a baud rate
     */
    private TextField badGuesses;

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

        //Set up the VBoxes
        this.rows = new VBox[rowCount];
        for(int i = 0; i < rowCount; i++)
            this.rows[i] = new VBox();

        //Set up the label
        rows[0].getChildren().add(makeLabel("Hangman Setup"));
        rows[0].getChildren().get(0).getStyleClass().add("program-title");
        rows[0].setAlignment(Pos.TOP_CENTER);

        //Set up the drop-down menu for choosing COM ports.
        this.comPorts = new ComboBox<String>();
        this.comPorts.setItems(FXCollections.observableList(getComPorts()));

        //Add it to a VBox
        rows[1].getChildren().add(makeLabel("Select a COM Port:"));
        rows[1].getChildren().add(this.comPorts);

        //Set up the baudRate TextField and VBox
        rows[2].getChildren().add(makeLabel("Select a baud rate:"));
        this.baudField = makeTextField("9600", true);
        rows[2].getChildren().add(this.baudField);

        //Set up the badGuesses TextField
        rows[3].getChildren().add(makeLabel("Maximum Number of Bad Guesses:"));
        this.badGuesses = makeTextField("6", true);
        rows[3].getChildren().add(this.badGuesses);

        //Set up the start Button
        this.startButton = makeButton("Start Game", SCREEN_WIDTH, SCREEN_HEIGHT);
        this.startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HangmanStats gameStats = new HangmanStats(Integer.parseInt(badGuesses.getText()));
                Window window = new NewGameWindow(comPorts.getValue(), Integer.parseInt(baudField.getText()), gameStats);
                try {
                    window.start(new Stage());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                primaryStage.close();
            }
        });

        //add it to a VBox
        rows[4].getChildren().add(this.startButton);

        //Make a grid to place the other items
        GridPane grid = new GridPane();
        grid.setPrefHeight(SCREEN_HEIGHT);
        grid.setPrefWidth(SCREEN_WIDTH);
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

        //Add constraints to the grid
        for(int i = 0; i < rowCount; i++)
            grid.getRowConstraints().add(i, rc);
        for(int i = 0; i < colCount; i++)
            grid.getColumnConstraints().add(cc);

        //Add things to the grid
        for(int i = 0; i < rowCount; i++)
            grid.add(rows[i], 0, i);

        //Set up the scene
        Scene scene = new Scene(grid, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add("file:./src/resources/login-style.css");

        //Show the program
        primaryStage.setTitle("Hangman Login");
        primaryStage.getIcons().add(new Image("file:./res/icon1.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        playSound();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * Placeholder
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
