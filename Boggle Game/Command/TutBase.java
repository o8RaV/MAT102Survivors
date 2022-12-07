package Command;

import java.util.*;

public class TutBase {
    private ArrayList<String> words; // All words required from user
    private int number_found; // How many needed words the user has submitted
    private int need; // Total number of words the user needs to submit
    private ArrayList<String> found; // Words that are required from the user that have been submitted
    private ArrayList<String> wordsNotFound; // Requried words that have not been submitted yet

    /**
     * Constructor for Tutbase, setting up the variables, setting number of found words to 0 and making the list of
     * found words empty
     * @param required Array List of required words from user
     */
    public TutBase(ArrayList<String> required){
        this.words = required;
        this.number_found = 0;
        this.need = required.size();
        this.found = new ArrayList<>();
        this.wordsNotFound = new ArrayList<>();
        this.wordsNotFound.addAll(required);
    }

    /**
     * Accepts a String, changing the list of found words and number of found words accordingly if it is one of the
     * required words that has not been found yet
     * @param in input from the user selected from letters of the tutorial board
     */
    public void addTo(String in){
        in = in.toLowerCase();
        if(this.words.contains(in) && !this.found.contains(in)){
            this.number_found++;
            this.found.add(in);
        }
    }

    /**
     * Used to keep the counter of required words that have been input by the user up to date
     * @param in input from the tutorial boggle board
     * @return amount of unique required words input by user
     */
    public int updateFoundWordCount(String in){
        in = in.toLowerCase();
        this.wordsNotFound.remove(in);
        return this.need - this.wordsNotFound.size();
    }

    /**
     * Whether or not all needed words have been submitted
     * @return true if all needed words have been passed into the Command
     */
    public boolean all_found(){
        return this.number_found == this.need;
    }

    /**
     * All needed words not submitted yet
     * @return ArrayList of all needed words that have not been submitted yet
     */
    public ArrayList<String> wordsNotFound(){
        return this.wordsNotFound;
    }
}
