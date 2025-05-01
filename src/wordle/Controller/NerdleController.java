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
import javafx.stage.Stage;
import wordle.Service.GameService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NerdleController implements Initializable {

	// labelling the grids
	@FXML
	private Label cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07;
	@FXML
	private Label cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17;
	@FXML
	private Label cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27;
	@FXML
	private Label cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37;
	@FXML
	private Label cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47;
	@FXML
	private Label cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57;

	@FXML
	private Button btnOpenMenu;
	@FXML
	private Label attemptLabel;
	@FXML
	private Label remainingAttemptsLabel;

	// game variables
	private final Label[][] row = new Label[6][8]; // The grid (6 rows x 8 columns)
	private int attemptNumber = 0;
	private int currentDigitIndex = 0;
	private final GameService gameService = new GameService();
	private String targetEquation;
	private boolean gameOver = false;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		// generate the equation in background
		new Thread(() -> {
			String equation = gameService.equationGeneration();
			Platform.runLater(() -> {
				targetEquation = equation;
				System.out.println(">>>>> Targetted Equation: " + targetEquation);

				setupRows();
				updateAttemptNumber();

				Scene scene = row[0][0].getScene();
				btnOpenMenu.setDefaultButton(false);
				btnOpenMenu.setCancelButton(false);

			
				scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
					String keyText = event.getCharacter();

					// when the enter is pressed, check the result
					if (keyText.equals("\r") || keyText.equals("\n")) {
						event.consume();
						if (currentDigitIndex == 8 && !gameOver && attemptNumber < 6) {
							resultCheck();
						}
					}
					
					// BackSpace key -> remove the last digit entered 
					else if (keyText.equals("\b")) {
						if (currentDigitIndex > 0) {
							currentDigitIndex--;
							row[attemptNumber][currentDigitIndex].setText("");
						}
					}
					// Valid key (numbers, operators, equal sign) -> Add to grid
					else if ("0123456789+-*/=".contains(keyText) && currentDigitIndex < 8) {
						row[attemptNumber][currentDigitIndex].setText(keyText);
						currentDigitIndex++;
					}
				});
			});
		}).start();
	}

	// linking all the labels to the grids
	private void setupRows() {
		row[0] = new Label[] { cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07 };
		row[1] = new Label[] { cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17 };
		row[2] = new Label[] { cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27 };
		row[3] = new Label[] { cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37 };
		row[4] = new Label[] { cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47 };
		row[5] = new Label[] { cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57 };
	}

	// update the number of attempts
	private void updateAttemptNumber() {
		attemptLabel.setText((attemptNumber + 1) + "/6");
		int rem = 6 - attemptNumber;
		remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
	}

	// checking and comparing the results
	private void resultCheck() {
		StringBuilder guessBuilder = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			guessBuilder.append(row[attemptNumber][i].getText());
		}

		String guessEquation = guessBuilder.toString();

		// Validate equation
		if (guessEquation.length() != 8 || !gameService.isValidEquation(guessEquation)) {
			System.out.println("Invalid equation!");

			for (int i = 0; i < 8; i++) {
				row[attemptNumber][i]
						.setStyle("-fx-background-color: #FF5C5C; -fx-border-color: #FF0000; -fx-border-width: 2; "
								+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
								+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;");
			}
			return;
		}

		String target = targetEquation;
		char[] targetChars = target.toCharArray();
		boolean[] targetMatched = new boolean[8];
		boolean[] guessMatched = new boolean[8];
		for (int i = 0; i < 8; i++) {
			Label cell = row[attemptNumber][i];
			char guessDigit = guessEquation.charAt(i);
			if (guessDigit == targetChars[i]) {
				cell.setStyle(correctStyle());
				targetMatched[i] = true;
				guessMatched[i] = true;
			}
		}

		// Check wrong positions or not found
		for (int i = 0; i < 8; i++) {
			if (guessMatched[i])
				continue;

			Label cell = row[attemptNumber][i];
			char guessDigit = guessEquation.charAt(i);
			boolean found = false;

			for (int j = 0; j < 8; j++) {
				if (!targetMatched[j] && guessDigit == targetChars[j]) {
					found = true;
					targetMatched[j] = true;
					break;
				}
			}

			cell.setStyle(found ? partialStyle() : wrongStyle());
		}

		// win equation here
		if (guessEquation.equals(target)) {
			System.out.println(">>>>> You win!");
			try {
				openWinViewHandler();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gameOver = true;
			return;
		}

		// next attempt 
		attemptNumber++;
		currentDigitIndex = 0;
		updateAttemptNumber();

		if (attemptNumber >= 6) {
			System.out.println("You lose! Correct answer was: " + target);
			try {
				openLoseViewHandler();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gameOver = true;
		}
	}

	// loading the menu
	@FXML
	private void openMenuHandler(ActionEvent e) throws IOException {
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
		stage.setScene(new Scene(root));
	}

	// Load Win view
	private void openWinViewHandler() throws IOException {
		Stage stage = (Stage) cell00.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WinView.fxml"));
		stage.setScene(new Scene(root));
	}

	// Load Lose view
	private void openLoseViewHandler() throws IOException {
		Stage stage = (Stage) cell00.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/LoseView.fxml"));
		stage.setScene(new Scene(root));
	}

	
	private String correctStyle() {
		return "-fx-background-color: #5AC85A; -fx-border-color: #5AC85A; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

	private String partialStyle() {
		return "-fx-background-color: #B79AFD; -fx-border-color: #B79AFD; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}

	private String wrongStyle() {
		return "-fx-background-color: #7C7C7C; -fx-border-color: #7C7C7C; -fx-border-width: 2; "
				+ "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
				+ "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
	}
}
