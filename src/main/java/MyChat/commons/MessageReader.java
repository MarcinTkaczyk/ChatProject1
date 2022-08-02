package MyChat.commons;

import lombok.extern.java.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

@Log
public class MessageReader {

    private final Consumer<Object> messageConsumer;
    private ObjectInputStream objectInputStream;
    private Runnable onClose;

    public MessageReader(Socket socket, Consumer<Object> messageConsumer, Runnable onClose) {
        this.messageConsumer = messageConsumer;
        this.onClose = onClose;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.severe("Creating input stream failed: " + e.getMessage());
        }
    }

    public void read(){
        Object object;
        try{
            while(true) {
                object = objectInputStream.readObject();
                messageConsumer.accept(object);
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        finally {
            if (onClose != null) {
                onClose.run();
            }
        }
    }

}
