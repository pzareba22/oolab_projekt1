package agh.ics.oop.proj1;

import javafx.scene.layout.GridPane;

public class Map {
    public final int width;
    public final int height;
    public final int jungleWidth;
    public final int jungleHeight;
    public int animalNumber;
    public final int grassEnergy;
    public final int breedingEnergy;
    public final boolean isMagical;
    public final MapField[][] fieldsMap;
    private final GridPane gridPane;

    Map(int[] parameters, boolean isMagical, GridPane gridPane){
        height = parameters[0];
        width = parameters[1];
        jungleHeight = parameters[2];
        jungleWidth = parameters[3];
        animalNumber = parameters[4];
        grassEnergy = parameters[5];
        breedingEnergy = parameters[6];
        this.isMagical = isMagical;
        this.gridPane = gridPane;

        this.fieldsMap = new MapField[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.fieldsMap[i][j] = new MapField(String.valueOf(getClass().getResource("/images/lightGreen.png")), 20);
            }
        }
        this.fieldsMap[0][0] = new MapField(String.valueOf(getClass().getResource("/images/darkGreen.png")), 20);
    }

    public boolean canMoveTo(Vector2d position){
        MapElement element = fieldsMap[position.x][position.y].getTopElement();
        return (!(element instanceof Animal));
    }

    public void show(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gridPane.add(fieldsMap[i][j].imageView, i, j, 1, 1);
            }
        }
    }

}
