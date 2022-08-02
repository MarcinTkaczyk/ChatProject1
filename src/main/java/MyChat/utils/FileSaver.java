package MyChat.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

@Slf4j
public class FileSaver {

    public static void SaveToFile(Object serObj, String filepath) {

        try {

            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            log.info("The Object was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
