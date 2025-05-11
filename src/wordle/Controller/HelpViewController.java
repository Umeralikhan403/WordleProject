package wordle.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpViewController {

	/**
	 * controller for the Help View Screen.
	 * provides functionality to close the help screen and return to the login view.
	 */
	@FXML private Button btnCloseHelp;
	
	 /**
     * Handles the action when the "Close Help" button is clicked.
     * Navigates the user back to the Login screen.
     * 
     * @param e the action event triggered by clicking the button
     * @throws IOException if the FXML file for the login view cannot be loaded
     */
	@FXML
	private void closeHelpHandler(ActionEvent e) throws IOException {
		 // load the LoginView FXML
		Parent registerRoot = FXMLLoader.load(getClass().getResource("/wordle/View/LoginView.fxml"));
        // Get current stage from the event and set the new scene
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
	}
}
