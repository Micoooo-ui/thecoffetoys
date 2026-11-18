package vehicle;

// INHERITANCE: vehicle inherits from the abstract Asset class
public class vehicle extends Asset {

    // ENCAPSULATION: Private fields protected from direct outside access
    private String brand;
    private String model;
    private String name;
    private int year;
    private double price;
    private String status;

    public vehicle(String brand, String model, String name, int year, double price, String status) {
        this.brand = brand;
        this.model = model;
        this.name = name;
        this.year = year;
        this.price = price;
        this.status = status;
    }

    // POLYMORPHISM: Overriding the abstract method from Asset with specific vehicle behavior
    @Override
    public String getAssetSummary() {
        return year + " " + brand + " " + model + " (" + status + ")";
    }

    // Getters and Setters (ENCAPSULATION)
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}