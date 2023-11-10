package com.example.nushack23.Controller;

import com.example.nushack23.Model.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatGroupController {
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
    private TextField messageField;
    @FXML
    private VBox chatPane;
    @FXML
    public void initialize() {
        Variable.chatGroupController = this;
    }

    @FXML
    public void send() {
// TODO: save message to database and update for every receiver and the sender itself
        String text = messageField.getText();
        System.out.println(text);

        if (!text.isEmpty()){
            //display newly sent message
            MessagePane sentMessage = new MessagePane(text);
            chatPane.getChildren().add(sentMessage);
            chatPane.setAlignment(Pos.BOTTOM_RIGHT);
            chatPane.setPadding(new Insets(10, 10, 10, 10));
        }

        // save message to database
        Chat chat = Database.getChatByName(groupChatName.getText());
        Message message = new Message(groupChatName.getText(), chat.getMessageCount(), Variable.currentAccount.getUsername(), text, new Date(), -1);
        chat.addMessage(message);
        chat.saveMessageCount();
        message.save();

        //send message to kafka
        /*

         */


        messageField.clear();
    }

    public void receive(String chatGroupName, Message msg) {
        //display message
        // TODO: add message to chat (chat.addmessage) and display message
    }

    public class MessageReceiver implements Runnable {
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private final KafkaConsumer<String, String> consumer;
        private ChatGroupController controller;
        public MessageReceiver(KafkaConsumer<String, String> consumer, ChatGroupController controller) {
            this.consumer = consumer;
            this.controller = controller;
        }

        @Override
        public void run() {

            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    for (ConsumerRecord<String, String> record : records) {
                        //chat group name (topic) and message (value)
//                        controller.receive(groupChatName.getText(), record.value());
                    }
                }

            } catch (WakeupException e) {
                if (!closed.get()) {
                    throw e;
                }
            } finally {
                consumer.close();
            }
        }

        public void shutdown() {
            closed.set(true);
            consumer.wakeup();
        }
    }

    public void displaySentMsg() {

    }

    public void displayReceivedMsg() {

    }

}
