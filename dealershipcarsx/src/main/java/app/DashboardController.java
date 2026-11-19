package app;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vehicle.vehicle;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label totalVehiclesLabel;
    @FXML private Label availableStockLabel;
    @FXML private Label soldReservedLabel;

    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private TextField searchField;

    @FXML private TableView<vehicle> vehicleTable;
    @FXML private TableColumn<vehicle, String> brandColumn;
    @FXML private TableColumn<vehicle, String> modelColumn;
    @FXML private TableColumn<vehicle, String> nameColumn;
    @FXML private TableColumn<vehicle, Integer> yearColumn;
    @FXML private TableColumn<vehicle, Double> priceColumn;
    @FXML private TableColumn<vehicle, String> statusColumn;

    // Shared list so inventory persists when switching between Dashboard and Transactions
    private static final ObservableList<vehicle> vehicleList = FXCollections.observableArrayList();
    private static boolean samplesLoaded = false;

    private FilteredList<vehicle> filteredVehicles;
    private final DecimalFormat phpFormat = new DecimalFormat("₱#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        loadSampleVehicles();

        filteredVehicles = new FilteredList<>(vehicleList, p -> true);
        vehicleTable.setItems(filteredVehicles);

        if (statusFilterCombo != null) {
            statusFilterCombo.setItems(FXCollections.observableArrayList("All", "Available", "Reserved"));
            statusFilterCombo.setValue("All");
            statusFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }

        updateSummaryCards();
    }

    private void setupColumns() {
        brandColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand()));
        modelColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        yearColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getYear()).asObject());
        priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText((empty || price == null) ? null : phpFormat.format(price));
            }
        });
    }

    private void loadSampleVehicles() {
        if (samplesLoaded) return;

        // Only Available and Reserved vehicles stay in the Dashboard inventory (Sold items go to Transactions)
        vehicleList.addAll(
                new vehicle("Toyota", "Hilux", "Conquest", 2023, 1455000.00, "Available"),
                new vehicle("Ford", "Ranger", "Wildtrak", 2024, 1949000.00, "Reserved"),
                new vehicle("Mitsubishi", "Montero", "Sport", 2022, 1568000.00, "Available"),
                new vehicle("Toyota", "Vios", "G CVT", 2023, 1035000.00, "Available"),
                new vehicle("Honda", "CR-V", "V Turbo", 2024, 2150000.00, "Available"),
                new vehicle("Toyota", "Fortuner", "V Diesel", 2024, 1957000.00, "Reserved"),
                new vehicle("Mazda", "CX-5", "Sport", 2023, 1950000.00, "Available"),
                new vehicle("Suzuki", "Jimny", "GLX AllGrip", 2024, 1285000.00, "Available"),
                new vehicle("Honda", "Civic", "RS Turbo", 2024, 1798000.00, "Available"),
                new vehicle("Mitsubishi", "Xpander", "GLS", 2023, 1129000.00, "Available"),
                new vehicle("Toyota", "Avanza", "E CVT", 2023, 959000.00, "Available"),
                new vehicle("Hyundai", "Creta", "GLS IVT", 2024, 1245000.00, "Available"),
                new vehicle("Ford", "Territory", "Titanium X", 2024, 1599000.00, "Available")
        );
        samplesLoaded = true;
    }

    private void applyFilters() {
        String selectedStatus = (statusFilterCombo == null || statusFilterCombo.getValue() == null)
                ? "All" : statusFilterCombo.getValue();
        String query = (searchField == null || searchField.getText() == null)
                ? "" : searchField.getText().trim().toLowerCase();

        filteredVehicles.setPredicate(v -> {
            boolean matchesStatus = selectedStatus.equals("All")
                    || (v.getStatus() != null && v.getStatus().equalsIgnoreCase(selectedStatus));

            boolean matchesQuery = query.isEmpty()
                    || (v.getBrand() != null && v.getBrand().toLowerCase().contains(query))
                    || (v.getModel() != null && v.getModel().toLowerCase().contains(query))
                    || (v.getName() != null && v.getName().toLowerCase().contains(query));

            return matchesStatus && matchesQuery;
        });
    }

    private void updateSummaryCards() {
        int total = vehicleList.size();
        int available = 0;
        int reservedCount = 0;

        for (vehicle v : vehicleList) {
            if ("Available".equalsIgnoreCase(v.getStatus())) {
                available++;
            } else if ("Reserved".equalsIgnoreCase(v.getStatus())) {
                reservedCount++;
            }
        }

        // Count completed sales from TransactionController + current Reserved vehicles
        int soldCount = TransactionController.getCompletedSalesCount();

        if (totalVehiclesLabel != null) totalVehiclesLabel.setText(String.valueOf(total));
        if (availableStockLabel != null) availableStockLabel.setText(String.valueOf(available));
        if (soldReservedLabel != null) soldReservedLabel.setText(String.valueOf(soldCount + reservedCount));
    }

    @FXML
    public void openAddVehicleModal() {
        openVehicleForm(null);
    }

    @FXML
    public void handleEditVehicle() {
        vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a vehicle to edit.");
            return;
        }
        openVehicleForm(selected);
    }

    private void openVehicleForm(vehicle existingVehicle) {
        try {
            URL url = getClass().getResource("/app/add.fxml");
            if (url == null) {
                showWarning("Missing File", "Could not find /app/add.fxml.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            controller.setDashboardController(this);
            if (existingVehicle != null) {
                controller.initEditData(existingVehicle);
            }

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle(existingVehicle == null ? "Add New Vehicle" : "Edit Vehicle");
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showWarning("Error", "Could not open vehicle form:\n" + e.getMessage());
        }
    }

    @FXML
    public void handleMarkAsSold() {
        vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a vehicle to mark as Sold.");
            return;
        }

        String clientName = promptClientName(selected);
        if (clientName != null) {
            selected.setStatus("Sold");
            TransactionController.recordSale(selected, clientName);
            vehicleList.remove(selected); // Remove from Dashboard list once moved to Transactions
            updateSummaryCards();
        }
    }

    @FXML
    public void handleDeleteVehicle() {
        vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a vehicle to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Vehicle");
        confirm.setHeaderText(null);
        confirm.setContentText("Remove " + selected.getAssetSummary() + " from inventory?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            vehicleList.remove(selected);
            updateSummaryCards();
        }
    }

    public void addVehicleToList(vehicle newVehicle) {
        if ("Sold".equalsIgnoreCase(newVehicle.getStatus())) {
            String clientName = promptClientName(newVehicle);
            if (clientName != null) {
                TransactionController.recordSale(newVehicle, clientName);
            }
        } else {
            vehicleList.add(newVehicle);
        }
        updateSummaryCards();
        vehicleTable.refresh();
    }

    public void updateVehicleInList(vehicle oldVehicle, vehicle updatedVehicle) {
        if ("Sold".equalsIgnoreCase(updatedVehicle.getStatus())) {
            String clientName = promptClientName(updatedVehicle);
            if (clientName != null) {
                TransactionController.recordSale(updatedVehicle, clientName);
                vehicleList.remove(oldVehicle); // Remove from Dashboard since it is now Sold
            }
        } else {
            int index = vehicleList.indexOf(oldVehicle);
            if (index >= 0) {
                vehicleList.set(index, updatedVehicle);
            }
        }
        updateSummaryCards();
        vehicleTable.refresh();
    }

    private String promptClientName(vehicle v) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Record Sold Vehicle");
        dialog.setHeaderText("Vehicle: " + v.getAssetSummary());
        dialog.setContentText("Enter client name:");

        Optional<String> input = dialog.showAndWait();
        if (input.isPresent()) {
            String name = input.get().trim();
            if (name.isEmpty()) {
                showWarning("Client Name Required", "Please enter a client name to move this vehicle to Transactions.");
                return null;
            }
            return name;
        }
        return null;
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}