package wordle.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import wordle.Service.Session;
import wordle.Util.AlertUtil;

public class MenuController implements Initializable {

	@FXML
	private Button btnCloseMenu;
	@FXML
	private Button btnViewScores;
	@FXML
	private Button btnResetScores;
	@FXML
	private Button btnSignOut;
	@FXML
	private Button btnQuit;
	@FXML private Label helloLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    var player = Session.getCurrentPlayer();
	    if (player != null) {
	        helloLabel.setText("Hello, " + player.getUsername() + "!");
	    } else {
	    	helloLabel.setText("Hello, Guest!");
	    }
	}
	 
	@FXML
	private void closeMenuHandler(ActionEvent e) throws IOException {
		if (ChooseGameController.getGameType() == 1) {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WordleView.fxml"));
			stage.setScene(new Scene(root));
		} else if (ChooseGameController.getGameType() == 2) {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/NerdleView.fxml"));
			stage.setScene(new Scene(root));
		}
	}

	@FXML
	private void viewScoresHandler(ActionEvent e) {
		try {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/StatisticsView.fxml"));
			stage.setScene(new Scene(root));
		} catch (IOException ex) {
			ex.printStackTrace();
			AlertUtil.warn("Navigation Error", "Statistics failed to load.");
		}
	}

	@FXML
	private void resetScoresHandler(ActionEvent e) {
		 alertDialogBuilder(AlertType.CONFIRMATION, "Reset Scores", "Confirmation",
				"Are you sure you want to reset the scores?");
		if (Session.getCurrentPlayer() != null) {
			Session.getCurrentPlayer().resetScores();
			AlertUtil.info("Reset Scores", "Scores have been reset.");
		} else {
			AlertUtil.warn("Reset Scores", "No player is currently logged in.");
		}
	}

	@FXML
	private void signOutHandler(ActionEvent e) {
		alertDialogBuilder(AlertType.CONFIRMATION, "Sign Out", "Confirmation", "Are you sure you want to sign out?");
		try {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/loginView.fxml"));
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

	// helper method to build dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
