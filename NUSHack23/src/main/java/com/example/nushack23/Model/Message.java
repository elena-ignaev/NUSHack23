package com.example.nushack23.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Message {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private String chat_name;
    private int ID;
    private String sender_name;
    private String content;
    private Date timestamp;
    private int replyID; // -1 if not replying to any message

    public static Message loadMessage(String chatName, int id) {
        if (!Database.connected) Database.connect_to_db();
        String path = chatName+"_messages_"+id+"_";
        String message_sender_name = Database.jedis.get(path+"senderName");
        String message_content = Database.jedis.get(path+"content");
        String message_timestamp_string = Database.jedis.get(path+"timestampString");
        int message_reply_ID = Integer.parseInt(Database.jedis.get(path+"replyID"));
        return new Message(chatName, id, message_sender_name, message_content, message_timestamp_string, message_reply_ID);
    }

    public void save() {
        if (!Database.connected) Database.connect_to_db();
        String path = this.chat_name+"_messages_"+this.ID+"_";
        Database.jedis.set(path+"senderName", this.sender_name);
        Database.jedis.set(path+"content", this.content);
        Database.jedis.set(path+"timestampString", this.getTimestampString());
        Database.jedis.set(path+"replyID", String.valueOf(this.replyID));
        // TODO: make sure it's clear that in this, message is saved only but not updated to be of chat
    }

    public Message(String chat_name, int ID, String sender_name, String content, Date timestamp, int replyID) {
        this.chat_name = chat_name;
        this.ID = ID;
        this.sender_name = sender_name;
        this.content = content;
        this.timestamp = timestamp;
        this.replyID = replyID;
    }

    public Message(String chat_name, int ID, String sender_name, String content, String timestampString, int replyID) {
        this.chat_name = chat_name;
        this.ID = ID;
        this.sender_name = sender_name;
        this.content = content;
        try {
            this.timestamp = dateFormat.parse(timestampString);
        } catch (ParseException e) {
            System.out.println("ERROR IN Message.java WHEN PARSING TIMESTAMP OF: "+timestampString);
            throw new RuntimeException(e);
        }
        this.replyID = replyID;
    }

    @Override
    public String toString() {
        return chat_name+"_"+ID+"_"+sender_name+"_"+content+"_"+timestamp+"_"+replyID;
        //return ID+" "+replyID+" [("+sender_name+") at "+getTimestampString()+"]: "+content;
    }

    public Message(String messageString) {
        String[] data = messageString.split("_");
        try {
            this.chat_name = data[0];
            this.ID = Integer.parseInt(data[1]);
            this.sender_name = data[2];
            this.content = data[3];
            this.timestamp = dateFormat.parse(data[4]);
            this.replyID = Integer.parseInt(data[5]);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() {
        return dateFormat.format(this.timestamp);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }
}
