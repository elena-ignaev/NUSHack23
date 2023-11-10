package com.example.nushack23.Model;

import java.util.ArrayList;

public class Chat {
    private String chatName, chatDesc;
    private ArrayList<Message> messages;


    public static Chat loadChat(String chatName) {
        if (!Database.connected) Database.connect_to_db();

        String chatDesc = Database.jedis.get(chatName + "_description");
        ArrayList<Message> chat_messages = new ArrayList<>();

        int messageCount = Integer.parseInt(Database.jedis.get(chatName + "_messageCount"));
        for (int id = 0; id < messageCount; id++) {
            chat_messages.add(Message.loadMessage(chatName, id));
        }

        return new Chat(chatName, chatDesc, chat_messages);
    }

    public void reload() {
        if (!Database.connected) Database.connect_to_db();

        this.chatDesc = Database.jedis.get(this.chatName + "_description");
        this.messages = new ArrayList<>();

        int messageCount = Integer.parseInt(Database.jedis.get(this.chatName + "_messageCount"));
        for (int id = 0; id < messageCount; id++) {
            this.messages.add(Message.loadMessage(chatName, id));
        }
    }

    public void save() {
        if (!Database.connected) Database.connect_to_db();

        Database.jedis.set(chatName + "_description", this.chatDesc);
        Database.jedis.set(chatName + "_messageCount", String.valueOf(this.messages.size()));

        for (Message m : messages) m.save();

        // TODO: make it clear that this saves the message but doesn't ensure chat name is saved to account.
    }


    public Chat(String chatName, String chatDesc, ArrayList<Message> messages) {
        this.chatName = chatName;
        this.chatDesc = chatDesc;
        this.messages = messages;
    }

    public void changeDesc(String new_desc) {
        this.chatDesc = new_desc;
        // TODO: SAVE NEW DESCRIPTION TO DATABASE ALREADY, with Database.jedis
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void sendMessage(Message message) {
        // live sending of message
        messages.add(message);
        // TODO: SEND MESSAGE
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Chat " + chatName + ": ");
        str.append(chatDesc);
        /*
        str.append("\nMessages: \n");
        for (Message m: messages) {
            str.append(m.toString());
            str.append("\n");
        }
        */
        str.append("\n");
        return str.toString();
    }

    public int getMessageCount() {
        return this.messages.size();
    }
    public void saveMessageCount() {
        if (!Database.connected) Database.connect_to_db();
        Database.jedis.set(chatName+"_messageCount", String.valueOf(this.messages.size()));
    }

    public String getChatName() {
        return this.chatName;
    }


    // vectorizer stuff
    //private static Word2Vec w2vModel = WordVectorSerializer.readWord2VecModel("path/to/w2v_model.bin");

    private static double compareTexts(String a, String b) {
        // TODO: CALL PYTHON API
        return 0.0;
    }

    private static double chat_name_weight=0.5, chat_desc_weight=0.1;
    public double similarityTo(Chat chat) {
        // compute similarity between chats using word vectorizer

        // compare chat names
        double chat_name_similarity = compareTexts(this.chatName, chat.chatName);

        // compare chat descriptions
        double chat_desc_similarity = compareTexts(this.chatDesc, chat.chatDesc);

        // weighted average
        return (chat_name_weight*chat_name_similarity + chat_desc_weight*chat_desc_similarity)/(chat_name_weight+chat_desc_weight);
    }

    public double compatibilityWith(String desc) {
        // compute compatibility between person's description and chats
        return Math.min(1.0, compareTexts(this.chatDesc, desc)*2);
    }
}
