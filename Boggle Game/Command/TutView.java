package Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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



public class TutView extends BoggleView{

    private final int windowMinWidth = 900; // sets the window's minimum width and height
    private final int windowMinHeight = 700;

    private final int defButtonHeight = 40; // default values to keep uniform look in application
    private final int defButtonWidth = 80;
    private final int defaultPadding = 20;

    private ArrayList<String> instructs = new ArrayList<String>();

    private Stage primaryStage; // the main game window
    private Stage tutStage;
    private TutGame game;
    private int place;

    /**
     * Constructor
     *
     * @param stage reference to the main application Stage
     */
    public TutView(Stage stage) {
        super(stage);
        this.tutStage = stage;
        tutStage.setTitle("Tutorial. It's dangerous to go alone");
        this.game = new TutGame();
        this.place = 0;

        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        tutStage.setScene(scene);
        tutStage.setMinWidth(windowMinWidth);
        tutStage.setMinHeight(windowMinHeight);
        tutStage.setWidth(windowMinWidth);
        tutStage.setHeight(windowMinHeight);
        tutStage.setFullScreen(true);
    }

    public void displayScene(Pane pane) {
        tutStage.getScene().setRoot(pane);
        if (!tutStage.isShowing()) {
            tutStage.show();
        }
    }

    /**
     * Initializes the tutorial by getting the instructions from a text file and inserting them into an array list to
     * be used later. The uses parameter where to move user to the location of the instructions.
     * @param where place in instructions
     */
    public void startGame(int where) {
        File myObj = new File("Tutorial instructions.txt");
        try {
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNextLine()){
                instructs.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.place = where;
        displayScene(instrucSMaker(where));
    }

    public void screenUpdater(int i){
        displayScene(instrucSMaker(i+1));
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

    /**
     * Displays the instructions on screen or decides when to create a tutorial board
     * @param input an integer to keep track of which instructions to print
     * @return a Pane for displaying the screen
     */
    public Pane instrucSMaker(int input){
        Text instructions = new Text(instructs.get(input));
        instructions.setLineSpacing(5);
        instructions.setFont(Font.font("arial", FontWeight.BOLD, 16));

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(defaultPadding));
        // sets the instructions in the center of the scene
        pane.setCenter(instructions);

        Button instrucCont = new Button("Continue");

        // Specific input values are checked to know where the user is in the tutorial
        if(input == 2){
            // Reached the first board with vertical and horizontal example
            ArrayList<String> words = new ArrayList<>();
            words.add("link");
            words.add("risk");
            instrucCont.setOnAction(e -> makeBoard("grnllinkzseiukbm", 4, words));
            pane.setBottom(contHBoxMaker(instrucCont));
        }else if(input == 4){
            // Reached the diagonal example
            ArrayList<String> words = new ArrayList<>();
            words.add("mega");
            instrucCont.setOnAction(e -> makeBoard("misxaehkndgozvea", 4, words));
            pane.setBottom(contHBoxMaker(instrucCont));
        }else if(input == 6){
            // Reached the mix of vertical and horizontal example
            ArrayList<String> words = new ArrayList<>();
            words.add("joker");
            instrucCont.setOnAction(e -> makeBoard("jokehzqreouqhaak", 4, words));
            pane.setBottom(contHBoxMaker(instrucCont));
        }
        else if(input != 12){
            // These are the cases when the text  is displayed and a continue button is there to move to next step
            this.place++;
            instrucCont.setOnAction(e -> screenUpdater(input));
            pane.setBottom(contHBoxMaker(instrucCont));
        }
        // lets the instructions text to wrap around the window when the window is resized
        instructions.wrappingWidthProperty().bind(pane.widthProperty().add(-2*defaultPadding));
        return pane;
    }

    /**
     * Used to create a new board using the overriden classes for use with the Tutorial
     * @param letters The letters to make the board
     * @param boardSize dimension of the board
     * @param a An array list of the Strings that need to be input before the user is done with the board
     */
    public void makeBoard(String letters, int boardSize, ArrayList<String> a) {
        // Starts a new game with the
        this.game.start(a);
        TutView b = new TutView(tutStage);
        b.place = this.place;
        // Need to create a TutController for the overridden methods
        TutController test = new TutController(b, game);
        test.constructGame(letters, boardSize);
    }

    /**
     * Way to keep track of place in tutorial to be used outside of class
     * @return place in tutorial
     */
    public int getPlace(){
        return this.place;
    }
}
