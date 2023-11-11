package com.example.nushack23.Model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GroupChatRep extends StackPane {
    /*
    public GroupChatRep(String chatName, String latestMessage) {
        paint(chatName, latestMessage);
    }
    */

    private HBox contentBox;
    public Button btn;

    public Chat chat;


    public GroupChatRep(Chat chat, boolean isSelected) {
        this.chat = chat;
        this.contentBox = new HBox();
        paintBox(chat);

        this.btn = new Button();
        btn.setPrefHeight(85);
        btn.setPrefWidth(271);
        btn.setPadding(new Insets(0, 5, 5, 5));
        this.btn.setStyle("-fx-background-color: transparent;");

        setSelected(isSelected);

        this.getChildren().add(contentBox);
        this.getChildren().add(btn);
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            this.contentBox.setStyle("-fx-background-color: #808080; -fx-background-radius: 2em;");
        } else {
            this.contentBox.setStyle("-fx-background-color: #aaaaaa; -fx-background-radius: 2em;");
        }
    }

    public void paintBox(Chat chat) {
        String chatName = chat.getChatName();
        String latest = chat.getMessages().get(chat.getMessageCount()-1).getContent();
        this.contentBox.setMaxHeight(85);
        this.contentBox.setMaxWidth(271);
        this.contentBox.setPadding(new Insets(0,10,0,5));
        this.contentBox.setAlignment(Pos.CENTER_LEFT);
        this.contentBox.setSpacing(10);

        ImageView groupChatPfp = new ImageView(new Image("file:defaultpfp.png"));
        groupChatPfp.setPreserveRatio(true);
        groupChatPfp.setFitWidth(52);
        groupChatPfp.setFitHeight(58);
        Label groupChatName = new Label(chatName);
        groupChatName.setStyle("-fx-font-size: 16px");
        Label latestMessage = new Label(latest);
        latestMessage.setStyle("-fx-font-size: 14px");

        VBox chatTextBox = new VBox(groupChatName, latestMessage);
        chatTextBox.setAlignment(Pos.CENTER_LEFT);
        chatTextBox.setSpacing(6);

        this.contentBox.getChildren().addAll(groupChatPfp, chatTextBox);
        //setStyle("-fx-background-color: #aaaaaa; -fx-background-radius: 5em;");
    }
}
