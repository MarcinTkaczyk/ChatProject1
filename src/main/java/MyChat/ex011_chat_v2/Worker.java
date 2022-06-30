package MyChat.ex011_chat_v2;

import MyChat.ex011_chat_v2.commons.FileWriter;
import MyChat.ex011_chat_v2.commons.TextReader;
import MyChat.ex011_chat_v2.commons.TextWriter;
import MyChat.ex011_chat_v2.logger.HistoryLogger;

import java.net.Socket;

import static MyChat.ex011_chat_v2.ServerEventType.*;

class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;
    private final FileWriter fileWriter;
    private final HistoryLogger historyLogger;
    private ChatRoom currentRoom;
    private final String ROOMCHANGEPHRASE = "ROOM>";
    private final String NAMESETPHRASE = "SETNAME>";
    private final String HISTORYREQUESTPHRASE = "SHOWHISTORY>";
    ChatRoomManager chatRoomManager;
    private String user;

    Worker(Socket socket, EventsBus eventsBus, ChatRoomManager chatRoomManager, HistoryLogger historyLogger) {
        this.socket = socket;
        this.eventsBus = eventsBus;
        writer = new TextWriter(socket);
        fileWriter = new FileWriter(socket);
        this.chatRoomManager = chatRoomManager;
        currentRoom = chatRoomManager.getDefaultRoom();
        this.historyLogger = historyLogger;
    }

    @Override
    public void run() {
        //to get from client console

        historyLogger.registerRoom(currentRoom);
        currentRoom.addUser(user);
        new TextReader(socket, this::onText, this::onInputClose).read();


    }


    private void onText(String text) {
        if (text.contains(NAMESETPHRASE)){
            user = text.substring(NAMESETPHRASE.length());
            return;
        }

        if (text.contains(ROOMCHANGEPHRASE)) {
            String choosenRoom = text.substring(ROOMCHANGEPHRASE.length());
            changeRoom(choosenRoom);
        }
        if (text.contains(HISTORYREQUESTPHRASE)) {
            String choosenRoom = text.substring(HISTORYREQUESTPHRASE.length());
            historyLogger.chatHistory(user, choosenRoom);


        } else {
            String payload = user + ": " + text;
            historyLogger.log(currentRoom, user, payload);
            eventsBus.publish(ServerEvent.builder()
                    .type(MESSAGE_RECEIVED)
                    .payload(payload)
                    .source(this)
                    .room(currentRoom)
                    .build());
        }
    }
    //to be moved to room manager
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
        writer.write("You are now in the room : " + currentRoom.getRoomName());



        //currentRoom.addUser();
    }

    private void onInputClose() {

        eventsBus.publish(ServerEvent.builder()
                .type(CONNECTION_CLOSED)
                .source(this)
                .build());
    }

    private void onFile(Byte[] file){
        eventsBus.publish(ServerEvent.builder()
                .type(FILE_RECEIVED)
                .source(this)
                .build());
    }

    void send(String text) {
        writer.write(text);
    }

    ChatRoom getWorkerRoom(){
        return currentRoom;
    }

    void sendFile(Byte[] file) {}

}
