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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class LoginController {

    private boolean onLogin = true;

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
    private VBox loginVBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        Database.loadDatabase();
    }

    @FXML
    void createNewAcc(ActionEvent event) {

        if (onLogin) {

            onLogin = false;

            if (usernameTextField.getText().isBlank()) usernameTextField.clear();
            if (passwordField.getText().isBlank()) passwordField.clear();
            loginButton.setVisible(false);
            descriptionTextArea.clear();
            descriptionTextArea.setVisible(true);
            backButton.setVisible(true);

        }

        else {
            try {
                String user = usernameTextField.getText().trim();
                String desc = descriptionTextArea.getText().trim();
                if (Database.accountExists(user)) throw new IllegalStateException();
                if (Account.validateUsername(user)) throw new IllegalArgumentException();
                if (!((user.length() >= 3) && (user.length() <= 16))) throw new IllegalArgumentException();
                if (desc.length() < 50) throw new InputMismatchException();
                Database.makeAccount(user, passwordField.getText(), descriptionTextArea.getText());

                login(event);

            }
            catch(IllegalStateException ex) {
                Utility.showPopup(usernameTextField, "Username is taken.");
            }
            catch(InputMismatchException ex) {
                Utility.showPopup(descriptionTextArea, "Description must contain at least 50 characters.");
            }
            catch(IllegalArgumentException ex) {
                Utility.showPopup(usernameTextField, "Invalid username (Must contain 3-16 characters, no underscores");
            }

            catch(NullPointerException | NoSuchAlgorithmException nex) {
                Utility.showPopup(loginButton, "Password hash algorithm could not be accessed.");
            }
            catch(Exception ex) {
                backToLogin(new ActionEvent());
                Utility.showPopup(createNewAccButton, "Account could not be created.");
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void login(ActionEvent event) {
        try {
            Variable.currentAccount = Database.getAccount(usernameTextField.getText().trim(), passwordField.getText().trim());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-group.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) ((Button) (event.getSource())).getScene().getWindow()).setScene(scene);
        }
        catch(NullPointerException | NoSuchAlgorithmException nex) {
            Utility.showPopup(loginButton, "Password hash algorithm could not be accessed.");
        }
        catch(NoSuchElementException nex) {
            Utility.showPopup(loginButton, "Invalid username/password.");
        }
        catch(IOException ioex) {
            Utility.showPopup(loginButton, "Home page could not be loaded.");
            ioex.printStackTrace();
        }

    }

    @FXML
    public void backToLogin(ActionEvent event) {
        onLogin = true;

        if (usernameTextField.getText().isBlank()) usernameTextField.clear();
        if (passwordField.getText().isBlank()) passwordField.clear();

        loginButton.setVisible(true);
        descriptionTextArea.setVisible(false);
        descriptionTextArea.clear();
        backButton.setVisible(false);
    }
}
