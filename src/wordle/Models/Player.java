package wordle.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    
	private String username;
	private String password;
	private int highScore;
	private ArrayList<Integer> scores;
	
	
	public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.scores = new ArrayList<>();
    }
	
	
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
	public int getHighScore() {
		return highScore;
	}
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	public ArrayList<Integer> getScores() {
		return scores;
	}
	public void setScores(ArrayList<Integer> scores) {
		this.scores = scores;
	}
	
	public void addScoreToList(int score) {
		this.scores.add(score);
	}
	
	
	

}
