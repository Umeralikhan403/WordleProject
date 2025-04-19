package wordle.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import wordle.Util.AlertUtil;

public class WinViewController {

	@FXML private Button btnAgain;
	@FXML private Button btnQuit;
	@FXML private TextArea lblAttempts;
	
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
                getClass().getResource("/wordle/MainView.fxml")
            );
            Parent mainRoot = loader.load();

            //Obtain the current window.
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            //Change back to the game view.
            Scene mainScene = new Scene(mainRoot);
            stage.setScene(mainScene);
            stage.setTitle("Wordle — Game"); 
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
