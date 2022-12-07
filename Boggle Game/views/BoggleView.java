package views;
import Command.*;

import boggle.Position;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  displays the main game window, where the user selects a boggle board and plays boggle.
 */
public class BoggleView {
    protected final int windowMinWidth = 900; // sets the window's minimum width and height
    protected final int windowMinHeight = 700;
    MediaPlayer mediaPlayer;
    TextField cusLettersField; // textfield that allows user to input custom set of letters
    public Label saveFileErrorLabel = new Label(""); // error label for saveview
    TextField saveFileNameTextField; // textfield that allows user to input name of they want o save the board by

    Button boardSCont = new Button("Continue"); // continue button in the board select scene

    Button saveBoardButton = new Button("Save This!"); // save this button
    Button selectBoardButton = new Button("Change board"); // change board button
    Button loadback = new Button("Back"); // back button for the second load button after the game
    public ListView boardsList = new ListView<>(); //list of boggle.boards
    Button cusCont = new Button("Continue"); // continue button in the custom letter input scene.
    private Stage primaryStage; // the main game window
    ToggleGroup boardSizeGroup; // toggle group housing the grid sizes toggles (radio buttons)
    ToggleGroup fontsizegroup; // toggle group housing the font size toggles (radio buttons)
    ToggleGroup boardTypeGroup; // toggle group housing the board type toggles

    ToggleGroup textReaderGroup; //toggle group housing whether the text reader is on or off

    ToggleGroup timerGroup; //toggle group housing whether the timer is on or off

    private int minBoardSize; // a boggle board's minimum and maximum sizes, set by BoggleGame
    private int maxBoardSize;

    private final int defButtonHeight = 40; // default values to keep uniform look in application
    private final int defButtonWidth = 90;
    private final int defaultPadding = 20;

    private Label gameInputDisplay = new Label(""); // label that displays the letters a player has selected

    private List<Button> selectedButtons = new ArrayList<>();  // list that contains the currently selected buttons
    private HashMap<Button, int[]> allButtons = new HashMap<>(); // list that contains all the buttons in a boggle board

    Button submitButton = new Button("Submit"); // button that allows user to submit a word

    Button saveButton = new Button("Save Game"); //button to save the game

    Button loadButton = new Button("Load Game"); // button to load an old game.
    
    Label scoreDisplay = new Label(); // displays the current score

    Label timerDisplay = new Label(); //timer display

    Button newGameButton = new Button("New Game"); // button that allows user to start a new game

    Button endRoundButton = new Button("End Round");

    ScrollPane roundFacts = new ScrollPane();

    boolean gameOn;

    public boolean textReaderEnabled;

    public boolean timerEnabled;
    protected String fontChoice = "Arial";
    protected Color boggleStillColor = Color.LIGHTGREY;
    protected Color highlightColor = Color.GOLD;
    protected Color inputRectColor = Color.LIGHTBLUE;
    protected Color boggleTextColor = Color.BLACK;

    protected Color boggleLetterColor = Color.BLACK;
    protected Color scoreColor = Color.LIGHTGREEN;

    protected Color scoreTextColor = Color.BLACK;

    /**
     * Constructor
     * @param stage reference to the main application Stage
     */
    public BoggleView(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("and his name is JOOOHHHN CENAAAA");

        Pane pane = new Pane();
        Scene scene = new Scene(pane, windowMinWidth, windowMinHeight);
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(windowMinWidth);
        primaryStage.setMinHeight(windowMinHeight);


    }

    /**
     * starts the game
     */
    public void startGame() {
        displayScene(viewTypeMaker());
    }
    /**
     * @param size the size of the boggle board
     * @param letters the string of letters to be used for the boggle board
     * @return the pane containing the play scene
     */
    public Pane playSMaker(int size, String letters){
        // fontsize assigner
        int fontsize = 20;
        if (getfontsizeoption() == "Small")
            fontsize = 15;
        else if (getFontSizeOption() == "Medium")
            fontsize = 20;
        else if (getFontSizeOption() == "Large")
            fontsize = 30;
        //set text reader option
        changeTextReaderOption(this.getTextReaderOption().equals("Yes"));
        //set the timer option
        changeTimerOption(!this.getTimerOption().equals("No Timer"));
        // construct menu bar at the top
        MenuBar menuBar = new MenuBar();
        Menu newGame = new Menu();
        Menu SaveGame = new Menu();
        Menu LoadGame = new Menu();

        saveButton.setFont(Font.font(fontChoice));
        newGameButton.setFont(Font.font(fontChoice));
        loadButton.setFont(Font.font(fontChoice));

        SaveGame.setGraphic(saveButton);
        newGame.setGraphic(newGameButton);
        LoadGame.setGraphic(loadButton);
        menuBar.getMenus().add(newGame);
        menuBar.getMenus().add(SaveGame);
        menuBar.getMenus().add(LoadGame);

        // forms the sidebar
        HBox playArea = mainDisplayCreator(size, letters);

        VBox rootPane = new VBox(menuBar, playArea);
        rootPane.setSpacing(10);
        return rootPane;
    }

