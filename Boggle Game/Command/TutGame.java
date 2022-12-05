package Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import boggle.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import views.BoggleView;

public class TutGame extends BoggleGame{
    public Scanner scanner;
    String boggleboard;
    public TutBase base;
    public TutOperator oper;

    public TutGame(){
        this.scanner = new Scanner(System.in);
        this.oper = new TutOperator();
    }

    public void start(ArrayList<String> a){
        this.base = new TutBase(a);
    }

    @Override
    public int humanMove(String word) {
        oper.acceptCommand(new InsertCommand(base, word));
        return base.tempUpdateScore(word);
    }

    public boolean proceed(){
        return this.base.all_found();
    }
}
