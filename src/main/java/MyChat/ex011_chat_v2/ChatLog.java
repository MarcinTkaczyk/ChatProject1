package MyChat.ex011_chat_v2;


import MyChat.ex011_chat_v2.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class ChatLog implements Serializable {
    private final String room;
    private Set<String> users;
    private List<String> messages;
    private Date closeDate;

    public ChatLog(ChatRoom room){
        this.room = room.getRoomName();
        users = new HashSet<>();
        messages = new ArrayList<>();
    }

    public void log(String text){
        messages.add(text);
    }

    public void addUser(String user){
        users.add(user);
    }

    public void  setCloseDate(Date date){
        this.closeDate = date;
    }


}
