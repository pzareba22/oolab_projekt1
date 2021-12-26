package agh.ics.oop.proj1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class SetupScreenController {

    private int[] parametersArray;


    @FXML
    private TextField mapHeight, mapWidth, jungleHeight, jungleWidth, animalNumber, grassEnergy, breedingEnegry;

    ObservableList<String> evolutionModeList = FXCollections.observableArrayList("Zwykla", "Magiczna", "Obie");

    @FXML
    private ChoiceBox<String> evolutionModeBox;

    @FXML
    private void initialize() {
        mapHeight.setText("5");
        mapWidth.setText("5");
        jungleHeight.setText("3");
        jungleWidth.setText("4");
        animalNumber.setText("5");
        grassEnergy.setText("6");
        breedingEnegry.setText("7");

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

        int simulationsMode = switch (evolutionModeBox.getValue()) {
            case "Zwykla" -> 0;
            case "Magiczna" -> 1;
            case "Obie" -> 2;
            default -> 0;
        };

        mainScreenController.startSimulation(simulationsMode);
    }

    private void validateData() throws NumberFormatException {
        parametersArray = new int[7];
        parametersArray[0] = Integer.parseInt(mapHeight.getText());
        parametersArray[1] = Integer.parseInt(mapWidth.getText());
        parametersArray[2] = Integer.parseInt(jungleHeight.getText());
        parametersArray[3] = Integer.parseInt(jungleWidth.getText());
        parametersArray[4] = Integer.parseInt(animalNumber.getText());
        parametersArray[5] = Integer.parseInt(grassEnergy.getText());
        parametersArray[6] = Integer.parseInt(breedingEnegry.getText());
    }

}
