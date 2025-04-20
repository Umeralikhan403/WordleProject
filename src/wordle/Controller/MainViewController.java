package wordle.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import wordle.Service.GameService;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

	@FXML
	private Label cell00, cell01, cell02, cell03, cell04;
	@FXML
	private Label cell10, cell11, cell12, cell13, cell14;
	@FXML
	private Label cell20, cell21, cell22, cell23, cell24;
	@FXML
	private Label cell30, cell31, cell32, cell33, cell34;
	@FXML
	private Label cell40, cell41, cell42, cell43, cell44;
	@FXML
	private Label cell50, cell51, cell52, cell53, cell54;

	@FXML
	private Label attemptLabel;
	@FXML
	private Label remainingAttemptsLabel;
	private final Label[][] row = new Label[6][5];
	private int attemptNumber = 0;
	private int currenttLetterIndex = 0;
	private final GameService gameService = new GameService();
	private String targettedWord;
	private boolean overGame = false;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// first we have to load the list of words
		gameService.loadWordsList();
		targettedWord = gameService.getTargetWords();
		System.out.println(">>>>> Targetted Word: " + targettedWord);

		setupRows();
		updateLettersSelected();
		
		// Setup key grid input listener after scene is loaded
		Platform.runLater(() -> {
			row[0][0].getScene().setOnKeyPressed(event -> {
				handleKeyPress(event.getCode());
			});
		});
	}

	// Link all labels/cells to a 2D array
	private void setupRows() {
		row[0] = new Label[] { cell00, cell01, cell02, cell03, cell04 };
		row[1] = new Label[] { cell10, cell11, cell12, cell13, cell14 };
		row[2] = new Label[] { cell20, cell21, cell22, cell23, cell24 };
		row[3] = new Label[] { cell30, cell31, cell32, cell33, cell34 };
		row[4] = new Label[] { cell40, cell41, cell42, cell43, cell44 };
		row[5] = new Label[] { cell50, cell51, cell52, cell53, cell54 };
	}

	// Update attempt text
	private void updateLettersSelected() {
		attemptLabel.setText((attemptNumber + 1) + "/6");
		int rem = 6 - attemptNumber;
		remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
	}

	// Next step will go here — handleKeyPress
	private void handleKeyPress(KeyCode key) {
		System.out.println(">>>>> key handling : " + key + ", attempt no. " + attemptNumber + ", letter index is: "
				+ currenttLetterIndex);
		if (attemptNumber >= 6 || overGame)
			return;

		if (key.isLetterKey() && currenttLetterIndex < 5) {
			String alphabet = key.getName().toUpperCase();
			row[attemptNumber][currenttLetterIndex].setText(alphabet);
			currenttLetterIndex++;
		} else if (key == KeyCode.BACK_SPACE && currenttLetterIndex > 0) {
			currenttLetterIndex--;
			row[attemptNumber][currenttLetterIndex].setText("");
		} else if (key == KeyCode.ENTER && currenttLetterIndex == 5) {
		}
	}

}
