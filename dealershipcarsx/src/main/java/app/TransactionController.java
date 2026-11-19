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
import vehicle.vehicle;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
            new Transaction("TXN-1001", "Toyota Hilux Conquest", "Juan Dela Cruz", "Sale", 1455000.00, "2026-11-01"),
            new Transaction("TXN-1002", "Ford Ranger Wildtrak", "Maria Santos", "Sale", 1949000.00, "2026-11-02"),
            new Transaction("TXN-1003", "Mitsubishi Montero Sport", "Carlos Gomez", "Sale", 1568000.00, "2026-11-03"),
            new Transaction("TXN-1004", "Toyota Vios G CVT", "Ana Reyes", "Sale", 1035000.00, "2026-11-04"),
            new Transaction("TXN-1005", "Honda CR-V V Turbo", "Pedro Penduko", "Sale", 2150000.00, "2026-11-05")
    );

    private FilteredList<Transaction> filteredTransactions;
    private final DecimalFormat currencyFormatter = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleDetails"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(col -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText("₱" + currencyFormatter.format(amount));
                }
            }
        });

        // Show only "Sale" records in the transaction view
        filteredTransactions = new FilteredList<>(globalTransactionList, this::isSaleTransaction);
        transactionTable.setItems(filteredTransactions);

        transactionFilterCombo.setItems(FXCollections.observableArrayList("All", "Sale"));
        transactionFilterCombo.setValue("All");

        transactionFilterCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        if (searchTransactionField != null) {
            searchTransactionField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        }

        updateTransactionStatistics();
    }

    private boolean isSaleTransaction(Transaction txn) {
        return txn != null && txn.getType() != null && txn.getType().trim().equalsIgnoreCase("Sale");
    }

    public static void recordSale(vehicle soldVehicle, String customerName) {
        int nextCode = 1001 + globalTransactionList.size();
        String txnId = "TXN-" + nextCode;
        String vehicleDetails = soldVehicle.getBrand() + " " + soldVehicle.getModel() + " " + soldVehicle.getName();
        String dateStr = LocalDate.now().toString();

        globalTransactionList.add(new Transaction(
                txnId,
                vehicleDetails,
                customerName,
                "Sale",
                soldVehicle.getPrice(),
                dateStr
        ));
    }

    public static int getCompletedSalesCount() {
        int count = 0;
        for (Transaction t : globalTransactionList) {
            if (t.getType() != null && t.getType().trim().equalsIgnoreCase("Sale")) {
                count++;
            }
        }
        return count;
    }

    private void updateFilter() {
        String selectedType = transactionFilterCombo.getValue();
        String searchText = searchTransactionField == null || searchTransactionField.getText() == null
                ? "" : searchTransactionField.getText().toLowerCase().trim();

        filteredTransactions.setPredicate(txn -> {
            if (!isSaleTransaction(txn)) {
                return false;
            }

            boolean matchesType = selectedType == null
                    || selectedType.equals("All")
                    || txn.getType().trim().equalsIgnoreCase(selectedType);

            boolean matchesSearch = searchText.isEmpty()
                    || (txn.getTransactionId() != null && txn.getTransactionId().toLowerCase().contains(searchText))
                    || (txn.getVehicleDetails() != null && txn.getVehicleDetails().toLowerCase().contains(searchText))
                    || (txn.getCustomerName() != null && txn.getCustomerName().toLowerCase().contains(searchText));

            return matchesType && matchesSearch;
        });
    }

    private void updateTransactionStatistics() {
        double totalRevenue = 0.0;
        int completedSales = 0;

        for (Transaction t : globalTransactionList) {
            if (isSaleTransaction(t)) {
                totalRevenue += t.getAmount();
                completedSales++;
            }
        }

        if (totalRevenueLabel != null) {
            totalRevenueLabel.setText("₱" + currencyFormatter.format(totalRevenue));
        }
        if (completedSalesLabel != null) {
            completedSalesLabel.setText(String.valueOf(completedSales));
        }
        if (activePurchasesLabel != null) {
            activePurchasesLabel.setText("0");
        }
    }
}