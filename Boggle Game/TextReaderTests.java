import java.util.*;
import boggle.BoggleController;
import boggle.BoggleGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.BoggleView;

public class TextReaderTests extends Application {

    BoggleView view;

    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    int r = new Random().nextInt(alphabet.length); // get a random index from alphabet

    Character c = Arrays.asList(alphabet).get(r).charAt(0);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage gameStage) {
        BoggleView boggleView = new BoggleView(gameStage);
        BoggleGame boggleGame = new BoggleGame();
        new BoggleController(boggleView, boggleGame);
        this.view = boggleView;
        System.out.println(textReader_disabled());
    }


    // test to see if disabling the text reader implies that the MediaPlayer object is null
    private String textReader_disabled() {
        view.changeTextReaderOption(false);
        view.runTextReader(c);
        if (view.getMediaPlayer() != null) {
            return "The test textReader_disabled failed its run. Since the text reader should be disabled, the mediaPlayer attribute in the BoggleView should be null.";
        }
        else {
            return "The test textReader_disabled passed its run! You're good to go!";
        }
    }
}
