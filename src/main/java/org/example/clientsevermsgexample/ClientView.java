package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientView {
    @FXML private TextField tf_message;
    @FXML private Button button_send;
    @FXML private VBox vbox_messages;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public void initialize() {
        new Thread(this::startClient).start();

        button_send.setOnAction(event -> {
            try {
                String msg = tf_message.getText();
                dos.writeUTF(msg);
                addMessage("You: " + msg, true);

                tf_message.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startClient() {
        try {
            socket = new Socket("localhost", 6666);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String message = dis.readUTF();
                addMessage("Server: " + message, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String message, boolean isOwnMessage) {
        Platform.runLater(() -> {
            Label msgLabel = new Label(message);
            msgLabel.setWrapText(true);
            msgLabel.setMaxWidth(300);
            msgLabel.setStyle(
                    "-fx-background-color: " + (isOwnMessage ? "#d1c4e9" : "#0a49ac") + ";" +
                            "-fx-padding: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-font-size: 14px;" +
                            "-fx-text-fill: #333;"
            );

            HBox messageBox = new HBox(msgLabel);
            messageBox.setSpacing(10);
            messageBox.setAlignment(isOwnMessage ? Pos.BASELINE_RIGHT : Pos.BASELINE_LEFT);

            vbox_messages.getChildren().add(messageBox);

        });
    }
    }
