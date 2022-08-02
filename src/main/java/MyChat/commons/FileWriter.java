package MyChat.commons;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

@Log
public class FileWriter {

    private OutputStream outputStream;

    public FileWriter(Socket socket) {
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException exception) {
           log.severe("Creating output stream failed: " + exception.getMessage());
        }
    }

}
