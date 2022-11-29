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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  displays the main game window, where the user selects a boggle board and plays boggle.
 */
public class BoggleView {

    private final int windowMinWidth = 700; // sets the window's minimum width and height
    private final int windowMinHeight = 500;

    TextField cusLettersField; // textfield that allows user to input custom set of letters
    Button boardSCont = new Button("Continue"); // continue button in the board select scene

    Button cusCont = new Button("Continue"); // continue button in the custom letter input scene.
    private Stage primaryStage; // the main game window
    ToggleGroup boardSizeGroup; // toggle group housing the grid sizes toggles (radio buttons)

    ToggleGroup boardTypeGroup; // toggle group housing the board type toggles

    ToggleGroup textReaderGroup; //toggle group housing whether the text reader is on or off

    private int minBoardSize; // a boggle board's minimum and maximum sizes, set by BoggleGame
    private int maxBoardSize;

    private final int defButtonHeight = 40; // default values to keep uniform look in application
    private final int defButtonWidth = 80;
    private final int defaultPadding = 20;

    private Label gameInputDisplay = new Label(""); // label that displays the letters a player has selected

    private List<Button> selectedButtons = new ArrayList<>();  // list that contains the currently selected buttons
    private HashMap<Button, int[]> allButtons = new HashMap<>(); // list that contains all the buttons in a boggle board

    Button submitButton = new Button("Submit"); // button that allows user to submit a word
    
    Label scoreDisplay = new Label(); // displays the current score

    Button newGameButton = new Button("New Game"); // button that allows user to start a new game

    Button endRoundButton = new Button("End Round");

    ScrollPane roundFacts = new ScrollPane();

    boolean gameOn;

    boolean textReaderEnabled;

    MediaPlayer mediaPlayer;

    Media media;

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
     * @param size the size of the boggle board
     * @param letters the string of letters to be used for the boggle board
     * @return the pane containing the play scene
     */
    public Pane playSMaker(int size, String letters){
        // construct menu bar at the top
        MenuBar menuBar = new MenuBar();
        Menu newGame = new Menu();
        newGame.setGraphic(newGameButton);
        menuBar.getMenus().add(newGame);

        // forms the sidebar
        VBox sidebar = initSidebar();

        // Constructs the label that displays user input
        // Rectangle serves as a background
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.widthProperty().bind(
                primaryStage.getScene().widthProperty().multiply(0.6).subtract(defaultPadding));
        rectangle.setFill(Color.LIGHTBLUE);
        gameInputDisplay.setFont(Font.font("arial", FontWeight.BOLD, 20));
        gameInputDisplay.prefWidthProperty().bind(rectangle.widthProperty());
        gameInputDisplay.setAlignment(Pos.CENTER);
        StackPane inputGraphic = new StackPane(rectangle, gameInputDisplay);
        inputGraphic.setAlignment(Pos.CENTER_LEFT);

        // Constructs the boggle baord
        GridPane buttonsPane = new GridPane();
        buttonsPane.maxWidthProperty().bind(rectangle.widthProperty());
        initBoggleButtons(buttonsPane, size, letters);

        // forms the pane housing the input label and the boggle board
        VBox boggleBoard = new VBox(inputGraphic, buttonsPane);
        boggleBoard.setSpacing(20);

        // constructs the play area
        HBox playArea = new HBox(boggleBoard, sidebar);
        playArea.setSpacing(20);
        playArea.setPadding(new Insets(defaultPadding));

        VBox rootPane = new VBox(menuBar, playArea);
        rootPane.setSpacing(10);
        return rootPane;

    }

    /**
     * helper function that initalizes a set number of boggle board buttons to a grid pane
     * @param buttonsPane gridpane that the buttons will be initialized to
     * @param size size of the boggle board
     * @param letters boggle board letters
     */
    private void initBoggleButtons(GridPane buttonsPane, int size, String letters)
    {
        buttonsPane.setVgap(5);
        buttonsPane.setHgap(5);

        int index = 0;
        for (int x = 0; x< size; x++) {
            for (int y = 0; y<size; y++) {
                Button letterButton = new Button(Character.toString(letters.charAt(index)));
                letterButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                letterButton.prefHeightProperty().bind(primaryStage.heightProperty());
                letterButton.prefWidthProperty().bind(primaryStage.widthProperty());
                letterButton.setMinWidth(20);
                letterButton.setMinHeight(25);
                letterButton.setBackground(Background.fill(Color.LIGHTGREY));
                allButtons.put(letterButton, new int[]{x, y});
                letterButton.setOnAction(e -> {
                    Button selectedButton = (Button) e.getSource();
                    // if not already selected, and if it's close enough as per Boggle rules:
                    if (!selectedButtons.contains(selectedButton) && checkProximity(selectedButton) && gameOn) {
                        textReader(selectedButton.getText().charAt(0)); //plays the audio for that letter
                        addBoggleInput(selectedButton.getText().charAt(0));
                        selectedButton.setBackground(Background.fill(Color.GOLD));
                        selectedButtons.add(selectedButton);
                    }

                });
                buttonsPane.add(letterButton, x, y);
                index++;
            }
        }
    }

