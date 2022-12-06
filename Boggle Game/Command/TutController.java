package Command;

import boggle.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;


public class TutController extends BoggleController{

    TutView view; // the boggle view
    TutGame game; // the game's model.


    /**
     * constructor, starts the game application
     *
     * @param view reference to boggleView, the game's view
     * @param game reference to boggleGame, the game's model
     */
    public TutController(TutView view, TutGame game) {
        super(view, game);
        this.view = view;
        this.game = game;
        changeEndRound();
    }

    public void start(){
        this.view.startGame();
    }

    /**
     * Only ends round when all desired words are submitted, moving on to next step of tutorial
     */
    public class handleEndRound implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (view.isGameOn()) {
                game.oper.operateAll();
                if(game.proceed()){
                    game.getGameStats().endRound();
                    view.setGameOn(false);
                    view.resetBoard();
                    int place = view.getPlace();
                    System.out.println(place);
                    view.startGame(place+1);
                }else{
                    //Placeholder, will discuss with Ahmed over weekend
                    System.out.println("Still more to go");
                }
            }
        }
    }

    /**
     * Changed to check if the word is from what was instructed to input
     */
    public class handleSubmit implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (view.isGameOn()) {
                String word = view.getGameInputDisplay();
                int newScore = game.humanMove(word);
                view.updateScore(newScore);
                view.resetBoard();
            }
        }
    }

    public void changeEndRound(){
        this.view.addEndRoundHandler(new handleEndRound());
        this.view.addSubmitHandler(new handleSubmit());
    }


}
