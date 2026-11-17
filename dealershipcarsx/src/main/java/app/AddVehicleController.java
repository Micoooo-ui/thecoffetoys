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

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    // Populate fields when editing an existing vehicle
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusField.setItems(FXCollections.observableArrayList("Available", "Reserved", "Sold"));
        statusField.setValue("Available");
    }

    @FXML
    private void handleSaveVehicle() {
        try {
            String brand = brandField.getText();
            String model = modelField.getText();
            String name = nameField.getText();
            int year = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());
            String status = statusField.getValue();

            if (isEditMode) {
                // Update the existing object fields
                vehicleToEdit.setBrand(brand);
                vehicleToEdit.setModel(model);
                vehicleToEdit.setName(name);
                vehicleToEdit.setYear(year);
                vehicleToEdit.setPrice(price);
                vehicleToEdit.setStatus(status);

                // Refresh table view
                if (dashboardController != null) {
                    dashboardController.updateVehicleInList(vehicleToEdit, vehicleToEdit);
                }
            } else {
                // Create brand new vehicle
                vehicle newVehicle = new vehicle(brand, model, name, year, price, status);
                if (dashboardController != null) {
                    dashboardController.addVehicleToList(newVehicle);
                }
            }

            closeWindow();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for Year and Price.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.close();
    }
}