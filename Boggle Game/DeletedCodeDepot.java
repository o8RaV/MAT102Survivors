import javafx.scene.text.Text;

public class DeletedCodeDepot {

    Text instructions = new Text("""
                The Boggle board contains a grid of letters that are randomly placed.
                We're both going to try to find words in this grid by joining the letters.
                You can form a word by connecting adjoining letters on the grid.
                Two letters adjoin if they are next to each other horizontally, 
                vertically, or diagonally. The words you find must be at least 4 letters long, 
                and you can't use a letter twice in any single word. Your points 
                will be based on word length: a 4-letter word is worth 1 point, 5-letter
                words earn 2 points, and so on. After you find as many words as you can,
                I will find all the remaining words.
                
                Hit return when you're ready...
                """);
}
