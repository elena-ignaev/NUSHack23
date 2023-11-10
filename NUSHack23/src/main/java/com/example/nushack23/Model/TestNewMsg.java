package com.example.nushack23.Model;

import com.example.nushack23.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TestNewMsg extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MsgPane pane = new MsgPane("hello!");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(pane);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
