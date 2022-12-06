package Command;
import java.util.*;

/**
 * Implementation of TutCommand to accept words from the user to be checked after a round has ended
 */
public class InsertCommand implements TutCommand {
    private TutBase toUse; // TutBase with data relating to the needed words
    private String toCheck; // String input from user

    public InsertCommand(TutBase current, String input){
        this.toUse = current;
        this.toCheck = input;
    }

    /**
     * Checks if toCheck is part of the needed words list and updates statistics accordingly
     */
    @Override
    public void execute() {
        this.toUse.addTo(toCheck);
    }
}
