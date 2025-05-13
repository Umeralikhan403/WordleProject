package wordle.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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

/**
 * The WordleViewController handles the game logic and UI interactions for the Wordle game.
 */
public class WordleViewController implements Initializable {

    @FXML
    private GridPane wordleGrid;
    @FXML
    private Button btnOpenMenu;
    @FXML
    private Label attemptLabel;
    @FXML
    private Label remainingAttemptsLabel;
    @FXML
    private ComboBox<Integer> wordLengthSelector;
    
    private Label[][] cells; // Dynamic array to store cell references
    private static int attemptNumber = 0;
    private int currentLetterIndex = 0;
    private final GameService gameService = new GameService();
    private static String targettedWord;
    private boolean overGame = false;
    private int maxAttempts = 6; // Default max attempts
    private int wordLength = 5; // Default word length

    /**
	 * Initialises the controller and sets up the game, generating a word to guess and setting up the grid.
	 * @param url - The URL location of the FXML file.
	 * @param resourceBundle - The resource bundle for localisation.
	 */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        attemptNumber = 0;
        
        // First we have to load the list of words
        gameService.loadWordsList();
        
        // Setup word length selector
        setupWordLengthSelector();
        
        // Get the target word
        targettedWord = gameService.getTargetWords();
        System.out.println(">>>>> Targetted Word: " + targettedWord);
        
        // Initialize the grid
        setupDynamicGrid();
        updateLettersSelected();

