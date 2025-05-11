package wordle.Service;

import wordle.Models.Player;

/**
 * The session class class manages the current user's session.
 * It stores and provides access to the currently logged-in player across the application.
 */
public class Session {
	// Holds the currently authenticated player
	private static Player currentPlayer;

	/**
     * Sets the currently logged-in player.
     * 
     * @param p the Player object to set as the current user
     */
    public static void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    /**
     * Returns the currently logged-in player.
     * 
     * @return the current Player object
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}
