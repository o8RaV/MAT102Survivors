package Command;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import boggle.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import views.BoggleView;

public class TutMain{

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;
    private Stage tutStage;
    private TutGame game;
    private TutView view;
    private TutGame operations;

    private final int windowMinWidth = 700; // sets the window's minimum width and height
    private final int windowMinHeight = 500;
    private int minBoardSize; // a boggle board's minimum and maximum sizes, set by BoggleGame
    private int maxBoardSize;

    private final int defButtonHeight = 40; // default values to keep uniform look in application
    private final int defButtonWidth = 80;
    private final int defaultPadding = 20;

    private ArrayList<String> instructs = new ArrayList<String>();

    public TutMain(){
        this.game = new TutGame();
        this.scanner = new Scanner(System.in);
        tutStage = new Stage();
        tutStage.setTitle("Tutorial. It's dangerous to go alone");

        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        tutStage.setScene(scene);
        tutStage.setMinWidth(windowMinWidth);
        tutStage.setMinHeight(windowMinHeight);
        tutStage.setWidth(windowMinWidth);
        tutStage.setHeight(windowMinHeight);
        this.view = new TutView(tutStage);
        view.startGame(0);

        /*instructs.add("To get you started with the game, this tutorial aims to provide short and precise guidance and interactive opportunities that help you with understanding the game. When you are done reading this and any further instructions, press enter to proceed to the next one.\n");
        instructs.add("Boggle is a game played on a board of letters; your goal is to construct words using adjacent letters.\n");
        instructs.add("Let’s look at an example of this. In the example board, input link and risk. These are words that are formed with horizontally placed letters and vertical ones.\n");
        */
        //startGame();
    }

    public void displayScene(Pane pane) {
        tutStage.getScene().setRoot(pane);
        if (!tutStage.isShowing()) {
            tutStage.show();
        }
    }

    public void startGame() {
        File myObj = new File("Tutorial instructions.txt");
        try {
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNextLine()){
                instructs.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        displayScene(instrucSMaker(0));
    }

    public void screenUpdater(int i){
        if(i < 3){
            displayScene(instrucSMaker(i+1));
        }
        else if(i == 3){
            //Create board
        }
    }

    private void setDefaultSize (Control button) {
        button.setPrefWidth(defButtonWidth);
        button.setPrefHeight(defButtonHeight);
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

    public Pane instrucSMaker(int input){
        Text instructions = new Text(instructs.get(input));
        instructions.setLineSpacing(5);
        instructions.setFont(Font.font("arial", FontWeight.BOLD, 16));

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(defaultPadding));
        // sets the instructions in the center of the scene
        pane.setCenter(instructions);

        Button instrucCont = new Button("Continue");
        if(input == 2){
            ArrayList<String> words = new ArrayList<>();
            words.add("link");
            instrucCont.setOnAction(e -> makeBoard("grnllinkzseiukbm", 4, words));
            pane.setBottom(contHBoxMaker(instrucCont));
        }else{
            instrucCont.setOnAction(e -> screenUpdater(input));
            pane.setBottom(contHBoxMaker(instrucCont));
        }


        // lets the instructions text to wrap around the window when the window is resized
        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-2*defaultPadding));
        return pane;
    }

    public void makeBoard(String letters, int boardSize, ArrayList<String> a) {
        this.game.start(a);
        this.view = new TutView(tutStage);
        TutController test = new TutController(view, game);
        test.constructGame(letters, boardSize);
    }



    public void playGame() {

        ArrayList<String> boards = new ArrayList<>();
        boards.add("grnllinkzseiukbm");
        boards.add("misxaehkndgozvea");
        boards.add("jokehzqreouqhaak");

        //Print instructions for board 1
        ArrayList<String> need1 = new ArrayList<>();
        need1.add("link");
        playTutRound(4,boards.get(0), need1);

        //Print instructions for board 2
        ArrayList<String> need2 = new ArrayList<>();
        need2.add("mega");
        playTutRound(4,boards.get(1), need2);

        //Print instructions for board 3
        ArrayList<String> need3 = new ArrayList<>();
        need3.add("joker");
        playTutRound(4,boards.get(2), need3);
        //Print last instructions
    }

    public void playTutRound(int size, String letters, ArrayList<String> need) {
        BoggleGrid grid = new BoggleGrid(size);
        grid.initalizeBoard(letters);
        custom_human_move(grid, need);
    }

    private void custom_human_move(BoggleGrid board, ArrayList<String> a){
        TutBase tutorial_interface = new TutBase(a);
        TutOperator operator = new TutOperator();
        System.out.println("It's your turn to find some words!");
        while(true) {
            //You write code here!
            //step 1. Print the board for the user, so they can scan it for words
            //step 2. Get a input (a word) from the user via the console
            //step 3. Check to see if it is valid (note validity checks should be case-insensitive)
            //step 4. If it's valid, update the player's word list and score (stored in boggleStats)
            //step 5. Repeat step 1 - 4
            //step 6. End when the player hits return (with no word choice).

            System.out.println(board.toString());
            this.scanner = new Scanner(System.in);
            String toCheck = scanner.nextLine().toUpperCase();

            if (toCheck.equals("")) {
                operator.operateAll();
                if(tutorial_interface.all_found()){
                    break;
                }else{
                    System.out.println("There are still more words to be found");
                }
            }else{
                operator.acceptCommand(new InsertCommand(tutorial_interface,toCheck));
            }
        }
    }
}
