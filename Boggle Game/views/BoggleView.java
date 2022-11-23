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

/**
 *  displays the main game window, where the user selects a boggle board and plays boggle.
 */
public class BoggleView {

    private final int windowMinWidth = 600; // sets the window's minimum width and height
    private final int windowMinHeight = 400;
    Button instrucCont; // continue button in the instructions scene
    Button boardSCont; // continue button in the board select scene.
    private Stage primaryStage; // the main game window
     ToggleGroup gridSizeToggle; // toggle group housing the grid sizes toggles (radio buttons)

     ToggleGroup typeToggle; // toggle group housing the board type toggles

    private final int minBoardSize = 4; // a boggle board's minimum and maximum sizes.
    private final int maxBoardSize = 6;

    /**
     * Constructor
     * @param stage reference to the main application Stage
     */
    public BoggleView(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("and his name is JOOOHHHN CENAAAA");

        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(windowMinWidth);
        primaryStage.setMinHeight(windowMinHeight);
        primaryStage.setWidth(windowMinWidth);
        primaryStage.setHeight(windowMinHeight);

    }

    /**
     * initializes the main UI with the boggleBoard
     */
    private void playScene (int size, String letters){
        MenuBar menuBar = new MenuBar();
        Menu newGame = new Menu("New Game");
        menuBar.getMenus().add(newGame);


        GridPane pane = new GridPane();
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

    /**
     * constructs a pane with the boggle game's instructions
     * @return the root pane of the instructions scene
     */
    public Pane instrucSMaker() {
        Text instructions = new Text(
                "The Boggle board contains a grid of letters that are randomly placed. "
                        +"We're both going to try to find words in this grid by joining the letters. "
                        +"You can form a word by connecting adjoining letters on the grid. "
                        +"Two letters adjoin if they are next to each other horizontally, "
                        +"vertically, or diagonally. The words you find must be at least 4 letters long, "
                        +"and you can't use a letter twice in any single word. Your points "
                        +"will be based on word length: a 4-letter word is worth 1 point, 5-letter "
                        +"words earn 2 points, and so on. After you find as many words as you can, "
                        +"I will find all the remaining words.\n\n"


                        +"Click continue when you're ready...");
        instructions.setFont(Font.font("arial", FontWeight.BOLD, 16));

        BorderPane pane = new BorderPane();
        int padding = 20;
        pane.setPadding(new Insets(padding));
        // sets the instructions in the center of the scene
        pane.setCenter(instructions);

        instrucCont = new Button("Continue");
        pane.setBottom(contHBoxMaker(instrucCont));

        // lets the instructions text to wrap around the window when the window is resized
        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-2*padding));
        return pane;

    }

    /**
     * Constructs a pane with toggles that allows user to select the type of boggle board.
     * @return the root pane of the board select scene
     */
    public Pane boardSMaker() {
        // grid pane to house toggles (radio buttons) and their respective text
        GridPane selectionPane = new GridPane();
        selectionPane.setVgap(20);
        selectionPane.setHgap(10);

        Text gridSizeText = new Text(
                "Please select what size boggle board you'd like to play on.");
        selectionPane.add(gridSizeText, 0, 0);

        // groups the grid size toggles together
        gridSizeToggle = new ToggleGroup();

        int numOfGrids = maxBoardSize-minBoardSize+1;
        String[] gridSizes = new String[numOfGrids];
        // generates the grid size names, ex: 4x4 grid
        for (int i = 0; i<numOfGrids; i++ ){
            String currentGridSize = Integer.toString(i + minBoardSize);
            gridSizes[i] = currentGridSize + "x" + currentGridSize + " Grid";
        }

        HBox gridBox = radioHBoxMaker(gridSizes, gridSizeToggle);
        selectionPane.add(gridBox, 0, 1);


        Text typeText = new Text(
                "Please select if you'd like random letters, or if you'd like to select them yourself.");
        selectionPane.add(typeText, 0, 3);

        typeToggle = new ToggleGroup();
        String[] types = {"Random", "Custom"};
        HBox typesBox = radioHBoxMaker(types, typeToggle);
        selectionPane.add(typesBox, 0, 4);

        // root pane
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(20));
        mainPane.setCenter(selectionPane);
        boardSCont = new Button("Continue");
        mainPane.setBottom(contHBoxMaker(boardSCont));

        return mainPane;
    }

    /**
     * constructs an HBox that spaces out radio buttons evently
     * @param choices the labels of each radio button
     * @param toggleGroup  the group that the toggles will be added to
     * @return the HBox that houses the buttons
     */
    private HBox radioHBoxMaker(String[] choices, ToggleGroup toggleGroup) {
        HBox selectHbox = new HBox();
        selectHbox.setSpacing(20);

        for (int i = 0; i<choices.length; i++) {
            RadioButton radioButton = new RadioButton(choices[i]);
            radioButton.setToggleGroup(toggleGroup);
            selectHbox.getChildren().add(radioButton);

            // ensures at least one toggle is always selected
            if (i == 0) {
                radioButton.setSelected(true);
            }
        }

        return selectHbox;
    }

    /**
     * constructs an HBox that centers the continue Button
     * @param continueButton the button that triggers the next scene to be displayed
     * @return an HBox housing continueButton
     */
    private HBox contHBoxMaker(Button continueButton) {
        continueButton.setPrefSize(80, 40);
        HBox bottomBox = new HBox();
        bottomBox.getChildren().add(continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        return bottomBox;
    }

    /**
     * adds an event handler to the button in the instructions scene
     * @param handler handles the button's action event
     */
    public void addInstrucHandler(EventHandler<ActionEvent> handler) {
        instrucCont.setOnAction(handler);

    }

    /**
     *  adds an event handler to the button in the board selection scene
     * @param handler handles the button's action event
     */
    public void addBoardSHandler(EventHandler<ActionEvent> handler) {
        boardSCont.setOnAction(handler);

    }

    /**
     * sets the current scene by changing the root pane to the inputted Pane
     * @param pane the root pane of the next scene
     */
    public void displayScene(Pane pane) {
        primaryStage.getScene().setRoot(pane);
        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
    }

    /**
     * gridSizeToggle getter method
     * @return gridSize toggle group
     */
    public ToggleGroup getSizeToggle () {
        return gridSizeToggle;
    }

    /**
     * typeToggle getter method
     * @return the types toggle group
     */
    public ToggleGroup getTypeToggle() {
        return typeToggle;
    }

}
