package vehicle;

// ABSTRACTION & INHERITANCE: Abstract base class acting as a blueprint for inventory assets
public abstract class Asset {

    // Abstract method that subclasses must override (POLYMORPHISM)
    public abstract String getAssetSummary();
}