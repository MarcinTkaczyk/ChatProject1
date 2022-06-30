package MyChat.ex011_chat_v2.commons;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

@Log
public class FileReader {

    private static final int BUFFER_SIZE = 8 * 1024;
    private InputStream inputStream;
    private OutputStream outputStream;

    public FileReader(Socket socket) {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException exception) {
            log.severe("Creating input stream failed: " + exception.getMessage());
        }
    }

    public void readFile(File file) throws IOException{

        byte[] buffer = new byte[BUFFER_SIZE];
        outputStream = new FileOutputStream(file);
        int count;
        while((count = inputStream.read(buffer)) > 0){
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        inputStream.close();

    }
}
