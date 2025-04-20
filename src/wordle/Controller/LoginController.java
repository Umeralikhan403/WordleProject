package wordle.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import wordle.Service.PlayerRepository;
import wordle.Util.AlertUtil;

import java.io.*;


public class LoginController {

    @FXML private TextField    usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button guestButton;

  

    /** Fired when the user clicks LOGIN */
    @FXML
    private void handleLogin(ActionEvent evt) {
    	PlayerRepository.printPlayers();
        var user = usernameField.getText().trim();
        var pass = passwordField.getText().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            AlertUtil.warn("Input Error", "Username and Password must not be empty.");
            return;
        }

        if (PlayerRepository.authenticate(user, pass).isPresent()) {
            AlertUtil.info("Welcome", "Hello, " + user + "!");
            // TODO: switch to your main/game view
            try {
                // 1. Load the FXML
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/wordle/View/MainView.fxml")
                );
                Parent mainRoot = loader.load();

                // 2. Grab the current window (Stage)
                Stage stage = (Stage) ((Node) evt.getSource())
                                      .getScene()
                                      .getWindow();

                // 3. Swap in the new Scene
                Scene mainScene = new Scene(mainRoot);
                stage.setScene(mainScene);
                stage.setTitle("Wordle — Main");    // optional: update window title
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtil.warn("Navigation Error", "Could not load the main view.");
            }
            
            
        } else {
            AlertUtil.warn("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleGoToRegister(ActionEvent evt) throws IOException {
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(
            getClass().getResource("/wordle/View/RegisterView.fxml")
        );
        stage.setScene(new Scene(root));
    }

    /** Fired when the user clicks Continue as Guest */
    @FXML
    private void handleGuest(ActionEvent event) {
        // TODO: load guest view or game directly
        AlertUtil.info( "Guest Mode", 
                  "Entering as Guest. Scores won’t be saved.");
    }


}
