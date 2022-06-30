package MyChat.ex011_chat_v2;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerEventsProcessor implements Consumer<ServerEvent> {

    private final ServerWorkers serverWorkers;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case MESSAGE_RECEIVED -> serverWorkers
                    .broadcast(String.valueOf(event.getPayload()), event.getRoom(), event.getSource());
            //case FILE_RECEIVED -> serverWorkers.broadcastFile(
                    //tu odbić plik, jako byte[]?
              //      event.getFile().get());
            case CONNECTION_CLOSED -> serverWorkers.remove(event.getSource());
        }
    }

}
