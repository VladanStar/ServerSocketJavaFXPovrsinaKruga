package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {
    // IO Streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;


    @Override // Redefinisanje metoda start() u klasi Application
    public void start(Stage primaryStage) throws Exception{

        // Pane pane za natpis i tekstualno slovo
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter A Radius: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        // Tekstualno polje za prikaz sadrzaja
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        // Kreiranje scene i njeno postavljanje
        Scene scene = new Scene(mainPane,500,500);
        primaryStage.setTitle("JavaFX Socket");
        primaryStage.setScene(scene);
        primaryStage.show();

        tf.setOnAction(e->{
            try{
                // Dobijanje poluprecnika iz tekstualnog polja
                double radius = Double.parseDouble(tf.getText().trim());
                // Slanje poluprecnika serveru
                toServer.writeDouble(radius);
                toServer.flush();
                // Dobijanje povrsine kruga sa servera
                double area = fromServer.readDouble();

                // Prikaz tekstualnog polja
                ta.appendText("Radius is: " + radius + "\n");
                ta.appendText("Area received from Server is: " +area + " \n");

            }
            catch (IOException ex){
                ex.printStackTrace();
            }
                });
        // Kreiranje uticnice sa serverom
        try{
            Socket socket = new Socket("localhost",8000);
            //Kreiranje ulaznog toka podataka
            fromServer =new DataInputStream(socket.getInputStream());
        // Kreiranje izlaznog toka podaaka
            toServer = new DataOutputStream(socket.getOutputStream());


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
