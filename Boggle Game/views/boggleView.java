package views;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class boggleView {

    GridPane pane;
    Stage primaryStage;
    int size;
    String letters;
    /**
     * Constructor
     * @param primaryStage reference to the main application Stage
     */
    public boggleView (Stage primaryStage, int size, String letters) {
        this.primaryStage = primaryStage;
        this.size = size;
        this.letters = letters;
        initUi();
    }

    /**
     * initializes the main UI with the boggleBoard
     */
    private void initUi (){
        MenuBar menuBar = new MenuBar();
        Menu newGame = new Menu("New Game");
        menuBar.getMenus().add(newGame);

        pane = new GridPane();
        int index = 0;
        for (int x = 0; x< size; x++) {
            for (int y = 0; y<size; y++) {
                Button letterButton = new Button(Character.toString(letters.charAt(index)));
                letterButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                letterButton.prefHeightProperty().bind(primaryStage.heightProperty());
                letterButton.prefWidthProperty().bind(primaryStage.widthProperty());
                letterButton.setMinWidth(15);
                letterButton.setMinHeight(20);
                pane.getChildren().add(letterButton);
                index++;
            }
        }
        VBox playArea = new VBox(menuBar, pane);
        Scene scene = new Scene(playArea, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();




    }


}
