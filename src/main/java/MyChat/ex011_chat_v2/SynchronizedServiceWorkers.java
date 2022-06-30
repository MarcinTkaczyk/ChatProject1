package MyChat.ex011_chat_v2;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
class SynchronizedServiceWorkers implements ServerWorkers {

    private final ServerWorkers serverWorkers;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void add(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.add(worker);
        lock.writeLock().unlock();
    }

    @Override
    public void remove(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.remove(worker);
        lock.writeLock().unlock();
    }

    @Override
    public void broadcast(String text,ChatRoom roomToBroadcast, Worker sender) {
        lock.readLock().lock();
        serverWorkers.broadcast(text, roomToBroadcast, sender);
        lock.readLock().unlock();
    }

 /*   @Override
    public void broadcastFile(Byte[] file) {
        lock.readLock().lock();
        serverWorkers.broadcastFile(file);
        lock.readLock().unlock();
    }*/

}
