package wordle.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.Player;
import wordle.Service.GameService;
import wordle.Service.PlayerRepository;
import wordle.Service.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WordleViewController implements Initializable {

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
	private static int attemptNumber = 0;
	private int currentLetterIndex = 0;
	private final GameService gameService = new GameService();
	private static String targettedWord;
	private boolean overGame = false;
	private static Scene menuView;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		 attemptNumber = 0;
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
					if (currentLetterIndex == 5 && !overGame && attemptNumber < 6) {
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
				+ currentLetterIndex);
		if (attemptNumber >= 6 || overGame)
			return;

		if (key.isLetterKey() && currentLetterIndex < 5) {
			String alphabet = key.getName().toUpperCase();
			row[attemptNumber][currentLetterIndex].setText(alphabet);
			currentLetterIndex++;
		} else if (key == KeyCode.BACK_SPACE && currentLetterIndex > 0) {
			currentLetterIndex--;
			row[attemptNumber][currentLetterIndex].setText("");
		} else if (key == KeyCode.ENTER && currentLetterIndex == 5) {
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

		if (!gameService.isValidWord(guessLetter)) {
		    System.out.println("Not in word list: " + guessLetter);
		    
		    // Provide visual feedback that the word is invalid
		    showInvalidWordFeedback();
		    
		    // Don't proceed with the rest of the check
		    return;
		}
		
		
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
				// openWinViewHandler(new ActionEvent());
				// Score results
				recordAndPersistResult(true);
				openWinViewHandler();
			} catch (Exception e) {
				System.out.println(">>>>> Error loading winning screen.");
				e.printStackTrace();
			}
			overGame = true;
			return;
		}
		/// move to the next attempt
		attemptNumber++;
		currentLetterIndex = 0;
		updateLettersSelected();

		if (attemptNumber >= 6) {
			System.out.println(">>>>>> Game over, the correct word is " + wordTarget);
			try {
				// Score results
				recordAndPersistResult(false);
				openLoseViewHandler();
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
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
		stage.setScene(new Scene(root));
	}

	private void openWinViewHandler() throws IOException {
		// Load the win view using any node from the scene to get the stage
		Stage stage = (Stage) cell00.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WinView.fxml"));
		stage.setScene(new Scene(root));
	}

	private void openLoseViewHandler() throws IOException {
		// Load the lose view using any node from the scene to get the stage
		Stage stage = (Stage) cell00.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/LoseView.fxml"));
		stage.setScene(new Scene(root));
	}

	// Recording scores
	private void recordAndPersistResult(boolean won) {
		Player me = Session.getCurrentPlayer();
		// attemptsUsed = attemptNumber+1 if you zero-index attempts
		int used = attemptNumber + 1;
		GameStatus status = won ? GameStatus.WON : GameStatus.FAILED;
		GameResult result = new GameResult(targettedWord, 6, used, status);

		me.addGameResult(result);
		PlayerRepository.updatePlayer(me);
	}
	
	// This method is used to get the number of attempts
	public static int getAttemptNumber() {
		return attemptNumber;
	}
	
	// This method is used to get the targeted word
	public static String getTargetedWord() {
		return targettedWord;
	}
	
	public static void setMenuView(Scene scene) {
		menuView = scene;
	}

	// styling the rows based on the result
	private String correctStyle() {
		return "-fx-background-color: #209702; -fx-openWinViewHandlerborder-color: #209702; -fx-border-width: 2; "
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
	
	private void showInvalidWordFeedback() {
	    // Flash effect using a timeline
	    Timeline timeline = new Timeline(
	        new KeyFrame(Duration.millis(200), evt -> {
	            // Flash cells red
	            for (int i = 0; i < 5; i++) {
	                row[attemptNumber][i].setStyle(invalidWordStyle());
	            }
	        }),
	        new KeyFrame(Duration.millis(400), evt -> {
	            // Restore original appearance
	            for (int i = 0; i < 5; i++) {
	                row[attemptNumber][i].setStyle(defaultStyle());
	            }
	        })
	    );
	    timeline.setCycleCount(2); // Flash twice
	    timeline.play();
	    
	    // Show "Not in word list!" message
	    Platform.runLater(() -> {
	        attemptLabel.setText("Not in word list!");
	        // Reset the attempt label after a delay
	        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
	        pause.setOnFinished(event -> updateLettersSelected());
	        pause.play();
	    });
	}
	
	private String invalidWordStyle() {
	    return "-fx-background-color: #FF5252; -fx-border-color: #FF0000; -fx-border-width: 2; "
	            + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
	            + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

	private String defaultStyle() {
	    return "-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 2; "
	            + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
	            + "-fx-font-weight: bold; -fx-text-fill: black; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

}
