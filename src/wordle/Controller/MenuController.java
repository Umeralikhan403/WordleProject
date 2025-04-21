package wordle.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import wordle.Util.AlertUtil;

public class MenuController {

	@FXML private Button btnCloseMenu;
	@FXML private Button btnViewScores;
	@FXML private Button btnResetScores;
	@FXML private Button btnSignOut;
	@FXML private Button btnQuit;
	
	@FXML
	private void closeMenuHandler(ActionEvent e) throws IOException {
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(
            getClass().getResource("/wordle/View/MainView.fxml")
        );
        stage.setScene(new Scene(root));
	}
	
	@FXML
	private void viewScoresHandler(ActionEvent e) {
		AlertUtil.info("Scores", "No scores to display.");
	}
	
	@FXML
	private void resetScoresHandler(ActionEvent e) {
		alertDialogBuilder(AlertType.CONFIRMATION, "Reset Scores", "Confirmation", "Are you sure you want to reset the scores?");
	}
	
	@FXML
	private void signOutHandler(ActionEvent e) {
		alertDialogBuilder(AlertType.CONFIRMATION, "Sign Out", "Confirmation", "Are you sure you want to sign out?");
		try {
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(
				getClass().getResource("/wordle/View/loginView.fxml")
			);
			stage.setScene(new Scene(root));
		} catch (IOException ex) {
			ex.printStackTrace();
			AlertUtil.warn("Navigation Error", "Login view failed to load.");
		}
	}
	
	@FXML
	private void menuQuitHandler(ActionEvent e) {
		// Close the application
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.close();
	}
	
	//helper method to build dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
