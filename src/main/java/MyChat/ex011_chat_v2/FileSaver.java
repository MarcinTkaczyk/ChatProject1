package MyChat.ex011_chat_v2;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class FileSaver {

    public static void SaveToFile(Object serObj, String filepath) {

        try {

            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
