package wordle.Util;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying alerts in a JavaFX application.
 * This class provides methods to show warning and information alerts.
 */
public class AlertUtil {

	/**
	 * Displays a warning alert with the specified title and message.
	 *
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
    public static void warn(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    /**
     * Displays an information alert with the specified title and message.
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    public static void info(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    /**
	 * Displays an alert of the specified type with the given title and message.
	 *
	 * @param type    The type of the alert (e.g., WARNING, INFORMATION).
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}