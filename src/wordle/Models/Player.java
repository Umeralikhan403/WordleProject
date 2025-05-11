package wordle.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The Player class represents a registered user who plays Wordle/Nerdle.
 * It stores the user's credentials and game performance statistics.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    
	private String username;
	private String password;
	 private List<GameResult> history = new ArrayList<>();
	 private double totalScore = 0;
	
	
	public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
	
	
	// getters and setters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	// getting history of the player
    public List<GameResult> getHistory() {
        if (history == null) {
            history = new ArrayList<>();
        }
        return Collections.unmodifiableList(history);
    }

    // Adds a new game result to the player's history and updates the total score.
    public void addGameResult(GameResult result) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(result);
        totalScore += result.getScore();
    }

    // Returns the highest score achieved by the player in any game
    public double getHighScore() {
        return history.stream()
                      .mapToDouble(GameResult::getScore)
                      .max()
                      .orElse(0);
    }
    
    // gets the total accumulated score of the player.
    public double getTotalScore() {
        return totalScore;
    }

    // sets the score directly 
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
