package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vehicle.vehicle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private TableView<vehicle> vehicleTable;
    @FXML private TableColumn<vehicle, String> brandColumn;
    @FXML private TableColumn<vehicle, String> modelColumn;
    @FXML private TableColumn<vehicle, String> nameColumn;
    @FXML private TableColumn<vehicle, Integer> yearColumn;
    @FXML private TableColumn<vehicle, Double> priceColumn;
    @FXML private TableColumn<vehicle, String> statusColumn;

    private ObservableList<vehicle> vehicleList = FXCollections.observableArrayList(
            new vehicle("Toyota", "Hilux", "Conquest", 2023, 1450000.0, "Available"),
            new vehicle("Ford", "Ranger", "Wildtrak", 2024, 1680000.0, "Reserved"),
            new vehicle("Mitsubishi", "Montero", "Sport", 2022, 1350000.0, "Available")
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        vehicleTable.setItems(vehicleList);
    }

    // Open Add Vehicle Modal
    @FXML
    public void openAddVehicleModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
            Parent root = loader.load();

            AddVehicleController addController = loader.getController();
            addController.setDashboardController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("VYC Motors - Add New Vehicle");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete the selected vehicle from the table
    @FXML
    public void handleDeleteVehicle() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleList.remove(selectedVehicle);
        } else {
            System.out.println("Please select a vehicle to delete.");
        }
    }

    // Open modal to edit the selected vehicle
    @FXML
    public void handleEditVehicle() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
                Parent root = loader.load();

                AddVehicleController addController = loader.getController();
                addController.setDashboardController(this);
                addController.initEditData(selectedVehicle);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("VYC Motors - Edit Vehicle");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a vehicle to edit.");
        }
    }

    // Add new vehicle record to list
    public void addVehicleToList(vehicle v) {
        vehicleList.add(v);
    }

    // Update existing vehicle record in list
    public void updateVehicleInList(vehicle oldVehicle, vehicle newVehicle) {
        int index = vehicleList.indexOf(oldVehicle);
        if (index >= 0) {
            vehicleList.set(index, newVehicle);
        }
    }
}