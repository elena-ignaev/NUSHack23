package com.example.nushack23.Controller;

import com.example.nushack23.Model.Message;
import com.example.nushack23.Model.Variable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ChatGroup {
    @FXML
    private Button joinGroup;
    @FXML
    private Button send;
    @FXML
    private Button addFunctions;
    @FXML
    private Label groupChatName;
    @FXML
    private Label memberNames;
    @FXML
    private ImageView groupChatPfp;
    @FXML
    public void initialize() {
        Variable.chatGroupController = this;
    }

    public void receiveMsg(Message msg) {
        //display message
    }
}
