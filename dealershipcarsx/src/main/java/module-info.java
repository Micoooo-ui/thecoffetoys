module dealershipcarsx {
    requires javafx.controls;
    requires javafx.fxml;

    opens app to javafx.fxml;
    opens vehicle to javafx.fxml;

    exports app;
    exports vehicle;
}