    public HBox mainDisplayCreator(int size, String letters){
        int fontsize = 20;
        if (getfontsizeoption() == "Small")
            fontsize = 15;
        else if (getFontSizeOption() == "Medium")
            fontsize = 20;
        else if (getFontSizeOption() == "Large")
            fontsize = 30;
        VBox sidebar = initSidebar();

        // Constructs the label that displays user input
        // Rectangle serves as a background
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.widthProperty().bind(
                primaryStage.getScene().widthProperty().multiply(0.6).subtract(defaultPadding));
        rectangle.setFill(inputRectColor);
        gameInputDisplay.setFont(Font.font(fontChoice, FontWeight.BOLD, fontsize));
        gameInputDisplay.setTextFill(boggleTextColor);
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
        return playArea;
    }

    /**
     * helper function that initalizes a set number of boggle board buttons to a grid pane
     * @param buttonsPane gridpane that the buttons will be initialized to
     * @param size size of the boggle board
     * @param letters boggle board letters
     */
    private void initBoggleButtons(GridPane buttonsPane, int size, String letters)
    {
        changeTextReaderOption(getTextReaderOption().equals("Yes"));
        // fontsize assigner
        int fontsize = 20;
        if (getfontsizeoption() == "Small")
            fontsize = 20;
        else if (getFontSizeOption() == "Medium")
            fontsize = 25;
        else if (getFontSizeOption() == "Large")
            fontsize = 30;

        buttonsPane.setVgap(10);
        buttonsPane.setHgap(10);

        int index = 0;
        for (int x = 0; x< size; x++) {
            for (int y = 0; y<size; y++) {
                Button letterButton = new Button(Character.toString(letters.charAt(index)));
                letterButton.setFont(Font.font(fontChoice, FontWeight.BOLD, fontsize));
                letterButton.prefHeightProperty().bind(primaryStage.heightProperty());
                letterButton.setTextFill(boggleLetterColor);
                letterButton.prefWidthProperty().bind(primaryStage.widthProperty());
                letterButton.setMinWidth(20);
                letterButton.setMinHeight(25);
                letterButton.setBackground(Background.fill(boggleStillColor));
                allButtons.put(letterButton, new int[]{x, y});
                letterButton.setOnAction(e -> {
                    Button selectedButton = (Button) e.getSource();
                    // if not already selected, and if it's close enough as per Boggle rules:
                    if (!selectedButtons.contains(selectedButton) && checkProximity(selectedButton) && gameOn) {
                        addBoggleInput(selectedButton.getText().charAt(0));
                        selectedButton.setBackground(Background.fill(highlightColor));
                        selectedButtons.add(selectedButton);
                        // reads out board letter if text reader is enabled
                        String letter = Character.toString(selectedButton.getText().charAt(0));
                        TextReaderView.playAudio(letter, textReaderEnabled);
                    }

                });
                buttonsPane.add(letterButton, x, y);
                index++;
            }
        }
    }

    /**
     * Initializes the methods needed to begin the tutorial
     */
    private void tutorial_init(){
        new TutMain();
    }

