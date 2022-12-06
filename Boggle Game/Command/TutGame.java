package Command;

import java.util.*;
import boggle.*;

/**
 * Extension of BoggleGame to implement Command design pattern and an alternative way of checking inputs
 */
public class TutGame extends BoggleGame{
    public Scanner scanner;
    public TutBase base;
    public TutOperator oper;

    public TutGame(){
        this.scanner = new Scanner(System.in);
        this.oper = new TutOperator();
    }

    /**
     * Initializes a new TutBase to be used with the Command design pattern for checking the inputs submitted by the
     * user
     * @param neededWords ArrayList with all words required from user
     */
    public void start(ArrayList<String> neededWords){
        this.base = new TutBase(neededWords);
    }

    /**
     * Accepts a String input to be stored for later for use with the Command design pattern and to update score
     * @param word input from the boggle board submitted by user
     * @return amount of words that were needed by the user that have been submitted
     */
    @Override
    public int humanMove(String word) {
        oper.acceptCommand(new InsertCommand(base, word));
        return base.updateFoundWordCount(word);
    }

    /**
     * Boolean representing if all words have been found yet
     * @return true if all words have been found
     */
    public boolean canProceed(){
        return this.base.all_found();
    }
}
