import boggle.BoggleGame;
import boggle.BoggleGrid;
import boggle.Dictionary;

public class Test {
    public static void main(String[] args){
        BoggleGame game = new BoggleGame();
        Dictionary boggleDict = new Dictionary("wordlist.txt");
        BoggleGrid grid = new BoggleGrid(6);
        grid.initalizeBoard("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJ");
        game.findAllWords(boggleDict, grid);
        System.out.println(game.getAllWords().toString());
    }
}
