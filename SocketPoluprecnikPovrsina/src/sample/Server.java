package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {
    @Override //Redefinisanje metoda start() klase Application
    public void start(Stage primaryStage) throws Exception {
        // Prostor za tekst za prikaz sadrzaja
        TextArea ta = new TextArea();
        // Kreiranje scene i njeno postavljanje na pozornicu
        Scene scene = new Scene(new ScrollPane(ta),450,450);
        primaryStage.setTitle("SERVER SIDE");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try {
                // Kreiranje utičnice servera
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(()
                        -> ta.appendText("Server started at " + new Date() + '\n'));

                // Očekivanje zahteva za poveziivanje
                Socket socket = serverSocket.accept();

                // Kreiranje ulaznog i izlaznog toka podataka
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                while (true) {
                    // Dobijanje poluprečnika od klijenta
                    double radius = inputFromClient.readDouble();

                    // Proračun površine
                    double area = radius * radius * Math.PI;

                    // Slanje površine nazad klijentu
                    outputToClient.writeDouble(area);

                    Platform.runLater(() -> {
                        ta.appendText("Radius received from client: "
                                + radius + '\n');
                        ta.appendText("Area is: " + area + '\n');
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}



