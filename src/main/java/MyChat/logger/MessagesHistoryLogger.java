package MyChat.logger;

import MyChat.server.ServerEvent;
import lombok.extern.java.Log;

import java.util.function.Consumer;;import static MyChat.server.ServerEventType.MESSAGE_RECEIVED;

@Log
public class MessagesHistoryLogger implements Consumer<ServerEvent> {

    @Override
    public void accept(ServerEvent event) {
        if (event.getType().equals(MESSAGE_RECEIVED)) {
            log.info("New message: " + event.getType().toString());
        }
    }

}
