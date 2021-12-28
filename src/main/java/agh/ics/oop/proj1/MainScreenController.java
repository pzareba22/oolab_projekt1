package agh.ics.oop.proj1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class MainScreenController {
    int[] parameters;
    private int mapHeight, mapWidth, jungleHeight, jungleWidth, animalNumber, grassEnergy, startEnergy, refreshFrequency;
    private SimulationEngine[] engines;
    private Map[] maps;
    private Thread[] engineThreads;
    private boolean running;
    private int simulationsNumber;
    public Animal watchedAnimal;
    private Canvas[] canvasArray;

    @FXML
    private Button runButton;

    @FXML
    private GridPane pane1, pane2;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox watchBox;

    private Text childNumber, descendantsNumber, deathDay, genotypeLabel, genotypeText;

    @FXML
    public Canvas plotCanvas, plotCanvas2;

    @FXML
    private StackPane plotPane2;

    @FXML
    private void initialize(){
        running = true;
        runButton.setText("Stop");
    }

    public void buttonClicked(ActionEvent e){
        if(running){
            try {
                stopEngines();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            runButton.setText("Start");

        } else {
            startEngines();
            runButton.setText("Stop");
        }
        running = !running;
    }

    public void loadData(int[] parameters){
        this.parameters = parameters;

        mapHeight = parameters[0];
        mapWidth = parameters[1];
        jungleHeight = parameters[2];
        jungleWidth = parameters[3];
        animalNumber = parameters[4];
        grassEnergy = parameters[5];
        startEnergy = parameters[6];
        refreshFrequency = parameters[7];
    }

    public void startSimulation(int mapMode, boolean isMagical){

        this.simulationsNumber = switch (mapMode){
            case 2 -> 2;
            default -> 1;
        };

        if(mapMode < 2){
            mainPane.getChildren().remove(pane2);
            mainPane.getChildren().remove(plotPane2);
            pane1.setPrefHeight(550);
            pane1.setPrefWidth(550);
            pane1.setLayoutX(494);
            pane1.setLayoutY(24);
        }


        engines = new SimulationEngine[simulationsNumber];
        engineThreads = new Thread[simulationsNumber];
        maps = new Map[simulationsNumber];
        canvasArray = new Canvas[simulationsNumber];

        switch (mapMode){
            case 2 -> {
                maps[0] = new Map(parameters, true, pane1, this);
                maps[1] = new Map(parameters, false, pane2, this);
                canvasArray[0] = plotCanvas;
                canvasArray[1] = plotCanvas2;
            }
            case 1 -> {
                maps[0] = new Map(parameters, true, pane1, this);
                canvasArray[0] = plotCanvas;
            }
            default -> {
                maps[0] = new Map(parameters, false, pane1, this);
                canvasArray[0] = plotCanvas;
            }
        }


        for (int i = 0; i < simulationsNumber; i++) {
            engines[i] = new SimulationEngine("Engine " + i, refreshFrequency, maps[i], animalNumber, isMagical, this, canvasArray[i]);
            if(isMagical){
                System.out.println("MAGICZNA SYMULACJA");
            }
            engineThreads[i] = new Thread(engines[i]);
            engineThreads[i].setDaemon(true);
            engineThreads[i].start();
        }
    }

    private void startEngines() {
        for (int i = 0; i < simulationsNumber; i++) {
            engines[i].setRunning(true);
        }
    }

    private void stopEngines() throws InterruptedException {
        for (int i = 0; i < simulationsNumber; i++) {
            engines[i].setRunning(false);
        }
    }

    public void setWatchedAnimal(Animal animal){
        watchedAnimal = animal;
        watchBox.getChildren().clear();

        childNumber = new Text();
        childNumber.setText("Liczba dzieci: " + watchedAnimal.getChildren());
        descendantsNumber = new Text();
        descendantsNumber.setText("Liczba potomkow: " + watchedAnimal.getDescendants());
        deathDay = new Text();
        deathDay.setText("Dzien smierci: ");

        genotypeLabel = new Text();
        genotypeLabel.setText("Genotyp:");

        genotypeText = new Text();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            stringBuilder.append(watchedAnimal.genotype[i]);
        }
        genotypeText.setText(stringBuilder.toString());

        Platform.runLater(() -> {
            watchBox.getChildren().add(childNumber);
            watchBox.getChildren().add(descendantsNumber);
            watchBox.getChildren().add(deathDay);
            watchBox.getChildren().add(genotypeLabel);
            watchBox.getChildren().add(genotypeText);
        });
    }

    public void updateWatchedAnimal(int death){
        childNumber.setText("Liczba dzieci: " + watchedAnimal.getChildren());
        descendantsNumber.setText("Liczba potomkow: " + watchedAnimal.getDescendants());
        if(death > 0){
            deathDay.setText("Dzien smierci: " + death);
        }
    }
}
