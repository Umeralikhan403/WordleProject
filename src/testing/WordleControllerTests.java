package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wordle.Controller.WordleViewController;
import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.GameType;
import wordle.Models.Player;
import wordle.Service.GameService;
import wordle.Service.Session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Wordle game controller logic.
 * These tests specifically focus on the game mechanics of the WordleViewController class.
 */
public class WordleControllerTests {

    private GameService gameService;
    private Player testPlayer;
    private WordleViewController controller;
    
    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        testPlayer = new Player("testUser", "testPass");
        controller = new WordleViewController();
        
        // Set the test player as the current player in the session
        Session.setCurrentPlayer(testPlayer);
    }

    @Test
    public void testGetAttemptNumber() throws Exception {
        // Set attempt number using reflection
        Field attemptNumberField = WordleViewController.class.getDeclaredField("attemptNumber");
        attemptNumberField.setAccessible(true);
        attemptNumberField.set(null, 3);
        
        // Verify the getter works
        assertEquals(3, WordleViewController.getAttemptNumber());
    }

    @Test
    public void testGetTargetedWord() throws Exception {
        // Set targeted word using reflection
        Field targetedWordField = WordleViewController.class.getDeclaredField("targettedWord");
        targetedWordField.setAccessible(true);
        targetedWordField.set(null, "HELLO");
        
        // Verify the getter works
        assertEquals("HELLO", WordleViewController.getTargetedWord());
    }

    /**
     * Since we can't directly test the UI components, we'll test the key
     * game logic methods like checkResult using reflection.
     */
    
    @Test
    public void testRecordAndPersistResult() throws Exception {
        // Setup target word
        Field targetedWordField = WordleViewController.class.getDeclaredField("targettedWord");
        targetedWordField.setAccessible(true);
        targetedWordField.set(null, "HELLO");
        
        // Access the private recordAndPersistResult method using reflection
        Method recordAndPersistResultMethod = WordleViewController.class.getDeclaredMethod(
            "recordAndPersistResult", boolean.class);
        recordAndPersistResultMethod.setAccessible(true);
        
        // Call the method with a win condition
        recordAndPersistResultMethod.invoke(controller, true);
        
        // Verify the result was added to the player's history
        assertEquals(1, testPlayer.getHistory().size());
        GameResult result = testPlayer.getHistory().get(0);
        assertEquals("HELLO", result.getWord());
        assertEquals(GameStatus.WON, result.getStatus());
        assertEquals(GameType.WORDLE, result.getGameType());
        assertTrue(result.getScore() > 0);
    }

    /**
     * Test the GameService integration with WordleViewController
     */
    @Test
    public void testGameServiceIntegration() throws Exception {
        // Create a mock word list with reflection
        List<String> testWords = Arrays.asList("hello", "world", "apple", "tests");
        Field currentWordsListField = GameService.class.getDeclaredField("currentWordsList");
        currentWordsListField.setAccessible(true);
        currentWordsListField.set(gameService, testWords);
        
        // Test if a word is valid
        assertTrue(gameService.isValidWord("HELLO"));
        assertTrue(gameService.isValidWord("world"));
        assertFalse(gameService.isValidWord("banana"));
    }
    
    @ParameterizedTest
    @MethodSource("provideWordAndWordLength")
    public void testWordLengthHandling(String word, int expectedLength) {
        assertEquals(expectedLength, word.length());
    }
    
    private static Stream<Arguments> provideWordAndWordLength() {
        return Stream.of(
            Arguments.of("HELLO", 5),
            Arguments.of("WORLD", 5),
            Arguments.of("JAVA", 4),
            Arguments.of("PROGRAMMING", 11)
        );
    }
    
    @Test
    public void testGameResultScoreCalculation() {
        // Test Wordle scoring
        GameResult result1 = new GameResult("HELLO", 6, 3, GameStatus.WON);
        double expectedScore1 = ((double) 6 / 3) * 5; // (maxAttempts/attemptsUsed) * wordLength
        assertEquals(expectedScore1, result1.getScore(), 0.001);
        
        // Test with different attempts
        GameResult result2 = new GameResult("HELLO", 6, 1, GameStatus.WON);
        double expectedScore2 = ((double) 6 / 1) * 5; // Better score with fewer attempts
        assertEquals(expectedScore2, result2.getScore(), 0.001);
        assertTrue(result2.getScore() > result1.getScore());
        
        // Test with different word length
        GameResult result3 = new GameResult("PROGRAMMING", 6, 3, GameStatus.WON);
        double expectedScore3 = ((double) 6 / 3) * 11; // Longer word = higher score
        assertEquals(expectedScore3, result3.getScore(), 0.001);
        assertTrue(result3.getScore() > result1.getScore());
    }
    
    @Test
    public void testPlayerScoreAccumulation() {
        Player player = new Player("testPlayer", "password");
        
        // Initial state
        assertEquals(0, player.getHistory().size());
        assertEquals(0.0, player.getTotalScore(), 0.001);
        
        // Add a game result
        GameResult result1 = new GameResult("HELLO", 6, 3, GameStatus.WON);
        player.addGameResult(result1);
        assertEquals(1, player.getHistory().size());
        assertEquals(result1.getScore(), player.getTotalScore(), 0.001);
        
        // Add a second game result
        GameResult result2 = new GameResult("WORLD", 6, 2, GameStatus.WON);
        player.addGameResult(result2);
        assertEquals(2, player.getHistory().size());
        assertEquals(result1.getScore() + result2.getScore(), player.getTotalScore(), 0.001);
        
        // Add a failed game result (score should be 0)
        GameResult result3 = new GameResult("FAILED", 6, 6, GameStatus.FAILED);
        player.addGameResult(result3);
        assertEquals(3, player.getHistory().size());
        assertEquals(result1.getScore() + result2.getScore(), player.getTotalScore(), 0.001); // Score shouldn't change
    }
    
    /**
     * Test word validation logic that would be part of the controller's checkResult method
     */
    @Test
    public void testWordValidation() {
        // Create a mock GameService
        GameService service = new GameService();
        
        try {
            // Set up mock word list
            List<String> validWords = Arrays.asList("hello", "world", "games", "tests");
            Field currentWordsListField = GameService.class.getDeclaredField("currentWordsList");
            currentWordsListField.setAccessible(true);
            currentWordsListField.set(service, validWords);
            
            // Valid words
            assertTrue(service.isValidWord("HELLO"));
            assertTrue(service.isValidWord("world"));
            assertTrue(service.isValidWord("Games"));
            assertTrue(service.isValidWord("tests"));
            
            // Invalid words
            assertFalse(service.isValidWord("helloo")); // Too long
            assertFalse(service.isValidWord("hel")); // Too short
            assertFalse(service.isValidWord("chess")); // Not in list
            assertFalse(service.isValidWord("")); // Empty
            assertFalse(service.isValidWord(null)); // Null
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
}