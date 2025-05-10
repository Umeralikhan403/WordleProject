package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wordle.Service.GameService;

public class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        manuallyLoadWordsFile(gameService, "src/wordle/Util/words.txt");
    }

    @Test
    public void testSetWordLength() throws Exception {
        boolean success = gameService.setWordLength(6);
        assertTrue(success, "Should successfully change word length");

        Field currentWordLengthField = GameService.class.getDeclaredField("currentWordLength");
        currentWordLengthField.setAccessible(true);
        int wordLength = (int) currentWordLengthField.get(gameService);
        assertEquals(6, wordLength);
    }

    @Test
    public void testGetCurrentWordLength() {
        assertEquals(5, gameService.getCurrentWordLength());
        gameService.setWordLength(4);
        assertEquals(4, gameService.getCurrentWordLength());
    }

    @Test
    public void testGetAvailableWordLengths() throws Exception {
        List<Integer> wordLengths = gameService.getAvailableWordLengths();
        assertFalse(wordLengths.isEmpty());

        Field wordsByLengthField = GameService.class.getDeclaredField("wordsByLength");
        wordsByLengthField.setAccessible(true);
        Object wordsByLength = wordsByLengthField.get(gameService);

        Method keysMethod = wordsByLength.getClass().getMethod("keySet");
        Set<?> keys = (Set<?>) keysMethod.invoke(wordsByLength);

        assertEquals(keys.size(), wordLengths.size());
    }

    @Test
    public void testGetTargetWords() {
        String targetWord = gameService.getTargetWords();
        assertNotNull(targetWord);
        assertEquals(gameService.getCurrentWordLength(), targetWord.length());
    }

    @Test
    public void testIsValidWord() {
        String targetWord = gameService.getTargetWords();
        assertTrue(gameService.isValidWord(targetWord));

        String randomWord = generateRandomString(targetWord.length());
        assertFalse(gameService.isValidWord(randomWord), "Random word should not be valid: " + randomWord);

        String newWord = "TESTS";
        addValidWord(gameService, newWord);
        assertTrue(gameService.isValidWord(newWord));
    }

    @Test
    public void testValidWordCaseInsensitivity() {
        String word = "UPPER";
        addValidWord(gameService, word);
        assertTrue(gameService.isValidWord("UPPER"));
        assertTrue(gameService.isValidWord("upper"));
        assertTrue(gameService.isValidWord("Upper"));
        assertTrue(gameService.isValidWord("uPpEr"));
    }

    @Test
    public void testWordLengthValidation() {
        gameService.setWordLength(5);
        String word5 = "VALID";
        addValidWord(gameService, word5);
        assertTrue(gameService.isValidWord(word5));

        String word4 = "FOUR";
        String word6 = "SIXLTR";
        addValidWord(gameService, word4);
        addValidWord(gameService, word6);

        assertFalse(gameService.isValidWord(word4));
        assertFalse(gameService.isValidWord(word6));

        gameService.setWordLength(4);
        assertTrue(gameService.isValidWord(word4));
        assertFalse(gameService.isValidWord(word5));

        gameService.setWordLength(6);
        assertTrue(gameService.isValidWord(word6));
        assertFalse(gameService.isValidWord(word5));
    }

    // === HELPER METHODS ===

   private void addValidWord(GameService service, String word) {
    try {
        Field wordsByLengthField = GameService.class.getDeclaredField("wordsByLength");
        wordsByLengthField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Integer, List<String>> wordsByLength = (Map<Integer, List<String>>) wordsByLengthField.get(service);

        int len = word.length();
        wordsByLength.computeIfAbsent(len, k -> new java.util.ArrayList<>()).add(word.toLowerCase());

        // Update current word list if lengths match
        if (service.getCurrentWordLength() == len) {
            Method getListMethod = GameService.class.getDeclaredMethod("getCurrentWordsList");
            getListMethod.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<String> currentWords = (List<String>) getListMethod.invoke(service);
            currentWords.add(word.toLowerCase());
        }

    } catch (Exception e) {
        fail("Failed to add valid word: " + e.getMessage());
    }
}


    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('A' + (int) (Math.random() * 26));
            sb.append(c);
        }
        return sb.toString();
    }

    private void manuallyLoadWordsFile(GameService service, String path) {
        try (InputStream in = Files.newInputStream(Paths.get(path));
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            Method loadMethod = GameService.class.getDeclaredMethod("loadWordsList");
            loadMethod.setAccessible(true);

            
            var grouped = reader.lines()
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(word -> word.chars().allMatch(Character::isLetter))
                .collect(java.util.stream.Collectors.groupingBy(String::length));

            // Keep only lengths with at least 10 entries
            var filtered = grouped.entrySet().stream()
                .filter(e -> e.getValue().size() >= 10)
                .collect(java.util.stream.Collectors.toMap(
                    java.util.Map.Entry::getKey,
                    java.util.Map.Entry::getValue
                ));

            Field wordsByLengthField = GameService.class.getDeclaredField("wordsByLength");
            wordsByLengthField.setAccessible(true);
            wordsByLengthField.set(service, filtered);

            service.setWordLength(5);

        } catch (Exception e) {
            fail("Failed to load words manually: " + e.getMessage());
        }
    }
}
