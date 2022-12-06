package boggle;

import java.util.*;
import Memento.src.*;

/**
 * The BoggleGame class for the first Assignment in CSC207, Fall 2022
 */
public class BoggleGame {

    Dictionary dict; // all possible words

    private final int minBoardSize = 4; // a boggle board's minimum and maximum sizes.

    private final int maxBoardSize = 6;

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;

    /**
     * stores game statistics
     */
    public BoggleStats gameStats;

    String boggleboard; //stores the current letters for teh game

    private HashMap<String, ArrayList<Position>> allWords; // all_words for the game

    /**
     * dice used to randomize letter assignments for a small grid (4x4)
     */
    private final String[] dice_small_grid = //dice specifications, for small and large grids
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"};
    /**
     * dice used to randomize letter assignments for a big grid (5x5)
     */
    private final String[] dice_big_grid =
            {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
                    "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
                    "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};
    /**
     * dice used to randomize letter for a massive grid (6x6)
     */
    private final String[] dice_massive_grid = {"AAAFRS", "AAEEEE", "AAEEOO", "AAFIRS", "ABDEIO", "ADENNN",
            "AEEEEM", "AEEGMU", "AEGMNN", "AEILMN", "AEINOU", "AFIRSY", "AEHIQT", "BBJKXZ", "CCENST",
            "CDDLNN", "CEIITT", "CEIPST", "CFGNUY", "DDHNOT", "DHHLOR", "DHHNOW", "DHLNOR", "EHILRS",
            "EIILST", "EILPST", "EIOAUA", "EMTTTO", "ENSSSU", "GORRVW", "HIRSTV", "HOPRST", "IPRSYY",
            "JKQWXZ", "NOOTUW","OOOTTU"};

    /*
     * BoggleGame constructor
     */
    public BoggleGame() {
        this.scanner = new Scanner(System.in);
        this.gameStats = new BoggleStats();
        this.dict = new Dictionary("wordlist.txt");
        this.allWords = new HashMap<>();
    }
    public void setLetters(String letters) {
        BoggleGrid grid  = new BoggleGrid((int) Math.sqrt(letters.length()));
        grid.initalizeBoard(letters);
        this.allWords.clear();
        findAllWords(dict, grid);
    }

    /*
     * This method should return a String of letters (length 16 or 25 depending on the size of the grid).
     * There will be one letter per grid position, and they will be organized left to right,
     * top to bottom. A strategy to make this string of letters is as follows:
     * -- Assign a one of the dice to each grid position (i.e. dice_big_grid or dice_small_grid)
     * -- "Shuffle" the positions of the dice to randomize the grid positions they are assigned to
     * -- Randomly select one of the letters on the given die at each grid position to determine
     *    the letter at the given position
     *
     * @return String a String of random letters (length 16 or 25 depending on the size of the grid)
     */
    public String randomizeLetters(int size) {
        ArrayList<Character> random = new ArrayList<Character>();
        if (size == 4) {
            for (String i : dice_small_grid) {
                int n = (int) (Math.random() * 6 + 1) - 1;
                random.add(i.charAt(n));
            }
        } else if (size == 5) {
            for (String j : dice_big_grid) {
                int n = (int) (Math.random() * 6 + 1) - 1;
                random.add(j.charAt(n));
            }
        } else if (size == 6) {
            for (String j : dice_massive_grid) {
                int n = (int) (Math.random() * 6 + 1) - 1;
                random.add(j.charAt(n));
            }
        }
        Collections.shuffle(random);
        String ans = "";
        for (char i: random) {
            ans += i;
        }
        boggleboard = ans;
        return ans;
    }

    /*
     * This should be a recursive function that finds all valid words on the boggle board.
     * Every word should be valid (i.e. in the boggleDict) and of length 4 or more.
     * Words that are found should be entered into the allWords HashMap.  This HashMap
     * will be consulted as we play the game.
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     */
    public void findAllWords(Dictionary boggleDict, BoggleGrid boggleGrid) {
        for (int i = 0; i <= boggleGrid.numCols(); i++)
            for (int j = 0; j <= boggleGrid.numCols(); j++)
                findWordsUtil(boggleDict, boggleGrid, new ArrayList<Position>(), new Position(i, j), "");
    }

    /**
     * Helper of findAllWords
     * @param boggleDict
     * @param boggleGrid
     * @param visited
     * @param pos
     * @param prefix
     */
    private void findWordsUtil(Dictionary boggleDict, BoggleGrid boggleGrid, ArrayList<Position> visited, Position pos, String prefix) {
        int i = pos.getRow(); int j = pos.getCol();
        int length = boggleGrid.numCols();
        if(i >= length || i <0 || j < 0 || j >= length){
            return;
        }
        if (!boggleDict.isPrefix(prefix)) {
            return;
        }
        for (Position x : visited)
            if(x.getCol() == pos.getCol() && x.getRow() == pos.getRow()){
                return;
        }
        visited.add(pos);
        prefix += boggleGrid.getCharAt(i, j);
        if(boggleDict.containsWord(prefix) && prefix.length() >= 4){
            allWords.put(prefix, visited);
        }
        for(int k =i-1 ; k <= i+1 ; k++){
            for(int l = j-1 ; l<=j+1 ; l++){
                findWordsUtil(boggleDict, boggleGrid, visited, new Position(k, l), prefix);
            }
        }
        visited.remove(pos);
    }

    /*
     * Gets words from the user.  As words are input, check to see that they are valid.
     * @param board The boggle board
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    public int humanMove(String word){
            if (word.length() > 0 && allWords.containsKey(word.toUpperCase()) && !gameStats.getPlayerWords().contains(word)) {
                gameStats.addWord(word.toUpperCase(), BoggleStats.Player.Human);
            }
            return this.gameStats.getScore();
        }


    /*
     * Gets words from the computer.  The computer should find words that are
     * both valid and not in the player's word list.  For each word that the computer
     * finds, update the computer's word list and increment the
     * computer's score (stored in boggleStats).
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    public void computerMove(){
        Set<String> temp = allWords.keySet();
        for (String i : gameStats.getPlayerWords()) {
            temp.remove(i);
        }
        for (String i: temp) {
            gameStats.addWord(i, BoggleStats.Player.Computer);
        }
    }


    public BoggleStats getGameStats() {
        return gameStats;
    }

    public int getMinBoardSize() {
        return minBoardSize;
    }

    public int getMaxBoardSize() {
        return maxBoardSize;
    }



    /**
     * gets the HashMap allWords. method used for testing
     * @return allWords
     */
    public HashMap<String, ArrayList<Position>> getAllWords() {return allWords;}

    /**
     * sets the dictioanry of all_words
     * @param dict
     */
    public void setAllWords(HashMap<String, ArrayList<Position>> dict){
        allWords = dict;
    }

    /**
     * returns the memento of the current game
     * @param name
     * @return Memento of the current game
     */
    public Memento getaMemento(){
        return new Memento(gameStats, boggleboard, allWords);
    }

    /**
     * Returns the value of the given Memento
     * @param memento
     * @return the contents of the provided Memento
     */
    public List getStateFromMemento(Memento memento) {
        return memento.getState();
    }

    /**
     * changes the gamestats of teh current game
     * @param gameStats
     */
    public void changeGameStats(BoggleStats gameStats){this.gameStats = gameStats;}
}



