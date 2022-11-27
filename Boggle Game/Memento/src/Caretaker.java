package Memento.src;

import java.io.*;

public class Caretaker {
    private static final File saved = new File("./saved/");
    public static File getfile(){
        return saved;
    }
    public static void save(String filename, Memento mementotosave){
        try {
            FileOutputStream fout = new FileOutputStream("./saved/"+filename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(mementotosave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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