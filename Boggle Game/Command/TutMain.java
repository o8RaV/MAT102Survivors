package Command;

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


/**
 * TO READ
 * This class and its methods only exist because I was having trouble with setting everything up. May move everything
 * into view when I have time. Find the descriptions of the methods in Tutview
 */
public class TutMain{

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;
    private Stage tutStage;
    private TutGame game;
    private TutView view;
    private TutGame operations;

    private final int windowMinWidth = 900; // sets the window's minimum width and height
    private final int windowMinHeight = 700;
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
        tutStage.setFullScreen(true);
        this.view = new TutView(tutStage);
        view.startGame(0);
    }

    public void displayScene(Pane pane) {
        tutStage.getScene().setRoot(pane);
        if (!tutStage.isShowing()) {
            tutStage.show();
        }
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
}
