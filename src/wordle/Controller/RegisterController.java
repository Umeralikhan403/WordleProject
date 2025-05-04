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


public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button registerButton;
    
    @FXML Button loginButton;



 @FXML
    private void handleRegister(ActionEvent evt) throws IOException {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();

        if (u.isEmpty() || p.isEmpty()) {
            AlertUtil.warn("Input Error", "Username and Password must not be empty.");
            return;
        }

        try {
            PlayerRepository.register(u, p);
            AlertUtil.info("Success", "Registered " + u);
        } catch (IllegalArgumentException ex) {
            AlertUtil.warn("Duplicate Error", ex.getMessage());
            return;
        }

        // go to login
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/wordle/View/LoginView.fxml")
        );
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    
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
