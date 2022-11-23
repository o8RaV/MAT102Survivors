import boggle.BoggleController;
import boggle.BoggleGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.BoggleView;

/**
 * The Main class for the Boggle Project in CSC207, Fall 2022
 * Team Members are: Rav, Chris, Justin, Ahmed.
 */

public class Main extends Application {

    /**
     * Main method. launches application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * starts the application
     * @param gameStage main window where user will play Boggle
     */
    @Override
    public void start(Stage gameStage) {
        BoggleView boggleView = new BoggleView(gameStage);
        BoggleGame boggleGame = new BoggleGame();
        BoggleController controller = new BoggleController(boggleView, boggleGame);
    }
}