package MyChat.client;

import MyChat.messages.ChatHistoryMessage;
import MyChat.messages.FileMessage;
import MyChat.messages.TextMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientUtils {

    public static void displayClientMessage(TextMessage message){
        System.out.println(message.getAuthor() + ": " + message.getText());
    }

    public static void displayClientHistory(ChatHistoryMessage message) {
        message.getChatLogs().stream().forEach(System.out::println);
    }

    public static void downloadFile(FileMessage message, File downloadLocation) {
        String fileName = message.getName();
        System.out.println("Downloading file :" +fileName+ " from: " + message.getSource());

        String destination = downloadLocation.getAbsolutePath()+File.separator+fileName;
        try(FileOutputStream fos = new FileOutputStream(destination)){

            fos.write(message.getFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showMenu(){
        System.out.println("You are in the GENERAL room \n" +
                "Type: \n" +
                "ROOM> to change room, if room does not exist it will be crated, example: ROOM>new \n" +
                "HISTORY> to display room history in which you chatted, example HISTORY>GENERAL \n" +
                "FILE> to send file to everyone in the room, please provide absolute path after>, example: FILE>/Users/user/file.txt \n" +
                "or just type to chat in the room");
    }
}
