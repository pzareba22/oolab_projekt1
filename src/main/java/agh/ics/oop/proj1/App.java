package agh.ics.oop.proj1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    public static void main(String[] args){
//        System.out.println("Hello world");
        launch(args);
    }


    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SetupScreen.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
/*
    1. pokazać ekran z ustawieniami symulacji - DONE
    2. po wciśnięciu guzika na ekranie rozpocząć symulację
    3. w trakcie symulacji pokazywać wykresy
    4. po symulacji pokazać ekran z podsumowaniem
 */

        primaryStage.setTitle("Simulation");
        primaryStage.show();

    }
}
