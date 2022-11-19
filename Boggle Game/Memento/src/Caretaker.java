package Memento.src;

import java.io.*;

public class Caretaker {
    private final File saved = new File("./saved/");
    public File getfile(){
        return saved;
    }
    public void save(String name, Memento memento){
        try {
            FileOutputStream fout = new FileOutputStream("./saved/"+name);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(memento);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Memento get(String name) throws IOException {
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream("./saved/"+name);
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