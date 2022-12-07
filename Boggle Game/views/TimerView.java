package views;

import boggle.BoggleController;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  A TimerView object facilitates the creation of a timer and all the methods associated with it for a Boggle game.
 */
public class TimerView {


    BoggleController boggleController; //the BoggleController being used for the timer
    int total_num_secs; //the number of seconds the timer will start counting down from
    int curr_num_secs; //the current number of seconds the timer is at. It decrements by 1 every second.
    Timer timer = new Timer(); //Java's built in timer object

    private static TimerView timerView; //TimerView object (is private to adhere to Singleton design pattern).


    /**
     * The constructor for a TimerView object (is private to adhere to Singleton design pattern).
     * @param boggleController The BoggleController that will use the timer
     * @param num_secs The number of seconds the timer will start counting down at
     */
    private TimerView(BoggleController boggleController, int num_secs) {
        this.boggleController = boggleController;
        this.total_num_secs = num_secs;
        this.curr_num_secs = num_secs;
    }

    /**
     * The getter method for a TimerView object (is public adhere to Singleton design pattern).
     * @param boggleController The BoggleController that will use the timer
     * @param num_secs The number of seconds the timer will start counting down at
     */
    public static TimerView getInstance(BoggleController boggleController, int num_secs) {
        timerView = new TimerView(boggleController, num_secs);
        return timerView;
    }

    /**
     * @return a String of the floor of the number of minutes left on the timer
     */
    public String get_mins() {
        return Integer.toString(curr_num_secs/60);
    }

    /**
     * @return the number of seconds remaining until there is a whole number of minutes left on the timer
     */
    public String get_secs() {
        if (curr_num_secs % 60 >= 10) {
            return Integer.toString(curr_num_secs % 60);
        }
        else {
            return "0"+ curr_num_secs % 60;
        }
    }

    /**
     * A TimerTask that updates the GUI every second (decrementing the timer by 1).
     * If there are 10 seconds left, the timer changes colour from yellow to red.
     */
    TimerTask countDown = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                curr_num_secs--;
                boggleController.getBoggleView().setTimerTextCurrTime(get_mins(), get_secs());
                if (curr_num_secs == 10) {
                    boggleController.getBoggleView().changeTimerColour();
                }
            });
        }
    };

    /**
     * A TimerTask that ends the round once 0 seconds are hit on the timer.
     * The timer GUI is updated such that it displays the message "End!"
     */
    TimerTask endRound = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                boggleController.endRound();
                boggleController.getBoggleView().setTimerTextCustomMessage("End!");
            });
        }
    };

    /**
     * Stops the timer when a new game or a new board is loaded, or the end round button is clicked on.
     * The timer GUI is updated such that it displays the message "End!"
     */
    public void stop() {
        timer.cancel();
        boggleController.getBoggleView().setTimerTextCustomMessage("End!");
    }

    /**
     * Starts the countdown for a timer in a Boggle game
     */
    public void start() {
        timer.schedule(endRound, total_num_secs*1000L);
        timer.scheduleAtFixedRate(countDown, 1000, 1000);
    }
}
