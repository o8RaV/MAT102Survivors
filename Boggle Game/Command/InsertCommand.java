package Command;
import java.util.*;

public class InsertCommand implements TutCommand {
    private TutBase toUse;
    private String toCheck;

    public InsertCommand(TutBase current, String input){
        this.toUse = current;
        this.toCheck = input;
    }

    @Override
    public void execute() {
        this.toUse.addTo(toCheck);
        System.out.println("Input received: "+ toCheck.toLowerCase());
    }
}
