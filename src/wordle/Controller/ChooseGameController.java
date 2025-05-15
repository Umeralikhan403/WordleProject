package wordle.Controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wordle.Models.GameType;
import javafx.event.ActionEvent;

/**
 * This is the controller for the Choose Game Screen. Allows user to select
 * either Wordle or Nerdle game. Also manages hover effects and stores selected
 * game type.
 */

public class ChooseGameController {

	@FXML
	private Button wordleButton;

	@FXML
	private Button nerdleButton;

	private static GameType gameType;

	/**
	 * THIS Handles starting the Wordle game. Loads the Wordle game view and sets
	 * the gameType to 1.
	 */
	@FXML
	private void startWordle(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WordleView.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
			gameType = GameType.WORDLE; // Wordle
			// Set the menu view for Wordle
			// Scene menuScene =
			// FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
			// WordleViewController.setMenuView(menuScene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * THIS handles the starting of the Nerdle game. Loads the Nerdle game view and
	 * sets the gameType to 2.
	 */
	@FXML
	private void startNerdle(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/NerdleView.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
			gameType = GameType.NERDLE; // Nerdle
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static GameType getGameType() {
		return gameType;
	}

	/**
	 * THIS handles the hover enter effect on Wordle button.
	 */
	@FXML
	private void wordleHoverEnter(MouseEvent event) {
		wordleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #6fb96c; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	/**
	 * THIS handles the hover exit effect on Wordle button.
	 */
	@FXML
	private void wordleHoverExit(MouseEvent event) {
		wordleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #538d4e; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	/**
	 * THIS handles the hover enter effect on Nerdle button.
	 */
	@FXML
	private void nerdleHoverEnter(MouseEvent event) {
		nerdleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #8a2be2; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	/**
	 * THIS handles the hover exit effect on Nerdle button.
	 */
	@FXML
	private void nerdleHoverExit(MouseEvent event) {
		nerdleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #6a0dad; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}
}
