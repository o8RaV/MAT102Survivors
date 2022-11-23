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

    /**
     * reference to the boggle view
     */
    BoggleView boggleView;
    /**
     * reference to boggle game, the application's model.
     */
    BoggleGame boggleGame;

    /**
     * constructor
     * @param boggleView
     * @param boggleGame
     */
    public BoggleController (BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;
    }

    /**
     * starts the game; displays instructions
     */
    public void startGame() {
        boggleView.displayScene(boggleView.instrucSMaker());
        this.boggleView.addInstrucHandler(new InstrucSHandler());
    }

    /**
     * Handles action events from the "Continue" button in the board select scene.
     */
    public class BoardSHandler implements EventHandler <ActionEvent> {
        /**
         * implements handle method
         * @param actionEvent button action event
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println(((RadioButton) boggleView.getSizeToggle().getSelectedToggle()).getText());
            System.out.println(((RadioButton) boggleView.getTypeToggle().getSelectedToggle()).getText());
        }
    }

    /**
     * Handles action events from the "Continue" button in the instructions scene.
     */
    public class InstrucSHandler implements EventHandler<ActionEvent> {
        /**
         * implements handle method
         * @param actionEvent button action event
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.boardSMaker());
            boggleView.addBoardSHandler(new BoardSHandler());
        }
    }

}
