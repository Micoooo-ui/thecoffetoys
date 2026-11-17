package app;

import javafx.beans.property.*;

public class Transaction {
    private final StringProperty transactionId;
    private final StringProperty vehicleDetails;
    private final StringProperty customerName;
    private final StringProperty type; // e.g., "Sale", "Reservation"
    private final DoubleProperty amount;
    private final StringProperty date;

    public Transaction(String transactionId, String vehicleDetails, String customerName, String type, double amount, String date) {
        this.transactionId = new SimpleStringProperty(transactionId);
        this.vehicleDetails = new SimpleStringProperty(vehicleDetails);
        this.customerName = new SimpleStringProperty(customerName);
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public String getTransactionId() { return transactionId.get(); }
    public StringProperty transactionIdProperty() { return transactionId; }

    public String getVehicleDetails() { return vehicleDetails.get(); }
    public StringProperty vehicleDetailsProperty() { return vehicleDetails; }

    public String getCustomerName() { return customerName.get(); }
    public StringProperty customerNameProperty() { return customerName; }

    public String getType() { return type.get(); }
    public StringProperty typeProperty() { return type; }

    public double getAmount() { return amount.get(); }
    public DoubleProperty amountProperty() { return amount; }

    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }
}