package wordle.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a username, password, and a history of game results.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private List<GameResult> history = new ArrayList<>();
	private double totalScore = 0;
	
	/**
	 * Constructor to create a new player with a username and password.
	 *
	 * @param username - the player's username
	 * @param password - the player's password
	 */
	public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
	
	/**
	 * Getter for the user's username.
	 * @return username - the player's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Setter for the user's username.
	 * @param username - the player's username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Getter for the user's password.
	 * @return password - the player's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setter for the user's password.
	 * @param password - the player's password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Getter for the user's score history.
	 * @return a list of game results for all players.
	 */
    public List<GameResult> getHistory() {
        if (history == null) {
            history = new ArrayList<>();
        }
        return Collections.unmodifiableList(history);
    }

    /**
	 * Method to add a result to the game history list.
	 * @param result - a game result.
	 */
    public void addGameResult(GameResult result) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(result);
        totalScore += result.getScore();
    }

    /**
     * Method to get the player's high score.
     * @return history - the highest value in the player's score history.
     */
    public double getHighScore() {
        return history.stream()
                      .mapToDouble(GameResult::getScore)
                      .max()
                      .orElse(0);
    }

    /**
	 * Method to get the player's total score.
	 * @return totalScore - the total value of the player's score history.
	 */
    public double getTotalScore() {
        return totalScore;
    }

    /**
	 * Method to set the player's total score.
	 * @param totalScore - the total value of the player's score history.
	 */
    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }
	
    /**
     * Resets the player's scores and clears the score history.
     */
	public void resetScores() {
		this.history.clear();
		this.totalScore = 0;
	}

}
