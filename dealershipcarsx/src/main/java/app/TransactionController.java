package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> idColumn;
    @FXML private TableColumn<Transaction, String> vehicleColumn;
    @FXML private TableColumn<Transaction, String> customerColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> dateColumn;

    @FXML private Label totalRevenueLabel;
    @FXML private Label completedSalesLabel;
    @FXML private Label activePurchasesLabel;

    @FXML private TextField searchTransactionField;
    @FXML private ComboBox<String> transactionFilterCombo;

    public static ObservableList<Transaction> globalTransactionList = FXCollections.observableArrayList(
            new Transaction("TXN-1001", "Toyota Hilux Conquest", "Juan Dela Cruz", "Inventory Addition", 1455000.0, "2026-11-01"),
            new Transaction("TXN-1002", "Ford Ranger Wildtrak", "Maria Santos", "Reservation", 1949000.0, "2026-11-02"),
            new Transaction("TXN-1003", "Mitsubishi Montero Sport", "Carlos Gomez", "Inventory Addition", 1568000.0, "2026-11-03"),
            new Transaction("TXN-1004", "Toyota Vios G CVT", "Ana Reyes", "Inventory Addition", 1035000.0, "2026-11-04"),
            new Transaction("TXN-1005", "Honda CR-V V Turbo", "Pedro Penduko", "Sale", 2150000.0, "2026-11-05"),
            new Transaction("TXN-1006", "Nissan Navara PRO-4X", "Jose Rizal", "Inventory Addition", 1850000.0, "2026-11-06"),
            new Transaction("TXN-1007", "Toyota Fortuner V Diesel", "Andres Bonifacio", "Reservation", 1957000.0, "2026-11-07"),
            new Transaction("TXN-1008", "Mazda CX-5 Sport", "Emilio Aguinaldo", "Inventory Addition", 1950000.0, "2026-11-08"),
            new Transaction("TXN-1009", "Hyundai Tucson GLS", "Apolinario Mabini", "Sale", 1570000.0, "2026-11-09"),
            new Transaction("TXN-1010", "Suzuki Jimny GLX AllGrip", "Gabriela Silang", "Inventory Addition", 1285000.0, "2026-11-10")
    );

    private FilteredList<Transaction> filteredTransactions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleDetails"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(col -> new TableCell<Transaction, Double>() {
            private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText("₱" + formatter.format(amount));
                }
            }
        });

        filteredTransactions = new FilteredList<>(globalTransactionList, t -> true);
        transactionTable.setItems(filteredTransactions);

        transactionFilterCombo.setItems(FXCollections.observableArrayList("All", "Sale", "Reservation", "Inventory Addition"));
        transactionFilterCombo.setValue("All");

        transactionFilterCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        if (searchTransactionField != null) {
            searchTransactionField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        }

        updateTransactionStatistics();
    }

    private void updateFilter() {
        String selectedType = transactionFilterCombo.getValue();
        String searchText = searchTransactionField == null || searchTransactionField.getText() == null
                ? "" : searchTransactionField.getText().toLowerCase().trim();

        filteredTransactions.setPredicate(txn -> {
            boolean matchesType = selectedType == null || selectedType.equals("All")
                    || txn.getType().equalsIgnoreCase(selectedType);
            boolean matchesSearch = searchText.isEmpty()
                    || txn.getTransactionId().toLowerCase().contains(searchText)
                    || txn.getVehicleDetails().toLowerCase().contains(searchText)
                    || txn.getCustomerName().toLowerCase().contains(searchText);
            return matchesType && matchesSearch;
        });
    }

    private void updateTransactionStatistics() {
        double totalRevenue = 0.0;
        int completedSales = 0;
        int activePurchases = 0;

        for (Transaction t : globalTransactionList) {
            String type = t.getType() == null ? "" : t.getType().trim();
            if (type.equalsIgnoreCase("Sale")) {
                totalRevenue += t.getAmount();
                completedSales++;
            } else if (type.equalsIgnoreCase("Reservation")) {
                activePurchases++;
            }
        }

        DecimalFormat revFormatter = new DecimalFormat("#,##0.00");
        totalRevenueLabel.setText("₱" + revFormatter.format(totalRevenue));
        completedSalesLabel.setText(String.valueOf(completedSales));
        activePurchasesLabel.setText(String.valueOf(activePurchases));
    }
}