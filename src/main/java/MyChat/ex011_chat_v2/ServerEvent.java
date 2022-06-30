package MyChat.ex011_chat_v2;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerEvent{

    private final ServerEventType type;
    private Worker source;
    String payload;
    ChatRoom room;
    }

