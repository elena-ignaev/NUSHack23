package com.example.nushack23.Controller;

import com.example.nushack23.HelloApplication;
import com.example.nushack23.Model.Account;
import com.example.nushack23.Model.Database;
import com.example.nushack23.Model.Utility;
import com.example.nushack23.Model.Variable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

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
        try {
            Variable.currentAccount = Database.getAccount(usernameTextField.getText().trim(), passwordField.getText().trim());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).setScene(scene);
        }
        catch(NullPointerException | NoSuchAlgorithmException nex) {
            Utility.showPopup(loginButton, "Password hash algorithm could not be accessed.");
        }
        catch(NoSuchElementException nex) {
            Utility.showPopup(loginButton, "Invalid username/password.");
        }
        catch(IOException ioex) {
            Utility.showPopup(loginButton, "Home page could not be loaded.");
        }



        // TODO: Log in (check credentials), send to main page
    }
}
