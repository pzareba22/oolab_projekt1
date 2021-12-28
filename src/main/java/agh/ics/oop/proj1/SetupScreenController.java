package agh.ics.oop.proj1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class SetupScreenController {

    private int[] parametersArray;


    @FXML
    private TextField mapHeight, mapWidth, jungleHeight, jungleWidth, animalNumber, grassEnergy, startEnergy, refreshFrequency;

    ObservableList<String> mapModeList = FXCollections.observableArrayList("Zwykla", "Zawinieta", "Obie");
    ObservableList<String> evolutionModeList = FXCollections.observableArrayList("Zwykla", "Magiczna");

    @FXML
    private ChoiceBox<String> mapModeBox, evolutionModeBox;


    @FXML
    private void initialize() {
        mapHeight.setText("15");
        mapWidth.setText("15");
        jungleHeight.setText("3");
        jungleWidth.setText("4");
        animalNumber.setText("15");
        grassEnergy.setText("30");
        startEnergy.setText("50");
        refreshFrequency.setText("50");

        mapModeBox.setItems(mapModeList);
        mapModeBox.setValue("Zwykla");

        evolutionModeBox.setItems(evolutionModeList);
        evolutionModeBox.setValue("Zwykla");
    }

    public void switchToMainScreen(ActionEvent e) throws IOException {

        try{
            validateData();
        }catch (NumberFormatException exception){
            AlertWindowHandler.showErrorAlert("Error Awaria", "Niepoprawne dane wejsciowe", "Prosze podac dane, bedace liczbami naturalnymi");
            return;
        }


        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/MainScreen.fxml")));
        Parent root = loader.load();

        MainScreenController mainScreenController = loader.getController();
        mainScreenController.loadData(parametersArray);

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        int mapMode = 0;
        if(mapModeBox.getValue().equals("Zawinieta")){
            mapMode = 1;
        }else if(mapModeBox.getValue().equals("Obie")){
            mapMode = 2;
        }

        boolean evolutionMode = evolutionModeBox.getValue().equals("Magiczna");

        mainScreenController.startSimulation(mapMode, evolutionMode);
    }

    private void validateData() throws NumberFormatException {
        parametersArray = new int[8];
        parametersArray[0] = Integer.parseInt(mapHeight.getText());
        parametersArray[1] = Integer.parseInt(mapWidth.getText());
        parametersArray[2] = Integer.parseInt(jungleHeight.getText());
        parametersArray[3] = Integer.parseInt(jungleWidth.getText());
        parametersArray[4] = Integer.parseInt(animalNumber.getText());
        parametersArray[5] = Integer.parseInt(grassEnergy.getText());
        parametersArray[6] = Integer.parseInt(startEnergy.getText());
        parametersArray[7] = Integer.parseInt(refreshFrequency.getText());
        for (int i = 0; i < 8; i++) {
            if(parametersArray[i] < 0){
                throw new NumberFormatException();
            }

        }
    }

}
