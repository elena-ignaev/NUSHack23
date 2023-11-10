package com.example.nushack23.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private Button createNewAccButton;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView loginPageImageView;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void createNewAcc(ActionEvent event) {
        // TODO: Create new Account object, store in db, then log in
        // create account method in Database will be created
        // return null = error
        // return the Account created = success
    }

    @FXML
    void login(ActionEvent event) {
        // TODO: Log in (check credentials), send to main page
    }
}
