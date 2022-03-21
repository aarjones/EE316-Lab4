package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.applet.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public abstract class Window extends Application {
    /**
     * An abstract method for performing operations based on a key received from the ComInterface.
     *
     * @param c The character received.
     */
    public abstract void keyPressed(char c);

    /**
     * Play music.
     */
    public static void playSound() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream( new File("./res/benny-hill.wav"));
                    clip.open(inputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Create an ImageView.  Used to create each Hangman piece
     *
     * @param filepath The path to the file
     * @param windowHeight The height of the window
     * @return An ImageView containing the image found at filepath.
     */
    public static ImageView buildImageView(String filepath, int windowHeight) {
        ImageView image = new ImageView(new Image(filepath));
        image.setTranslateY(30);
        image.setTranslateX(-90);
        image.setPreserveRatio(false);
        image.setFitHeight(windowHeight * 3/7d);

        return image;
    }

    /**
     * Builds a TextField
     *
     * @param text The default text of the TextField
     * @param editable Should the TextField be editable?
     * @return A new TextField with the given parameters
     */
    protected static TextField makeTextField(String text, boolean editable) {
        TextField toReturn = new TextField(text);
        toReturn.setEditable(editable);
        return toReturn;
    }

    /**
     * Builds a Label
     *
     * @param label The text to show on the Label
     * @return A new Label with the given parameters
     */
    protected static Label makeLabel(String label) {
        Label toReturn = new Label(label);
        toReturn.setAlignment(Pos.TOP_CENTER);
        return toReturn;
    }

    /**
     * Builds a Button
     *
     * @param label The text to show on the button
     * @param width The width of the button
     * @param height The height of the button
     * @return A new Button with the given parameters
     */
    protected static Button makeButton(String label, int width, int height) {
        Button toReturn = new Button(label);
        toReturn.setPrefHeight(height);
        toReturn.setPrefWidth(width);
        return toReturn;
    }
}