    /**
     * Initializes the game sidebar which contains the game control buttons and score
     * @return a vbox that contains the game sidebar
     */
    private VBox initSidebar () {
        int fontsize = 20;
        if (getfontsizeoption() == "Small")
            fontsize = 18;
        else if (getFontSizeOption() == "Medium")
            fontsize = 23;
        else if (getFontSizeOption() == "Large")
            fontsize = 27;
        Pos elementAlign = Pos.CENTER_LEFT;

        // construct the score graphic; consists of the score, and its string title
        Label scoreTitle = new Label("Score:");
        scoreTitle.setFont(Font.font(fontChoice, FontWeight.BOLD, fontsize));
        scoreTitle.setAlignment(Pos.CENTER);
        scoreTitle.setPrefWidth(defButtonWidth);
        // construct the score graphic; consists of the score, and its string title
        scoreDisplay.setText("0");
        scoreDisplay.setTextFill(scoreTextColor);
        setDefaultSize(scoreDisplay);
        scoreDisplay.setFont(Font.font(fontChoice, 18));
        scoreDisplay.setAlignment(Pos.CENTER);
        scoreDisplay.setBackground(Background.fill(scoreColor));
        VBox scoreGraphic = new VBox(scoreTitle, scoreDisplay);
        scoreGraphic.setAlignment(elementAlign);
        scoreGraphic.setSpacing(10);

        //construct the timer (if timerEnabled is true)
        setDefaultSize(timerDisplay);
        timerDisplay.setFont(Font.font(fontsize));
        timerDisplay.setAlignment(Pos.CENTER);
        timerDisplay.setBackground(Background.fill(Color.YELLOW));
        VBox timerGraphic = new VBox(timerDisplay);
        timerGraphic.setAlignment(elementAlign);
        timerGraphic.setSpacing(10);

        // initialize/resize submit and backspace buttons
        Button backspace = new Button("Backspace");
        backspace.setOnAction(e -> {
            backspaceBoggle();
            if (gameOn) {
                TextReaderView.playAudio("backspace", textReaderEnabled);
            }
        });
        setDefaultSize(backspace);
        setDefaultSize(submitButton);
        setDefaultSize(endRoundButton);
        VBox vbox;
        if (timerEnabled) {
            vbox = new VBox(timerGraphic, scoreGraphic, submitButton, backspace, endRoundButton);
        }
        else {
            vbox = new VBox(scoreGraphic, submitButton, backspace, endRoundButton);
        }
        submitButton.setFont(Font.font(fontChoice));
        endRoundButton.setFont(Font.font(fontChoice));
        backspace.setFont(Font.font(fontChoice));
        timerGraphic.setFont(Font.font(fontChoice))
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setMinHeight(330);
        roundFacts.prefWidthProperty().bind(primaryStage.widthProperty());
        roundFacts.prefHeightProperty().bind(primaryStage.getScene().heightProperty());

        // forms the sidebar
        VBox sidebar = new VBox(vbox, roundFacts);
        sidebar.setSpacing(20);
        sidebar.setAlignment(Pos.TOP_CENTER);
        return sidebar;
    }

