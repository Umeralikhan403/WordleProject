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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wordle.Models.GameType;
import wordle.Service.GameService;
import wordle.Util.AlertUtil;

/**
 * This class is the controller for the WinView.fxml file.
 * It handles the logic for the winning screen.
 * 
 * @author 
 */
public class WinViewController implements Initializable {

	@FXML private Button btnAgain;
	@FXML private Button btnQuit;
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
	 * This method is called when the FXML file is loaded.
	 * It initialises the labels with the number of attempts that were made as well as the correct word.
	 * 
	 * @param url The URL of the FXML file.
	 * @param resourceBundle The resource bundle for localisation.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	    int attempts = WordleViewController.getAttemptNumber() + 1;
	    GameType gameType = ChooseGameController.getGameType();
	    String finalAnswer = "";

	    if (gameType == GameType.WORDLE) { // Wordle
	        finalAnswer = WordleViewController.getTargetedWord();
	        if (lblAttempts != null) {
	            lblAttempts.setText("You completed the game in " + attempts + " attempts.");
	        }
	        if (lblComplete != null) {
	            lblComplete.setText("The word was: " + finalAnswer + ".");
	        }
	    } else if (gameType == GameType.NERDLE) { // Nerdle
	    	finalAnswer = NerdleController.getTargetEquation();
	        if (lblAttempts != null) {
	            lblAttempts.setText("You completed the game in " + attempts + " attempts.");
	        }
	        if (lblComplete != null) {
	            lblComplete.setText("The equation was: " + finalAnswer + ".");
	        }
	    }

	    // Ensure proper formatting
	    if (lblComplete != null) {
	        lblComplete.setWrapText(true);
	        lblComplete.setPrefWidth(400);
	    }
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
                getClass().getResource("/wordle/View/ChooseGameView.fxml")
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
