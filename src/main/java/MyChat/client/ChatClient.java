package MyChat.client;

import MyChat.commons.MessageWriter;
import MyChat.commons.PropertiesLoader;
import MyChat.commons.Sockets;
import MyChat.messages.Command;
import MyChat.messages.CommandMessage;
import MyChat.messages.Message;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

@Log
public class ChatClient {

    private static final int DEFAULT_PORT = 8888;

    private final Runnable readFromSocket;
    private final Runnable readFromConsole;
    private final String name;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private File clientOutputFolder;
    private Properties properties;

    public ChatClient(String host, int port, String name) throws IOException {
        socket = new Socket(host, port);
        properties = PropertiesLoader.loadProperties();
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.name = name;
        clientOutputFolder = new File(properties.getProperty("client.downloadLocation"),name);
        if(!clientOutputFolder.exists()){
            clientOutputFolder.mkdir();
        }
        readFromSocket = new ClientSocketReader(socket, clientOutputFolder);
        readFromConsole = new Runnable() {
            @Override
            public void run() {
                new ClientInputReader(System.in, new Consumer<String>() {
                    @Override
                    public void accept(String text) {
                        Message message = new ClientCommandDecoder(name).decodeAndPrepare(text);
                        new MessageWriter(objectOutputStream).write(message);
                    }
                }).read();
            }
        };
    }

    private void start() {
        new MessageWriter(objectOutputStream).write( new CommandMessage.CommandMessageBuilder()
                .command(Command.STARTSESSION)
                .payload(Map.ofEntries(Map.entry("name", name)))
                .build()
        );
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
