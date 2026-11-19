package app;

import vehicle.Asset;
import java.text.DecimalFormat;

public class Transaction extends Asset {
    private String transactionId;
    private String vehicleDetails;
    private String customerName;
    private String type;
    private double amount;
    private String date;

    public Transaction(String transactionId, String vehicleDetails, String customerName, String type, double amount, String date) {
        this.transactionId = transactionId;
        this.vehicleDetails = vehicleDetails;
        this.customerName = customerName;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String getAssetSummary() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return transactionId + " - " + type + " (" + vehicleDetails + ") - ₱" + df.format(amount);
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}