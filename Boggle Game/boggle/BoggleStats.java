package boggle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The BoggleStats class for the first Assignment in CSC207, Fall 2022
 * The BoggleStats will contain statsitics related to game play Boggle 
 */
public class BoggleStats implements Serializable {

    /**
     * set of words the player finds in a given round 
     */  
    private Set<String> playerWords = new HashSet<String>();  
    /**
     * set of words the computer finds in a given round 
     */  
    private Set<String> computerWords = new HashSet<String>();  
    /**
     * the player's score for the current round
     */  
    private int pScore; 
    /**
     * the computer's score for the current round
     */  
    private int cScore;
    /**
     * the player's total score across every round
     */  
    private int pScoreTotal; 
    /**
     * the computer's total score across every round
     */  
    private int cScoreTotal; 
    /**
     * the average number of words, per round, found by the player
     */  
    private double pAverageWords; 
    /**
     * the average number of words, per round, found by the computer
     */  
    private double cAverageWords; 
    /**
     * the current round being played
     */  
    private int round; 

    /**
     * enumarable types of players (human or computer)
     */  
    public enum Player {
        Human("Human"),
        Computer("Computer");
        private final String player;
        Player(final String player) {
            this.player = player;
        }
    }

    /* BoggleStats constructor
     * ----------------------
     * Sets round, totals and averages to 0.
     * Initializes word lists (which are sets) for computer and human players.
     */
    public BoggleStats() {
        this.pScore = 0;
        this.cScore = 0;
        this.round = 0;
        this.cAverageWords = 0;
        this.cScoreTotal = 0;
        this.pScoreTotal = 0;
        this.pAverageWords = 0;
        this.playerWords = new HashSet<>();
        this.computerWords = new HashSet<>();
    }

    /* 
     * Add a word to a given player's word list for the current round.
     * You will also want to increment the player's score, as words are added.
     *
     * @param word     The word to be added to the list
     * @param player  The player to whom the word was awarded
     */
    public void addWord(String word, Player player) {
        if (Player.Human == player && !this.playerWords.contains(word)) {
            this.playerWords.add(word);
            if (word.length() >= 4) {
                this.pScore += word.length() - 3;

            }
        } else if (Player.Computer == player) {
            this.computerWords.add(word);
            if (word.length() >= 4) {
                this.cScore += word.length() - 3;
            }
        }
    }

    /* 
     * End a given round.
     * This will clear out the human and computer word lists, so we can begin again.--
     * The function will also update each player's total scores, average scores, and--
     * reset the current scores for each player to zero.--
     * Finally, increment the current round number by 1.--
     */
    public void endRound() {
        this.round += 1;
        this.pScoreTotal += this.pScore;
        this.cScoreTotal += this.cScore;
        this.pAverageWords = this.playerWords.size()/this.round;
        this.cAverageWords = this.computerWords.size()/this.round;
        this.computerWords.clear();
        this.playerWords.clear();
        this.pScore = 0;
        this.cScore = 0;
    }

    /*
     * Summarize one round of boggle.  Print out:
     * The words each player found this round.
     * Each number of words each player found this round.
     * Each player's score this round.
     */
    public String summarizeRound() {
        String summary = "";
        String pWordsString = createWordString(this.playerWords);

        summary += "The Human's score this round : " + this.pScore
                + "\nThe total number of words that the Human found : " + this.playerWords.size()
                + "\nThe words that the Human found : " + pWordsString + "\n\n";


        String cWordString = createWordString(this.computerWords);
        summary += "The Computer's score this round is " + this.cScore
                + "\nThe total number of words that the Computer found : " + this.computerWords.size()
                + "\nThe words that the Computer found : " + cWordString;

        return summary;
    }

    private String createWordString (Set<String> words) {
        StringBuilder s = new StringBuilder();
        if(!words.isEmpty()) {
            boolean first = true;
            for (String i :  words) {
                // just so the first word doesn't have a comma before it.
                if (!first) {
                    s.append(", ").append(i);
                }
                else {
                    s.append(i);
                    first = false;
                }
            }
            return s.toString();
        }
        return "";
    }

    /*
     * Summarize the entire boggle game.  Print out:
     * The total number of rounds played.
     * The total score for either player.
     * The average number of words found by each player per round.
     */
    public void summarizeGame() {
        System.out.println("The total number of rounds played are " + this.round);
        System.out.println("Computer's total score is " + this.cScoreTotal);
        System.out.println("Human's total score is " + this.pScoreTotal);
        System.out.println("Computer's Average words found per game are " + this.cAverageWords);
        System.out.println("Human's Average words found per game are " + this.pAverageWords);
    }

    /* 
     * @return Set<String> The player's word list
     */
    public Set<String> getPlayerWords() {
        return this.playerWords;
    }
    public Set<String> getComputerWords() {
        return this.computerWords;
    }
    /*
     * @return int The number of rounds played
     */
    public int getRound() { return this.round; }

    /*
    * @return int The current player score
    */
    public int getScore() {
        return this.pScore;
    }
    public void setpScore(int score){this.pScore = score;}
    public int getcompScore() {
        return this.cScore;
    }
}
