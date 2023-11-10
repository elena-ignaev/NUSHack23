package com.example.nushack23.Model;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MsgPane extends Pane {
    private Message message;
    public Message getMessage() {
        return this.message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
    public MsgPane(String msg) {
        paint(msg);
    }
    public void paint(String msg) {
        Rectangle frame = new Rectangle(160,60);
        frame.setFill(Color.WHITE);
        frame.setStroke(Color.CORNFLOWERBLUE);

        Label label = new Label(msg);

        frame.heightProperty().bind(label.heightProperty().add(20));
        frame.widthProperty().bind(label.widthProperty().add(20));



        getChildren().addAll(frame, label);

    }

}
