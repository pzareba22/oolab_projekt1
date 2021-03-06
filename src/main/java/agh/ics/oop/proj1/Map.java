package agh.ics.oop.proj1;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
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
    public final boolean isWrapped;
    private final GridPane gridPane;
    public final double gridSquareSide;
    public final int jungleX;
    public final int jungleY;
    public final boolean[][] foodMap;
    public final HashMap<Vector2d, LinkedList<Animal>> animalMap;
    private final HashSet<Vector2d> grassMap;
    private final Vector2d upperRight;
    private final Vector2d lowerLeft;
    private final HashMap<Vector2d, Circle> circleMap;
    private LinkedList<Vector2d> grassToBeEaten;
    private LinkedList<Animal> deadAnimals;
//    private final SimulationEngine engine;
    private final MainScreenController controller;


    Map(int[] parameters, boolean isWrapped, GridPane gridPane, MainScreenController controller){

//      inicjalizacja pól
        this.height = parameters[0];
        this.width = parameters[1];
        this.jungleHeight = parameters[2];
        this.jungleWidth = parameters[3];
        this.animalNumber = parameters[4];
        this.grassEnergy = parameters[5];
        this.startEnergy = parameters[6];
        this.isWrapped = isWrapped;
        this.gridPane = gridPane;
        this.gridSquareSide = calculateGridSquareSide();
        this.foodMap = new boolean[width][height];
        this.animalMap = new HashMap<>();
        this.grassMap = new HashSet<>();
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.lowerLeft = new Vector2d(0, 0);
        this.circleMap = new HashMap<>();
        this.deadAnimals = new LinkedList<>();
        this.controller = controller;


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
        if(isWrapped){
            return true;
        }
        if(position.follows(lowerLeft) && position.precedes(upperRight)){
            return true;
        }
        return false;

    }

    public void show(){
        gridPane.getChildren().clear();
        LinkedList<GuiElement> toAdd = new LinkedList<>();
//      umieszczenie prostokąta z dżunglą
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(javafx.scene.paint.Color.web("#1b6b05"));

        rectangle.setWidth(jungleWidth * gridSquareSide);
        rectangle.setHeight(jungleHeight * gridSquareSide);

        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);

        toAdd.add(new GuiElement(rectangle, jungleX, jungleY, (int) (jungleWidth), (int) (jungleHeight)));

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if(animalMap.containsKey(position)){
                    toAdd.add(addCircle(position));
                }else if (grassMap.contains(position)) {
                    toAdd.add(addSquare(position));
                }
            }
        }
        Platform.runLater(() -> {
            for(GuiElement element : toAdd){
                gridPane.add(element.node, element.x, element.y, element.width, element.height);
            }
        });
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
            if(!grassMap.contains(position1) && !animalMap.containsKey(position1)){
                grassMap.add(position1);
//                addSquare(position1);
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
            if(!grassMap.contains(position2) && !animalMap.containsKey(position2)){
                grassMap.add(position2);
//                addSquare(position2);
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

        if(isWrapped){
            if(newPosition.x < 0 || newPosition.y < 0 || newPosition.x >= width || newPosition.y >= height){
                int newX = newPosition.x;
                int newY = newPosition.y;

                if(newX == -1){
                    newX = width-1;
                }else if(newX == width){
                    newX = 0;
                }
                if(newY == -1){
                    newY = height-1;
                }else if(newY == height){
                    newY = 0;
                }

                Vector2d newerPosition = new Vector2d(newX, newY);
                newPosition = newerPosition;
                animal.setPosition(newerPosition);
            }
        }

        // usuwanie starej pozycji
        animalMap.get(oldPosition).remove(animal);
        if(animalMap.get(oldPosition).isEmpty()){
            animalMap.remove(oldPosition);
        }

        // dodawanie nowej pozycji
        if(!animalMap.containsKey(newPosition)){
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animalMap.put(newPosition, list);
        }else{
            animalMap.get(newPosition).add(animal);
        }

        if(grassMap.contains(newPosition)){
            if(grassToBeEaten == null){
                grassToBeEaten = new LinkedList<>();
            }
            grassToBeEaten.add(newPosition);
        }
    }

    private GuiElement addCircle(Vector2d position){
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

        Animal finalStrongestAnimal = strongestAnimal;
        circle.addEventHandler(MouseEvent.MOUSE_CLICKED , e -> {
            finalStrongestAnimal.setWatched();
            controller.setWatchedAnimal(finalStrongestAnimal);

        });

        return new GuiElement(circle, position.x, position.y, 1, 1);
    }

//    private void removeCircle(Vector2d position){
//        Circle circle = circleMap.get(position);
//        circleMap.remove(position);
//        Platform.runLater(() -> gridPane.getChildren().remove(circle));
//    }

    private GuiElement addSquare(Vector2d position){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(gridSquareSide*0.9);
        rectangle.setHeight(gridSquareSide*0.9);
        rectangle.setFill(javafx.scene.paint.Color.web("#12f50a"));
        GridPane.setHalignment(rectangle, HPos.CENTER);
        GridPane.setValignment(rectangle, VPos.CENTER);

        grassMap.add(position);

        return new GuiElement(rectangle, position.x, position.y, 1, 1);
//        Platform.runLater(() -> gridPane.add(rectangle, position.x, position.y, 1, 1));
    }

//    private void removeSquare(Vector2d position){
//        Rectangle rectangle = grassMap.get(position);
//        grassMap.remove(position);
//        Platform.runLater(() -> gridPane.getChildren().remove(rectangle));
//    }

    public void eatGrass(){
        if(grassToBeEaten == null){
            return;
        }
        for(Vector2d position : grassToBeEaten){
            if(animalMap.get(position) == null){
                continue;
            }
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
        animalNumber -= 1;
    }

    public void removeDead(){
        for(Animal animal : deadAnimals){
//            if(animalMap.get(animal.getPosition()) == null){
//                continue;
//            }
            animalMap.get(animal.getPosition()).remove(animal);
            if(animalMap.get(animal.getPosition()).isEmpty()){
                animalMap.remove(animal.getPosition());
            }
        }
        deadAnimals.clear();
    }

    public ArrayList<Animal> selectAnimalsForBreeding() {
        ArrayList<Animal> animalsToBreed = new ArrayList<>();

        for(LinkedList<Animal> animalList: animalMap.values()){
            if(animalList.size() < 2){
                continue;
            }

            Animal strongest1 = animalList.getFirst();
            Animal strongest2 = null;
            int strongestCount = 0;
            for(Animal animal : animalList){
                if(animal.getEnergy() > strongest1.getEnergy()){
                    strongest1 = animal;
                    strongestCount = 1;
                }else if (animal.getEnergy() == strongest1.getEnergy()){
                    strongestCount += 1;
                }
            }

            if(strongestCount > 2){
                int j = 0;
                Random random = new Random();
                int ind1 = random.nextInt(strongestCount);
                int ind2;
                while (true){
                    ind2 = random.nextInt(strongestCount);
                    if(ind2 != ind1){
                        break;
                    }
                }

                int cnt = 0;
                for(Animal animal : animalList){
                    if(animal.getEnergy() == strongest1.getEnergy()){
                        if(cnt == ind1){
                            strongest1 = animal;
                        }else if(cnt == ind2){
                            strongest2 = animal;
                        }
                        cnt += 1;
                    }
                }
            }else if(strongestCount == 2){
                for(Animal animal : animalList){
                    if(animal.getEnergy() == strongest1.getEnergy() && animal != strongest1){
                        strongest2 = animal;
                        break;
                    }
                }
            }else {
                int energy = 0;
                for(Animal animal : animalList){
                    if(animal != strongest1 && animal.getEnergy() > energy){
                        energy = animal.getEnergy();
                        strongest2 = animal;
                        break;
                    }
                }
            }

            if(strongest2.getEnergy() >= startEnergy/2){
                animalsToBreed.add(strongest1);
                animalsToBreed.add(strongest2);
            }

        }
        return animalsToBreed;
    }

    public int getGrassCount(){
        return grassMap.size();
    }

}
