package wordle.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import wordle.Util.AlertUtil;
import wordle.Service.*;
import java.io.*;

/**
 * Controller class for the user registration view.
 * Handles registering new users and navigating to the login screen.
 */
public class RegisterController {

    // FXML fields for user inputs and buttons
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML Button loginButton;

    

    /**
     * Handles user registration logic when the "Register" button is clicked.
     * Validates inputs, registers the user using PlayerRepository,
     * and redirects to the login screen upon success.
     */
 @FXML
    private void handleRegister(ActionEvent evt) throws IOException {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();

        if (u.isEmpty() || p.isEmpty()) {
            AlertUtil.warn("Input Error", "Username and Password must not be empty.");
            return;
        }

        try {
        	// Attempt to register the user
            PlayerRepository.register(u, p);
            AlertUtil.info("Success", "Registered " + u);
        } catch (IllegalArgumentException ex) {
        	// Show error if user already exists
            AlertUtil.warn("Duplicate Error", ex.getMessage());
            return;
        }

        // Navigate to login screen
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/wordle/View/LoginView.fxml")
        );
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }


 /**
  * Navigates back to the login screen wehn the "Login" button is clicked.
  */
    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Parent registerRoot = FXMLLoader.load(
                getClass().getResource("/wordle/View/LoginView.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource())
                            .getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.warn("Error", "Could not load registration screen.");
        }
    }
}
