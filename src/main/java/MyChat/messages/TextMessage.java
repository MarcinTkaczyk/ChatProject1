package MyChat.messages;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class TextMessage extends Message implements Serializable {
    private String author;
    private String text;

    public static class TextMessageBuilder {
        public TextMessageBuilder() {
        }
    }
}
