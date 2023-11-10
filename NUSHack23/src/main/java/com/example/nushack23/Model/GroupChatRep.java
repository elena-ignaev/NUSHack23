package com.example.nushack23.Model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GroupChatRep extends HBox {
    public GroupChatRep(String chatName, String latestMessage) {
        paint(chatName, latestMessage);
    }

    public void paint(String chatName, String latest) {
        setHeight(85);
        setWidth(289);
        setPadding(new Insets(0,5,5,5));
        ImageView groupChatPfp = new ImageView(new Image("defaultpfp.png"));
        Label groupChatName = new Label(chatName);
        groupChatName.setStyle("-fx-font-size: 16px");
        Label latestMessage = new Label(latest);
        latestMessage.setStyle("-fx-font-size: 14px");
        getChildren().addAll(groupChatPfp, new VBox(groupChatName, latestMessage));
    }
}
