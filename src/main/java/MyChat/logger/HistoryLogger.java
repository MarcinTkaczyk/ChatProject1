package MyChat.logger;

import MyChat.server.ChatRoom;

import java.util.List;

public interface HistoryLogger {
    public void log(ChatRoom room, String text, String user);
    public List<String> chatHistory(String user, String room);
    public void registerRoom(ChatRoom room);
    public void registerUser(ChatRoom room, String user);
    public void save(ChatRoom room);
}
