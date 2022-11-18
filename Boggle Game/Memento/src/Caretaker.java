package Memento.src;

import Memento.src.Memento;

import java.util.ArrayList;
import java.util.List;

public class Caretaker {
    private List<Memento> mementoList = new ArrayList<Memento>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
    public List<Memento> getlist(){
        return mementoList;
    }
    public List<String> getnames(){
        List<String> temp = new ArrayList<>();
        for (Memento i : mementoList){
            temp.add((String) i.getState().get(0));
        }
        return temp;
    }
    public int size(){
        return mementoList.size();
    }
    public Caretaker getcaretaker(){
        return this;
    }
}