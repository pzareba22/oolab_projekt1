package agh.ics.oop.proj1;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Map {
    public final int width;
    public final int height;
    public final int jungleWidth;
    public final int jungleHeight;
    public int animalNumber;
    public final int grassEnergy;
    public final int startEnergy;
    public final boolean isMagical;
    private final GridPane gridPane;
    public final double gridSquareSide;
    public final int jungleX;
    public final int jungleY;
    public final boolean[][] foodMap;
    public final HashMap<Vector2d, LinkedList<Animal>> animalMap;
    public final HashMap<Vector2d, Rectangle> grassMap;
    private final Vector2d upperRight;
    private final Vector2d lowerLeft;
    private final HashMap<Vector2d, Circle> circleMap;
    private LinkedList<Vector2d> grassToBeEaten;
    private LinkedList<Animal> deadAnimals;


    Map(int[] parameters, boolean isMagical, GridPane gridPane){

//      inicjalizacja pól
        this.height = parameters[0];
        this.width = parameters[1];
        this.jungleHeight = parameters[2];
        this.jungleWidth = parameters[3];
        this.animalNumber = parameters[4];
        this.grassEnergy = parameters[5];
        this.startEnergy = parameters[6];
        this.isMagical = isMagical;
        this.gridPane = gridPane;
        this.gridSquareSide = calculateGridSquareSide();
        this.foodMap = new boolean[width][height];
        this.animalMap = new HashMap<>();
        this.grassMap = new HashMap<>();
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.lowerLeft = new Vector2d(0, 0);
        this.circleMap = new HashMap<>();
        this.deadAnimals = new LinkedList<>();


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

        this.jungleX = (width-jungleWidth) / 2;
        this.jungleY = (height-jungleHeight) / 2;

//      umieszczenie prostokąta z dżunglą
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(javafx.scene.paint.Color.web("#1b6b05"));

        rectangle.setWidth(jungleWidth * gridSquareSide);
        rectangle.setHeight(jungleHeight * gridSquareSide);

        gridPane.add(rectangle, this.jungleX, this.jungleY, jungleWidth, jungleHeight);
        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);


        //TEMPORARY

    }

    public boolean canMoveTo(Vector2d position){
        if(isMagical){
            return true;
        }
        if(position.follows(lowerLeft) && position.precedes(upperRight)){
            return true;
        }
        return false;

    }

    public void show(){
        gridPane.getChildren().clear();
//      umieszczenie prostokąta z dżunglą
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(javafx.scene.paint.Color.web("#1b6b05"));

        rectangle.setWidth(jungleWidth * gridSquareSide);
        rectangle.setHeight(jungleHeight * gridSquareSide);

        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);

