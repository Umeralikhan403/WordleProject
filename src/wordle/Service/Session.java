package wordle.Service;

import wordle.Models.Player;

public class Session {
	private static Player currentPlayer;

    public static void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}
