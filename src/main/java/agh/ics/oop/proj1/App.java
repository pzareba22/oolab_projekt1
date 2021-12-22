package agh.ics.oop.proj1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args){
        System.out.println("Hello world");
        launch(args);
    }


    public void start(Stage primaryStage){
        primaryStage.setTitle("Henlo");
        StackPane root = new StackPane();

//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(event -> System.out.println("Hello World!"));

//        root.getChildren().add(btn);


        primaryStage.setScene(new Scene(root, 600, 800));
        primaryStage.show();
    }
}
