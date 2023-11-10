package com.example.nushack23.Model;

import java.util.ArrayList;

public class Chat {
    private String chatName, chatDesc;
    private ArrayList<Message> messages;


    public static Chat loadChat(String chatName) {
        if (!Database.connected) Database.connect_to_db();

        String chatDesc = Database.jedis.get(chatName+"_description");
        ArrayList<Message> chat_messages = new ArrayList<>();

        int messageCount = Integer.parseInt(Database.jedis.get(chatName+"_messageCount"));
        for (int id=0; id<messageCount; id++) {
            chat_messages.add(Message.loadMessage(chatName, id));
        }

        return new Chat(chatName, chatDesc, chat_messages);
    }

    public void save() {
        if (!Database.connected) Database.connect_to_db();

        Database.jedis.set(chatName+"_description", this.chatDesc);
        Database.jedis.set(chatName+"_messageCount", String.valueOf(this.messages.size()));

        for (Message m: messages) m.save();

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
        str.append("\nMessages: \n");
        for (Message m: messages) {
            str.append(m.toString());
            str.append("\n");
        }
        str.append("\n");
        return str.toString();
    }

    public String getChatName() { return this.chatName; }


    // vectorizer stuff
    //private static Word2Vec w2vModel = WordVectorSerializer.readWord2VecModel("path/to/w2v_model.bin");

    private ArrayList<String> filterText(String text) {
        String[] tokens = text.split("[ ,.;]+");
        // TODO
        return new ArrayList<>();
    }

    public double similarityTo(Chat chat) {
        // compute similarity between chats using word vectorizer

        // compare chat names

        return 0.0;
    }

    public double compatibilityWith(String desc) {
        // compute compatibility between person's description and chats
        return 0.0;
    }
}