        // Setup key grid input listener after scene is loaded
        Platform.runLater(() -> {
            Scene scene = wordleGrid.getScene();

            // Explicitly set the menu button to not be a default button
            btnOpenMenu.setDefaultButton(false);
            btnOpenMenu.setCancelButton(false);

            // Set a scene filter to capture all key events at the scene level
            scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    // Always consume ENTER events to prevent them from triggering buttons
                    event.consume();
                    // Then manually handle what should happen with ENTER
                    if (currentLetterIndex == wordLength && !overGame && attemptNumber < maxAttempts) {
                        checkResult();
                    }
                } else {
                    handleKeyPress(event.getCode());
                }
            });
        });
    }

    // Setup word length selector
    /**
     * Sets up the word length selector with available options.
     */
    private void setupWordLengthSelector() {
        // Get available word lengths from the service
    	wordLengthSelector.setItems(FXCollections.observableArrayList(
    		    gameService.getAvailableWordLengths().stream()
    		        .filter(length -> length <= 10)
    		        .toList()
    		));
        
        // Set default value
        wordLengthSelector.setValue(gameService.getCurrentWordLength());
        
        // Add listener for change
        wordLengthSelector.setOnAction(event -> {
            int newLength = wordLengthSelector.getValue();
            changeWordLength(newLength);
        });
    }
    
    // Change word length and reset the game
    /**
	 * Change the word length and reset the game.
	 * @param newLength - The new word length selected by the user.
	 */
    private void changeWordLength(int newLength) {
        if (newLength != wordLength) {
            wordLength = newLength;
            
            // Update game service word length
            if (gameService.setWordLength(newLength)) {
                // Reset the game
                resetGame();
                
                // Setup new grid
                setupDynamicGrid();
                
                // Get new target word
                targettedWord = gameService.getTargetWords();
                System.out.println(">>>>> New Targetted Word: " + targettedWord);
            }
        }
    }
    
    // Reset game state
    /**
	 * Resets the game state for a new game.
	 */
    private void resetGame() {
        attemptNumber = 0;
        currentLetterIndex = 0;
        overGame = false;
        updateLettersSelected();
    }

    // Create and set up a dynamic grid based on word length
    /**
     * Sets up the dynamic grid based on the selected word length.
     */
    private void setupDynamicGrid() {
        // Clear existing grid
        wordleGrid.getChildren().clear();
        wordleGrid.getColumnConstraints().clear();
        wordleGrid.getRowConstraints().clear();
        
        // Create new cells array based on word length
        cells = new Label[maxAttempts][wordLength];
        
        // Create and add labels to the grid
        for (int row = 0; row < maxAttempts; row++) {
            for (int col = 0; col < wordLength; col++) {
                Label cell = new Label();
                cell.setStyle(defaultStyle());
                cell.setText("");
                
                // Store reference in the array
                cells[row][col] = cell;
                
                // Add to grid
                wordleGrid.add(cell, col, row);
            }
        }
    }

    // Update attempt text
    /**
	 * Update the attempt label and remaining attempts label.
	 */
    private void updateLettersSelected() {
        attemptLabel.setText((attemptNumber + 1) + "/" + maxAttempts);
        int rem = maxAttempts - attemptNumber;
        if (attemptNumber - 1 == maxAttempts) {
			remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
			remainingAttemptsLabel.setStyle("-fx-text-fill: red;");
		} else {
			remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
		}
        //remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
    }

    // Handle key press
    /**
     * Handles key press events.
     * @param key - The key on the keyboard that was pressed.
     */
    private void handleKeyPress(KeyCode key) {
        System.out.println(">>>>> key handling : " + key + ", attempt no. " + attemptNumber + ", letter index is: "
                + currentLetterIndex);
        if (attemptNumber >= maxAttempts || overGame)
            return;

        if (key.isLetterKey() && currentLetterIndex < wordLength) {
            String alphabet = key.getName().toUpperCase();
            cells[attemptNumber][currentLetterIndex].setText(alphabet);
            currentLetterIndex++;
        } else if (key == KeyCode.BACK_SPACE && currentLetterIndex > 0) {
            currentLetterIndex--;
            cells[attemptNumber][currentLetterIndex].setText("");
        } else if (key == KeyCode.ENTER && currentLetterIndex == wordLength) {
            checkResult();
        }
    }

    // Main function of the game
    /**
	 * Check the result of the current guess.
	 */
    private void checkResult() {
        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            guessBuilder.append(cells[attemptNumber][i].getText());
        }

        String guessLetter = guessBuilder.toString().toUpperCase();
        // Ignore empty guess like if the user presses space button
        if (guessLetter.length() != wordLength || guessLetter.contains(" ")) {
            System.out.println("Guess must be " + wordLength + " letters.");
            return;
        }

        String wordTarget = targettedWord.toUpperCase();
        char[] targetChars = wordTarget.toCharArray();
        boolean[] targetMatched = new boolean[wordLength];
        boolean[] guessMatched = new boolean[wordLength];

        if (!gameService.isValidWord(guessLetter)) {
            System.out.println("Not in word list: " + guessLetter);
            
            // Provide visual feedback that the word is invalid
            showInvalidWordFeedback();
            
            // Don't proceed with the rest of the check
            return;
        }
        
        // First try, correct position i.e green colour row
        for (int i = 0; i < wordLength; i++) {
            Label cell = cells[attemptNumber][i];
            char guessChar = guessLetter.charAt(i);

            if (guessChar == targetChars[i]) {
                cell.setStyle(correctStyle());
                targetMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        // Second try — wrong position yellow row or all grey row if wrong
        for (int i = 0; i < wordLength; i++) {
            if (guessMatched[i])
                continue;
            Label cell = cells[attemptNumber][i];
            char guessChar = guessLetter.charAt(i);
            boolean wordFound = false;

            for (int j = 0; j < wordLength; j++) {
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
        
        // If the word is guessed correctly
        if (guessLetter.equals(wordTarget)) {
            System.out.println(">>>>> You have guessed the word correctly in " + (attemptNumber + 1) + " attempts.");
            try {
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
        
        // Move to the next attempt
        attemptNumber++;
        currentLetterIndex = 0;
        updateLettersSelected();

        if (attemptNumber >= maxAttempts) {
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
    
    /**
	 * Opens the menu view when the menu button is clicked.
	 * @param e - The ActionEvent triggered by the button click.
	 * @throws IOException if there is an error loading the FXML file.
	 */
    @FXML
    private void openMenuHandler(ActionEvent e) throws IOException {
        // Load the menu view
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
        stage.setScene(new Scene(root));
    }

    /**
     * Opens the win view when the game is won.
     * @throws IOException if there is an error loading the FXML file.
     */
    private void openWinViewHandler() throws IOException {
        // Load the win view using any node from the scene to get the stage
        Stage stage = (Stage) wordleGrid.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WinView.fxml"));
        stage.setScene(new Scene(root));
    }

    /**
	 * Opens the lose view when the game is over.
	 * @throws IOException if there is an error loading the FXML file.
	 */
    private void openLoseViewHandler() throws IOException {
        // Load the lose view using any node from the scene to get the stage
        Stage stage = (Stage) wordleGrid.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/LoseView.fxml"));
        stage.setScene(new Scene(root));
    }

    // Recording scores
    /**
	 * Record and persist the game result.
	 * @param won - Indicates if the game was won or lost.
	 */
    private void recordAndPersistResult(boolean won) {
        Player me = Session.getCurrentPlayer();
        // attemptsUsed = attemptNumber+1 if you zero-index attempts
        int used = attemptNumber + 1;
        GameStatus status = won ? GameStatus.WON : GameStatus.FAILED;
        GameResult result = new GameResult(targettedWord, maxAttempts, used, status);

        me.addGameResult(result);
        PlayerRepository.updatePlayer(me);
    }
    
    // This method is used to get the number of attempts
    /**
     * Get the number of attempts.
     * @return attemptNumber - The number of attempts made.
     */
    public static int getAttemptNumber() {
        return attemptNumber;
    }
    
    // This method is used to get the targeted word
    /**
	 * Get the targeted word.
	 * @return targettedWord - The targeted word.
	 */
    public static String getTargetedWord() {
        return targettedWord;
    }

    // Styling the rows based on the result
    /**
	 * Style for correct letters.
	 * @return The CSS style string for correct letters.
	 */
    private String correctStyle() {
        return "-fx-background-color: #209702; -fx-border-color: #209702; -fx-border-width: 2; "
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
    }

    /**
     * Style for partially correct letters.
     * @return The CSS style string for partially correct letters.
     */
    private String partialStyle() {
        return "-fx-background-color: #E2DA00; -fx-border-color: #E2DA00; -fx-border-width: 2; "
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
    }

    /**
	 * Style for incorrect letters.
	 * @return The CSS style string for incorrect letters.
	 */
    private String wrongStyle() {
        return "-fx-background-color: #878787; -fx-border-color: #878787; -fx-border-width: 2; "
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
    }
    
    /**
     * Shows feedback when an invalid word is entered.
     */
    private void showInvalidWordFeedback() {
        // Flash effect using a timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), evt -> {
                // Flash cells red
                for (int i = 0; i < wordLength; i++) {
                    cells[attemptNumber][i].setStyle(invalidWordStyle());
                }
            }),
            new KeyFrame(Duration.millis(400), evt -> {
                // Restore original appearance
                for (int i = 0; i < wordLength; i++) {
                    cells[attemptNumber][i].setStyle(defaultStyle());
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
    
    /**
     * Style for invalid word input.
     * @return The CSS style string for invalid word input.
     */
    private String invalidWordStyle() {
        return "-fx-background-color: #FF5252; -fx-border-color: #FF0000; -fx-border-width: 2; "
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;";
    }

    /**
     * Default style for the cells.
     * @return The CSS style string for the default cell appearance.
     */
    private String defaultStyle() {
        return "-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 2; "
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                + "-fx-font-weight: bold; -fx-text-fill: black; -fx-background-radius: 10; -fx-border-radius: 10;";
    }
}