package boggle;

import Memento.src.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import views.BoggleView;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * The application's main controller. Processes events from BoggleView.
 */
public class BoggleController {


    BoggleView boggleView; // the boggle view
    BoggleGame boggleGame; // the game's model.

    /**
     * constructor, starts the game application
     *
     * @param boggleView reference to boggleView, the game's view
     * @param boggleGame reference to boggleGame, the game's model
     */
    public BoggleController(BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;

        this.boggleView.setBoardRange
                (this.boggleGame.getMinBoardSize(), this.boggleGame.getMaxBoardSize());
        this.boggleView.startGame();
        addEventHandlers();
    }

    public void constructGame(String letters, int boardSize) {
        boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
        boggleGame.setLetters(letters);
        boggleView.setGameOn(true);
    }

    private void addEventHandlers() {
        boggleView.addBoardSHandler(new handleBoardSelect());
        boggleView.addCustomHandler(new handleCustomSelect());
        boggleView.addSubmitHandler(new handleSubmit());
        boggleView.addNewGameHandler(new handleNewGame());
        boggleView.addEndRoundHandler(new handleEndRound());
        boggleView.addLoadGameHandler(new handleLoadGame());
        boggleView.addSaveGameHandler(new handleSaveGame());
        boggleView.addsaveboardhandler(new HandleSaveBoard());
        boggleView.addchangeboardhandler(new HandleChangeBoard());
    }

    public class handleBoardSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.getBoardType().equals("custom")) {
                boggleView.displayScene(boggleView.customSMaker());
            } else {
                int boardSize = boggleView.getBoardSize();
                constructGame(boggleGame.randomizeLetters(boardSize), boardSize);
            }
            boggleView.changeTextReaderOption(boggleView.getTextReaderOption().equals("yes")); // handles whether the player wants to play with a text reader
        }
    }

    public class handleCustomSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            int boardSize = boggleView.getBoardSize();
            String inputtedLetters = boggleView.getCusLettersField();
            if (checkString(inputtedLetters, boardSize)) {
                constructGame(inputtedLetters, boardSize);
            } else {
                boggleView.sendCustomAlert();
            }
        }

        private boolean checkString(String input, int boardSize) {
            for (char c : input.toCharArray()) {
                if (!Character.isLetter(c)) {
                    return false;
                }
            }
            return (int) Math.pow(boardSize, 2) == input.length();
        }
    }

    // easier way to do using model? take out app logic...
    public class handleSubmit implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (boggleView.isGameOn()) {
                String word = boggleView.getGameInputDisplay();
                int newScore = boggleGame.humanMove(word);
                boggleView.updateScore(newScore);
                boggleView.resetBoard();
            }

        }
    }


    public class handleNewGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.boardSMaker());
            boggleGame.getGameStats().endRound();
            boggleView.clearValues();
        }
    }

    public class handleEndRound implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.isGameOn()) {
                boggleGame.computerMove();
                boggleView.displayRoundFacts(boggleGame.getGameStats().summarizeRound());
                boggleGame.getGameStats().endRound();
                boggleView.setGameOn(false);
                boggleView.resetBoard();
            }
        }
    }

    public class handleSaveGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.setGameOn(false);
            boggleView.displayScene(boggleView.SaveView());
        }
    }

    public class HandleSaveBoard implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(boggleView.getsaveFileNameTextField().endsWith(".bbg")
                    || !boggleView.getsaveFileNameTextField().contains("\\") || !boggleView.getsaveFileNameTextField().contains("/")
                    || !boggleView.getsaveFileNameTextField().contains(":") || !boggleView.getsaveFileNameTextField().contains("|")
                    || !boggleView.getsaveFileNameTextField().contains("*") || !boggleView.getsaveFileNameTextField().contains("?")
                    || !boggleView.getsaveFileNameTextField().contains("\"") || !boggleView.getsaveFileNameTextField().contains("<")
                    || !boggleView.getsaveFileNameTextField().contains(">")){
                Caretaker.save(boggleView.getsaveFileNameTextField(), boggleGame.getaMemento(boggleView.getsaveFileNameTextField()));
            }
            String name = boggleView.getsaveFileNameTextField();
            List loaded = null;
            try {
                Memento temp = Caretaker.get(name);
                loaded = boggleGame.getstatefrommemento(temp);
                boggleGame.changegamestats((BoggleStats) loaded.get(0));
                String letters = (String) loaded.get(1);
                int boardSize = (int) Math.sqrt(letters.length());
                boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boggleView.setGameOn(true);
        }
    }

    public class handleLoadGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.setGameOn(false);
            boggleView.displayScene(boggleView.LoadView());
        }
    }

    public class HandleChangeBoard implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            String name = (String) boggleView.boardsList.getSelectionModel().getSelectedItem();
            List loaded = null;
            try {
                Memento temp = Caretaker.get(name);
                loaded = boggleGame.getstatefrommemento(temp);
                boggleGame.changegamestats((BoggleStats) loaded.get(0));
                String letters = (String) loaded.get(1);
                int boardSize = (int) Math.sqrt(letters.length());
                boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boggleView.setGameOn(true);
        }
    }
}
