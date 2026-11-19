package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button saveButton;

    private DashboardController dashboardController;
    private vehicle vehicleToEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (statusCombo != null) {
            statusCombo.setItems(FXCollections.observableArrayList("Available", "Reserved", "Sold"));
            statusCombo.setValue("Available");
        }
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    public void initEditData(vehicle v) {
        this.vehicleToEdit = v;
        if (brandField != null) brandField.setText(v.getBrand());
        if (modelField != null) modelField.setText(v.getModel());
        if (nameField != null) nameField.setText(v.getName());
        if (yearField != null) yearField.setText(String.valueOf(v.getYear()));
        if (priceField != null) priceField.setText(String.valueOf(v.getPrice()));
        if (statusCombo != null) statusCombo.setValue(v.getStatus());
        if (saveButton != null) saveButton.setText("Update Vehicle");
    }

    @FXML
    public void handleSaveVehicle() {
        String brand = brandField.getText() == null ? "" : brandField.getText().trim();
        String model = modelField.getText() == null ? "" : modelField.getText().trim();
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String yearText = yearField.getText() == null ? "" : yearField.getText().trim();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();
        String status = (statusCombo != null && statusCombo.getValue() != null) ? statusCombo.getValue() : "Available";

        if (brand.isEmpty() || model.isEmpty() || name.isEmpty() || yearText.isEmpty() || priceText.isEmpty()) {
            showWarning("Missing Input", "Please fill in all fields before saving.");
            return;
        }

        int year;
        double price;

        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            showWarning("Invalid Year", "Please enter a valid whole number for the year (e.g., 2024).");
            return;
        }

        if (year < 1980 || year > 2030) {
            showWarning("Invalid Year Range", "Please enter a realistic vehicle year (1980–2030).");
            return;
        }

        try {
            price = Double.parseDouble(priceText.replace(",", "").replace("₱", "").trim());
        } catch (NumberFormatException e) {
            showWarning("Invalid Price", "Please enter a valid numeric price (e.g., 1455000).");
            return;
        }

        if (price <= 0) {
            showWarning("Invalid Price", "Price must be greater than zero.");
            return;
        }

        vehicle newVehicle = new vehicle(brand, model, name, year, price, status);

        if (dashboardController != null) {
            if (vehicleToEdit != null) {
                dashboardController.updateVehicleInList(vehicleToEdit, newVehicle);
            } else {
                dashboardController.addVehicleToList(newVehicle);
            }
        }

        closeWindow();
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        if (brandField != null && brandField.getScene() != null) {
            Stage stage = (Stage) brandField.getScene().getWindow();
            stage.close();
        }
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}