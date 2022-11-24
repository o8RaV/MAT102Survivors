package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 *  displays the main game window, where the user selects a boggle board and plays boggle.
 */
public class BoggleView {

    private final int windowMinWidth = 600; // sets the window's minimum width and height
    private final int windowMinHeight = 500;

    TextField inputLetters; // textfield that allows user to input custom set of letters
    Button boardSCont = new Button("Continue"); // continue button in the board select scene

    Button customCont = new Button("Continue"); // continue button in the custom letter input scene.
    private Stage primaryStage; // the main game window
     ToggleGroup gridSizeToggle; // toggle group housing the grid sizes toggles (radio buttons)

     ToggleGroup typeToggle; // toggle group housing the board type toggles

    private final int minBoardSize = 4; // a boggle board's minimum and maximum sizes.
    private final int maxBoardSize = 6;

    private final int defButtonHeight = 40;
    private final int defButtonWidth = 80;
    private final int defaultPadding = 20;

    private Label boggleLabel = new Label("");


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
     * starts the game
     */
    public void startGame() {
        displayScene(instrucSMaker());
    }


    /**
     * initializes the main UI with the boggleBoard
     */
    public Pane playScene (int size, String letters){
        MenuBar menuBar = new MenuBar();
        Menu newGame = new Menu();
        Button menuLabel = new Button("New game");

        newGame.setGraphic(menuLabel);
        menuLabel.setOnAction(e-> {
            displayScene(boardSMaker());
            boggleLabel.setText("");
        });
        menuLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        menuBar.getMenus().add(newGame);


        GridPane boggleButtons = new GridPane();
        boggleButtons.setVgap(5);
        boggleButtons.setHgap(5);
        int index = 0;
        for (int x = 0; x< size; x++) {
            for (int y = 0; y<size; y++) {
                Button letterButton = new Button(Character.toString(letters.charAt(index)));
                letterButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                letterButton.prefHeightProperty().bind(primaryStage.heightProperty());
                letterButton.prefWidthProperty().bind(primaryStage.widthProperty());
                letterButton.setMinWidth(20);
                letterButton.setMinHeight(25);
                letterButton.setOnAction(e -> addBoggleInput(((Button) e.getSource()).getText().charAt(0)));
                boggleButtons.add(letterButton, x, y);
                index++;
            }
        }

        Button backspace = new Button("Backspace");
        backspace.setPrefWidth(50);
        VBox sidebar = new VBox(backspace);
        backspace.setOnAction(e -> backspaceBoggle());
        sidebar.setAlignment(Pos.CENTER);

        double labelBoardWidth = primaryStage.getWidth()/1.5;
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.setWidth(labelBoardWidth);
        rectangle.setFill(Color.GOLD);

        boggleLabel.setFont(Font.font(20));
        HBox labelContainer = new HBox(boggleLabel);
        labelContainer.setAlignment(Pos.CENTER);
        StackPane rectLabel = new StackPane(rectangle, labelContainer);

        VBox labelBoard = new VBox(rectLabel, boggleButtons);
        labelBoard.setPrefWidth(labelBoardWidth);
        labelBoard.setSpacing(20);
        labelBoard.setPadding(new Insets(defaultPadding));

        HBox playArea = new HBox(labelBoard, sidebar);;
        playArea.setSpacing(20);

        VBox rootPane = new VBox(menuBar, playArea);
        rootPane.setSpacing(10);
        return rootPane;

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
        pane.setPadding(new Insets(defaultPadding));
        // sets the instructions in the center of the scene
        pane.setCenter(instructions);

        Button instrucCont = new Button("Continue");
        instrucCont.setOnAction(e -> displayScene(boardSMaker()));
        pane.setBottom(contHBoxMaker(instrucCont));

        // lets the instructions text to wrap around the window when the window is resized
        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-2*defaultPadding));
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
        mainPane.setPadding(new Insets(defaultPadding));
        mainPane.setCenter(selectionPane);
        mainPane.setBottom(contHBoxMaker(boardSCont));

        return mainPane;
    }

    public Pane customSMaker () {
        Text prompt = new Text("Please input the letters you would like to use on your Boggle Board.");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        HBox promptContainer = new HBox(prompt);
        promptContainer.setAlignment(Pos.CENTER);
        promptContainer.setPrefHeight(100);
        inputLetters = new TextField();
        inputLetters.setPrefHeight(100);
        inputLetters.setMaxWidth(400);
        inputLetters.setFont(Font.font(20));

        inputLetters.setPromptText("Type " + (int) Math.pow(getBoardSize(), 2) + " letters here...");

        Button customBack = new Button("Back");
        customBack.setOnAction(e -> displayScene(boardSMaker()));

        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setPadding(new Insets(defaultPadding));

        setDefaultSize(customBack); setDefaultSize(customCont);
        bottomPanel.setLeft(customBack);
        bottomPanel.setRight(customCont);

        BorderPane rootPane = new BorderPane();
        // padding on top and bottom + prefHeight
        bottomPanel.setPrefHeight(defButtonHeight + bottomPanel.getPadding().getBottom()*2);
        rootPane.setBottom(bottomPanel);
        rootPane.setCenter(inputLetters);
        promptContainer.setPrefHeight(defButtonHeight + promptContainer.getPadding().getBottom()*2);
        rootPane.setTop(promptContainer);
        prompt.setTextAlignment(TextAlignment.CENTER);

        return rootPane;
    }

    public void sendCustomAlert () {
        Alert wrongInput = new Alert(Alert.AlertType.ERROR);
        wrongInput.setHeaderText("Illegal Input");
        wrongInput.setContentText("The letters you have inputted cannot be used to create a boggle board. \n" +
                "Please ensure they are all english alphabetic letters, and that you input " +
                (int) Math.pow(getBoardSize(), 2) + " letters in total.\n");
        wrongInput.setHeight(300);
        wrongInput.show();
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
        setDefaultSize(continueButton);
        HBox bottomBox = new HBox();
        bottomBox.getChildren().add(continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        return bottomBox;
    }

    private void addBoggleInput(Character c) {
        if (boggleLabel.getText().length() < (int) Math.pow(getBoardSize(), 2)) {
            boggleLabel.setText(boggleLabel.getText() + c);
        }
    }

    private void backspaceBoggle() {
        if (boggleLabel.getText().length() > 0) {
            int length = boggleLabel.getText().length();
            boggleLabel.setText(boggleLabel.getText().substring(0, length-1));
        }
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

    private void setDefaultSize (Button button) {
        button.setPrefWidth(defButtonWidth);
        button.setPrefHeight(defButtonHeight);
    }

    /**
     * sets an event handler to the continue button in the custom letters scene
     * @param handler handles the action events from the button in the custom letters scene
     */
    public void addCustomHandler (EventHandler<ActionEvent> handler) {
        customCont.setOnAction(handler);
    }

    public void addBoardSHandler (EventHandler<ActionEvent> handler) {
        boardSCont.setOnAction(handler);
    }

    public String getBoardType () {
        return ((RadioButton) typeToggle.getSelectedToggle()).getText().toLowerCase();
    }

    public int getBoardSize () {
        return Integer.parseInt(String.valueOf(((RadioButton) gridSizeToggle.getSelectedToggle()).getText().charAt(0)));

    }

    public String getInputLetters() {
        return inputLetters.getText();
    }
}