    /**
     *  constructs the scene that allows a player to select a font, and a color preset for the boggle board
     * @return a Pane to serve as the root of a scene
     */
    private Pane viewTypeMaker () {
        // There are two main selections (font and color). So there's some default values to easily adjust them.
        int exampleWidth = 180;
        int defaultSpacing = 20;
        int selectionWidth = 350;
        Pos defaultAlignment = Pos.CENTER;

        // root pane that will house all other nodes
        BorderPane mainHouse = new BorderPane();

        // Screen title
        Label titleLabel = makeTitleLabel("Select Your View Type", 36);
        titleLabel.setPrefHeight(90);
        titleLabel.setAlignment(Pos.BOTTOM_CENTER);
        mainHouse.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // color selection Pane
        HBox colorMainPane = new HBox();

        VBox colorSelections = new VBox();
        colorSelections.setSpacing(20);

        Label colorTitle = makeTitleLabel("Color Preset", 30);
        colorTitle.setAlignment(Pos.TOP_LEFT);
        colorTitle.setPrefHeight(40);

        ToggleGroup colorToggles = new ToggleGroup();
        VBox colorPresets = radioVboxMaker(
                new String[]{"Normal", "High Contrast", "Deuteranomaly (Color-blindness)"}, colorToggles);

        colorSelections.getChildren().addAll(colorTitle, colorPresets);

        Pane exampleButtons = generateExButtons(exampleWidth);
        colorMainPane.getChildren().addAll(colorSelections, exampleButtons);
        colorMainPane.setAlignment(defaultAlignment);
        colorMainPane.setSpacing(defaultSpacing);
        colorSelections.setPrefWidth(selectionWidth);

        // this allows for the seamless adjustment of color presets as a user selects a differnet radio button
        colorToggles.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton newPalette = (RadioButton) t1;
                String selectedChoice = newPalette.getText();

                if (selectedChoice.equalsIgnoreCase("Normal")) {
                    boggleStillColor = Color.LIGHTGREY;
                    highlightColor = Color.GOLD;
                    inputRectColor = Color.LIGHTBLUE;
                    boggleTextColor = Color.BLACK;
                    boggleLetterColor = Color.BLACK;
                    scoreColor = Color.LIGHTGREEN;
                    scoreTextColor = Color.BLACK;

                }
                else if (selectedChoice.equalsIgnoreCase("High Contrast")){
                    boggleStillColor = Color.BLACK;
                    highlightColor = Color.DARKRED;
                    inputRectColor = Color.DARKBLUE;
                    boggleTextColor = Color.WHITE;
                    boggleLetterColor = Color.WHITE;
                    scoreColor = Color.DARKGREEN;
                    scoreTextColor = Color.WHITE;
                }
                else if (selectedChoice.equalsIgnoreCase("Deuteranomaly (Color-blindness)")){
                    boggleStillColor = Color.BLUE;
                    highlightColor = Color.MAGENTA;
                    inputRectColor = Color.DARKORANGE;
                    boggleTextColor = Color.WHITE;
                    boggleLetterColor = Color.WHITE;
                    scoreColor = Color.DARKBLUE;
                    scoreTextColor = Color.WHITE;
                }

                // update the example buttons
                updateExButtons(exampleButtons);
            }
        });



        // font selection Pane
        HBox fontMainPane = new HBox();

        VBox fontSelections = new VBox();
        fontSelections.setSpacing(20);

        Label fontTitle = makeTitleLabel("Font Pick", 30);
        fontTitle.setAlignment(Pos.TOP_LEFT);
        fontTitle.setPrefHeight(40);

        ToggleGroup fontToggles = new ToggleGroup();
        VBox fontPresets = radioVboxMaker(new String[]{"Arial", "Times New Roman", "Comic Sans MS", "Impact"}, fontToggles);

        fontSelections.getChildren().addAll(fontTitle, fontPresets);
        fontSelections.setPrefWidth(selectionWidth);

        fontMainPane.getChildren().add(fontSelections);

        Label sampleText = new Label("Sample Text");
        sampleText.setAlignment(Pos.CENTER);
        sampleText.setPrefWidth(exampleWidth);
        sampleText.setFont(Font.font(fontChoice, 30));

        fontMainPane.getChildren().add(sampleText);
        fontMainPane.setAlignment(defaultAlignment);
        fontMainPane.setSpacing(defaultSpacing);

        // allows for the seamless change of the application's font as a user selects different radio buttons
        fontToggles.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                fontChoice = ((RadioButton) t1).getText();
                sampleText.setFont(Font.font(fontChoice, 30));
            }
        });

        // bottom continue button box
        Button nextButton = new Button("Continue");
        setDefaultSize(nextButton);
        nextButton.setOnAction(e -> {
            fontChoice = ((RadioButton) fontToggles.getSelectedToggle()).getText();
            displayScene(instrucSMaker());
        });
        HBox continueHBox = contHBoxMaker(nextButton);

        // Adding all selections to one box to control the spacing between the main different sections.
        VBox totalSelectionArea = new VBox(colorMainPane, fontMainPane);
        totalSelectionArea.setAlignment(Pos.CENTER);
        totalSelectionArea.setSpacing(defaultPadding*2);

        mainHouse.setCenter(totalSelectionArea);
        mainHouse.setBottom(continueHBox);

        BorderPane.setMargin(totalSelectionArea, new Insets(defaultPadding));
        BorderPane.setMargin(continueHBox, new Insets(0, defaultPadding, defaultPadding, defaultPadding));
        BorderPane.setMargin(titleLabel, new Insets(0, 0, defaultPadding*1.5 , 0));

        return mainHouse;
    }

    /**
     * updates the example boggle board to reflect the newly selected color preset
     * @param buttons a pane containing the example boggle board buttons
     */
    private void updateExButtons (Pane buttons) {
        for (Node n: buttons.getChildren()) {
            Button pickedButton = (Button) n;
            pickedButton.setBackground(Background.fill(boggleStillColor));
            pickedButton.setTextFill(boggleLetterColor);
        }
    }

    /**
     * generates the example boggle board. It's a 3x3 boggle board
     * @param paneSize sets the width (pixels) of the example boggle board
     * @return a pane containing the example boggle board
     */
    private Pane generateExButtons(int paneSize) {
        FlowPane flowPane = new FlowPane();
        double setWidth = paneSize;
        flowPane.setMaxWidth(setWidth);
        flowPane.setVgap(5);
        flowPane.setHgap(5);
        flowPane.setPrefWrapLength(setWidth);
        String sampleLetters = "THEQUICKB";
        for (int i = 0; i< 9; i++){
            Button sampleButton = new Button(Character.toString(sampleLetters.charAt(i)));
            sampleButton.setPrefWidth(setWidth/3 - flowPane.getHgap());
            sampleButton.prefHeightProperty().bind(sampleButton.widthProperty());
            sampleButton.setBackground(Background.fill(boggleStillColor));
            sampleButton.setFont(Font.font(fontChoice, FontWeight.BOLD, 16));

            // when a user hovers over a button, reveal the highlighted color of the button
            sampleButton.hoverProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if (t1) {
                        sampleButton.setBackground(Background.fill(highlightColor));
                    }
                    else{
                        sampleButton.setBackground(Background.fill(boggleStillColor));
                    }
                }
            });
            flowPane.getChildren().add(sampleButton);

        }
        return flowPane;

    }

    /**
     * generates a basic title label. Avoids repeated code
     * @param text the text the label will be set to have
     * @param fontSize the size of the text's font
     * @return the label containing the text
     */
    private Label makeTitleLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setFont(Font.font(fontChoice, FontWeight.BOLD, fontSize));
        return label;
    }

    /**
     * generates a vertical list of radio buttons
     * @param choices a string array representing the names of the radio buttons
     * @param toggleGroup the toggleGroup the radioButtons will be added to
     * @return a VBox containing the radio buttions
     */
    private VBox radioVboxMaker(String[] choices, ToggleGroup toggleGroup) {
        VBox selectVBox = new VBox();
        selectVBox.setSpacing(20);

        for (int i = 0; i<choices.length; i++) {
            RadioButton radioButton = new RadioButton(choices[i]);
            radioButton.setToggleGroup(toggleGroup);
            selectVBox.getChildren().add(radioButton);

            // ensures at least one toggle is always selected
            if (i == 0) {
                radioButton.setSelected(true);
            }
            radioButton.setFont(Font.font(fontChoice, 16));
        }

        return selectVBox;
    }



     /* Used to display words above the counter to describe the counter's purpose
     */
    public String countDisplay(){
        return "Score:";
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


                        +"Click continue when you're ready... or the tutorial button for a more-hands on guide on "
                        +"how to play");
        instructions.setLineSpacing(5);
        instructions.setFont(Font.font(fontChoice, FontWeight.BOLD, 20));

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(defaultPadding));
        // sets the instructions in the center of the scene
        pane.setCenter(instructions);

        Button tutAccess = new Button("Tutorial");
        tutAccess.setOnAction(e -> tutorial_init());
        //pane.setTop(tutHBoxMaker(tutAccess));

        Button instrucCont = new Button("Continue");
        instrucCont.setOnAction(e -> displayScene(boardSMaker()));
        pane.setBottom(startScreenBoxMaker(instrucCont, tutAccess));


        // lets the instructions text to wrap around the window when the window is resized
        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-4*defaultPadding));
        return pane;

    }

    /**
     * updates the timer with the correct amount of seconds remaining. Counts down by 1 second each time
     */
    public void setTimerTextCurrTime(String mins, String secs) {
        timerDisplay.setText(mins + ":" + secs);
    }

    /**
     * updates the timer with the correct amount of seconds remaining. Counts down by 1 second each time
     */
    public void setTimerTextCustomMessage(String message) {
        timerDisplay.setText(message);
    }

    /**
     * updates the timer with the correct amount of seconds remaining. Counts down by 1 second each time
     */
    public void changeTimerColour() {
        timerDisplay.setBackground(Background.fill(Color.INDIANRED));
    }

    /**
     * Constructs a pane with toggles that allows user to select the type of boggle board.
     * @return the root pane of the board select scene
     */
    public Pane boardSMaker() {
        // grid pane to house toggles (radio buttons) and their respective text
        VBox selectionPane = new VBox();

        Font textFont = Font.font(fontChoice, 16);
        Text gridSizeText = new Text(
                "Please select what boggle board you'd like to play on.");
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
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        // selection for font size
        Text fonttext = new Text(
                "Please select the font size you would like to play on.");
        fonttext.setFont(textFont);
        fontsizegroup = new ToggleGroup();
        String[] sizes = {"Small", "Medium", "Large"};
        HBox fontbox = radioHBoxMaker(sizes, fontsizegroup);

        VBox fontselection = new VBox(fonttext, fontbox);
        fontselection.setSpacing(15);
        selectionPane.getChildren().add(fontselection);
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        //selection for text reader
        Text textReaderText = new Text(
                "Do you want to play with a text reader?");
        textReaderText.setFont(textFont);

        textReaderGroup = new ToggleGroup();
        String[] options = {"No", "Yes"};
        HBox optionsBox = radioHBoxMaker(options, textReaderGroup);

        VBox optionSelection = new VBox(textReaderText, optionsBox);
        optionSelection.setSpacing(15);
        selectionPane.getChildren().add(optionSelection);
        selectionPane.setSpacing(defaultPadding*2);
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        //selection for timer
        Text timerText = new Text(
                "What kind of timer do you want to play with?");
        timerText.setFont(textFont);

        timerGroup = new ToggleGroup();
        String[] timerOptions = {"No Timer", "30 sec", "1 min", "2 min", "3 min"};
        HBox timerBox = radioHBoxMaker(timerOptions, timerGroup);

        VBox timerSelection = new VBox(timerText, timerBox);
        timerSelection.setSpacing(15);
        selectionPane.getChildren().add(timerSelection);
        selectionPane.setSpacing(50);
        selectionPane.setPadding(new Insets(40, 0, 0, 0));

        // root pane
        loadButton.setFont(Font.font(fontChoice));
        setDefaultSize(loadButton);
        selectionPane.getChildren().add(loadButton);
        BorderPane mainPane = new BorderPane();
        Label title = new Label("Board Selection");
        title.setFont(Font.font(fontChoice, FontWeight.BOLD, 30));
        title.prefWidthProperty().bind(primaryStage.widthProperty());
        title.setPrefHeight(30);
        title.setAlignment(Pos.BOTTOM_CENTER);
        mainPane.setPadding(new Insets(3*defaultPadding, defaultPadding, defaultPadding, defaultPadding));
        mainPane.setTop(title);
        mainPane.setCenter(selectionPane);
        mainPane.setBottom(contHBoxMaker(boardSCont));
        return mainPane;
    }

    /**
     * constructs the pane that allows players to input a custom boggle board
     * @return a root pane that serves to show the custom input screen
     */
    public Pane customSMaker () {
        // fontsize assigner
        int fontsize = 20;
        if (getfontsizeoption() == "Small")
            fontsize = 15;
        if (getFontSizeOption().equals("Medium"))
            fontsize = 20;
        else if (getFontSizeOption().equals("Large"))
            fontsize = 30;

        Label prompt = new Label("Please input the letters you would like to use on your Boggle Board.");
        prompt.setFont(Font.font(fontChoice, FontWeight.BOLD, fontsize));
        prompt.setPrefWidth(primaryStage.getWidth());
        prompt.setAlignment(Pos.BOTTOM_CENTER);
        cusLettersField = new TextField();
        cusLettersField.setFont(Font.font(fontChoice));
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

    /**
     * sends a pop up alert. alerts a player that an invalid string of letters has been inputted
     */
    public void sendCustomAlert () {
        Alert wrongInput = new Alert(Alert.AlertType.ERROR);
        wrongInput.setHeaderText("Illegal Input");
        wrongInput.setContentText("The letters you have inputted cannot be used to create a boggle board. \n" +
                "Please ensure they are all English alphabetic letters, and that you input " +
                (int) Math.pow(getBoardSize(), 2) + " letters in total.\n");
        wrongInput.setHeight(300);
        wrongInput.show();
    }

    /**
     * easter egg class
     * @param all_words
     */
    public void sendwordhack(HashMap<String, ArrayList<Position>> all_words) {
        Alert wrongInput = new Alert(Alert.AlertType.INFORMATION);
        wrongInput.setHeaderText("Hacker Page");
        wrongInput.setContentText(all_words.toString());
        wrongInput.setHeight(700);
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
            radioButton.setFont(Font.font(fontChoice, 14));
        }

        return selectHbox;
    }

    /**
     * constructs an HBox that centers the continue Button
     * @param continueButton the button that triggers the next scene to be displayed
     * @return an HBox housing continueButton
     */
    private HBox contHBoxMaker(Button continueButton) {
        continueButton.setFont(Font.font(fontChoice));
        setDefaultSize(continueButton);
        HBox bottomBox = new HBox();
        bottomBox.getChildren().add(continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(defaultPadding));
        return bottomBox;
    }

    /**
     *
     * @param continueButton continues to the next screen
     * @param tutButton starts the tutorial
     * @return an HBox containing theses 2 buttons
     */
    private HBox startScreenBoxMaker(Button continueButton, Button tutButton){
        continueButton.setFont(Font.font(fontChoice));
        tutButton.setFont(Font.font(fontChoice));
        setDefaultSize(continueButton);
        setDefaultSize(tutButton);
        HBox bottomBox = new HBox(continueButton, tutButton);
        bottomBox.setSpacing(20);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setPadding(new Insets(defaultPadding));
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
            selectedButtons.get(lastIndex).setBackground(Background.fill(boggleStillColor));
            selectedButtons.remove(lastIndex);
        }
    }

    public void resetBoard() {
        if (gameInputDisplay.getText().length() > 0) {
            gameInputDisplay.setText("");
            for (Button b: selectedButtons) {
                b.setBackground(Background.fill(boggleStillColor));
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
        textFacts.setFont(Font.font(fontChoice, 14));
        textFacts.wrappingWidthProperty().bind(roundFacts.widthProperty().subtract(20));
        roundFacts.setContent(textFacts);
        roundFacts.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     *the follow methods add the appropriate event handlers to buttons in the view.
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

    /**
     * class for load game button in loadview
     * @param handler
     */
    public void addLoadGameHandler (EventHandler<ActionEvent> handler) {
        loadButton.setOnAction(handler);
    }

    /**
     * class for save this!! button in saveview
     * @param handler
     */
    public void addSaveGameHandler (EventHandler<ActionEvent> handler) {
        saveButton.setOnAction(handler);
    }

    /**
     * class for save button in Saveview
     * @param handler
     */
    public void addsaveboardhandler (EventHandler<ActionEvent> handler) {
        saveBoardButton.setOnAction(handler);
    }

    /**
     * class for change board button in loadview
     * @param handler
     */
    public void addchangeboardhandler (EventHandler<ActionEvent> handler) {
        selectBoardButton.setOnAction(handler);
    }

    public void addEndRoundHandler (EventHandler<ActionEvent> handler) {
        endRoundButton.setOnAction(handler);
    }

    /**
     * class for back button in loadview
     * @param handler
     */
    public void addloadbackhandler (EventHandler<ActionEvent> handler) {
        loadback.setOnAction(handler);
    }

     /* sets the board size range to generate the appropriate number of toggle buttons
     * @param minSize the minimum size of a board
     * @param maxSize the maximum size of a board
     */
    public void setBoardRange (int minSize, int maxSize) {
        minBoardSize = minSize;
        maxBoardSize = maxSize;
    }

    /**
     * checks if a game is ongoing
     * @return a boolean that indicates whether a game is ongoing
     */
    public boolean isGameOn() {
        return gameOn;
    }

    /**
     * sets whether a game is ongoing.
     * used to ensure that when a round is ended,
     * a player can't input any more words.
     * @param gameOn whether the game has started or stopped.
     */
    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }


    /**
     * gets the type of board selected (custom vs random)
     * @return the selected radio button text
     */
    public String getBoardType () {
        if (boardTypeGroup != null) {
            return ((RadioButton) boardTypeGroup.getSelectedToggle()).getText();
        }
        return "Random";
    }

    /**
     * gets whether the player has selected to play with a text reader
     * @return the selected radio button text
     */
    public String getTextReaderOption() {
        if (textReaderGroup != null) {
            return ((RadioButton) textReaderGroup.getSelectedToggle()).getText();
        }
        return "Yes";
    }

    /**
     * gets whether the player has selected to play with a timer
     * @return the selected radio button text
     */
    public String getTimerOption() {
        if (timerGroup != null) {
            return ((RadioButton) timerGroup.getSelectedToggle()).getText();
        }
        return "No Timer";
    }

    /**
     * getter for fontsize
     * @return String representation of fontsize
     */
    public String getfontsizeoption(){
        if (fontsizegroup != null) {
            return ((RadioButton) fontsizegroup.getSelectedToggle()).getText();
        }
        return "Small";
    }

    /**
     * gets the selected font size
     * @return the size of the font selected
     */
    public String getFontSizeOption(){
        if (fontsizegroup != null) {
            return ((RadioButton) fontsizegroup.getSelectedToggle()).getText();
        }
        else {
            return "Medium";
        }
    }

    /**
     * changes whether a player wants to continue playing with a text reader
     * @param bool sets whether the text reader is active
     */
    public void changeTextReaderOption(boolean bool) {
        textReaderEnabled = bool;
    }

    /**
     * changes whether a player wants to continue playing with a text reader
     * @param bool sets whether the text reader is active
     */
    public void changeTimerOption(boolean bool) {
        timerEnabled = bool;
    }


    public void runTextReader(Character c) { //helper method for text reader testing
        TextReaderView.playAudio(Character.toString(c), textReaderEnabled);
    }

    /**
     * gets the selected size of the boggle board
     * @return the int size of the boggle board
     */
    public int getBoardSize () {
        return Integer.parseInt(String.valueOf(((RadioButton) boardSizeGroup.getSelectedToggle()).getText().charAt(0)));
    }

    /**
     * gets the custom letters inputted
     * @return the string inputted in the textfield
     */
    public String getCusLettersField() {
        return cusLettersField.getText();
    }

    /**

     * getter for Filename
     * @return String represntation for Filename.
     */
    public String getsaveFileNameTextField() {
        return saveFileNameTextField.getText();}

     /* gets the name of the saved file
     * @return a string of the file name
     */

    /**
     * gets the currently displayed word during the game
     * @return the string of the current word
     */
    public String getGameInputDisplay() {
        return gameInputDisplay.getText();
    }

    /**

     * Constructor of pane for LoadView
     * @return pane of loadview
     * sets up the load view. allows a player to load a saved board
     */
    public Pane LoadView(){
        boardsList.getItems().clear();
        int fontsize = 20;
        if (getFontSizeOption() == "Small")
            fontsize = 15;
        else if (getFontSizeOption() == "Large")
            fontsize = 30;
        Label selectBoardLabel = new Label(String.format("Currently playing: Default Board"));

        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setPadding(new Insets(defaultPadding));

        Button customBack = new Button("Back");
        customBack.setFont(Font.font(fontChoice));
        customBack.setOnAction(e -> displayScene(boardSMaker()));

        setDefaultSize(customBack); setDefaultSize(cusCont);
        setDefaultSize(loadback);

        if (isGameOn())
            bottomPanel.setLeft(loadback);
        else if (!isGameOn())
            bottomPanel.setLeft(customBack);


        selectBoardLabel.setId("CurrentBoard");

        boardsList.setId("BoardsList");
        boardsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // changes the font based on what was selected in the View Select Screen
        boardsList.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setText(s);
                if (!empty) {
                    setFont(Font.font(fontChoice, 18));
                }
            }
        });
        getFiles(boardsList); //get files for file selector


        VBox selectBoardBox = new VBox(20, selectBoardLabel, boardsList, selectBoardButton);
        selectBoardBox.setPadding(new Insets(defaultPadding, 0, 0, 0));
        BorderPane LoadPane = new BorderPane();
        boardsList.setPrefHeight(200);

        selectBoardLabel.setStyle("-fx-text-fill: #000000");
        selectBoardLabel.setFont(new Font(fontChoice, 30));

        selectBoardButton.setStyle("-fx-background-color: #ffeb00; -fx-text-fill: black;");
        selectBoardButton.setPrefSize(250, 50);
        selectBoardButton.setFont(new Font(fontChoice, fontsize));

        selectBoardBox.setAlignment(Pos.CENTER);
        // padding on top and bottom + prefHeight
        bottomPanel.setPrefHeight(defButtonHeight + bottomPanel.getPadding().getBottom()*2);
        LoadPane.setBottom(bottomPanel);
        LoadPane.setTop(selectBoardBox);
        LoadPane.setPadding(new Insets(defaultPadding));
        return LoadPane;
    }

    /**
     * Populate the listView with all the .SER files in the boards directory
     *
     * @param listView ListView to update
     */
    private void getFiles(ListView<String> listView) {
        File temp = new File("./saved/");
        String[] b = temp.list();
        for (String i : b) {
            if (i.regionMatches(true, i.length() - 4, ".bbg", 0, 4))
                listView.getItems().add(i);
        }
    }

    /**
     * Constructs the pane for SaveView
     * @return pane for saveview
     */
    public Pane SaveView(){
        int fontsize = 20;
        if (getFontSizeOption() == "Small")
            fontsize = 15;
        else if (getFontSizeOption() == "Large")
            fontsize = 30;
        Label saveBoardLabel = new Label("Enter name of file to save");
        saveFileErrorLabel = new Label("");
        saveFileNameTextField = new TextField(""); // adding the different buttons


        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");


        saveBoardLabel.setId("SaveBoard");
        saveFileNameTextField.setId("SaveFileNameTextField");

        saveBoardLabel.setId("SaveBoard"); // DO NOT MODIFY ID
        saveFileNameTextField.setId("SaveFileNameTextField");

        saveBoardLabel.setStyle("-fx-text-fill: #000000;");
        saveBoardLabel.setFont(new Font(fontChoice, fontsize));
        saveFileNameTextField.setStyle("-fx-text-fill: #000000;");
        saveFileNameTextField.setFont(new Font(fontChoice, fontsize));

        saveFileErrorLabel.setId("SaveFileErrorLabel");
        saveFileErrorLabel.setStyle("-fx-text-fill: #000000;");
        saveFileErrorLabel.setFont(new Font(fontChoice, fontsize));

        String boardName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".bbg";
        saveFileNameTextField.setText(boardName);

        saveBoardButton.setId("SaveBoard");
        saveBoardButton.setStyle("-fx-background-color: #ffeb00; -fx-text-fill: #000000;");
        saveBoardButton.setPrefSize(200, 50);
        saveBoardButton.setFont(new Font(fontChoice, fontsize));

        VBox saveBoardBox = new VBox(10, saveBoardLabel, saveFileNameTextField, saveBoardButton, saveFileErrorLabel);
        dialogVbox.getChildren().add(saveBoardBox);
        BorderPane SavePane = new BorderPane();
        SavePane.setTop(saveBoardBox);
        return SavePane;
    }

}
