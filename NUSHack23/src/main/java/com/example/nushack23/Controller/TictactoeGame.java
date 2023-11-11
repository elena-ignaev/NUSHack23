package com.example.nushack23.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class TictactoeGame {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button square1;
    @FXML
    private Button square2;
    @FXML
    private Button square3;
    @FXML
    private Button square4;
    @FXML
    private Button square5;
    @FXML
    private Button square6;
    @FXML
    private Button square7;
    @FXML
    private Button square8;
    @FXML
    private Button square9;
    private boolean player1 = true;
    private ArrayList<Button> buttons;
    @FXML
    private Label turn;
    private int count = 0;
    @FXML
    public void initialize() {
        player1 = false;
        buttons = new ArrayList<>(9);
        buttons.add(square1);
        buttons.add(square2);
        buttons.add(square3);
        buttons.add(square4);
        buttons.add(square5);
        buttons.add(square6);
        buttons.add(square7);
        buttons.add(square8);
        buttons.add(square9);

        for (Button button : buttons) {
            ImageView blahaj = new ImageView(new Image("file:blahaj.png"));
            blahaj.setFitHeight(192);
            blahaj.setFitWidth(192);
            ImageView rooster = new ImageView(new Image("file:rooster.png"));
            rooster.setFitWidth(192);
            rooster.setFitHeight(192);
            button.setOnAction(e -> {
                if (checkWinner()) {
                    turn.setText("We have a winner!");
                    for (Button button1 : buttons) {
                        button1.setDisable(true);
                    }
                } else {
                    button.setDisable(true);
                    count++;
                    player1 = !player1;
                    if (player1) {
                        button.setGraphic(blahaj);
                        turn.setText("It is player 2's turn (Rooster!)");
                        button.setId("Blahaj");
                    } else {
                        button.setGraphic(rooster);
                        turn.setText("It is player 1's turn (Blahaj!)");
                        button.setId("Rooster");
                    }
                    if (checkWinner()) {
                        turn.setText("We have a winner!");
                        for (Button button1 : buttons) {
                            button1.setDisable(true);
                        }
                    } else if (count == 9) {
                        turn.setText("It is a draw!");
                    }
                }


            });
        }
    }

    public boolean checkWinner() {
        return (buttons.get(0).getId().equals(buttons.get(1).getId()) && buttons.get(1).getId().equals(buttons.get(2).getId())) ||
                (buttons.get(3).getId().equals(buttons.get(4).getId()) && buttons.get(4).getId().equals(buttons.get(5).getId())) ||
                (buttons.get(6).getId().equals(buttons.get(7).getId()) && buttons.get(7).getId().equals(buttons.get(8).getId())) ||
                (buttons.get(0).getId().equals(buttons.get(3).getId()) && buttons.get(3).getId().equals(buttons.get(6).getId())) ||
                (buttons.get(1).getId().equals(buttons.get(4).getId()) && buttons.get(4).getId().equals(buttons.get(7).getId())) ||
                (buttons.get(2).getId().equals(buttons.get(5).getId()) && buttons.get(5).getId().equals(buttons.get(8).getId())) ||
                (buttons.get(0).getId().equals(buttons.get(4).getId()) && buttons.get(4).getId().equals(buttons.get(8).getId())) ||
                (buttons.get(2).getId().equals(buttons.get(4).getId()) && buttons.get(4).getId().equals(buttons.get(6).getId()));
    }

}
