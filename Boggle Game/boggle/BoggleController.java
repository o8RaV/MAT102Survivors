package boggle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import views.BoggleView;


public class BoggleController {

    BoggleView boggleView;
    BoggleGame boggleGame;
    Stage primaryStage;

    public BoggleController (BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;
        this.boggleView.addInstructHandler(new InstructSelectHandler());
        this.boggleView.addTypeSelHandler(new BoardSelectHandler());
    }


    public class BoardSelectHandler implements EventHandler <ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println(((RadioButton) boggleView.getSizeToggle().getSelectedToggle()).getText());
            System.out.println(((RadioButton) boggleView.getTypeToggle().getSelectedToggle()).getText());
        }
    }

    public class InstructSelectHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.selectionScene());
        }
    }
    public void handleBoardSelect(ToggleGroup gridSelect, ToggleGroup typeSelect){

    }

}
