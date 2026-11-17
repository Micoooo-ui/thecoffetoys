package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> idColumn;
    @FXML private TableColumn<Transaction, String> vehicleColumn;
    @FXML private TableColumn<Transaction, String> customerColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> dateColumn;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList(
            new Transaction("TXN-1001", "Toyota Hilux Conquest", "Juan Dela Cruz", "Sale", 1450000.0, "2026-11-15"),
            new Transaction("TXN-1002", "Ford Ranger Wildtrak", "Maria Santos", "Reservation", 50000.0, "2026-11-16"),
            new Transaction("TXN-1003", "Mitsubishi Montero Sport", "Carlos Gomez", "Sale", 1350000.0, "2026-11-17")
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleDetails"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        transactionTable.setItems(transactionList);
    }
}