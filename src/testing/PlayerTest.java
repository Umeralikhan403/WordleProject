package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.Player;

class PlayerTest {

	 // ------------- Player Tests -------------

    @Test
    public void testPlayerConstructor() {
        Player player = new Player("username", "password");
        
        assertEquals("username", player.getUsername());
        assertEquals("password", player.getPassword());
        assertEquals(0, player.getHistory().size());
        assertEquals(0.0, player.getTotalScore(), 0.001);
    }

    @Test
    public void testPlayerAddGameResult() {
        Player player = new Player("username", "password");
        GameResult result1 = new GameResult("HELLO", 6, 3, GameStatus.WON);
        GameResult result2 = new GameResult("WORLD", 6, 4, GameStatus.WON);
        
        player.addGameResult(result1);
        assertEquals(1, player.getHistory().size());
        assertEquals(result1.getScore(), player.getTotalScore(), 0.001);
        
        player.addGameResult(result2);
        assertEquals(2, player.getHistory().size());
        assertEquals(result1.getScore() + result2.getScore(), player.getTotalScore(), 0.001);
    }

    @Test
    public void testPlayerGetHighScore() {
        Player player = new Player("username", "password");
        GameResult result1 = new GameResult("HELLO", 6, 3, GameStatus.WON); // Score = 10
        GameResult result2 = new GameResult("WORLD", 6, 2, GameStatus.WON); // Score = 15
        GameResult result3 = new GameResult("FAIL", 6, 6, GameStatus.FAILED); // Score = 0
        
        player.addGameResult(result1);
        assertEquals(result1.getScore(), player.getHighScore(), 0.001);
        
        player.addGameResult(result2);
        assertEquals(result2.getScore(), player.getHighScore(), 0.001);
        
        player.addGameResult(result3);
        assertEquals(result2.getScore(), player.getHighScore(), 0.001);
    }

    @Test
    public void testPlayerResetScores() {
        Player player = new Player("username", "password");
        GameResult result = new GameResult("HELLO", 6, 3, GameStatus.WON);
        
        player.addGameResult(result);
        assertEquals(1, player.getHistory().size());
        assertTrue(player.getTotalScore() > 0);
        
        player.resetScores();
        assertEquals(0, player.getHistory().size());
        assertEquals(0.0, player.getTotalScore(), 0.001);
    }

}
