package Command;

import java.util.*;

public class TutOperator {
    ArrayList<TutCommand> commandqueue;

    public TutOperator(){
        this.commandqueue = new ArrayList<TutCommand>();
    }

    public void acceptCommand(TutCommand command) {
        this.commandqueue.add(command);
    }

    /**
     * execute all commands in queue
     */
    public void operateAll() {

        for (TutCommand command: this.commandqueue) {
            command.execute();
        }
        commandqueue.clear();
    }
}
