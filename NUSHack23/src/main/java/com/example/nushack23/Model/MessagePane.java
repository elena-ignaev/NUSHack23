package com.example.nushack23.Model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MessagePane extends HBox {
    private Label senderLabel, msgLabel;
    private boolean sent_by_curr_user;
    public MessagePane(String sender_name, String msg, boolean sent_by_curr_user) {
        this.sent_by_curr_user = sent_by_curr_user;
        senderLabel = new Label();
        senderLabel.setFont(new Font("Verdana", 15));
        msgLabel = new Label();
        msgLabel.setFont(new Font("Verdana", 13));
        paintLabels(sender_name, msg);
        if (sent_by_curr_user) {
            setId("sentMessage");
            this.setAlignment(Pos.CENTER_RIGHT);
        } else {
            this.setAlignment(Pos.CENTER_LEFT);
        }
        this.setSpacing(5);
    }
    public void paintLabels(String sender_name, String msg) {
        senderLabel.setText(sender_name);
        msgLabel.setText(msg);
        setPadding(new Insets(5,10,5,10));
    }
}
