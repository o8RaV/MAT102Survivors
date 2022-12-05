package boggle;

import Memento.src.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import views.BoggleView;
import views.TextReaderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TransferQueue;


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
        addEventHandlers();
    }

    /**
     * Starts up the game (loads up the window)
     */
    public void start(){
        this.boggleView.startGame();
    }

    /**
     * constructs a new boggle game (new board with reset score)
     * @param letters the letters to be used in the boggle board
     * @param boardSize the size of the boggle board
     */
    public void constructGame(String letters, int boardSize) {
        boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
        boggleGame.setLetters(letters);
        boggleView.setGameOn(true);
        TextReaderView.playAudio("continue", boggleView.textReaderEnabled);
    }

    /**
     * adds all the event handlers to the buttons in the main application (excluding tutorial)
     * This implementation allows for more concise and easy-to-follow code. However, it takes a small toll on memory.
     */
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
        boggleView.addloadbackhandler(new handleloadback());
    }

    /**
     * inner class that handles events from the board selection screen (the radio buttons particularly)
     */
    public class handleBoardSelect implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.getBoardType().equals("custom")) {
                boggleView.displayScene(boggleView.customSMaker());
            } else {
                int boardSize = boggleView.getBoardSize();
                constructGame(boggleGame.randomizeLetters(boardSize), boardSize);
            }
        }
    }

    /**
     * inner class that handles events from the custom letter input screen
     */
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

        /**
         * Checks whether the inputted string is valid
         * @param input the inputted string
         * @param boardSize the selected board size
         * @return a boolean that determines the validity of the inputted string
         */
        private boolean checkString(String input, int boardSize) {
            for (char c : input.toCharArray()) {
                if (!Character.isLetter(c)) {
                    return false;
                }
            }
            return (int) Math.pow(boardSize, 2) == input.length();
        }
    }

    /**
     * handles the event where a user clicks the submit button
     */
    public class handleSubmit implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (boggleView.isGameOn()) {
                String word = boggleView.getGameInputDisplay();
                int newScore = boggleGame.humanMove(word);
                boggleView.updateScore(newScore);
                boggleView.resetBoard();
                TextReaderView.playAudio("submit", boggleView.textReaderEnabled);
            }
        }
    }

    /**
     * handler for loadback button in second usage of load button
     */
    public class handleloadback implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.playSMaker(boggleView.getBoardSize(), boggleGame.boggleboard));
            boggleView.setGameOn(true);
        }
    }


    /**
     * Handles the event where the user initializes a new game
     */
    public class handleNewGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.boardSMaker());
            boggleGame.getGameStats().endRound();
            boggleView.clearValues();
            TextReaderView.playAudio("newgame", boggleView.textReaderEnabled);
            boggleView.setGameOn(false);
        }
    }

    /**
     * handles the event where a user clicks the end round button
     */
    public class handleEndRound implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.isGameOn()) {
                boggleGame.computerMove();
                boggleView.displayRoundFacts(boggleGame.getGameStats().summarizeRound());
                boggleGame.getGameStats().endRound();
                boggleView.setGameOn(false);
                boggleView.resetBoard();
                TextReaderView.playAudio("endround", boggleView.textReaderEnabled);
            }
        }
    }

    /**
     * handler for save this button
     * handles the event where the user clicks save game
     */
    public class handleSaveGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.SaveView());
            TextReaderView.playAudio("save", boggleView.textReaderEnabled);
        }
    }

    /**
     * handler for save this button
     * handles the event where the player inputs a file name, and clicks save.
     */
    public class HandleSaveBoard implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.getSaveFileTF().endsWith(".bbg") && boggleView.getSaveFileTF() != null
            && !containsAnyOf(boggleView.getSaveFileTF(), "\\/:|*?\"<>")) {

                String name = boggleView.getsaveFileNameTextField();
                if (name.contains("PointHack") && name.length() > 13){
                    boggleGame.gameStats.setpScore(1000);
                    name = name.replace("PointHack", "");
                }
                if (name.contains("WordHack") && name.length() > 12){
                    name = name.replace("WordHack", "");
                    boggleView.sendwordhack(boggleGame.getAllWords());
                }
                Caretaker.save(name, boggleGame.getaMemento(name));
                List loaded = null;
                Memento temp = null;
                try {
                    temp = Caretaker.get(name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                loaded = boggleGame.getStateFromMemento(temp);
                boggleGame.changeGameStats((BoggleStats) loaded.get(0));
                boggleGame.setAllWords((HashMap<String, ArrayList<Position>>) loaded.get(2));
                String letters = (String) loaded.get(1);
                int boardSize = (int) Math.sqrt(letters.length());
                boggleGame.boggleboard = letters;
                boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
                boggleView.setGameOn(true);
                boggleView.updateScore(((BoggleStats) loaded.get(0)).getScore());
                boggleView.saveFileErrorLabel.setText("Saved board!!");
            }
            boggleView.saveFileErrorLabel.setText("The board should end with .bbg and should not contain illegal characters");
            TextReaderView.playAudio("saveboard", boggleView.textReaderEnabled);
        }

        /**
         * checks whether the desired save file name is legal
         * @param sourceString the desired save file name
         * @param charsToFind the characters to find in the file name (checks whether they're legal)
         * @return boolean of whether the sourceString is a legal file name
         */
        private boolean containsAnyOf(String sourceString, String charsToFind) {
            for (char c: charsToFind.toCharArray()) {
                if (sourceString.indexOf(c) != -1) {
                    return true;

                }
            }
            return false;
        }
    }

    /**
     * handler for load board button
     * handles the event where the user clicks load
     */
    public class handleLoadGame implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            boggleView.displayScene(boggleView.LoadView());
            boggleView.changeTextReaderOption(boggleView.getTextReaderOption().equals("yes"));
            TextReaderView.playAudio("load", boggleView.textReaderEnabled);
        }
    }

    /**
     * handler for change board button
     * handles the event where the user changes the type of the board they desire to play with.
     */
    public class HandleChangeBoard implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (boggleView.boardsList.getSelectionModel().getSelectedItem() != null) {
                String name = (String) boggleView.boardsList.getSelectionModel().getSelectedItem();
                List loaded = null;
                try {
                    Memento temp = Caretaker.get(name);
                    loaded = boggleGame.getStateFromMemento(temp);
                    boggleGame.changeGameStats((BoggleStats) loaded.get(0));
                    boggleGame.setAllWords((HashMap<String, ArrayList<Position>>) loaded.get(2));
                    String letters = (String) loaded.get(1);
                    int boardSize = (int) Math.sqrt(letters.length());
                    boggleGame.boggleboard = letters;
                    boggleView.displayScene(boggleView.playSMaker(boardSize, letters));
                    boggleView.updateScore(((BoggleStats) loaded.get(0)).getScore());
                    boggleView.displayRoundFacts("");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                boggleView.setGameOn(true);
                TextReaderView.playAudio("changeboard", boggleView.textReaderEnabled);
            }
        }
    }

}
