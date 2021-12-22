package agh.ics.oop.proj1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class App extends Application {
    public static void main(String[] args){
        System.out.println("Hello world");
        launch(args);
    }


    public void start(Stage primaryStage){
/*
    1. pokazać ekran z ustawieniami symulacji
    2. po wciśnięciu guzika na ekranie rozpocząć symulację
    3. w trakcie symulacji pokazywać wykresy
    4. po symulacji pokazać ekran z podsumowaniem
 */

        primaryStage.setTitle("Simulation");
//        Pokazać nowy ekran
//        Wczytać na nim parametry

        primaryStage.show();






//        primaryStage.setTitle("Henlo");
//        StackPane root = new StackPane();
//        AtomicBoolean red = new AtomicBoolean(false);
//
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(event -> {
//            System.out.println("Hello World!");
//            if(red.get()){
//                btn.setStyle("-fx-background-color: #f00");
//            }else{
//                btn.setStyle("-fx-background-color: #0f0");
//            }
//            red.set(!red.get());
//        });
//
//        root.getChildren().add(btn);
//
//
//        primaryStage.setScene(new Scene(root, 600, 800));
//        primaryStage.show();
    }
}
