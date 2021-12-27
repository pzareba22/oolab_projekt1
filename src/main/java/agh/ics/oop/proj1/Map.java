package agh.ics.oop.proj1;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

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
    public final HashSet<Vector2d> grassMap;

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
        this.grassMap = new HashSet<>();





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
        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);

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
    }

    public void show(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if(animalMap.containsKey(position)){
                    Circle animalCricle = new Circle();
                    animalCricle.setRadius(gridSquareSide/2);
                    animalCricle.setFill(javafx.scene.paint.Color.web("#c22"));
                    gridPane.add(animalCricle, i, j, 1, 1);
                    GridPane.setHalignment(animalCricle, HPos.CENTER);
                    GridPane.setValignment(animalCricle, VPos.CENTER);
                }else if (grassMap.contains(position)) {
                    Rectangle grassRectangle = new Rectangle();
                    grassRectangle.setHeight(gridSquareSide * 0.9);
                    grassRectangle.setWidth(gridSquareSide * 0.9);
                    grassRectangle.setFill(javafx.scene.paint.Color.web("#12f50a"));
                    gridPane.add(grassRectangle, i, j, 1, 1);
                    GridPane.setHalignment(grassRectangle, HPos.CENTER);
                    GridPane.setValignment(grassRectangle, VPos.CENTER);
                }
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


    // funkcja generująca 2 'porcje' trawy (w dżungli i poza nią)
    public void generateGrass(){
        //generowanie trawy w dżungli
        Random random = new Random();
        for (int i = 0; i < jungleWidth*jungleHeight; i++){
            int x1 = (width - jungleWidth)/2 + random.nextInt(jungleWidth);
            int y1 = (height - jungleHeight)/2 + random.nextInt(jungleHeight);
            Vector2d position1 = new Vector2d(x1, y1);
            if(!grassMap.contains(position1)){
                grassMap.add(position1);
                break;
            }
        }
        //generowanie trawy poza dżunglą
        for (int i = 0; i < width*height; i++) {
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);

            if(x2 >= (width - jungleWidth)/2 && y2 >= (height - jungleHeight)/2){
                int choice = random.nextInt(2);
                switch (choice){
                    case 1 -> y2 += jungleHeight;
                    default -> x2 += jungleWidth;
                }
            }

//            }
            Vector2d position2 = new Vector2d(x2, y2);
            if(!grassMap.contains(position2)){
                grassMap.add(position2);
                break;
            }
        }
    }

}
