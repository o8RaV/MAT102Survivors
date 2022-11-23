package boggle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import views.BoggleView;


/**
 * The application's main controller. Processes events from BoggleView.
 */
public class BoggleController {


    BoggleView boggleView; // the boggle view
    BoggleGame boggleGame; // the game's model.

    /**
     * constructor, starts the game application
     * @param boggleView reference to boggleView, the game's view
     * @param boggleGame reference to boggleGame, the game's model
     */
    public BoggleController (BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;

        this.boggleView.startGame();
    }


}
