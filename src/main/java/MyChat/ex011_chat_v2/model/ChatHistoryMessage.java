package MyChat.ex011_chat_v2.model;

import MyChat.ex011_chat_v2.ChatLog;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public class ChatHistoryMessage implements Serializable {
    private List<ChatLog> chatLogs;
}
