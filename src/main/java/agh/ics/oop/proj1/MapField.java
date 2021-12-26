package agh.ics.oop.proj1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;

public class MapField {
    private final LinkedList<MapElement> objectsList;
    public final ImageView imageView;


    MapField(String imgPath, double squareSide) {
        this.objectsList = new LinkedList<>();
        Image image = new Image(imgPath, 50, 50, false, false);
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(squareSide);
        this.imageView.setFitHeight(squareSide);

    }

    void add(MapElement element){
        objectsList.addFirst(element);
    }

    MapElement getTopElement(){
        return objectsList.getFirst();
    }

    MapElement popTopElement() {
        return objectsList.pop();
    }

}
