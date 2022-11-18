package Memento.src;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MementoPatternDemo {
    public static void main(String[] args) throws IOException {

        Originator originator = new Originator();
        Caretaker careTaker = new Caretaker();


        System.out.println("Current State: " + originator.getState());

        originator.loadBoard(careTaker.get(0).toString());
        System.out.println("First saved State: " + originator.getState());
        originator.loadBoard(careTaker.get(1).toString());
        System.out.println("Second saved State: " + originator.getState());
    }
}
