package MyChat.client;

import MyChat.commons.MessageReader;
import MyChat.commons.Sockets;
import MyChat.messages.ChatHistoryMessage;
import MyChat.messages.FileMessage;
import MyChat.messages.TextMessage;

import java.io.File;
import java.net.Socket;
import java.util.function.Consumer;


public class ClientSocketReader implements Runnable{

    private final Socket socket;
    private File downloadLocation = null;

    Consumer<Object> messageConsumer = object -> {
      if(object instanceof TextMessage){
          TextMessage message = (TextMessage) object;
          ClientUtils.displayClientMessage(message);
      }
      if(object instanceof ChatHistoryMessage){
          ChatHistoryMessage message = (ChatHistoryMessage) object;
          ClientUtils.displayClientHistory(message);
      }
      if(object instanceof FileMessage){
          FileMessage message = (FileMessage) object;
          ClientUtils.downloadFile(message, downloadLocation);
      }


    };

    public ClientSocketReader(Socket socket, File downloadLocation) {
        this.socket = socket;
        this.downloadLocation = downloadLocation;
    }

    @Override
    public void run() {
            new MessageReader(socket, messageConsumer, () -> Sockets.close(socket)).read();
        }

}
