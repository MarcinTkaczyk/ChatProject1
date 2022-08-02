package MyChat.server;

import MyChat.commons.FileWriter;
import MyChat.commons.MessageReader;
import MyChat.commons.MessageWriter;
import MyChat.logger.HistoryLogger;
import MyChat.messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static MyChat.server.ServerEventType.*;

class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final FileWriter fileWriter;
    private final MessageWriter messageWriter;
    private ObjectOutputStream objectOutputStream;
    private final HistoryLogger historyLogger;
    private ChatRoom currentRoom;
    private Boolean close = false;
    private ChatRoomManager chatRoomManager;
    private String user;

    Worker(Socket socket, EventsBus eventsBus, ChatRoomManager chatRoomManager, HistoryLogger historyLogger){

        this.socket = socket;
        this.eventsBus = eventsBus;
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        messageWriter = new MessageWriter(objectOutputStream);
        fileWriter = new FileWriter(socket);
        this.chatRoomManager = chatRoomManager;
        currentRoom = chatRoomManager.getDefaultRoom();
        this.historyLogger = historyLogger;
    }

    @Override
    public void run() {

        new MessageReader(socket, this::onMessage, this::onInputClose).read();
    }
    private void onMessage(Object message){
        if(message instanceof CommandMessage) {
            CommandMessage command = (CommandMessage) message;
            switch (command.getCommand()){
                case STARTSESSION -> {
                    user = command.getPayload().get("name");
                    currentRoom.addUser(user);
                    historyLogger.registerRoom(currentRoom);
                    historyLogger.registerUser(currentRoom, user);
                }
                case ROOMCHOICE -> changeRoom(command.getPayload().get("room"));
                case HISTORYREQUEST -> {
                        var chatLogs = historyLogger.chatHistory(command.getPayload().get("user"),
                        command.getPayload().get("room"));
                        messageWriter.write(new ChatHistoryMessage.ChatHistoryMessageBuilder()
                                .chatLogs(chatLogs).build());
                }
                case CLOSESESSION -> close = true;
            }

        }else if(message instanceof FileMessage){
            FileMessage fileMessage = (FileMessage) message;
            eventsBus.publish(ServerEvent.builder()
                            .type(FILE_RECEIVED)
                            .room(currentRoom)
                            .source(this)
                            .message(fileMessage)
                            .build());

        }else if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            String payload = user + ": " + textMessage.getText();
            historyLogger.log(currentRoom, user, payload);
            eventsBus.publish(ServerEvent.builder()
                    .type(MESSAGE_RECEIVED)
                    .message(textMessage)
                    .source(this)
                    .room(currentRoom)
                    .build());
        }
    }


    private void changeRoom(String room){
        ChatRoom newRoom;
        if(chatRoomManager.chatRoomExists(room)){
            newRoom = chatRoomManager.getRoomByName(room);
        } else {
            newRoom = chatRoomManager.openChatRoom(room);
        }
        chatRoomManager.removeUser(currentRoom, user, currentRoom->historyLogger.save(currentRoom));
        newRoom.addUser(user);
        currentRoom = newRoom;
        historyLogger.registerRoom(currentRoom);
        historyLogger.registerUser(currentRoom, user);
        messageWriter.write(new TextMessage.TextMessageBuilder()
                .text("You are now in the room : " + currentRoom.getRoomName())
                .author("System")
                .build());
    }

    private void onInputClose() {
        chatRoomManager.removeUser(currentRoom, user, currentRoom->historyLogger.save(currentRoom));
        eventsBus.publish(ServerEvent.builder()
                .type(CONNECTION_CLOSED)
                .source(this)
                .build());
    }

    void send(Message message) {
        messageWriter.write(message);
    }

    ChatRoom getWorkerRoom(){
        return currentRoom;
    }
}
