package com.example.nushack23.Controller;

import com.example.nushack23.HelloApplication;
import com.example.nushack23.Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;


import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatGroupController {
    @FXML
    private Button send;
    @FXML
    private Button moreFunctions;
    @FXML
    private Button groupChatName;
    @FXML
    private Button memberNamesBtn;
    @FXML
    private ImageView groupChatPfp;
    @FXML
    private TextField messageField;
    @FXML
    private VBox chatPane;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchGroupField;

    private Properties consumer_props, producer_props;
    private KafkaConsumer<String, String> consumer;
    private KafkaProducer<String, String> producer;
    private MessageReceiver receiver;

    @FXML
    private VBox chatsBox;

    @FXML
    private Button logOutButton;

    @FXML
    private HBox chatInfoHBox, chatOptionsHBox;

    public void logOut(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) ((Button) (event.getSource())).getScene().getWindow()).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        Variable.chatGroupController = this;

        // get password

        File kafka_auth_file = new File(System.getProperty("user.dir")+"/kafka_auth.txt");
        String password = "";
        try {
            Scanner kafka_auth_in = new Scanner(kafka_auth_file);
            password = kafka_auth_in.nextLine().trim();
            kafka_auth_in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        consumer_props = new Properties();
        consumer_props.put("bootstrap.servers", "mature-boa-9431-eu2-kafka.upstash.io:9092");
        consumer_props.put("sasl.mechanism", "SCRAM-SHA-256");
        consumer_props.put("security.protocol", "SASL_SSL");
        consumer_props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bWF0dXJlLWJvYS05NDMxJFfoq3WcuAPA3vTDAryHiJmOHFFK5ZmAZ--cGMYnlsM\" password=\""+password+"\";");
        consumer_props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer_props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer_props.put("auto.offset.reset", "earliest");
        consumer_props.put("group.id", "NUSHHACK2023Cluster");

        producer_props = new Properties();
        producer_props.put("bootstrap.servers", "mature-boa-9431-eu2-kafka.upstash.io:9092");
        producer_props.put("sasl.mechanism", "SCRAM-SHA-256");
        producer_props.put("security.protocol", "SASL_SSL");
        producer_props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bWF0dXJlLWJvYS05NDMxJFfoq3WcuAPA3vTDAryHiJmOHFFK5ZmAZ--cGMYnlsM\" password=\""+password+"\";");
        producer_props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer_props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // make and run consumer

        consumer = new KafkaConsumer<String, String>(consumer_props);
        consumer.subscribe(List.of("Refreshes"));
        receiver = new MessageReceiver(consumer, this);
        new Thread(receiver).start();
        System.out.println("Receiver: "+receiver);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshChats();
            }
        });

        chatInfoHBox.setVisible(false);
        chatPane.setVisible(false);
        groupChatPfp.setVisible(false);
        groupChatName.setVisible(false);
        memberNamesBtn.setVisible(false);
        chatOptionsHBox.setVisible(false);
    }

    private int selectedIdx = -1;

    public void refreshChats() {
        chatsBox.getChildren().clear();
        int i = 0;
        // add current chats
        if (Variable.currentAccount.getChats() != null){
            ArrayList<Chat> chats = Variable.currentAccount.getChats();
            for (Chat chat : chats) {
                if (!chat.getChatName().contains(searchGroupField.getText())) continue;
                GroupChatRep gcr = new GroupChatRep(chat, i==selectedIdx);
                chatsBox.getChildren().add(gcr);
                int finalI = i;
                gcr.btn.setOnAction( (ActionEvent e) -> select(finalI) );
                i++;
            }
        } else {
            Label noChats = new Label("You are currently in new chat. Join the below suggested group chats.");
            chatsBox.getChildren().add(noChats);
        }

        // add dividing line
        Line line = new Line(0, 0, 250, 0);
        VBox.setMargin(line, new Insets(5, 20, 5, 20));
        line.setStrokeWidth(5);
        chatsBox.getChildren().add(line);
        i++;

        // add chats not in yet, in order of recommendation
        for (Chat c: ChatRecommender.getDisplayOrder(Variable.currentAccount)) {
            if (!c.getChatName().contains(searchGroupField.getText())) continue;
            GroupChatRep gcr = new GroupChatRep(c, i==selectedIdx);
            chatsBox.getChildren().add(gcr);
            int finalI = i;
            gcr.btn.setOnAction( (ActionEvent e) -> select(finalI) );
            i++;
        }
    }

    public void select(int idx) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!scrollPane.isVisible()) scrollPane.setVisible(true);
                scrollPane.setFitToWidth(true);
                selectedIdx = idx;
                updateSelecteds();
                Chat currChat = ((GroupChatRep)chatsBox.getChildren().get(selectedIdx)).chat;
                Variable.currentChat = currChat;
                updateChatArea(Variable.currentAccount.getChats().contains(currChat));
            }
        });
    }

    private void updateSelecteds() {
        for (int i=0; i<chatsBox.getChildren().size(); i++) {
            if (chatsBox.getChildren().get(i) instanceof GroupChatRep) {
                ((GroupChatRep)chatsBox.getChildren().get(i)).setSelected(i==selectedIdx);
            }
        }
    }


    private void updateChatArea(boolean alreadyIn) {
        if (alreadyIn) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatInfoHBox.setVisible(true);
                    chatPane.setVisible(true);
                    groupChatPfp.setVisible(true);
                    groupChatName.setVisible(true);
                    memberNamesBtn.setVisible(true);
                    groupChatName.setText(Variable.currentChat.getChatName());
                    memberNamesBtn.setText(Variable.currentChat.getMemberNames());
                    showChatHistory();
                    chatOptionsHBox.setVisible(true);
                }
            });
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatInfoHBox.setVisible(false);
                    chatPane.setVisible(false);
                    groupChatPfp.setVisible(false);
                    groupChatName.setVisible(false);
                    memberNamesBtn.setVisible(false);
                    chatOptionsHBox.setVisible(false);
                }
            });


            Label joinGroupLabel = new Label("Join group?");
            Button joinGroupBtn = new Button("Yes");
            joinGroupBtn.setOnAction(this::joinCurrentGroup);
            HBox hbox = new HBox(joinGroupLabel, joinGroupBtn);
            hbox.setSpacing(5);
            hbox.setPrefHeight(10);
            Utility.showPopup(chatsBox.getChildren().get(selectedIdx), hbox);
        }
    }

    public void joinCurrentGroup(ActionEvent event) {
        Variable.currentAccount.addChat(Variable.currentChat);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshChats();
            }
        });

        updateChatArea(true);
    }


    public void showChatHistory() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatPane.getChildren().clear();
                for (Message m: Variable.currentChat.getMessages()) {
                    MessagePane sentMessage = new MessagePane(m.getSender_name(), m.getContent(), m.getSender_name().equals(Variable.currentAccount.getUsername()));
                    chatPane.getChildren().add(sentMessage);
                }
            }
        });
    }

    @FXML
    public void updateSearch(KeyEvent event) {
        // TODO: UPDATE WHICH CHATS ARE SHOWN IN SEARCH
        System.out.println(searchGroupField.getText());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshChats();
            }
        });
    }


    @FXML
    public void send() {
    // TODO: save message to database and update for every receiver and the sender itself
        String text = messageField.getText();
        System.out.println(text);

        if (!text.isEmpty()){
            // save message to database
            Chat chat = Database.getChatByName(groupChatName.getText());
            Message message = new Message(groupChatName.getText(), chat.getMessageCount(), Variable.currentAccount.getUsername(), text, new Date(), -1);
            chat.addMessage(message);
            chat.saveMessageCount();
            message.save();

            //send message to kafka
            producer = new KafkaProducer<>(producer_props);
            producer.send(new ProducerRecord<>("Refreshes", groupChatName.getText()));
            producer.flush();
            producer.close();

            System.out.println("SENT");

            //display newly sent message
            MessagePane sentMessage = new MessagePane(Variable.currentAccount.getUsername(), text, true);
            chatPane.getChildren().add(sentMessage);
            chatPane.setAlignment(Pos.BOTTOM_RIGHT);
            chatPane.setPadding(new Insets(10, 10, 10, 10));
        }
        messageField.clear();

    }

    public void receive(String chatName) {
        System.out.println("RECEIVED ORDER TO RELOAD "+chatName);
        Database.getChatByName(chatName).reload();
        System.out.println(Database.getChatByName(chatName));
        if (Variable.currentChat.getChatName().equals(chatName)) {
            showChatHistory();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshChats();
            }
        });
    }

    class MessageReceiver implements Runnable {
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
                        System.out.println("RECEIVED");
                        //chat group name (topic) and message (value)
//                        controller.receive(groupChatName.getText(), record.value());
                        controller.receive(record.value());
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

    @FXML
    public void addFunctions() {

    }


    @FXML
    public void showGroupChatName(ActionEvent e) {
        Utility.showPopup(groupChatName, Variable.currentChat.getChatName());
    }

    @FXML
    public void showMemberNames(ActionEvent e) {
        TextArea temp = new TextArea(Variable.currentChat.getMemberNames());
        temp.setEditable(false);
        temp.setWrapText(true);
        Utility.showPopup(memberNamesBtn, temp);
    }

    @FXML
    public void showDescription(ActionEvent e) {
        Utility.showPopup((Node)e.getSource(), Variable.currentChat.getDesc());
    }
}
