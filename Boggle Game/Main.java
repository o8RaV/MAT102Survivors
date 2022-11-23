import boggle.BoggleController;
import boggle.BoggleGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.BoggleView;

/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 * Team Members are: Rav, Chris, Justin, Ahmed.
 */

public class Main extends Application {
    private final int windowMinWidth = 600;
    private final int windowMinHeight = 400;
    Stage primaryStage;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("and his name is JOOOHHHN CENAAAA");
        BoggleView boggleView = new BoggleView(stage);
        BoggleGame boggleGame = new BoggleGame();
        BoggleController controller = new BoggleController(boggleView, boggleGame);

        primaryStage = stage;
        Pane pane = new Pane();
        scene = new Scene(pane);
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(windowMinWidth);
        primaryStage.setMinHeight(windowMinHeight);
        primaryStage.setWidth(windowMinWidth);
        primaryStage.setHeight(windowMinHeight);

        boggleView.displayScene(boggleView.instructionsScene());
    }
}