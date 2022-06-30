package MyChat.ex011_chat_v2.Client;

import lombok.extern.java.Log;
import MyChat.ex011_chat_v2.commons.Sockets;
import MyChat.ex011_chat_v2.commons.TextReader;
import MyChat.ex011_chat_v2.commons.TextWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

@Log
public class ChatClient {

    private static final int DEFAULT_PORT = 8888;

    private final Runnable readFromSocket;
    private final Runnable readFromConsole;
    private final String name;
    private Socket socket;
    private final String NAMESETPHRASE = "SETNAME>";

    public ChatClient(String host, int port, String name) throws IOException {
        socket = new Socket(host, port);
        this.name = name;
        readFromSocket = () -> new TextReader(socket, System.out::println, () -> Sockets.close(socket)).read();
        readFromConsole = new Runnable() {
            @Override
            public void run() {
                new TextReader(System.in, new Consumer<String>() {
                    @Override
                    public void accept(String text) {
                        new TextWriter(socket).write(text);
                    }
                }).read();
            }
        };
    }

    private void start() {
        new TextWriter(socket).write(NAMESETPHRASE+name); //inform the server about the name
        new Thread(readFromSocket).start();
        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();
    }

    public static void main(String[] args) throws IOException {
        var port = Sockets.parsePort(args[1], DEFAULT_PORT);
        new ChatClient(args[0], port, args[2]).start();
    }

}
