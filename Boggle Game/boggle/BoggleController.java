package boggle;

import Memento.src.Caretaker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import views.BoggleView;

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
     * @param boggleView reference to boggleView, the game's view
     * @param boggleGame reference to boggleGame, the game's model
     */
    public BoggleController (BoggleView boggleView, BoggleGame boggleGame) {
        this.boggleView = boggleView;
        this.boggleGame = boggleGame;

        this.boggleView.setBoardRange
                (this.boggleGame.getMinBoardSize(), this.boggleGame.getMaxBoardSize());
        this.boggleView.startGame();
        addEventHandlers();
    }

    public void constructGame (String letters, int boardSize) {
        boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
        boggleGame.setLetters(letters);
        boggleView.setGameOn(true);
    }

    private void addEventHandlers () {
        boggleView.addBoardSHandler(new handleBoardSelect());
        boggleView.addCustomHandler(new handleCustomSelect());
        boggleView.addSubmitHandler(new handleSubmit());
        boggleView.addNewGameHandler(new handleNewGame());
        boggleView.addEndRoundHandler(new handleEndRound());
        boggleView.addLoadGameHandler(new handleLoadGame());
        boggleView.addSaveGameHandler(new handleSaveGame());
    }

    public class handleBoardSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.getBoardType().equals("custom")) {
                boggleView.displayScene(boggleView.customSMaker());
            }
            else {
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
            if (checkString(inputtedLetters, boardSize)){
                constructGame(inputtedLetters, boardSize);
            }
            else {
                boggleView.sendCustomAlert();
            }
        }

        private boolean checkString (String input, int boardSize) {
            for (char c: input.toCharArray()) {
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
            int boardSize = boggleView.getBoardSize();
            boggleView.displayScene(boggleView.SaveView());
            if (boggleView.savename != null){
                Caretaker.save(boggleView.savename, boggleGame.getaMemento(boggleView.savename));
            }
        }
    }
    public class handleLoadGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            int boardSize = boggleView.getBoardSize();
            boggleView.displayScene(boggleView.LoadView());
            if (boggleView.loadname != null){
                try {
                    List loaded = Caretaker.get(boggleView.loadname).getState();
                    boggleGame.gameStats = (BoggleStats) loaded.get(1);
                    boggleView.displayScene(boggleView.playSMaker(boardSize, (String) loaded.get(0)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    }


