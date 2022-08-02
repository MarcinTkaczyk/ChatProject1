package MyChat.commons;

import MyChat.messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageWriter {

    private ObjectOutputStream objectOutputStream;

    public MessageWriter(ObjectOutputStream objectOutputStream){
        this.objectOutputStream = objectOutputStream;
    }

    public void write(Message message){
        try{
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
