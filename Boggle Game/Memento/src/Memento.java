package Memento.src;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import boggle.*;

public class Memento implements Serializable {
    private List state;

    public Memento(BoggleStats Bogglestats, String Boggleboard){
        this.state = new ArrayList(Arrays.asList(Bogglestats, Boggleboard));
    }

    public List getState(){
        return state;
    }
}
