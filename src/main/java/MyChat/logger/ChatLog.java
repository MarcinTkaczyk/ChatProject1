package MyChat.logger;


import MyChat.server.ChatRoom;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Getter
@Setter
public class ChatLog implements Serializable {
    private Set<String> users;
    private final String room;
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
