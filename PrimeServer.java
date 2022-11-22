package com.example.primenumberserver;

import java.io.*;
import java.net.*;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class PrimeServer extends Application {
    ServerSocket serverSocket = null;

    @Override
    public void start(Stage primaryStage) {
        TextArea textArea = new TextArea();

        Scene scene = new Scene(new ScrollPane(textArea), 480, 220);
        primaryStage.setTitle("Prime Number Server");
        textArea.setEditable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


        new Thread( () -> {
            try {
                serverSocket = new ServerSocket(1234);
                textArea.appendText("Connection Established.");
            } catch (IOException ex) {
                System.out.println(ex);
            }

            while (true) {
                Socket clientSocket = null;
                DataInputStream inputFromClient = null;
                DataOutputStream outputToClient = null;

                try {
                    clientSocket = serverSocket.accept();
                    inputFromClient = new DataInputStream(clientSocket.getInputStream());
                    outputToClient = new DataOutputStream(clientSocket.getOutputStream());

                    int number = inputFromClient.readInt();
                    textArea.appendText("\nThe number the client sent to check was: " + number);
                    outputToClient.writeBoolean(isPrime(number));
                    outputToClient.flush();

                } catch (IOException e) {
                    System.out.println(e);

                }
                textArea.appendText("\nConnection Closed.");
            }}).start();
    }

    public static boolean isPrime(int number) {
        boolean primeChecker = false;
        for (int i = 2; i <= number / 2; i++) {
            if (number % i == 0) {
                primeChecker = true;
                break;
            }
        }
        if (!primeChecker) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
