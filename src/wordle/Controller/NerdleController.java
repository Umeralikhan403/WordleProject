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

public class NerdleController implements Initializable {

    @FXML private Label cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07;
    @FXML private Label cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17;
    @FXML private Label cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27;
    @FXML private Label cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37;
    @FXML private Label cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47;
    @FXML private Label cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57;

    @FXML private Button btnOpenMenu;
    @FXML private Label attemptLabel;
    @FXML private Label remainingAttemptsLabel;

    private final Label[][] row = new Label[6][8];
    private int attemptNumber = 0;
    private int currentDigitIndex = 0;
    private final GameService gameService = new GameService();
    private String targetEquation;
    private boolean gameOver = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Thread(() -> {
            gameService.loadEquationsList();
            Platform.runLater(() -> {
                targetEquation = gameService.getTargetEquation();
                System.out.println(">>>>> Targetted Equation: " + targetEquation);

                setupRows();
                updateAttemptNumber();

                Scene scene = row[0][0].getScene();
                btnOpenMenu.setDefaultButton(false);
                btnOpenMenu.setCancelButton(false);

                // Handle normal character keys
                scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {

                    String keyText = event.getCharacter();

                    // Ignore SHIFT, CTRL, ALT or EMPTY
                    if (keyText.isEmpty() || keyText.trim().isEmpty() || keyText.charAt(0) < 32) {
                        return;
                    }

                    if ("0123456789+-*/=".contains(keyText) && currentDigitIndex < 8) {
                        row[attemptNumber][currentDigitIndex].setText(keyText);
                        currentDigitIndex++;
                    }
                });

                // Handle ENTER and BACKSPACE separately
                scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {

                    KeyCode code = event.getCode();

                    if (code == KeyCode.ENTER) {
                        event.consume();
                        if (currentDigitIndex == 8 && !gameOver && attemptNumber < 6) {
                            resultCheck();
                        }
                    } 
                    else if (code == KeyCode.BACK_SPACE) {
                        if (currentDigitIndex > 0) {
                            currentDigitIndex--;
                            row[attemptNumber][currentDigitIndex].setText("");
                        }
                    }
                });
            });
        }).start();
    }

    private void setupRows() {
        row[0] = new Label[] { cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07 };
        row[1] = new Label[] { cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17 };
        row[2] = new Label[] { cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27 };
        row[3] = new Label[] { cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37 };
        row[4] = new Label[] { cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47 };
        row[5] = new Label[] { cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57 };
    }

    private void updateAttemptNumber() {
        attemptLabel.setText((attemptNumber + 1) + "/6");
        int rem = 6 - attemptNumber;
        remainingAttemptsLabel.setText(rem + " attempt" + (rem > 1 ? "s" : "") + " remaining");
    }

    private boolean isProperEquationSyntax(String equation) {
        if (equation == null || equation.length() != 8) return false;
        if (!equation.contains("=")) return false;

        String[] parts = equation.split("=");

        if (parts.length != 2) return false;

        String left = parts[0];
        String right = parts[1];

        if (left.isEmpty() || right.isEmpty()) return false;

        // Left side must contain only valid math chars
        if (!left.matches("[0-9+\\-*/]+")) return false;

        // Right side must contain only digits
        if (!right.matches("[0-9]+")) return false;

        return true; // Everything looks good
    }


    private void resultCheck() {

        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            guessBuilder.append(row[attemptNumber][i].getText());
        }

        String guessEquation = guessBuilder.toString();

        if (!isProperEquationSyntax(guessEquation)) {
            System.out.println(">>>> Invalid Syntax!");

            for (int i = 0; i < 8; i++) {
                row[attemptNumber][i].setStyle("-fx-background-color: #FF5C5C; -fx-border-color: #FF0000; -fx-border-width: 2; "
                        + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center; -fx-font-size: 32; "
                        + "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10;");
            }
            return;
        }

        String target = targetEquation;
        char[] targetChars = target.toCharArray();
        boolean[] targetMatched = new boolean[8];
        boolean[] guessMatched = new boolean[8];

        // Correct Position Check
        for (int i = 0; i < 8; i++) {
            Label cell = row[attemptNumber][i];
            char guessChar = guessEquation.charAt(i);

            if (guessChar == targetChars[i]) {
                cell.setStyle(correctStyle());
                targetMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        // Wrong place or not found
        for (int i = 0; i < 8; i++) {
            if (guessMatched[i]) continue;

            Label cell = row[attemptNumber][i];
            char guessChar = guessEquation.charAt(i);
            boolean found = false;

            for (int j = 0; j < 8; j++) {
                if (!targetMatched[j] && guessChar == targetChars[j]) {
                    found = true;
                    targetMatched[j] = true;
                    break;
                }
            }

            cell.setStyle(found ? partialStyle() : wrongStyle());
        }

        // Win
        if (guessEquation.equals(target)) {
            System.out.println(">>>>> You win!");
            try { openWinViewHandler(); } catch (Exception e) { e.printStackTrace(); }
            gameOver = true;
            return;
        }

        attemptNumber++;
        currentDigitIndex = 0;
        updateAttemptNumber();

        if (attemptNumber >= 6) {
            System.out.println("You lose! Correct answer was: " + target);
            try { openLoseViewHandler(); } catch (Exception e) { e.printStackTrace(); }
            gameOver = true;
        }
    }

    @FXML private void openMenuHandler(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/MenuView.fxml"));
        stage.setScene(new Scene(root));
    }

    private void openWinViewHandler() throws IOException {
        Stage stage = (Stage) cell00.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/WinView.fxml"));
        stage.setScene(new Scene(root));
    }

    private void openLoseViewHandler() throws IOException {
        Stage stage = (Stage) cell00.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/wordle/View/LoseView.fxml"));
        stage.setScene(new Scene(root));
    }

    // Styles
    private String correctStyle() {
        return "-fx-background-color: #5AC85A; -fx-border-color: #5AC85A; -fx-border-width: 2;"
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center;"
                + "-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: white;"
                + "-fx-background-radius: 10; -fx-border-radius: 10;";
    }

    private String partialStyle() {
        return "-fx-background-color: #B79AFD; -fx-border-color: #B79AFD; -fx-border-width: 2;"
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center;"
                + "-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: white;"
                + "-fx-background-radius: 10; -fx-border-radius: 10;";
    }

    private String wrongStyle() {
        return "-fx-background-color: #7C7C7C; -fx-border-color: #7C7C7C; -fx-border-width: 2;"
                + "-fx-min-width: 62; -fx-min-height: 62; -fx-alignment: center;"
                + "-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: white;"
                + "-fx-background-radius: 10; -fx-border-radius: 10;";
    }

}
