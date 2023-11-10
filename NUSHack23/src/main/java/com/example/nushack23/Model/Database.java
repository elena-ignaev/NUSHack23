package com.example.nushack23.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import redis.clients.jedis.Jedis;

public class Database {
    public static ArrayList<Chat> all_chats = new ArrayList<>();

    public static ArrayList<Account> accounts = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();

    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static Account getAccount(String username, String password) throws NullPointerException, NoSuchAlgorithmException, NoSuchElementException { // password is before hash function
        for (int i=0; i<usernames.size(); i++) {
            if (Objects.equals(usernames.get(i), username)) {
                if (accounts.get(i).correctPwdHash(Account.pwdHashFunc(password))) {
                    return accounts.get(i);
                }
            }
        }
        throw new NoSuchElementException();
    }

    public static Account makeAccount(String username, String password, String description) {
        // make account
        ArrayList<Double> affinities = new ArrayList<>();
        for (Chat c: all_chats) affinities.add(0.0);
        Account a = new Account(username, password, description, new ArrayList<>(), affinities);
        accounts.add(a);
        // save to db
        a.save();
        saveAccountNames();
        return a;
    }


    public static Chat createChat(String chatName, String chatDesc) {
        // make chat
        Chat c = new Chat(chatName, chatDesc, new ArrayList<>());
        all_chats.add(c);
        // save to db
        c.save();
        saveChatNames();
        return c;
    }


    public static Chat getChatByName(String chatName) {
        for (Chat c: all_chats) {
            if (c.getChatName().equals(chatName)) {
                return c;
            }
        }
        return null;
    }


    public static boolean connected = false;

    public static Jedis jedis = new Jedis("apn1-assured-koala-34057.upstash.io", 34057, true);
    public static boolean connect_to_db() {
        if (connected) return true;

        try {
            // reading in password from file
            File jedis_auth_file = new File(System.getProperty("user.dir")+"/jedis_auth.txt");
            Scanner jedis_auth = new Scanner(jedis_auth_file);
            String auth = jedis_auth.nextLine().trim();

            if (jedis.auth(auth).equals("OK")) {
                connected = true;
                jedis_auth.close();
                return true;
            } else {
                jedis_auth.close();
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR READING JEDIS AUTH FILE");
            return false;
        }
    }

    /*
    * Syntax of database:
    * Accounts to get list of account names
    * then [account name]_password_hash will have password hash to accounts
    * then [account name]_chats will have list of all chat names it has
    *
    * Chats to get list of all chat names
    * then [chat name]_description will have a chat description
    * then [chat name]_messageCount will have the number of messages
    * then [chat name]_messages_[message id] will have the message details:
    *   [chat name]_messages_[message id]_senderName has name of sender
    *   [chat name]_messages_[message id]_content has content
    *   [chat name]_messages_[message id]_timestampString has timestamp string
    *   [chat name]_messages_[message id]_replyID has ID of message it's replying to
    * */

    // set a list in jedis to a certain set of values, since pushing records adds to index 0.
    // because jedis seems to use a stack data structure
    public static void setJedisList(String path, ArrayList<String> list) {
        int prev_size = (int) jedis.llen(path);

        // make sure size is same first
        if (prev_size > list.size()) {
            jedis.lpop(path, (prev_size - list.size()));
        } else if (prev_size < list.size()) {
            for (int i=0; i<(list.size() - prev_size); i++) {
                jedis.lpush(path, "");
            }
        }

        // set all values to be the new values
        for (int j=0; j<list.size(); j++) {
            jedis.lset(path, j, list.get(j));
        }
    }


    public static void loadDatabase() {
        loadChats();
        loadAccounts();
    }

    public static void loadChats() {
        if (!connected) connect_to_db();
        List<String> chatNames = jedis.lrange("Chats", 0, -1);
        for (String chatName: chatNames) {
            all_chats.add(Chat.loadChat(chatName));
        }
    }

    public static void loadAccounts() {
        if (!connected) connect_to_db();

        List<String> accountNames = jedis.lrange("Accounts", 0, -1);
        for (String accountName: accountNames) {
            Account a = Account.loadAccount(accountName);
            accounts.add(a);
            usernames.add(a.getUsername());
        }
    }

    public static void saveAccountsToDatabase() {
        if (!connected) connect_to_db();
        saveAccountNames();
        saveAccounts();
    }

    public static void saveAccountNames() {
        ArrayList<String> accountNames = new ArrayList<>();
        for (Account a: accounts) accountNames.add(a.getUsername());
        setJedisList("Accounts", accountNames);
    }

    public static void saveAccounts() {
        for (Account a: accounts) {
            a.save();
        }
    }


    public static void saveChatsToDatabase() {
        if (!connected) connect_to_db();
        saveChatNames();
        for (Chat c: all_chats) {
            c.save();
        }
    }

    public static void saveChatNames() {
        if (!connected) connect_to_db();
        ArrayList<String> chatNames = new ArrayList<>();
        for (Chat c: all_chats) chatNames.add(c.getChatName());
        setJedisList("Chats", chatNames);
    }
}
