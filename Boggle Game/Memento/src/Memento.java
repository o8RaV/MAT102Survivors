package Memento.src;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import boggle.*;

public class Memento {
    private List state;

    public Memento(BoggleGrid boggleboard, BoggleStats boggleStats){
        List state = new ArrayList(Arrays.asList(boggleboard, boggleStats));
        this.state = state;
    }

    public List getState(){
        return state;
    }
}
