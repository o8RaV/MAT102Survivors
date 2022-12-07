package views;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 *  A class with all methods for the text reader.
 *  CRUCIAL: DO NOT CHANGE ANY FILE NAMES IN THE AUDIOFILES FOLDER
 */
public class TextReaderView {

    static MediaPlayer textReaderMediaPlayer;

    static Media textReaderMedia;

    /**
     * This method allows the program to read aloud the text corresponding to a specified file name (.mp3 only).
     * @param str the string of the audio to be played
     */
    public static void playAudio(String str, Boolean textReaderEnabled) {
        if (textReaderEnabled) {
            String file_name = "audiofiles/" +  str + ".mp3";
            try {
                textReaderMedia = new Media(new File(file_name).toURI().toString());
                textReaderMediaPlayer = new MediaPlayer(textReaderMedia);
                textReaderMediaPlayer.play();
            }
            catch (IllegalAccessError e){
                System.out.println("The text reader is not working! Add \",javafx.media\" to the end of the VM arguments to fix this!");
            }

        }
    }


}
