package MyChat.server;

import MyChat.messages.Message;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerEvent{

    private final ServerEventType type;
    private Worker source;
    Message message;
    ChatRoom room;
    }

