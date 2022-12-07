import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import boggle.*;
import boggle.Dictionary;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.BoggleView;
import views.TimerView;

import static org.junit.jupiter.api.Assertions.*;

public class BoggleTests {

    //Timer Test
    @Test
    void timer_test_two_digit_seconds() { //test to see if get_mins and get_secs works properly
        TimerView timerView = TimerView.getInstance(null, 80);
        assertEquals(timerView.get_mins(), "1");
        assertEquals(timerView.get_secs(), "20");
    }

    @Test
    void timer_test_one_digit_seconds() { //test to see if get_mins and get_secs works properly
        TimerView timerView = TimerView.getInstance(null, 121);
        assertEquals(timerView.get_mins(), "2");
        assertEquals(timerView.get_secs(), "01");
    }

    //BoggleGame  Test
    @Test
    void findAllWords_small() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BoggleGame game = new BoggleGame();
        Method method = game.getClass().getDeclaredMethod("findAllWords", Dictionary.class, BoggleGrid.class);
        method.setAccessible(true);

        Dictionary boggleDict = new Dictionary("wordlist.txt");
        BoggleGrid grid = new BoggleGrid(4);
        grid.initalizeBoard("RHLDNHTGIPHSNMJO");
        method.invoke(game, boggleDict, grid);
        Map<String, ArrayList<Position>> allWords = game.getAllWords();

        Set<String> expected = new HashSet<>(Arrays.asList("GHOST", "HOST", "THIN"));
        assertEquals(expected, allWords.keySet());
    }

    //Dictionary Test
    @Test
    void containsWord() {
        Dictionary dict = new Dictionary("./wordlist.txt");
        assertTrue(dict.containsWord("ENZYME"));
        assertTrue(dict.isPrefix("pench"));
    }

    //BoggleGrid Test
    @Test
    void setupBoard() {
        BoggleGrid grid = new BoggleGrid(10);
        String letters = "";
        for (int i = 0; i < 10; i++) {
            letters = letters + "0123456789";
        }

        grid.initalizeBoard(letters);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(letters.charAt(i*10+j), grid.getCharAt(i, j));
            }
        }
    }

    //BoggleStats Test
    @Test
    void endRoundTest() {
        BoggleStats stats = new BoggleStats();
        stats.endRound();
        stats.endRound();
        stats.endRound();
        assertEquals(3, stats.getRound());
    }

    // checks if a repeatedly submitted word will not count for any more points
    @Test
    void repeatedWordTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BoggleGame game = new BoggleGame();
        Method method = game.getClass().getDeclaredMethod("findAllWords", Dictionary.class, BoggleGrid.class);
        method.setAccessible(true);

        Dictionary boggleDict = new Dictionary("wordlist.txt");
        Map<String, ArrayList<Position>> allWords = new HashMap<>();
        BoggleGrid grid = new BoggleGrid(4);
        grid.initalizeBoard("RHLDNHTGIPHSNMJO");
        Object r = method.invoke(game, boggleDict, grid);

        game.humanMove("GHOST");
        game.humanMove("GHOST");
        assertEquals(game.getGameStats().getScore(), 2);
    }

    // checks if the method is appropriately setting the attribute allWords with all the words found from a given string
    @Test
    void setLetters() {
        BoggleGame game = new BoggleGame();
        Map<String, ArrayList<Position>> allWords;

        game.setLetters("RHLDNHTGIPHSNMJO");
        allWords = game.getAllWords();
        assertEquals(3, allWords.size());
    }

}
