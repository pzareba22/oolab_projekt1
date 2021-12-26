package agh.ics.oop.proj1;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;

public class Map {
    public final int width;
    public final int height;
    public final int jungleWidth;
    public final int jungleHeight;
    public int animalNumber;
    public final int grassEnergy;
    public final int breedingEnergy;
    public final boolean isMagical;
    private final GridPane gridPane;
    public final double gridSquareSide;
    public final int jungleX;
    public final int jungleY;
    public final boolean[][] foodMap;
    public final HashMap<Vector2d, Animal> animalMap;

    Map(int[] parameters, boolean isMagical, GridPane gridPane){

//      inicjalizacja pól
        this.height = parameters[0];
        this.width = parameters[1];
        this.jungleHeight = parameters[2];
        this.jungleWidth = parameters[3];
        this.animalNumber = parameters[4];
        this.grassEnergy = parameters[5];
        this.breedingEnergy = parameters[6];
        this.isMagical = isMagical;
        this.gridPane = gridPane;
        this.gridSquareSide = calculateGridSquareSide();
        this.foodMap = new boolean[width][height];
        this.animalMap = new HashMap<>();





//      inicjalizacja wierszy i kolumn w GridPane
        for (int i = 0; i < width; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / width);
            gridPane.getColumnConstraints().add(constraints);
        }
        for (int i = 0; i < height; i++) {
            RowConstraints constraints = new RowConstraints();
            constraints.setPercentHeight(100.0/ height);
            gridPane.getRowConstraints().add(constraints);
        }

//      umieszczenie prostokąta z dżunglą
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(javafx.scene.paint.Color.web("#1b6b05"));
        rectangle.setWidth(jungleWidth);
        rectangle.setHeight(jungleHeight);

        this.jungleX = (width-jungleWidth) / 2;
        this.jungleY = (height-jungleHeight) / 2;

        int jungleRealWidth = (int) (gridPane.getPrefWidth() * jungleWidth / width);
        int jungleRealHeight = (int) (gridPane.getPrefHeight() * jungleHeight / height);
        rectangle.setWidth(jungleRealWidth);
        rectangle.setHeight(jungleRealHeight);

        gridPane.add(rectangle, this.jungleX, this.jungleY, jungleWidth, jungleHeight);


    }


    public boolean canMoveTo(Vector2d position){
        return animalMap.containsKey(position);
//        MapElement element = fieldsMap[position.x][position.y].getTopElement();
//        return (!(element instanceof Animal));
    }

    public void show(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if(animalMap.containsKey(position)){

                }
//                if(fieldsMap[i][j] != null){
//                    gridPane.add(fieldsMap[i][j].imageView, i, j, 1, 1);
//                }
            }
        }
        Circle circle = new Circle();
        circle.setRadius(gridSquareSide/2);
        circle.setFill(javafx.scene.paint.Color.web("#c22"));
        gridPane.add(circle, 1, 0, 1, 1);
    }

    private double calculateGridSquareSide(){
        double gridPaneWidth = gridPane.getPrefWidth();
        double gridPaneHeight = gridPane.getPrefHeight();

        double xSize = gridPaneWidth / width;
        double ySize = gridPaneHeight / height;

        return Math.min(xSize, ySize);

    }

}
