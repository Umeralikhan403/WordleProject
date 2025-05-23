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
import wordle.Models.Player;
import wordle.Service.PlayerRepository;
import wordle.Service.Session;
import wordle.Util.AlertUtil;

import java.io.*;


/**
 * controller for handling the login screen logic.
 * supports login with username and password, guest login, navigation to register screen, and help screen.
 */
public class LoginController {

	// FXML controls
    @FXML private TextField    usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button guestButton;
    @FXML private Button btnOpenHelp;
 
    /**
     * handles user login using username and password
     * if successful, stores the player in the session and loads the main Choose Game view.
     * If failed, shows a warning.
     */
    @FXML
    private void handleLogin(ActionEvent evt) {
    	PlayerRepository.printPlayers();
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            AlertUtil.warn("Input Error", "Username and Password must not be empty.");
            return;
        }

        var opt = PlayerRepository.authenticate(user, pass);
        if (opt.isPresent()) {
        	//Store the login player into session!
        	Player player = opt.get();
            Session.setCurrentPlayer(player);
            		
            //AlertUtil.info("Welcome", "Hello, " + user + "!");
            try {
                // 1. Load the FXML
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/wordle/View/ChooseGameView.fxml")
                );
                Parent mainRoot = loader.load();

                // 2. Grab the current window (Stage)
                Stage stage = (Stage) ((Node) evt.getSource())
                                      .getScene()
                                      .getWindow();

                // 3. Swap in the new Scene
                Scene mainScene = new Scene(mainRoot);
                stage.setScene(mainScene);
                stage.setTitle("Wordle — Main");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtil.warn("Navigation Error", "Could not load the main view.");
            }
            
            
        } else {
            AlertUtil.warn("Login Failed", "Invalid username or password.");
        }
    }

    /**
     * navigates the user to the Register screen.
     */
    @FXML
    private void handleGoToRegister(ActionEvent evt) throws IOException {
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(
            getClass().getResource("/wordle/View/RegisterView.fxml")
        );
        stage.setScene(new Scene(root));
    }

    /**
     * Handles "Continue as Guest" button click.
     * Navigates directly to the game view without saving scores.
     */
    @FXML
    private void handleGuest(ActionEvent e) {
        // TODO: load guest view or game directly
        AlertUtil.info( "Guest Mode", 
                  "Entering as Guest. Scores won’t be saved.");
        try {
            // 1. Load the FXML
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/wordle/View/ChooseGameView.fxml")
            );
            Parent mainRoot = loader.load();

            // 2. Grab the current window (Stage)
            Stage stage = (Stage) ((Node) e.getSource())
                                  .getScene()
                                  .getWindow();

            // 3. Swap in the new Scene
            Scene mainScene = new Scene(mainRoot);
            stage.setScene(mainScene);
            stage.setTitle("Wordle — Main");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtil.warn("Navigation Error", "Could not load the main view.");
        }
    }
    
    /**
     * Opens the Helo View when the Help button is clicked.
     */
    @FXML
    private void openHelpHandler(ActionEvent e) {
		try {
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(
				getClass().getResource("/wordle/View/HelpView.fxml")
			);
			stage.setScene(new Scene(root));
		} catch (IOException ex) {
			ex.printStackTrace();
			AlertUtil.warn("Navigation Error", "Help view failed to load.");
		}
	}
}
