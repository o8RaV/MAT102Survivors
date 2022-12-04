package views;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 *  A class with all methods for the text reader.
 */
public class TextReaderView {

    static MediaPlayer textReaderMediaPlayer;

    static Media textReaderMedia;

    /**
     * Helper method for all other methods to play the file of a certain file name.
     * @param file_name The name of the audio file to play
     */
    private static void playAudio(String file_name) {
        textReaderMedia = new Media(new File(file_name).toURI().toString());
        textReaderMediaPlayer = new MediaPlayer(textReaderMedia);
        textReaderMediaPlayer.play();
    }

    /**
     * This method allows the program to read aloud the sound of the letter that corresponds to parameter c
     * (iff textReaderEnabled is set to true).
     * @param c The letter to be read by text to speech
     */
    public static void playLetter(Character c) {
        String file_name = "./audiofiles/" + Character.toUpperCase(c) + ".mp3";
        playAudio(file_name);
    }

    /**
     * This method allows the program to read aloud the text that appears on the button that is clicked.
     * @param button The button to be read by text to speech
     */
    public static void playButton(String button) {
        String file_name = "./audiofiles/" +  button + ".mp3";
        playAudio(file_name);
    }


}
