package com.example.nushack23.Model;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;

public class Utility {
    // showing popups e.g. error popups 
    public static void showPopup(Node node, String text) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Label label = new Label(text);
                    label.setFont(new Font("Verdana", 15));
                    PopOver popover = new PopOver(label);

                    popover.show(node);
                }
            });
        } catch (NullPointerException nex) {
            System.out.println("UNABLE TO SHOW UTILITY POPUP FOR "+text);
        }
    }

    public static void showPopup(Node node, Node content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PopOver popover = new PopOver(content);
                popover.show(node);
            }
        });
    }
}
