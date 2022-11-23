package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoggleView {

    Button instructContinue;
    Button typeSelCont;
    private Stage primaryStage;

     ToggleGroup gridSizeToggle;

     ToggleGroup typeToggle;

    private final int minBoardSize = 4;
    private final int maxBoardSize = 6;

    GridPane pane;
    int size;
    String letters;
    /**
     * Constructor
     * @param stage reference to the main application Stage
     */
    public BoggleView(Stage stage) {
        primaryStage = stage;
        instructContinue = new Button("Continue");
        typeSelCont = new Button("Continue");

    }

    /**
     * initializes the main UI with the boggleBoard
     */
    private void playScene (){
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

    }

    /*
     * Provide instructions to the user, so they know how to play the game.
     */
    public Pane instructionsScene() {
        Text instructions = new Text(
                "The Boggle board contains a grid of letters that are randomly placed. "
                        +"We're both going to try to find words in this grid by joining the letters. "
                        +"You can form a word by connecting adjoining letters on the grid. "
                        +"Two letters adjoin if they are next to each other horizontally, "
                        +"vertically, or diagonally. The words you find must be at least 4 letters long, "
                        +"and you can't use a letter twice in any single word. Your points "
                        +"will be based on word length: a 4-letter word is worth 1 point, 5-letter "
                        +"words earn 2 points, and so on. After you find as many words as you can, "
                        +"I will find all the remaining words.\n"


                        +"Hit return when you're ready...");

        instructions.setFont(Font.font("arial", FontWeight.BOLD, 20));
        BorderPane pane = new BorderPane();
        int pading = 20;
        pane.setPadding(new Insets(pading));
        pane.setCenter(instructions);
        pane.setBottom(contButtonBox(instructContinue));

        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-2*pading));
        return pane;

    }

    public Pane selectionScene () {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(20));

        GridPane selectionPane = new GridPane();
        mainPane.setCenter(selectionPane);
        selectionPane.setVgap(20);
        selectionPane.setHgap(10);

        Text gridSizeText = new Text("Please select what size boggle board you'd like to play on.");
        selectionPane.add(gridSizeText, 0, 0);

        gridSizeToggle = new ToggleGroup();
        int numOfGrids = maxBoardSize-minBoardSize+1;
        String[] gridSizes = new String[numOfGrids];
        for (int i = 0; i<numOfGrids; i++ ){
            String currentGridSize = Integer.toString(i + minBoardSize);
            gridSizes[i] = currentGridSize + "x" + currentGridSize + " Grid";
        }

        HBox gridBox = selectHBoxMaker(gridSizes, gridSizeToggle);
        selectionPane.add(gridBox, 0, 1);


        Text typeText = new Text("Please select if you'd like random letters, or if you'd like to select them yourself.");
        selectionPane.add(typeText, 0, 3);
        typeToggle = new ToggleGroup();
        String[] types = {"Random", "Custom"};
        HBox typesBox = selectHBoxMaker(types, typeToggle);
        selectionPane.add(typesBox, 0, 4);

        mainPane.setBottom(contButtonBox(typeSelCont));


        return mainPane;
    }

    private HBox selectHBoxMaker (String[] choices, ToggleGroup toggleGroup) {
        HBox selectHbox = new HBox();
        selectHbox.setSpacing(20);

        for (int i = 0; i<choices.length; i++) {
            RadioButton radioButton = new RadioButton(choices[i]);
            radioButton.setToggleGroup(toggleGroup);
            selectHbox.getChildren().add(radioButton);

            if (i == 0) {
                radioButton.setSelected(true);
            }
        }

        return selectHbox;
    }

    private HBox contButtonBox(Button continueButton) {
        continueButton.setPrefSize(80, 40);
        HBox bottomBox = new HBox();
        bottomBox.getChildren().add(continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        return bottomBox;
    }

    public void addInstructHandler(EventHandler<ActionEvent> handler) {
        instructContinue.setOnAction(handler);

    }

    public void addTypeSelHandler(EventHandler<ActionEvent> handler) {
        typeSelCont.setOnAction(handler);

    }

    public void displayScene(Pane pane) {
        primaryStage.getScene().setRoot(pane);
        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
    }

    public ToggleGroup getSizeToggle () {
        return gridSizeToggle;
    }

    public ToggleGroup getTypeToggle() {
        return typeToggle;
    }



}
