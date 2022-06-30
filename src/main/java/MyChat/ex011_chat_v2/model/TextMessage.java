package MyChat.ex011_chat_v2.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class TextMessage implements Serializable {
    private String text;
}