    /**
     * This method allows the program to read aloud the sound of the letter that corresponds to parameter c
     * (iff textReaderEnabled is set to true).
     * @param c The letter to be read by text to speech
     */
    private void textReader(Character c) {
        if (textReaderEnabled) {
            String file_name = "audiofiles\\" + Character.toUpperCase(c) + ".mp3";
            media = new Media(new File(file_name).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }

    private VBox initSidebar () {
        Pos elementAlign = Pos.CENTER_LEFT;
        // construct the score graphic; consists of the score, and its string title
        Label scoreTitle = new Label("Score:");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreTitle.setAlignment(Pos.CENTER);
        scoreTitle.setPrefWidth(defButtonWidth);
        scoreDisplay.setText("0");
        setDefaultSize(scoreDisplay);
        scoreDisplay.setFont(Font.font(18));
        scoreDisplay.setAlignment(Pos.CENTER);
        scoreDisplay.setBackground(Background.fill(Color.LIGHTGREEN));
        VBox scoreGraphic = new VBox(scoreTitle, scoreDisplay);
        scoreGraphic.setAlignment(elementAlign);
        scoreGraphic.setSpacing(10);

        // initialize/resize submit and backspace buttons
        Button backspace = new Button("Backspace");
        backspace.setOnAction(e -> backspaceBoggle());
        setDefaultSize(backspace);
        setDefaultSize(submitButton);
        setDefaultSize(endRoundButton);
        VBox vbox = new VBox(scoreGraphic, submitButton, backspace, endRoundButton);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setMinHeight(260);
        roundFacts.prefWidthProperty().bind(primaryStage.widthProperty());
        roundFacts.prefHeightProperty().bind(primaryStage.getScene().heightProperty());

        // forms the sidebar
        VBox sidebar = new VBox(vbox, roundFacts);
        sidebar.setSpacing(20);
        sidebar.setAlignment(Pos.TOP_CENTER);
        return sidebar;
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
        instructions.setLineSpacing(5);
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
        VBox selectionPane = new VBox();

        Font textFont = Font.font("Arial", 16);
        Text gridSizeText = new Text(
                "Please select what size boggle board you'd like to play on.");
        gridSizeText.setFont(textFont);

        // groups the grid size toggles together
        boardSizeGroup = new ToggleGroup();

        int numOfGrids = maxBoardSize-minBoardSize+1;
        String[] gridSizes = new String[numOfGrids];
        // generates the grid size names, ex: 4x4 grid
        for (int i = 0; i<numOfGrids; i++ ){
            String currentGridSize = Integer.toString(i + minBoardSize);
            gridSizes[i] = currentGridSize + "x" + currentGridSize + " Grid";
        }

        HBox gridBox = radioHBoxMaker(gridSizes, boardSizeGroup);
        VBox sizeSelection = new VBox(gridSizeText, gridBox);
        sizeSelection.setSpacing(15);
        selectionPane.getChildren().add(sizeSelection);


        Text typeText = new Text(
                "Please select if you'd like random letters, or if you'd like to select them yourself.");
        typeText.setFont(textFont);

        boardTypeGroup = new ToggleGroup();
        String[] types = {"Random", "Custom"};
        HBox typesBox = radioHBoxMaker(types, boardTypeGroup);

        VBox typeSelection = new VBox(typeText, typesBox);
        typeSelection.setSpacing(15);
        selectionPane.getChildren().add(typeSelection);
        selectionPane.setSpacing(50);
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        //selection for text reader
        Text textReaderText = new Text(
                "Do you want to play with a text reader?");
        textReaderText.setFont(textFont);

        textReaderGroup = new ToggleGroup();
        String[] options = {"Yes", "No"};
        HBox optionsBox = radioHBoxMaker(options, textReaderGroup);

        VBox optionSelection = new VBox(textReaderText, optionsBox);
        optionSelection.setSpacing(15);
        selectionPane.getChildren().add(optionSelection);
        selectionPane.setSpacing(50);
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        // root pane
        BorderPane mainPane = new BorderPane();
        Label title = new Label("Board Selection");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.prefWidthProperty().bind(primaryStage.widthProperty());
        title.setPrefHeight(50);
        title.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(defaultPadding));
        mainPane.setTop(title);
        mainPane.setCenter(selectionPane);
        mainPane.setBottom(contHBoxMaker(boardSCont));

        return mainPane;
    }

    public Pane customSMaker () {
        Label prompt = new Label("Please input the letters you would like to use on your Boggle Board.");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        prompt.setPrefWidth(primaryStage.getWidth());
        prompt.setAlignment(Pos.BOTTOM_CENTER);
        cusLettersField = new TextField();
        cusLettersField.setPrefHeight(100);
        cusLettersField.setMaxWidth(400);
        cusLettersField.setFont(Font.font(20));
        cusLettersField.setPromptText("Type " + (int) Math.pow(getBoardSize(), 2) + " letters here...");

        VBox titleAInput = new VBox(prompt, cusLettersField);
        titleAInput.setAlignment(Pos.BOTTOM_CENTER);
        titleAInput.setPrefHeight(primaryStage.getWidth()/3);
        titleAInput.setSpacing(30);

        Button customBack = new Button("Back");
        customBack.setOnAction(e -> displayScene(boardSMaker()));

        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setPadding(new Insets(defaultPadding));

        setDefaultSize(customBack); setDefaultSize(cusCont);
        bottomPanel.setLeft(customBack);
        bottomPanel.setRight(cusCont);

        BorderPane rootPane = new BorderPane();
        // padding on top and bottom + prefHeight
        bottomPanel.setPrefHeight(defButtonHeight + bottomPanel.getPadding().getBottom()*2);
        rootPane.setBottom(bottomPanel);
        rootPane.setTop(titleAInput);
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
            radioButton.setFont(Font.font("Arial", 14));
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
            gameInputDisplay.setText(gameInputDisplay.getText() + c);
    }

    public void backspaceBoggle() {
        if (gameInputDisplay.getText().length() > 0) {
            int length = gameInputDisplay.getText().length();
            gameInputDisplay.setText(gameInputDisplay.getText().substring(0, length-1));
            int lastIndex = selectedButtons.size()-1;
            selectedButtons.get(lastIndex).setBackground(Background.fill(Color.LIGHTGREY));
            selectedButtons.remove(lastIndex);
        }
    }

    public void resetBoard() {
        if (gameInputDisplay.getText().length() > 0) {
            gameInputDisplay.setText("");
            for (Button b: selectedButtons) {
                b.setBackground(Background.fill(Color.LIGHTGREY));
            }
            selectedButtons.clear();
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

    private boolean checkProximity (Button b) {
        if (selectedButtons.size() == 0) {
            return true;
        }
        else {
            Button compareButton = selectedButtons.get(selectedButtons.size()-1);
            int[] compareCoordinates = allButtons.get(compareButton);
            int[] bCoordinates = allButtons.get(b);

            return (Math.abs(compareCoordinates[0] - bCoordinates[0]) <=1)
                    && (Math.abs(compareCoordinates[1] - bCoordinates[1]) <=1);
        }
    }

    private void setDefaultSize (Control button) {
        button.setPrefWidth(defButtonWidth);
        button.setPrefHeight(defButtonHeight);
    }
    
    public void updateScore (int numScore) {scoreDisplay.setText(Integer.toString(numScore));}

    public void clearValues () {
        gameInputDisplay.setText("");
        selectedButtons.clear();
        allButtons.clear();
        scoreDisplay.setText("");

        if (roundFacts.getContent() != null) {
            ((Text) roundFacts.getContent()).setText("");
        }
    }

    public void displayRoundFacts (String facts){
        Text textFacts = new Text(facts);
        textFacts.wrappingWidthProperty().bind(roundFacts.widthProperty().subtract(20));
        roundFacts.setContent(textFacts);
        roundFacts.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     * sets an event handler to the continue button in the custom letters scene
     * @param handler handles the action events from the button in the custom letters scene
     */
    public void addCustomHandler (EventHandler<ActionEvent> handler) {
        cusCont.setOnAction(handler);
    }

    public void addBoardSHandler (EventHandler<ActionEvent> handler) {
        boardSCont.setOnAction(handler);
    }

    public void addSubmitHandler (EventHandler<ActionEvent> handler) {
        submitButton.setOnAction(handler);
    }

    public void addNewGameHandler (EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

    public void addEndRoundHandler (EventHandler<ActionEvent> handler) {
        endRoundButton.setOnAction(handler);
    }

    public void setBoardRange (int minSize, int maxSize) {
        minBoardSize = minSize;
        maxBoardSize = maxSize;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }


    public String getBoardType () {
        return ((RadioButton) boardTypeGroup.getSelectedToggle()).getText().toLowerCase();
    }

    public String getTextReaderOption() {
        return ((RadioButton) textReaderGroup.getSelectedToggle()).getText().toLowerCase();
    }

    public void changeTextReaderOption(boolean bool) {
        textReaderEnabled = bool;
    }

    public void runTextReader(Character c) { //helper method for text reader testing
        textReader(c);
    }

    public MediaPlayer getMediaPlayer() { //getter method for mediaPlayer
        return mediaPlayer;
    }

    public int getBoardSize () {
        return Integer.parseInt(String.valueOf(((RadioButton) boardSizeGroup.getSelectedToggle()).getText().charAt(0)));

    }

    public String getCusLettersField() {
        return cusLettersField.getText();
    }

    public String getGameInputDisplay() {
        return gameInputDisplay.getText();
    }

}
