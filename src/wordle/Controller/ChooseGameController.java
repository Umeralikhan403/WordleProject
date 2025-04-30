package wordle.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ChooseGameController {

	@FXML
	private Button wordleButton;

	@FXML
	private Button nerdleButton;

	@FXML
	private void startWordle(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WordleView.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void startNerdle(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/NerdleView.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// added hover effect 
	@FXML
	private void wordleHoverEnter(MouseEvent event) {
		wordleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #6fb96c; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	@FXML
	private void wordleHoverExit(MouseEvent event) {
		wordleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #538d4e; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	// added hover effect
	@FXML
	private void nerdleHoverEnter(MouseEvent event) {
		nerdleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #8a2be2; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}

	@FXML
	private void nerdleHoverExit(MouseEvent event) {
		nerdleButton.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; "
				+ "-fx-background-color: #6a0dad; -fx-pref-width: 250; -fx-pref-height: 80; "
				+ "-fx-background-radius: 10; -fx-cursor: hand;");
	}
}
