package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class SidebarController {

    @FXML
    public void handleNavigateDashboard(ActionEvent event) {
        switchScene(event, "dashboard.fxml");
    }

    @FXML
    public void handleNavigateTransactions(ActionEvent event) {
        switchScene(event, "transaction.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to log out and exit the application?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            URL fileUrl = getClass().getResource(fxmlFile);
            if (fileUrl == null) {
                showAlert("Navigation Error", "Could not find file: " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Loading Error", "Failed to load " + fxmlFile + "\n\nReason: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}