package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vehicle.vehicle;

import java.net.URL;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField nameField;
    @FXML private TextField yearField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusField;

    private DashboardController dashboardController;
    private vehicle vehicleToEdit;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate status dropdown options
        statusField.setItems(FXCollections.observableArrayList("Available", "Reserved", "Sold"));
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    // This method populates the fields when editing an existing vehicle
    public void initEditData(vehicle v) {
        this.vehicleToEdit = v;
        this.isEditMode = true;

        brandField.setText(v.getBrand());
        modelField.setText(v.getModel());
        nameField.setText(v.getName());
        yearField.setText(String.valueOf(v.getYear()));
        priceField.setText(String.valueOf(v.getPrice()));
        statusField.setValue(v.getStatus());
    }

    @FXML
    public void handleSaveVehicle() {
        try {
            String brand = brandField.getText();
            String model = modelField.getText();
            String name = nameField.getText();
            int year = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());
            String status = statusField.getValue() != null ? statusField.getValue() : "Available";

            if (isEditMode) {
                // Update existing vehicle properties
                vehicleToEdit.setBrand(brand);
                vehicleToEdit.setModel(model);
                vehicleToEdit.setName(name);
                vehicleToEdit.setYear(year);
                vehicleToEdit.setPrice(price);
                vehicleToEdit.setStatus(status);

                if (dashboardController != null) {
                    dashboardController.updateVehicleInList(vehicleToEdit, vehicleToEdit);
                }
            } else {
                // Create new vehicle
                vehicle newVehicle = new vehicle(brand, model, name, year, price, status);
                if (dashboardController != null) {
                    dashboardController.addVehicleToList(newVehicle);
                }
            }

            closeWindow();
        } catch (NumberFormatException e) {
            System.err.println("Please enter valid numbers for Year and Price.");
        }
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.close();
    }
}