package com.example.nushack23.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.nushack23.Model.ChatRecommender.*;

public class Account {
    private String username, pwdHash, description;

    public String getUsername() {
        return username;
    }
    public String getDescription() { return description; }

    public void setDescription(String new_desc) { this.description = new_desc; }

    public boolean correctPwdHash(String pwdHash) {
        return (this.pwdHash.equals(pwdHash));
    }


    private ArrayList<Chat> chats;
    private ArrayList<Double> affinities;

    public ArrayList<Chat> getChats() { return this.chats; }
    public ArrayList<Double> getAffinities() { return this.affinities; }


    public Account(String username, String pwdHash, String description, ArrayList<Chat> chats, ArrayList<Double> affinities) {
        this.username = username;
        this.pwdHash = pwdHash;
        this.description = description;
        this.chats = chats;
        this.affinities = affinities;
    }

    public static Account loadAccount(String username) {
        if (!Database.connected) Database.connect_to_db();

        String pwdHash = Database.jedis.get(username+"_password_hash");
        String desc = Database.jedis.get(username+"_description");

        List<String> chatNames = Database.jedis.lrange(username+"_chats", 0, -1);
        ArrayList<Chat> chats = new ArrayList<>();
        for (String chatName: chatNames) {
            chats.add(Database.getChatByName(chatName));
        }

        ArrayList<Double> affinities = new ArrayList<>();
        for (String s: Database.jedis.lrange(username+"_affinities", 0, -1)) {
            affinities.add(Double.parseDouble(s));
        }

        return new Account(username, pwdHash, desc, chats, affinities);
    }

    public void save() {
        if (!Database.connected) Database.connect_to_db();

        Database.jedis.set(this.username+"_password_hash", this.pwdHash);
        Database.jedis.set(this.username+"_description", this.description);

        ArrayList<String> chatNames = new ArrayList<>();
        for (Chat c: this.chats) chatNames.add(c.getChatName());
        Database.setJedisList(this.username+"_chats", chatNames);

        ArrayList<String> affinities_string = new ArrayList<>();
        for (double d: affinities) affinities_string.add(String.valueOf(d));
        Database.setJedisList(this.username+"_affinities", affinities_string);

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
        accountString.append("ACCOUNT AFFINITIES: ");
        for (double d: affinities) {
            accountString.append(d);
            accountString.append(", ");
        }
        accountString.append("\n");
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

<<<<<<< Updated upstream
    // 602fae580f5863abfb31bf356decfdc73bf1224a62aacf6b4829ffa2dbe58390

}
=======

    public ArrayList<Double> getScores() {
        // compute compability with each chat
        ArrayList<Double> compatibilities = new ArrayList<>();
        for (Chat c: Database.all_chats) {
            compatibilities.add(c.compatibilityWith(this.description));
        }

        // compute highest chat similarities to current chats
        ArrayList<Double> similarities = new ArrayList<>();
        for (Chat c1: chats) {
            double highest_similarity = 0;
            for (Chat c2: Database.all_chats) {
                highest_similarity = Math.max(highest_similarity, c1.similarityTo(c2));
            }
            similarities.add(highest_similarity);
        }

        // compute scores by weighted average
        ArrayList<Double> scores = new ArrayList<>();
        for (int i=0; i<Database.all_chats.size(); i++) {
            scores.add((affinity_weight*affinities.get(i) +
                    compatibility_weight*compatibilities.get(i) +
                    similarity_weight*similarities.get(i) ) /
                    (affinity_weight + compatibility_weight + similarity_weight) );
        }

        return scores;
    }
}
>>>>>>> Stashed changes
