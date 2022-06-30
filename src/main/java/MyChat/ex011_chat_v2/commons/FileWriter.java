package MyChat.ex011_chat_v2.commons;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

@Log
public class FileWriter {

    private static final int BUFFER_SIZE = 16 * 1024;
    private InputStream inputStream;
    private OutputStream outputStream;

    public FileWriter(Socket socket) {
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException exception) {
           log.severe("Creating output stream failed: " + exception.getMessage());
        }
    }

    /*
    public void writeFile(Byte[] file) throws IOException{

        byte[] buffer = new byte[BUFFER_SIZE];
        inputStream = new FileInputStream(file);
        int count;
        while((count = inputStream.read(buffer)) > 0){
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        inputStream.close();

    }*/



}
