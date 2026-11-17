package vehicle;

import javafx.beans.property.*;

public class vehicle {
    private final StringProperty brand;
    private final StringProperty model;
    private final StringProperty name;
    private final IntegerProperty year;
    private final DoubleProperty price;
    private final StringProperty status;

    public vehicle(String brand, String model, String name, int year, double price, String status) {
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.name = new SimpleStringProperty(name);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.status = new SimpleStringProperty(status);
    }

    // Getters for Property values
    public String getBrand() { return brand.get(); }
    public void setBrand(String brand) { this.brand.set(brand); }
    public StringProperty brandProperty() { return brand; }

    public String getModel() { return model.get(); }
    public void setModel(String model) { this.model.set(model); }
    public StringProperty modelProperty() { return model; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public int getYear() { return year.get(); }
    public void setYear(int year) { this.year.set(year); }
    public IntegerProperty yearProperty() { return year; }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
}