package MyChat.ex011_chat_v2.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class FileMessage implements Serializable {
    Byte[] file;
}
