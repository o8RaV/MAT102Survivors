import boggle.BoggleGame;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 * Team Members are: Rav, Chris, Justin, Ahmed.
 */
public class Main extends Application {
    /**
    * Main method. 
    * @param args command line arguments.
    **/
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BoggleGame b = new BoggleGame(stage);
        b.giveInstructions();
        b.playGame();
    }
}
