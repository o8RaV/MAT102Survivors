package boggle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import views.BoggleView;


public class BoggleController {

    BoggleView boggleView;
    BoggleGame boggleGame;

    public BoggleController (BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;
    }

    public void startApp () {
        boggleView.displayScene(boggleView.instrucSMaker());
        this.boggleView.addInstrucHandler(new InstrucSHandler());
    }


    public class BoardSHandler implements EventHandler <ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println(((RadioButton) boggleView.getSizeToggle().getSelectedToggle()).getText());
            System.out.println(((RadioButton) boggleView.getTypeToggle().getSelectedToggle()).getText());
        }
    }

    public class InstrucSHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.boardSMaker());
            boggleView.addBoardSHandler(new BoardSHandler());
        }
    }
    public void handleBoardSelect(ToggleGroup gridSelect, ToggleGroup typeSelect){

    }

}
