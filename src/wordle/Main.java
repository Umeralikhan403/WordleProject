package wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The Main class loads the application and sets the scene.
 */
public class Main extends Application {
	/**
	 * The start method is the entry point of the JavaFX application.
	 * Loads the FXML file and sets it to the scene, specifying the title of the window.
	 */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wordle/View/loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Wordle Project");
        stage.setScene(scene);
        
        stage.show();
    }

    /**
	 * The main method is the entry point of the Java application.
	 * @param args - command line arguments
	 */
    public static void main(String[] args) {
        launch();
    }
}
