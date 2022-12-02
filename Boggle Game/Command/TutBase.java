package Command;

import java.util.*;

public class TutBase {
    private ArrayList<String> words;
    private int number_found;
    private int need;
    private ArrayList<String> found;
    private ArrayList<String> placeholdScoreUpdate;

    public TutBase(ArrayList<String> required){
        this.words = required;
        this.number_found = 0;
        this.need = required.size();
        this.found = new ArrayList<>();
        this.placeholdScoreUpdate = new ArrayList<>();
    }

    public void addTo(String in){
        in = in.toLowerCase();
        if(this.words.contains(in) && !this.found.contains(in)){
            this.number_found++;
            this.found.add(in);
        }
    }

    public int tempUpdateScore(String in){
        in = in.toLowerCase();
        if(this.words.contains(in) && !this.placeholdScoreUpdate.contains(in)){
            this.placeholdScoreUpdate.add(in);
        }
        return this.placeholdScoreUpdate.size();
    }

    public boolean all_found(){
        return this.number_found == this.need;
    }

}
