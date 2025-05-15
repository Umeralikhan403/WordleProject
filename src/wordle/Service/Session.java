package wordle.Service;

import wordle.Models.Player;

/**
 * This class handles the session of the current player.
 * It stores the current player in a static variable.
 */
public class Session {
	private static Player currentPlayer;

	/**
	 * This method sets the current player.
	 * @param p - the current player signed in
	 */
    public static void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    /**
     * This method returns the current player.
     * @return currentPlayer - the current player signed in
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public static void signOut() {
		currentPlayer = null;
	}
}
