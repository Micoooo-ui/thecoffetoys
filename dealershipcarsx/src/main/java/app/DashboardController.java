package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vehicle.Asset;
import vehicle.vehicle;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private TableView<vehicle> vehicleTable;
    @FXML private TableColumn<vehicle, String> brandColumn;
    @FXML private TableColumn<vehicle, String> modelColumn;
    @FXML private TableColumn<vehicle, String> nameColumn;
    @FXML private TableColumn<vehicle, Integer> yearColumn;
    @FXML private TableColumn<vehicle, Double> priceColumn;
    @FXML private TableColumn<vehicle, String> statusColumn;
    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private TextField searchField;

    @FXML private Label totalVehiclesLabel;
    @FXML private Label availableStockLabel;
    @FXML private Label soldReservedLabel;

    private ObservableList<vehicle> vehicleList = FXCollections.observableArrayList(
            new vehicle("Toyota", "Hilux", "Conquest", 2023, 1455000.0, "Available"),
            new vehicle("Ford", "Ranger", "Wildtrak", 2024, 1949000.0, "Reserved"),
            new vehicle("Mitsubishi", "Montero", "Sport", 2022, 1568000.0, "Available"),
            new vehicle("Toyota", "Vios", "G CVT", 2023, 1035000.0, "Available"),
            new vehicle("Honda", "CR-V", "V Turbo", 2024, 2150000.0, "Sold"),
            new vehicle("Nissan", "Navara", "PRO-4X", 2023, 1850000.0, "Available"),
            new vehicle("Toyota", "Fortuner", "V Diesel", 2024, 1957000.0, "Reserved"),
            new vehicle("Mazda", "CX-5", "Sport", 2023, 1950000.0, "Available"),
            new vehicle("Hyundai", "Tucson", "GLS", 2023, 1570000.0, "Sold"),
            new vehicle("Suzuki", "Jimny", "GLX AllGrip", 2024, 1285000.0, "Available"),
            new vehicle("Honda", "Civic", "RS Turbo", 2024, 1798000.0, "Available"),
            new vehicle("Mitsubishi", "Xpander", "GLS", 2023, 1129000.0, "Available"),
            new vehicle("Nissan", "Terra", "VL 4x4", 2024, 2439000.0, "Reserved"),
            new vehicle("Toyota", "Innova", "Zenix Q Hybrid", 2024, 1953000.0, "Sold"),
            new vehicle("Kia", "Stonic", "Style AT", 2023, 935000.0, "Available")
    );

    private FilteredList<vehicle> filteredVehicles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(col -> new TableCell<vehicle, Double>() {
            private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText("₱" + formatter.format(price));
                }
            }
        });

        filteredVehicles = new FilteredList<>(vehicleList, v -> true);
        vehicleTable.setItems(filteredVehicles);

        statusFilterCombo.setItems(FXCollections.observableArrayList("All", "Available", "Reserved", "Sold"));
        statusFilterCombo.setValue("All");

        statusFilterCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());

        updateStatistics();
    }

    private void updateFilter() {
        String selectedStatus = statusFilterCombo.getValue();
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();

        filteredVehicles.setPredicate(v -> {
            boolean matchesStatus = selectedStatus == null || selectedStatus.equals("All")
                    || v.getStatus().equalsIgnoreCase(selectedStatus);
            boolean matchesSearch = searchText.isEmpty()
                    || v.getBrand().toLowerCase().contains(searchText)
                    || v.getModel().toLowerCase().contains(searchText)
                    || v.getName().toLowerCase().contains(searchText);
            return matchesStatus && matchesSearch;
        });
    }

    @FXML
    public void handleResetFilters() {
        if (searchField != null) {
            searchField.clear();
        }
        if (statusFilterCombo != null) {
            statusFilterCombo.setValue("All");
        }
        updateFilter();
    }

    private void updateStatistics() {
        int total = vehicleList.size();
        int availableCount = 0;
        int soldReservedCount = 0;

        for (vehicle v : vehicleList) {
            String status = v.getStatus() == null ? "" : v.getStatus().trim();
            if (status.equalsIgnoreCase("Available")) {
                availableCount++;
            } else if (status.equalsIgnoreCase("Reserved") || status.equalsIgnoreCase("Sold")) {
                soldReservedCount++;
            }
        }

        totalVehiclesLabel.setText(String.valueOf(total));
        availableStockLabel.setText(String.valueOf(availableCount));
        soldReservedLabel.setText(String.valueOf(soldReservedCount));
    }

    public void addVehicleToList(vehicle v) {
        vehicleList.add(v);
        updateFilter();
        updateStatistics();

        String transId = "TXN-" + (1000 + TransactionController.globalTransactionList.size() + 1);
        String vehicleDetails = v.getBrand() + " " + v.getModel() + " " + v.getName();
        TransactionController.globalTransactionList.add(
                new Transaction(transId, vehicleDetails, "Walk-in Client", "Inventory Addition", v.getPrice(), LocalDate.now().toString())
        );
    }

    public void updateVehicleInList(vehicle oldVehicle, vehicle newVehicle) {
        int index = vehicleList.indexOf(oldVehicle);
        if (index >= 0) {
            vehicleList.set(index, newVehicle);
            updateFilter();
            updateStatistics();

            String updatedDetails = newVehicle.getBrand() + " " + newVehicle.getModel() + " " + newVehicle.getName();
            for (Transaction t : TransactionController.globalTransactionList) {
                if (t.getVehicleDetails().contains(oldVehicle.getModel())) {
                    t.setVehicleDetails(updatedDetails);
                    t.setAmount(newVehicle.getPrice());
                }
            }
        }
    }

    @FXML
    public void openAddVehicleModal() {
        try {
            URL fileUrl = getClass().getResource("/app/add.fxml");
            if (fileUrl == null) {
                showAlert("File Error", "Could not find add.fxml in resources/app/");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            if (controller != null) {
                controller.setDashboardController(this);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Loading Error", "Could not open Add Vehicle window: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditVehicle() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle row from the table first!");
            return;
        }

        try {
            URL fileUrl = getClass().getResource("/app/add.fxml");
            if (fileUrl == null) {
                showAlert("File Error", "Could not find add.fxml in resources/app/");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            if (controller != null) {
                controller.setDashboardController(this);
                controller.initEditData(selectedVehicle);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatistics();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Loading Error", "Failed to open edit window: " + e.getMessage());
        }
    }

    @FXML
    public void handleMarkAsReserved() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle row from the table to mark as Reserved!");
            return;
        }

        selectedVehicle.setStatus("Reserved");
        vehicleTable.refresh();
        updateStatistics();
        updateFilter();

        String transId = "TXN-" + (1000 + TransactionController.globalTransactionList.size() + 1);
        String vehicleDetails = selectedVehicle.getBrand() + " " + selectedVehicle.getModel() + " " + selectedVehicle.getName();
        Transaction newTxn = new Transaction(transId, vehicleDetails, "Reserved Client", "Reservation", selectedVehicle.getPrice(), LocalDate.now().toString());
        TransactionController.globalTransactionList.add(newTxn);

        Asset vehicleAsset = selectedVehicle;
        Asset txnAsset = newTxn;
        showAlert("Marked as Reserved", vehicleAsset.getAssetSummary() + "\n" + txnAsset.getAssetSummary());
    }

    @FXML
    public void handleMarkAsSold() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("No Selection", "Please select a vehicle row from the table to mark as Sold!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("Direct Buyer");
        dialog.setTitle("Customer Name");
        dialog.setHeaderText("Record Vehicle Sale");
        dialog.setContentText("Enter customer name:");

        Optional<String> input = dialog.showAndWait();
        if (!input.isPresent()) {
            return;
        }

        String customerName = input.get().trim();
        if (customerName.isEmpty()) {
            customerName = "Direct Buyer";
        }

        selectedVehicle.setStatus("Sold");
        vehicleTable.refresh();
        updateStatistics();
        updateFilter();

        String transId = "TXN-" + (1000 + TransactionController.globalTransactionList.size() + 1);
        String vehicleDetails = selectedVehicle.getBrand() + " " + selectedVehicle.getModel() + " " + selectedVehicle.getName();
        Transaction newTxn = new Transaction(transId, vehicleDetails, customerName, "Sale", selectedVehicle.getPrice(), LocalDate.now().toString());
        TransactionController.globalTransactionList.add(newTxn);

        Asset vehicleAsset = selectedVehicle;
        Asset txnAsset = newTxn;
        showAlert("Marked as Sold",
                "Customer: " + customerName + "\n" +
                        vehicleAsset.getAssetSummary() + "\n" +
                        txnAsset.getAssetSummary());
    }

    @FXML
    public void handleDeleteVehicle() {
        vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleList.remove(selectedVehicle);
            updateFilter();
            updateStatistics();

            TransactionController.globalTransactionList.removeIf(t -> t.getVehicleDetails().contains(selectedVehicle.getModel()));
        } else {
            showAlert("No Selection", "Please select a vehicle row from the table first to delete!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}