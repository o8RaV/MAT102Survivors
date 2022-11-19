import boggle.BoggleGame;

/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 * Team Members are: Rav, Chris, Justin, Ahmed.
 */
public class Main {
    /**
    * Main method. 
    * @param args command line arguments.
    **/
    public static void main(String[] args) {
        BoggleGame b = new BoggleGame();
        b.giveInstructions();
        b.playGame();
    }
}
