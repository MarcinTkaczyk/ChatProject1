package MyChat.server;

import MyChat.messages.Message;

interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    void broadcast(Message message, ChatRoom roomToBroadcast, Worker sender);
}
