package agh.ics.oop.proj1;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class SetupScreen {
    public final Scene scene;
    SetupScreen(int width, int height){
        StackPane sPane = new StackPane();
        scene = new Scene(sPane, width, height);
    }
}
