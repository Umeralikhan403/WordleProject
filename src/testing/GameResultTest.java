package testing;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.GameType;

class GameResultTest {

	    // ------------- GameResult Tests -------------

	    @Test
	    public void testGameResultConstructor() {
	        GameResult result = new GameResult("HELLO", 6, 3, GameStatus.WON);
	        
	        assertEquals("HELLO", result.getWord());
	        assertEquals(5, result.getWordLength());
	        assertEquals(6, result.getMaxAttempts());
	        assertEquals(3, result.getAttemptsUsed());
	        assertEquals(GameStatus.WON, result.getStatus());
	        assertEquals(GameType.WORDLE, result.getGameType());
	        
	        // Test score calculation
	        double expectedScore = ((double) 6 / 3) * 5; // (maxAttempts/attemptsUsed) * wordLength
	        assertEquals(expectedScore, result.getScore(), 0.001);
	    }

	    @Test
	    public void testGameResultWithGameType() {
	        GameResult wordleResult = new GameResult("HELLO", 6, 3, GameStatus.WON, GameType.WORDLE);
	        GameResult nerdleResult = new GameResult("25+5=30", 6, 3, GameStatus.WON, GameType.NERDLE);
	        
	        assertEquals(GameType.WORDLE, wordleResult.getGameType());
	        assertEquals(GameType.NERDLE, nerdleResult.getGameType());
	        
	        // Test different scoring formulas
	        double wordleExpectedScore = ((double) 6 / 3) * 5; // (maxAttempts/attemptsUsed) * wordLength
	        double nerdleExpectedScore = ((double) 6 / 3) * 10.0; // (maxAttempts/attemptsUsed) * 10.0
	        
	        assertEquals(wordleExpectedScore, wordleResult.getScore(), 0.001);
	        assertEquals(nerdleExpectedScore, nerdleResult.getScore(), 0.001);
	    }

	    @Test
	    public void testGameResultFailedScore() {
	        GameResult result = new GameResult("HELLO", 6, 6, GameStatus.FAILED);
	        
	        assertEquals(0.0, result.getScore(), 0.001);
	    }

	    @Test
	    public void testGameResultDisplayText() {
	        GameResult wordleResult = new GameResult("HELLO", 6, 3, GameStatus.WON, GameType.WORDLE);
	        GameResult nerdleResult = new GameResult("25+5=30", 6, 3, GameStatus.WON, GameType.NERDLE);
	        
	        assertEquals("HELLO", wordleResult.getDisplayText());
	        assertEquals("Equation", nerdleResult.getDisplayText());
	    }

}
