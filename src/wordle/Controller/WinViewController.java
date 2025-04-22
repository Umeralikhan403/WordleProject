package wordle.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wordle.Util.AlertUtil;

public class WinViewController {

	@FXML private Button btnAgain;
	@FXML private Button btnQuit;
	//@FXML private TextArea lblAttempts;
	@FXML private VBox box;
	@FXML private VBox titleBox;
	@FXML private Label lblCongrats;
	@FXML private Label lblComplete;
	@FXML private Label lblAttempts;
	@FXML private HBox buttonBox;
	
	
	@FXML private Label lblFailure;
	@FXML private VBox againBox;
	@FXML private Label lblAttemptsFailed;
	@FXML private Label lblTryAgain;
	
	/**
	 * This method is called when the view is loaded.
	 * It sets the text of the label to show the number of attempts.
	 */
	@FXML
	private void setAttempts() {
		int attempts = 3;
		lblAttempts.setText("You completed the game in " + attempts + " attempts.");
	}
	
	/**
	 * The game will restart when the user clicks the Play Again button.
	 * @param e The event that triggered this method.
	 */
	@FXML
	private void PlayAgainHandler(ActionEvent e) {
		try {
            //Load the FXML file.
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/wordle/View/MainView.fxml")
            );
            Parent mainRoot = loader.load();

            //Obtain the current window.
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            //Change back to the game view.
            Scene mainScene = new Scene(mainRoot);
            stage.setScene(mainScene);
            stage.setTitle("Wordle — Main"); 
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtil.warn("Navigation Error", "Game view failed to load.");
        }
	}
	
	/**
	 * The game will close when the user clicks the Quit button.
	 * @param e	 The event that triggered this method.
	 * */
	@FXML
	private void QuitHandler(ActionEvent e) {
		// Close the application
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.close();
	}
}
