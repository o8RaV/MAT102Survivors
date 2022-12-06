package Memento.src;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import boggle.*;

public class Memento implements Serializable {
    private List state;

    /**
     * Memento constructor class
     * @param Bogglestats
     * @param Boggleboard
     * @param allWords
     */
    public Memento(BoggleStats Bogglestats, String Boggleboard, HashMap<String, ArrayList<Position>> allWords){
        this.state = new ArrayList(Arrays.asList(Bogglestats, Boggleboard, allWords));
    }

    /**
     * returns the value of the provided memento
     * @return value of provided memento
     */
    public List getState(){
        return state;
    }
}
