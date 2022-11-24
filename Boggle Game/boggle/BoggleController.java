package boggle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import views.BoggleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

        this.boggleView.setBoardRange(this.boggleGame.getMinBoardSize(), this.boggleGame.getMaxBoardSize());
        this.boggleView.startGame();
        addEventHandlers();
    }

    private void addEventHandlers () {
        boggleView.addBoardSHandler(new handleBoardSelect());
        boggleView.addCustomHandler(new handleCustomSelect());
        boggleView.addSubmitHandler(new handleSubmit());
        boggleView.addNewGameHandler(new handleNewGame());
    }

    public class handleBoardSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.getBoardType().equals("custom")) {
                boggleView.displayScene(boggleView.customSMaker());
            }
            else {
                int boardSize = boggleView.getBoardSize();
                String letters = boggleGame.randomizeLetters(boardSize);
                boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
            }
        }
    }



    public class handleCustomSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            int boardSize = boggleView.getBoardSize();
            String inputtedLetters = boggleView.getCusLettersField();
            if (checkString(inputtedLetters, boardSize)){
                boggleView.displayScene(boggleView.playSMaker(boardSize, inputtedLetters));
                boggleGame.setLetters(inputtedLetters);
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

    public class handleSubmit implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Map <String, ArrayList<Position>> allWords = new HashMap<>();
            BoggleGrid grid = new BoggleGrid(boggleView.getBoardSize());
            grid.initalizeBoard(boggleGame.getLetters());
            boggleGame.findAllWords(allWords, boggleGame.dict, grid);

            String word = boggleView.getGameInputDisplay();
            if (boggleGame.dict.containsWord(word)){
                boggleGame.getGameStats().addWord(word, BoggleStats.Player.Human);
                boggleView.updateScore(boggleGame.getGameStats().getScore());
            }

            boggleView.clearBoggle();
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

    }


