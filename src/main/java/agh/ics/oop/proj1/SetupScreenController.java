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
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int[] parametersArray;


    @FXML
    private TextField mapHeight, mapWidth, jungleHeight, jungleWidth, animalNumber, grassEnergy, breedingEnegry;

    ObservableList<String> evolutionModeList = FXCollections.observableArrayList("Zwykla", "Magiczna");
    @FXML
    private ChoiceBox<String> evolutionModeBox;

    @FXML
    private void initialize() {
        evolutionModeBox.setItems(evolutionModeList);
        evolutionModeBox.setValue("Zwykla");
    }

    public void switchToMainScreen(ActionEvent e) throws IOException {

        try{
            validateData();
        }catch (NumberFormatException exception){
            System.out.println("Nieprawidłowe dane wejściowe");
            // TODO okienko z alertem
            return;
        }


//        System.out.println("\nData entered");
//        System.out.println("wysokość mapy: " + mapHeight.getText());
//        System.out.println("szerokość mapy: " + mapWidth.getText());
//        System.out.println("wysokość dżungli: " + jungleHeight.getText());
//        System.out.println("szerokość dżungli: " + jungleWidth.getText());
//        System.out.println("liczba zwierząt: " + animalNumber.getText());
//        System.out.println("energia trawy: " + grassEnergy.getText());
//        System.out.println("energia do rozmnażania się: " + breedingEnegry.getText());
//        System.out.println("tryb ewolucji: " + evolutionModeBox.getValue());



        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/MainScreen.fxml")));
        root = loader.load();
        MainScreenController mainScreenController = loader.getController();

        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
