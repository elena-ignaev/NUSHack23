package com.example.nushack23;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("NUSH Hack '23");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("mini_logo.png").toExternalForm()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}