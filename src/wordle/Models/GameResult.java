package wordle.Models;
import java.io.Serializable;

/**
 * Class that calculates the result of a game, by taking in the word guessed, the number of attempts used,
 * the maximum number of attempts allowed, the status of the game (won or lost), and the score.
 */
public class GameResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String word;               
    private final int wordLength;            
    private final int maxAttempts;
    private final int attemptsUsed;
    private final GameStatus status;
    private final double score;
    private final GameType gameType;         // Indicates if this is a Wordle or Nerdle result
    
    /**
     * Constructor for GameResult.
     * @param word - the word being guess in Wordle
     * @param maxAttempts - the maximum number of attempts allowed
     * @param attemptsUsed - the number of attempts used
     * @param status - the status of the game (won or lost)
     */
    public GameResult(String word, int maxAttempts, int attemptsUsed, GameStatus status) {
        this(word, maxAttempts, attemptsUsed, status, GameType.WORDLE);
    }
    
    // New constructor with gameType parameter
    /**
	 * Constructor for GameResult with gameType.
	 * @param word - the word being guessed in Wordle
	 * @param maxAttempts - the maximum number of attempts allowed
	 * @param attemptsUsed - the number of attempts used
	 * @param status - the status of the game (won or lost)
	 * @param gameType - the type of game (Wordle or Nerdle)
	 */
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
    /**
     * Getter to obtain the Wordle word.
     * @return word - the word being guessed
     */
    public String getWord() { return word; }
    
    /**
     * Getter to obtain the word length.
     * @return wordLength - the length of the word
     */
    public int getWordLength() { return wordLength; }
    
    /**
	 * Getter to obtain the maximum number of attempts allowed.
	 * @return maxAttempts - the maximum number of attempts
	 */
    public int getMaxAttempts() { return maxAttempts; }
    
    /**
     * Getter to obtain the number of attempts used.
     * @return attemptsUsed - the number of attempts used
     */
    public int getAttemptsUsed() { return attemptsUsed; }
    public GameStatus getStatus() { return status; }
    
    /**
     * Getter to obtain the score.
     * @return score - the score of the game
     */
    public double getScore() { return score; }
    
    /**
	 * Getter to obtain the game type.
	 * @return gameType - the type of game (Wordle or Nerdle)
	 */
    public GameType getGameType() { return gameType; }
    
    // Helper method to get display text based on game type
    /**
	 * Helper method to get display text based on game type.
	 * @return displayText - the display text for the game result
	 */
    public String getDisplayText() {
        return gameType == GameType.WORDLE ? word : "Equation";
    }
}