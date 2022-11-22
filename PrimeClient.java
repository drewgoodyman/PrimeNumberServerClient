package com.example.primenumberclient;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimeClient extends Application {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) {
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setLeft(new Label("Enter a number: "));

        TextField textField = new TextField();
        textField.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(textField);

        BorderPane mainPane = new BorderPane();
        TextArea textArea = new TextArea();
        mainPane.setCenter(new ScrollPane(textArea));
        mainPane.setTop(paneForTextField);

        Scene scene = new Scene(mainPane, 480, 220);
        primaryStage.setTitle("Prime Number Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        textArea.setEditable(false);

        try {
            Socket socket = new Socket("localhost", 1234);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            textArea.appendText(ex.toString());
        }

        textField.setOnAction(e -> {
            try {
                int number = Integer.parseInt(textField.getText().trim());
                toServer.writeInt(number);

                boolean answer = fromServer.readBoolean();
                if (answer == true) {
                    textArea.appendText(number + " is a prime number. \nConnection Terminated");
                } else {
                    textArea.appendText(number + " is is not a prime number.\nConnection Terminated");
                }
                toServer.flush();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
