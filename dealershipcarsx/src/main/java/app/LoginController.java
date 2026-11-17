package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check hardcoded admin credentials
        if (username.equals("admin") && password.equals("admin")) {
            try {
                // Load the dashboard view (which includes your sidebar and vehicle table)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();

                // Get current login stage
                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("VYC Motors - Admin Dashboard");
                stage.setResizable(false);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show error message if credentials are incorrect
            if (errorLabel != null) {
                errorLabel.setText("Invalid username or password!");
            } else {
                System.out.println("Invalid username or password!");
            }
        }
    }
}