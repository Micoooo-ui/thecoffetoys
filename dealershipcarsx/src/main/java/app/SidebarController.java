package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    private void handleNavigateDashboard(javafx.event.ActionEvent event) {
        loadScene(event, "dashboard.fxml", "VYC Motors - Admin Dashboard");
    }

    @FXML
    private void handleNavigateTransactions(javafx.event.ActionEvent event) {
        loadScene(event, "transaction.fxml", "VYC Motors - Transactions");
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        loadScene(event, "login.fxml", "VYC Motors - Admin Log In");
    }

    private void loadScene(javafx.event.ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Button sourceButton = (Button) event.getSource();
            Stage stage = (Stage) sourceButton.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}