package com.example.nushack23.Controller;

import com.example.nushack23.Model.Message;
import com.example.nushack23.Model.Variable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
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
    private Pane chatPane;
    @FXML
    public void initialize() {
        Variable.chatGroupController = this;
    }

    @FXML
    public void send() {
// TODO: save message to database and update for every receiver and the sender itself
        String message = messageField.getText();
        System.out.println(message);

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
