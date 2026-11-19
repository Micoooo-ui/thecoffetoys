package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Update these default credentials if your class uses a specific admin login
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField == null || usernameField.getText() == null
                ? "" : usernameField.getText().trim();
        String password = passwordField == null || passwordField.getText() == null
                ? "" : passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Please enter both your username and password.");
            return;
        }

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            openDashboard(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Account", "Wrong username or password. Please try again.");
        }
    }

    @FXML
    public void handleForgotPassword(ActionEvent event) {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Forgot Password",
                "Default admin credentials: \nUsername = " + VALID_USERNAME + "\nPassword = " + VALID_PASSWORD
        );
    }

    private void openDashboard(ActionEvent event) {
        try {
            URL fileUrl = getClass().getResource("/app/dashboard.fxml");
            if (fileUrl == null) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not find /app/dashboard.fxml.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Torque and Tread Dealership - Dashboard");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not open dashboard:\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}