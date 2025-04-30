package wordle.Models;

import java.io.Serializable;

public class GameResult implements Serializable {
	 private static final long serialVersionUID = 1L;

	    private final String word;
	    private final int wordLength;
	    private final int maxAttempts;
	    private final int attemptsUsed;
	    private final GameStatus status;
	    private final double score;

	    public GameResult(String word, int maxAttempts, int attemptsUsed, GameStatus status) {
	        this.word       = word;
	        this.wordLength = word.length();
	        this.maxAttempts = maxAttempts;
	        this.attemptsUsed = attemptsUsed;
	        this.status     = status;
	        // compute score: if won, formula; if failed, score = 0
	        this.score = (status == GameStatus.WON)
	            ? ((double) maxAttempts / attemptsUsed) * wordLength
	            : 0.0;
	    }

	    // getters
	    public String getWord()        { return word; }
	    public int    getWordLength()  { return wordLength; }
	    public int    getMaxAttempts() { return maxAttempts; }
	    public int    getAttemptsUsed(){ return attemptsUsed; }
	    public GameStatus getStatus()  { return status; }
	    public double getScore()       { return score; }

}
