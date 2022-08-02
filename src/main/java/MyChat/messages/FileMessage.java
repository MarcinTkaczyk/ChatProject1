package MyChat.messages;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class FileMessage extends Message implements Serializable {
    String source;
    String name;
    byte[] file;

    public static class FileMessageBuilder{
        public FileMessageBuilder(){};
    }
}
