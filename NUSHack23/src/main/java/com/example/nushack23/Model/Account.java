package com.example.nushack23.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String pwdHashFunc(String pwd) throws NullPointerException, NoSuchAlgorithmException {
        String pwdHash = "";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            final String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 602fae580f5863abfb31bf356decfdc73bf1224a62aacf6b4829ffa2dbe58390

}