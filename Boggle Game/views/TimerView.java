package views;

import boggle.BoggleController;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  A class with all methods for the timer.
 */
public class TimerView {


    BoggleController boggleController;
    int total_num_secs;
    int curr_num_secs;
    Timer timer = new Timer();

    public TimerView(BoggleController boggleController, int num_secs) {
        this.boggleController = boggleController;
        this.total_num_secs = num_secs;
        this.curr_num_secs = num_secs;
    }

    public String get_mins() {
        return Integer.toString(curr_num_secs/60);
    }

    public String get_secs() {
        if (curr_num_secs >= 10) {
            return Integer.toString(curr_num_secs%60);
        }
        else {
            return "0"+ curr_num_secs % 60;
        }
    }


    TimerTask countDown = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                curr_num_secs--;
                boggleController.getBoggleView().setTimerText(get_mins(), get_secs());
            });
        }
    };

    TimerTask endRound = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                boggleController.endRound();
                timer.cancel();
            });
        }
    };


    public void start() {
        timer.schedule(endRound, total_num_secs* 1000L);
        timer.scheduleAtFixedRate(countDown, 1000, 1000);
    }
}
