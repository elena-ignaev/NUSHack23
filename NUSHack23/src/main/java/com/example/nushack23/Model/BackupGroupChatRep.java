package com.example.nushack23.Model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BackupGroupChatRep extends HBox {
    /*
    public GroupChatRep(String chatName, String latestMessage) {
        paint(chatName, latestMessage);
    }
    */

    public BackupGroupChatRep(Chat chat) {
        paint(chat);
    }

    public void paint(Chat chat) {
        String chatName = chat.getChatName();
        String latest = chat.getMessages().get(chat.getMessageCount()-1).getContent();
        setHeight(85);
        setWidth(289);
        setPadding(new Insets(0,5,5,5));
        ImageView groupChatPfp = new ImageView(new Image("file:defaultpfp.png"));
        groupChatPfp.setPreserveRatio(true);
        groupChatPfp.setFitWidth(52);
        groupChatPfp.setFitHeight(58);
        Label groupChatName = new Label(chatName);
        groupChatName.setStyle("-fx-font-size: 16px");
        Label latestMessage = new Label(latest);
        latestMessage.setStyle("-fx-font-size: 14px");
        getChildren().addAll(groupChatPfp, new VBox(groupChatName, latestMessage));
        //setStyle("-fx-background-color: #aaaaaa; -fx-background-radius: 5em;");
    }
}
