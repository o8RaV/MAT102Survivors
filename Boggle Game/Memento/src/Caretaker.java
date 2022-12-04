package Memento.src;

import java.io.*;

public class Caretaker {
    private static final File saved = new File("./saved/");

    /**
     * getter for saved file folder
     * @return Saved file folder
     */
    public static File getfile(){
        return saved;
    }

    /**
     * saves the memento in saved board folder with the provided name
     * @param filename
     * @param mementotosave
     */
    public static void save(String filename, Memento mementotosave){
        try {
            FileOutputStream fout = new FileOutputStream("./saved/"+filename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(mementotosave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return the memento inside a given file name in saved boards
     * @param filename
     * @return
     * @throws IOException
     */
    public static Memento get(String filename) throws IOException {
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream("./saved/"+filename);
            in = new ObjectInputStream(file);
            return (Memento) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            assert in != null;
            in.close();
            file.close();
        }
    }
}