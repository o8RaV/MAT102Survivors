package Memento.src;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import boggle.*;

public class Memento implements Serializable {
    private List state;

    public Memento(String name, String boggleboard, BoggleStats boggleStats){
        List state = new ArrayList(Arrays.asList(name, boggleboard, boggleStats));
        this.state = state;
    }

    public List getState(){
        return state;
    }
}
