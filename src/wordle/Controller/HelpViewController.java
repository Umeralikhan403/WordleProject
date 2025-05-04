package wordle.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpViewController {

	@FXML private Button btnCloseHelp;
	
	@FXML
	private void closeHelpHandler(ActionEvent e) throws IOException {
		Parent registerRoot = FXMLLoader.load(getClass().getResource("/wordle/View/LoginView.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
	}
}
