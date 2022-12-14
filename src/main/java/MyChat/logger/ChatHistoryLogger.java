package MyChat.logger;

import MyChat.server.ChatRoom;
import MyChat.utils.FileSaver;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ChatHistoryLogger implements HistoryLogger{
    private static final String FILEPATH = System.getProperty("user.dir");
    private static final String LOGS_DIR = FILEPATH + File.separator + "logs";
    private ChatRoom room;
    private List<String> chatHistory;
    private Map<ChatRoom, ChatLog> chatLogs;

    public ChatHistoryLogger(){
        chatLogs = new HashMap();
    }

    @Override
    public void log(ChatRoom room, String user, String text) {
        chatLogs.get(room).log(text);
    }

    @Override
    public List<String> chatHistory(String user, String room) {
        File directory = new File(LOGS_DIR);
        File fileList[] = directory.listFiles();
        List<ChatLog> archivedChatLogs = new ArrayList<>();
        for(File file : fileList){
            try(FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
                ChatLog log = (ChatLog) objectInputStream.readObject();
                archivedChatLogs.add(log);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<ChatLog>userChatHistory = new ArrayList<>();
        if(!archivedChatLogs.isEmpty()){
            for(ChatLog log : archivedChatLogs){
                if(log.getUsers().contains(user)){
                    if (log.getRoom().equals(room)){
                        userChatHistory.add(log);
                    }
                }
            }
        }

        userChatHistory.sort((log1, log2) -> log1.getCloseDate().compareTo(log2.getCloseDate()));
        List<String> userChatHistoryFormatted = new ArrayList<>();
        for(ChatLog log : userChatHistory){
            userChatHistoryFormatted.add("Closed on " + log.getCloseDate().toString());
            for(String line : log.getMessages()){
                userChatHistoryFormatted.add(line);
            }
        }

        return userChatHistoryFormatted;
    }

    @Override
    public void registerRoom(ChatRoom room) {
        if(!chatLogs.containsKey(room)){
        chatLogs.put(room, new ChatLog(room));
        }
    }

    @Override
    public void registerUser(ChatRoom room, String user) {
        chatLogs.get(room).addUser(user);
    }

    @Override
    public void save(ChatRoom room) {
        ChatLog chatLogToSave = chatLogs.get(room);
        Path logsFolder;
        String newPath = FILEPATH + File.separator + "logs";
        File directory = new File(newPath);
        if(!directory.exists()) {
            try {
                logsFolder = Files.createDirectory(directory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            logsFolder = directory.toPath();
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fileNameDate = dateFormat.format(date);
        chatLogToSave.setCloseDate(date);

        FileSaver.SaveToFile(chatLogs.get(room), logsFolder.toString()+File.separator+fileNameDate);

        chatLogs.remove(room);
        log.info("closing room: "+ room);
    }
}
