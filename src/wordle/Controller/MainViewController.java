package wordle.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import wordle.Service.GameService;

import java.io.IOException;
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
	private Button btnOpenMenu;
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
//		Platform.runLater(() -> {
//			row[0][0].getScene().setOnKeyPressed(event -> {
//				handleKeyPress(event.getCode());
//			});
//		});
		
	    // Setup key grid input listener after scene is loaded
		// Code added to prevent menu from opening.
		 Platform.runLater(() -> {
		        Scene scene = row[0][0].getScene();
		        
		        // Explicitly set the menu button to not be a default button
		        btnOpenMenu.setDefaultButton(false);
		        btnOpenMenu.setCancelButton(false);
		        
		        // Set a scene filter to capture all key events at the scene level
		        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
		            if (event.getCode() == KeyCode.ENTER) {
		                // Always consume ENTER events to prevent them from triggering buttons
		                event.consume();
		                // Then manually handle what should happen with ENTER
		                if (currenttLetterIndex == 5 && !overGame && attemptNumber < 6) {
		                    checkResult();
		                }
		            } else {
		                handleKeyPress(event.getCode());
		            }
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
			checkResult();
		}
	}

	// main function of the game
	private void checkResult() {
		StringBuilder guessBuilder = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			guessBuilder.append(row[attemptNumber][i].getText());
		}

		String guessLetter = guessBuilder.toString().toUpperCase();
		// ignore empty guess like if the user presses space button
		if (guessLetter.length() != 5 || guessLetter.contains(" ")) {
			System.out.println("Guess must be 5 letters.");
			return;
		}

		String wordTarget = targettedWord.toUpperCase();
		char[] targetChars = wordTarget.toCharArray();
		boolean[] targetMatched = new boolean[5];
		boolean[] guessMatched = new boolean[5];

		// first try, correct position i.e green colour row
		for (int i = 0; i < 5; i++) {
			Label cell = row[attemptNumber][i];
			char guessChar = guessLetter.charAt(i);

			if (guessChar == targetChars[i]) {
				cell.setStyle(correctStyle());
				targetMatched[i] = true;
				guessMatched[i] = true;
			}
		}
		
		// second try — wrong position yellow row or all grey row if wrong
		for (int i = 0; i < 5; i++) {
			if (guessMatched[i])
				continue;
			Label cell = row[attemptNumber][i];
			char guessChar = guessLetter.charAt(i);
			boolean wordFound = false;

			for (int j = 0; j < 5; j++) {
				if (!targetMatched[j] && guessChar == targetChars[j]) {
					wordFound = true;
					targetMatched[j] = true;
					break;
				}
			}
			if (wordFound) {
				cell.setStyle(partialStyle());
			} else {
				cell.setStyle(wrongStyle());
			}
		}
		/// if the word is guessed correctly
		if (guessLetter.equals(wordTarget)) {
			System.out.println(">>>>> You have guessed the word correctly in " + (attemptNumber + 1) + " attempts.");
			try {
				openWinViewHandler(new ActionEvent());
			} catch (Exception e) {
				System.out.println(">>>>> Error loading winning screen.");
				e.printStackTrace();
			}
			overGame = true;
			return;
		}
		/// move to the next attempt
		attemptNumber++;
		currenttLetterIndex = 0;
		updateLettersSelected();

		if (attemptNumber >= 6) {
			System.out.println(">>>>>> Game over, the correct word is " + wordTarget);
			try {
				openLoseViewHandler(new ActionEvent());
			} catch (Exception e) {
				System.out.println(">>>>> Error loading losing screen.");
				e.printStackTrace();
			}
			overGame = true;
		}
	}
	
	@FXML
	private void openMenuHandler(ActionEvent e) throws IOException {
		// Load the menu view
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
        stage.setScene(new Scene(root));
	}
	
	private void openWinViewHandler(ActionEvent e) throws IOException {
		// Load the win view
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WinView.fxml"));
        stage.setScene(new Scene(root));
	}
	
	private void openLoseViewHandler(ActionEvent e) throws IOException {
		// Load the lose view
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/LoseView.fxml"));
        stage.setScene(new Scene(root));
	}

	// styling the rows based on the result
	private String correctStyle() {
		return "-fx-background-color: #209702; -fx-border-color: #209702; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

	private String partialStyle() {
		return "-fx-background-color: #E2DA00; -fx-border-color: #E2DA00; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

	private String wrongStyle() {
		return "-fx-background-color: #878787; -fx-border-color: #878787; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

}
