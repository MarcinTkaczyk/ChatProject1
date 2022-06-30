package MyChat.ex011_chat_v2;

import java.io.File;

interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    void broadcast(String text,  ChatRoom roomToBroadcast, Worker sender);

    //void broadcastFile(Byte[] file);

}
