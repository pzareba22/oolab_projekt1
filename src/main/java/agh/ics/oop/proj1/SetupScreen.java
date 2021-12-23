package agh.ics.oop.proj1;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/*
    PARAMETRY SYMULACJI:

        *zasada ewolucyjna (zwykła lub magizna)
        *mapa (zwinięta, nie lub obie)
        *wysokość mapy
        *szerokość mapy
        *wysokość dżungli
        *szerokość dżungli
        *początkowa liczba zwierząt
        *zysk energii z trawy
        *minimalna energia do kopulacji


 */


public class SetupScreen {
    public final Scene scene;
    SetupScreen(int width, int height){
        StackPane sPane = new StackPane();
        scene = new Scene(sPane, width, height);
    }
}
