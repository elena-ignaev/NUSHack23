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
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
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

    private Properties consumer_props, producer_props;
    private KafkaConsumer<String, String> consumer;
    private KafkaProducer<String, String> producer;
    private MessageReceiver receiver;

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
    }

    @FXML
    public void send() {
// TODO: save message to database and update for every receiver and the sender itself
        String text = messageField.getText();
        System.out.println(text);

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

        if (!text.isEmpty()){
            //display newly sent message
            MessagePane sentMessage = new MessagePane(text);
            chatPane.getChildren().add(sentMessage);
            chatPane.setAlignment(Pos.BOTTOM_RIGHT);
            chatPane.setPadding(new Insets(10, 10, 10, 10));
        }


        messageField.clear();
    }

    public void receive(String chatName) {
        Database.getChatByName(chatName).reload();
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

    public void displaySentMsg() {

    }

    public void displayReceivedMsg() {

    }

}
