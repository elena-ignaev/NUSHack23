package com.example.nushack23.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account {
    private String username, pwdHash;

    public String getUsername() {
        return username;
    }
    public String getPwdHash() { return pwdHash; }


    private ArrayList<Chat> chats = new ArrayList<>();


    public Account(String username, String pwdHash, ArrayList<Chat> chats) {
        this.username = username;
        this.pwdHash = pwdHash;
        this.chats = chats;
    }

    public static Account loadAccount(String username) {
        if (!Database.connected) Database.connect_to_db();

        String pwdHash = Database.jedis.get(username+"_password_hash");
        List<String> chatNames = Database.jedis.lrange(username+"_chats", 0, -1);
        ArrayList<Chat> chats = new ArrayList<>();
        for (String chatName: chatNames) {
            chats.add(Database.getChatByName(chatName));
        }

        return new Account(username, pwdHash, chats);
    }

    public void save() {
        if (!Database.connected) Database.connect_to_db();

        Database.jedis.set(this.username+"_password_hash", this.pwdHash);
        ArrayList<String> chatNames = new ArrayList<>();
        for (Chat c: this.chats) chatNames.add(c.getChatName());
        Database.setJedisList(this.username+"_chats", chatNames);

        // TODO: make it clear that this saves the account information but not to the list of accounts
    }

    @Override
    public String toString() {
        StringBuilder accountString = new StringBuilder();
        accountString.append("Account with username: ").append(username).append(" and chats: \n\n");
        for (Chat c: chats) {
            accountString.append(c.toString());
            accountString.append("\n");
        }
        return accountString.toString();
    }


    // TODO: DO MESSAGES WITH KAFKA
    public void sendMessage() {
        // TODO
    }

    public static String pwdHashFunc(String pwd) {
        String pwdHash = "";
        pwdHash = pwd;
        return pwdHash;
    }

}
