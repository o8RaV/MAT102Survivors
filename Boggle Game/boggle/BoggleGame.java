package boggle;

import Memento.src.Caretaker;
import Memento.src.Memento;

import java.io.File;
import java.util.*;


/**
 * The BoggleGame class for the first Assignment in CSC207, Fall 2022
 */
public class BoggleGame {

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;
    /**
     * stores game statistics
     */
    private BoggleStats gameStats;
    private String boggleboard;
    private Caretaker Caretaker = new Caretaker();
    private List state ;


    /**
     * dice used to randomize letter assignments for a small grid
     */
    private final String[] dice_small_grid = //dice specifications, for small and large grids
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"};
    /**
     * dice used to randomize letter assignments for a big grid
     */
    private final String[] dice_big_grid =
            {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
                    "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
                    "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};

    /*
     * BoggleGame constructor
     */
    public BoggleGame() {
        this.scanner = new Scanner(System.in);
        this.gameStats = new BoggleStats();
    }

    /*
     * Provide instructions to the user, so they know how to play the game.
     */
    public void giveInstructions() {
        System.out.println("The Boggle board contains a grid of letters that are randomly placed.");
        System.out.println("We're both going to try to find words in this grid by joining the letters.");
        System.out.println("You can form a word by connecting adjoining letters on the grid.");
        System.out.println("Two letters adjoin if they are next to each other horizontally, ");
        System.out.println("vertically, or diagonally. The words you find must be at least 4 letters long, ");
        System.out.println("and you can't use a letter twice in any single word. Your points ");
        System.out.println("will be based on word length: a 4-letter word is worth 1 point, 5-letter");
        System.out.println("words earn 2 points, and so on. After you find as many words as you can,");
        System.out.println("I will find all the remaining words.");
        System.out.println("\nHit return when you're ready...");
    }


    /*
     * Gets information from the user to initialize a new Boggle game.
     * It will loop until the user indicates they are done playing.
     */
    public void playGame(){
        int boardSize = 0;
        while (true) {
            System.out.println("Enter 1 to play on a big (5x5) grid; 2 to play on a small (4x4) one; 3 to play on a saved board:");
            String choiceGrid = scanner.nextLine();

            //get grid size preference
            if (choiceGrid == "") break; //end game if user inputs nothing
            while (!choiceGrid.equals("1") && !choiceGrid.equals("2") && !choiceGrid.equals("3")) {
                System.out.println("Please try again.");
                System.out.println("Enter 1 to play on a big (5x5) grid; 2 to play on a small (4x4) one; 3 to play on a saved board:");
                choiceGrid = scanner.nextLine();
            }

            if (choiceGrid.equals("1")){
                boardSize = 5;
            }
            else if (choiceGrid.equals("2")) {
                boardSize = 4;
            }
            else {
                try {
                    File temp = new File("C:\\Users\\ragha\\OneDrive\\Desktop\\MAT102Survivors\\Boggle Game\\saved");
                    System.out.println("These are the available boards");
                    String[] b = temp.list();
                    List<String> a = new ArrayList<String>();
                    assert b != null;
                    for (String i : b) {
                        if (i.endsWith(".bbg"))
                            a.add(i);
                            System.out.println(i);
                    }
                    System.out.println("Type the board name please:");
                    choiceGrid = scanner.nextLine();
                    while (!a.contains(choiceGrid)) {
                        System.out.println("Invalid input");
                        choiceGrid = scanner.nextLine();
                    }
                    Memento i = Caretaker.get(choiceGrid);
                    getstatefrommemento(i);
                    gameStats = (BoggleStats) state.get(2);
                    boggleboard = (String) state.get(1);
                    boardSize = (int) Math.sqrt(boggleboard.length());
                    playRound(boardSize, boggleboard);
                }
                catch(Exception e){
                    throw new RuntimeException("idk some error");
                }
                this.gameStats.summarizeRound();
                this.gameStats.endRound();

                //Shall we repeat?
                System.out.println("Play again? Type 'Y' or 'N'");
                String choiceRepeat = scanner.nextLine().toUpperCase();

                if (choiceRepeat == "") break; //end game if user inputs nothing
                while (!choiceRepeat.equals("Y") && !choiceRepeat.equals("N")) {
                    System.out.println("Please try again.");
                    System.out.println("Play again? Type 'Y' or 'N'");
                    choiceRepeat = scanner.nextLine().toUpperCase();
                }

                if (choiceRepeat == "" || choiceRepeat.equals("N")) break; //end game if user inputs nothing
            }

            //get letter choice preference
            System.out.println("Enter 1 to randomly assign letters to the grid; 2 to provide your own.");
            String choiceLetters = scanner.nextLine();

            if (choiceLetters == "") break; //end game if user inputs nothing
            while (!choiceLetters.equals("1") && !choiceLetters.equals("2")) {
                System.out.println("Please try again.");
                System.out.println("Enter 1 to randomly assign letters to the grid; 2 to provide your own.");
                choiceLetters = scanner.nextLine();
            }

            if (choiceLetters.equals("1")) {
                this.boggleboard = randomizeLetters(boardSize);
                playRound(boardSize, boggleboard);
            } else if (choiceLetters.equals("2")) {
                System.out.println("Input a list of " + boardSize * boardSize + " letters:");
                choiceLetters = scanner.nextLine();
                while (!(choiceLetters.length() == boardSize * boardSize)) {
                    System.out.println("Sorry, bad input. Please try again.");
                    System.out.println("Input a list of " + boardSize * boardSize + " letters:");
                    choiceLetters = scanner.nextLine();
                }
                playRound(boardSize, choiceLetters.toUpperCase());
            }

            //round is over! So, store the statistics, and end the round.
            this.gameStats.summarizeRound();
            this.gameStats.endRound();

            //Shall we repeat?
            System.out.println("Play again? Type 'Y' or 'N'");
            String choiceRepeat = scanner.nextLine().toUpperCase();

            if (choiceRepeat == "") break; //end game if user inputs nothing
            while (!choiceRepeat.equals("Y") && !choiceRepeat.equals("N")) {
                System.out.println("Please try again.");
                System.out.println("Play again? Type 'Y' or 'N'");
                choiceRepeat = scanner.nextLine().toUpperCase();
            }

            if (choiceRepeat == "" || choiceRepeat.equals("N")) break; //end game if user inputs nothing

        }

        //we are done with the game! So, summarize all the play that has transpired and exit.
        this.gameStats.summarizeGame();
        System.out.println("Thanks for playing!");
    }

    /*
     * Play a round of Boggle.
     * This initializes the main objects: the board, the dictionary, the map of all
     * words on the board, and the set of words found by the user. These objects are
     * passed by reference from here to many other functions.
     */
    public void playRound(int size, String letters) {
        //step 1. initialize the grid
        BoggleGrid grid = new BoggleGrid(size);
        grid.initalizeBoard(letters);
        //step 2. initialize the dictionary of legal words
        Dictionary boggleDict = new Dictionary("wordlist.txt"); //you may have to change the path to the wordlist, depending on where you place it.
        //step 3. find all legal words on the board, given the dictionary and grid arrangement.
        Map<String, ArrayList<Position>> allWords = new HashMap<String, ArrayList<Position>>();
        findAllWords(allWords, boggleDict, grid);
        //step 4. allow the user to try to find some words on the grid
        System.out.println("Type 101 to save the board");
        humanMove(grid, allWords);
        //step 5. allow the computer to identify remaining words
        computerMove(allWords);
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
    private String randomizeLetters(int size) {
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
        }
        Collections.shuffle(random);
        String ans = "";
        for (char i: random) {
            ans += i;
        }

        return ans;
    }

    /*
     * This should be a recursive function that finds all valid words on the boggle board.
     * Every word should be valid (i.e. in the boggleDict) and of length 4 or more.
     * Words that are found should be entered into the allWords HashMap.  This HashMap
     * will be consulted as we play the game.
     *
     * Note that this function will be a recursive function.  You may want to write
     * a wrapper for your recursion. Note that every legal word on the Boggle grid will correspond to
     * a list of grid positions on the board, and that the Position class can be used to represent these
     * positions. The strategy you will likely want to use when you write your recursion is as follows:
     * -- At every Position on the grid:
     * ---- add the Position of that point to a list of stored positions
     * ---- if your list of stored positions is >= 4, add the corresponding word to the allWords Map
     * ---- recursively search for valid, adjacent grid Positions to add to your list of stored positions.
     * ---- Note that a valid Position to add to your list will be one that is either horizontal, diagonal, or
     *      vertically touching the current Position
     * ---- Note also that a valid Position to add to your list will be one that, in conjunction with those
     *      Positions that precede it, form a legal PREFIX to a word in the Dictionary (this is important!)
     * ---- Use the "isPrefix" method in the Dictionary class to help you out here!!
     * ---- Positions that already exist in your list of stored positions will also be invalid.
     * ---- You'll be finished when you have checked EVERY possible list of Positions on the board, to see
     *      if they can be used to form a valid word in the dictionary.
     * ---- Food for thought: If there are N Positions on the grid, how many possible lists of positions
     *      might we need to evaluate?
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     */
    private void findAllWords(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid) {
        // Mark all characters as not visited
        // Initialize current string
        // Consider every character and look for all words
        // starting with this character
        for (int i = 0; i <= boggleGrid.numCols(); i++)
            for (int j = 0; j <= boggleGrid.numCols(); j++)
                findWordsUtil(allWords, boggleDict, boggleGrid, new ArrayList<Position>(), new Position(i, j), "");

    }

    static void findWordsUtil(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid, ArrayList<Position> visited, Position pos, String prefix) {
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
                findWordsUtil(allWords, boggleDict, boggleGrid, visited, new Position(k, l), prefix);
            }
        }
        prefix = prefix.substring(0, prefix.length() - 1);
        visited.remove(pos);
    }

    /*
     * Gets words from the user.  As words are input, check to see that they are valid.
     * If yes, add the word to the player's word list (in boggleStats) and increment
     * the player's score (in boggleStats).
     * End the turn once the user hits return (with no word).
     *
     * @param board The boggle board
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    private void humanMove(BoggleGrid board, Map<String,ArrayList<Position>> allWords){
        System.out.println("It's your turn to find some words!\n\n");
        System.out.println(board.toString() + "\n\n");
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("Type Input : ");
            String temp = input.nextLine();
            if (temp.length() == 0) {
                break;
            }
            if (allWords.containsKey(temp.toUpperCase())) {
                gameStats.addWord(temp.toUpperCase(), BoggleStats.Player.Human);
            }
            if (temp.equals("101")){
                System.out.println("Enter Board name:");
                String i = input.nextLine();
                while (!i.endsWith(".bbg")) {
                    System.out.println("Invalid name");
                    i = scanner.nextLine();
                }
                Caretaker.save(i, getaMemento(i));
            }
        }
    }

    //You write code here!
    //step 1. Print the board for the user, so they can scan it for words
    //step 2. Get a input (a word) from the user via the console
    //step 3. Check to see if it is valid (note validity checks should be case-insensitive)
    //step 4. If it's valid, update the player's word list and score (stored in boggleStats)
    //step 5. Repeat step 1 - 4
    //step 6. End when the player hits return (with no word choice).


    /*
     * Gets words from the computer.  The computer should find words that are
     * both valid and not in the player's word list.  For each word that the computer
     * finds, update the computer's word list and increment the
     * computer's score (stored in boggleStats).
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    private void computerMove(Map<String,ArrayList<Position>> all_words){
        Set<String> temp = all_words.keySet();
        for (String i : gameStats.getPlayerWords()) {
            if (temp.contains(i)) {
                temp.remove(i);
            }
        }
        for (String i: temp) {
            gameStats.addWord(i, BoggleStats.Player.Computer);
        }
    }





    public void setState(List state){
        this.state = state;
    }

    public List getState(){
        return state;
    }

    public Memento getaMemento(String name){
        return new Memento(name, boggleboard, gameStats);
    }

    public void getstatefrommemento(Memento memento) {
        state = memento.getState();
    }
}
