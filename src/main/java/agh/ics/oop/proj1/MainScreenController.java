package agh.ics.oop.proj1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class MainScreenController {
    int[] parameters;
    private int mapHeight, mapWidth, jungleHeight, jungleWidth, animalNumber, grassEnergy, breedingEnegry, refreshFrequency;
    private SimulationEngine[] engines;
    private Map[] maps;
    private Thread[] engineThreads;
    private boolean running;
    private int simulationsNumber;


    @FXML
    private Button runButton;

    @FXML
    private GridPane pane1, pane2;

    @FXML
    private AnchorPane mainPane;


//    @FXML
//    private Button sim1Button, sim2Button;

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
        breedingEnegry = parameters[6];
        refreshFrequency = parameters[7];
    }

    public void startSimulation(int simulationsMode){
        this.simulationsNumber = switch (simulationsMode){
            case 2 -> 2;
            default -> 1;
        };

        if(simulationsMode < 2){
            mainPane.getChildren().remove(pane2);
            pane1.setPrefHeight(550);
            pane1.setPrefWidth(550);
            pane1.setLayoutX(494);
            pane1.setLayoutY(24);
        }

        engines = new SimulationEngine[simulationsNumber];
        engineThreads = new Thread[simulationsNumber];
        maps = new Map[simulationsNumber];

        switch (simulationsMode){
            case 2 -> {
                maps[0] = new Map(parameters, false, pane1);
                maps[1] = new Map(parameters, true, pane2);
            }
            case 1 -> {
                maps[0] = new Map(parameters, true, pane1);
            }
            default -> {
                maps[0] = new Map(parameters, false, pane1);
            }
        }


        for (int i = 0; i < simulationsNumber; i++) {
            engines[i] = new SimulationEngine("Engine " + i, refreshFrequency, maps[i]);
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
}
