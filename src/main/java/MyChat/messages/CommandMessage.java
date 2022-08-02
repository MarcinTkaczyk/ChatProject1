package MyChat.messages;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Builder
@Setter
public class CommandMessage extends Message implements Serializable {
    private final Command command;
    private final Map<String, String> payload;

    public static class CommandMessageBuilder{
        public CommandMessageBuilder(){}
    }
}
