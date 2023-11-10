package com.example.nushack23.Model;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

public class BackupMessagePane extends Button {
    public BackupMessagePane(String msg) {
        paint(msg);
    }
    public void paint(String msg) {
        setText(msg);
        setId("sentMessage");
        setPadding(new Insets(5,10,5,10));
    }
}
