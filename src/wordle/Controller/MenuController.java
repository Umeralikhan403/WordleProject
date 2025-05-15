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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import wordle.Models.GameType;
import wordle.Service.Session;
import wordle.Util.AlertUtil;

/**
 * The MenuController sets up and handles events from the menu view.
 */
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

	/**
	 * Initialise the menu view by obtaining the username and putting it in the label.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    var player = Session.getCurrentPlayer();
	    if (player != null) {
	        helloLabel.setText("Hello, " + player.getUsername() + "!");
	    } else {
	    	helloLabel.setText("Hello, Guest!");
	    }
	}
	
	/**
	 * Close the menu when the button is clicked.
	 * Checks which game was chosen and loads back to the correct game.
	 * @param e - the action event
	 * @throws IOException - if the FXML file cannot be loaded
	 */
	@FXML
	private void closeMenuHandler(ActionEvent e) throws IOException {
		if (ChooseGameController.getGameType() == GameType.WORDLE) {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WordleView.fxml"));
			stage.setScene(new Scene(root));
		} else if (ChooseGameController.getGameType() == GameType.NERDLE) {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/NerdleView.fxml"));
			stage.setScene(new Scene(root));
		}
	}

	/**
	 * Loads the statistics view when the button is clicked
	 * @param e - the action event
	 */
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

	/**
	 * Opens a dialog for the user to confirm if they want to reset the scores.
	 * @param e - the action event
	 */
	@FXML
	private void resetScoresHandler(ActionEvent e) {
		 alertDialogBuilder(AlertType.CONFIRMATION, "Reset Scores", "Confirmation",
				"Are you sure you want to reset the scores?");
		 Alert alert = new Alert(AlertType.CONFIRMATION);
		 alert.setTitle("Reset Scores");
		 alert.setHeaderText("Confirmation");
		 alert.setContentText("Are you sure you want to reset the scores?");
		 alert.showAndWait().ifPresent(response -> {
			 if (response == ButtonType.OK) {
				 if (Session.getCurrentPlayer() != null) {
						Session.getCurrentPlayer().resetScores();
						AlertUtil.info("Reset Scores", "Scores have been reset.");
					} else {
						AlertUtil.warn("Reset Scores", "No player is currently logged in.");
					}
			 } else {
				 alert.close();
			 }
		 });
	}
	
	/**
	 * Asks the user whether they want to sign out, and then signs them out if they confirm.
	 * @param e - the action event
	 */
	@FXML
	private void signOutHandler(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Sign Out");
		alert.setHeaderText("Confirmation");
		alert.setContentText("Are you sure you want to sign out?");
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				Session.signOut();
				try {
					Session.signOut();
					System.out.println("Signed out");
					Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/loginView.fxml"));
					stage.setScene(new Scene(root));
				} catch (IOException ex) {
					ex.printStackTrace();
					AlertUtil.warn("Navigation Error", "Login view failed to load.");
				}
			} else {
				alert.close();
			}
		});
	}
	
	/**
	 * Switches to the choose game view when the button is clicked.
	 * @param e - the action event
	 */
	@FXML
	private void switchGameHandler(ActionEvent e) {
		try {
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/ChooseGameView.fxml"));
			stage.setScene(new Scene(root));
		} catch (IOException ex) {
			ex.printStackTrace();
			AlertUtil.warn("Navigation Error", "Choose game view failed to load.");
		}
	}

	/**
	 * Closes the application when the button is clicked.
	 * @param e - the action event
	 */
	@FXML
	private void menuQuitHandler(ActionEvent e) {
		// Close the application
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.close();
	}

	// helper method to build dialogs
	/**
	 * Creates an alert dialog with the specified parameters.
	 * @param type - the type of alert
	 * @param title - the title of the alert
	 * @param header - the header text of the alert
	 * @param content - the content text of the alert
	 */
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
