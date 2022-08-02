package MyChat.server;

import MyChat.messages.Message;

import java.util.HashSet;
import java.util.Set;

class HashSetServerWorkers implements ServerWorkers {

    private final Set<Worker> workers = new HashSet<>();

    @Override
    public void add(Worker worker) {
        workers.add(worker);
    }

    @Override
    public void remove(Worker worker) {
        workers.remove(worker);
    }

    @Override
    public void broadcast(Message message, ChatRoom roomToBroadcast, Worker sender) {
        workers.stream().filter(worker -> worker.getWorkerRoom().equals(roomToBroadcast) && !worker.equals(sender))
                .forEach(worker -> worker.send(message));
    }

}
