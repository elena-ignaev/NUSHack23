package com.example.nushack23.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Account {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwordHashes = new ArrayList<>();
    private String username, pwdHash;

    public String getUsername() {
        return username;
    }
    public String getPwdHash() { return pwdHash; }
    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static Account login(String username, String password) {
        for (int i=0; i<usernames.size(); i++) {
            if (Objects.equals(usernames.get(i), username)) {
                if (Objects.equals(passwordHashes.get(i), pwdHashFunc(password))) {
                    return Account.accounts.get(i);
                }
            }
        }
        return null;
    }


    public static String pwdHashFunc(String pwd) {
        String pwdHash = "";
        pwdHash = pwd;
        return pwdHash;
    }

}
