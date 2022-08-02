package MyChat.messages;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class ChatHistoryMessage extends Message implements Serializable {
    private List<String> chatLogs;

    public static class ChatHistoryMessageBuilder{
        public ChatHistoryMessageBuilder(){}
    }

}
