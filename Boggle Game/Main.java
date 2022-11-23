import boggle.BoggleController;
import boggle.BoggleGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.BoggleView;

/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 * Team Members are: Rav, Chris, Justin, Ahmed.
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BoggleView boggleView = new BoggleView(stage);
        BoggleGame boggleGame = new BoggleGame();
        BoggleController controller = new BoggleController(boggleView, boggleGame);

        controller.startApp();
    }
}