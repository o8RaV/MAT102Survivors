package Memento.src;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import boggle.*;

public class Originator {
    private List state;

    public void setState(List state){
        this.state = state;
    }

    public List getState(){
        return state;
    }

    public Memento savestatetomemento(){
        return new Memento();
    }

    public void getstatefrommemento(Memento memento) {
        state = memento.getState();
    }
}

