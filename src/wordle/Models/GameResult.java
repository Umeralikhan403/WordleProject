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
    private final GameType gameType;         // Indicates if this is a Wordle or Nerdle result
    
    
    public GameResult(String word, int maxAttempts, int attemptsUsed, GameStatus status) {
        this(word, maxAttempts, attemptsUsed, status, GameType.WORDLE);
    }
    
    // New constructor with gameType parameter
    public GameResult(String word, int maxAttempts, int attemptsUsed, GameStatus status, GameType gameType) {
        this.word = word;
        this.wordLength = word.length();
        this.maxAttempts = maxAttempts;
        this.attemptsUsed = attemptsUsed;
        this.status = status;
        this.gameType = gameType;
        
        // Compute score: if won, formula; if failed, score = 0
        // Wordle and Nerdle have different scoring formulas
        if (status == GameStatus.WON) {
            if (gameType == GameType.WORDLE) {
                // Original Wordle scoring
                this.score = ((double) maxAttempts / attemptsUsed) * wordLength;
            } else {
                // Nerdle scoring - higher base score since equations are more complex
                this.score = ((double) maxAttempts / attemptsUsed) * 10.0;
            }
        } else {
            this.score = 0.0;
        }
    }
    
    // getters
    public String getWord() { return word; }
    public int getWordLength() { return wordLength; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getAttemptsUsed() { return attemptsUsed; }
    public GameStatus getStatus() { return status; }
    public double getScore() { return score; }
    public GameType getGameType() { return gameType; }
    
    // Helper method to get display text based on game type
    public String getDisplayText() {
        return gameType == GameType.WORDLE ? word : "Equation";
    }
}