//        gridPane.add(rectangle, this.jungleX, this.jungleY, jungleWidth, jungleHeight);
        Platform.runLater(() -> gridPane.add(rectangle, this.jungleX, this.jungleY, jungleWidth, jungleHeight));

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if(animalMap.containsKey(position)){
                    addCircle(position);
                }else if (grassMap.containsKey(position)) {
                    addSquare(position);
                }
            }
        }
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
            if(!grassMap.containsKey(position1) && !animalMap.containsKey(position1)){
//                grassMap.add(position1);
                addSquare(position1);
                break;
            }
        }
        //generowanie trawy poza dżunglą
        for (int i = 0; i < width*height; i++) {
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);

            if(x2 >= (width - jungleWidth)/2 && x2 <= (width + jungleWidth)/2 && y2 >= (height - jungleHeight)/2 && y2 <= (height + jungleHeight)/2){
                int choice = random.nextInt(2);
                if (choice == 1) {
                    y2 += jungleHeight;
                } else {
                    x2 += jungleWidth;
                }
            }
            Vector2d position2 = new Vector2d(x2, y2);
            if(!grassMap.containsKey(position2) && !animalMap.containsKey(position2)){
                addSquare(position2);
                break;
            }
        }
    }

    public void place(Animal animal){
        Vector2d position = animal.getPosition();
        if(!animalMap.containsKey(position)){
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animalMap.put(position, list);
        }else{
            LinkedList<Animal> animalList = animalMap.get(animal.getPosition());
            animalList.add(animal);
        }
    }

    public void animalPositionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition){
        animalMap.get(oldPosition).remove(animal);
//        removeCircle(oldPosition);
        if(animalMap.get(oldPosition).isEmpty()){
            animalMap.remove(oldPosition);
        }else{
//            addCircle(oldPosition);
        }

        if(!animalMap.containsKey(newPosition)){
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animalMap.put(newPosition, list);
        }else{
            animalMap.get(newPosition).add(animal);
        }
        if(grassMap.containsKey(newPosition)){
            if(grassToBeEaten == null){
                grassToBeEaten = new LinkedList<>();
            }
            grassToBeEaten.add(newPosition);
        }
    }
    private void addCircle(Vector2d position){
        LinkedList<Animal> animalList = animalMap.get(position);
        Animal strongestAnimal = animalList.getFirst();
        for(Animal animal: animalList){
            if(animal.getEnergy() > strongestAnimal.getEnergy()){
                strongestAnimal = animal;
            }
        }
        Circle circle = new Circle();
        circle.setRadius(gridSquareSide/2 - gridSquareSide*0.08);
        int color = strongestAnimal.getEnergy() * 255 / 2 / startEnergy;
        color = Math.min(color, 255);
        circle.setFill(Color.rgb(255, color, color));
        GridPane.setHalignment(circle, HPos.CENTER);
        GridPane.setValignment(circle, VPos.CENTER);

        circleMap.put(position, circle);
        Platform.runLater(() -> gridPane.add(circle, position.x, position.y, 1, 1));
    }

    private void removeCircle(Vector2d position){
        Circle circle = circleMap.get(position);
        circleMap.remove(position);
        Platform.runLater(() -> gridPane.getChildren().remove(circle));
    }

    private void addSquare(Vector2d position){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(gridSquareSide*0.9);
        rectangle.setHeight(gridSquareSide*0.9);
        rectangle.setFill(javafx.scene.paint.Color.web("#12f50a"));
        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);

        grassMap.put(position, rectangle);
        Platform.runLater(() -> gridPane.add(rectangle, position.x, position.y, 1, 1));
    }

    private void removeSquare(Vector2d position){
        Rectangle rectangle = grassMap.get(position);
        grassMap.remove(position);
        Platform.runLater(() -> gridPane.getChildren().remove(rectangle));
    }

    public void eatGrass(){
        if(grassToBeEaten == null){
            return;
        }
        for(Vector2d position : grassToBeEaten){
            LinkedList<Animal> animalList = animalMap.get(position);
            Animal strongestAnimal = animalList.getFirst();
            int strongestCount = 0;
            for(Animal animal: animalList){
                if(animal.getEnergy() == strongestAnimal.getEnergy()){
                    strongestCount += 1;
                }else if(animal.getEnergy() > strongestAnimal.getEnergy()) {
                    strongestCount = 1;
                    strongestAnimal = animal;
                }
            }
            int energyGain = grassEnergy / strongestCount;
            for(Animal animal : animalList){
                if(animal.getEnergy() == strongestAnimal.getEnergy()){
                    animal.increaseEnergy(energyGain);
//                    removeCircle(animal.getPosition());
//                    addCircle(animal.getPosition());
                }
            }
            grassMap.remove(position);
//            removeSquare(position);
        }
        grassToBeEaten = null;
    }

    public void animalDied(Animal animal){
        deadAnimals.add(animal);
    }

    public void removeDead(){
        for(Animal animal : deadAnimals){
            animalMap.get(animal.getPosition()).remove(animal);
            if(animalMap.get(animal.getPosition()).isEmpty()){
                animalMap.remove(animal.getPosition());
            }
        }
    }

}
