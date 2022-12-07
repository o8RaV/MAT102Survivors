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
 * Starts and initializes methods for use with tutorial mode of the game
 */
public class TutMain{

    private final int windowMinWidth = 700; // sets the window's minimum width and height
    private final int windowMinHeight = 500;

    public TutMain(){
        // Sets up the Stage and Pane for use in the tutorial
        Stage tutStage = new Stage();
        tutStage.setTitle("Tutorial. It's dangerous to go alone");
        TutView view = new TutView(tutStage);
        view.startGame(0);
    }
}
