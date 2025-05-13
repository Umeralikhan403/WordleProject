package wordle.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * GameService class to manage the game logic for Wordle and Nerdle.
 * It handles loading words, validating inputs to ensure they are real words/equations.
 */
public class GameService {
    private String targetWords;
    private Map<Integer, List<String>> wordsByLength;  // Map to store words by their length
    private List<String> currentWordsList;  // Words of current selected length
    private List<String> equationList;
    private static String targetEquation;
    private int currentWordLength = 5;  // Default word length
    private final Random random = new Random();
    
    /**
	 * Constructor to initialise the GameService.
	 * Creates a HashMap for the words ordered by length.
	 */
    public GameService() {
        wordsByLength = new HashMap<>();
    }

    //// WORDLE METHODS
    /**
	 * Load words from the words.txt file and group them by their length.
	 * Filter out any length categories with fewer than 10 words.
	 */
    public void loadWordsList() {
        try {
            InputStream in = getClass().getResourceAsStream("/wordle/Util/words.txt");
            if (in == null) {
                System.err.println(">>>>> ERROR: words.txt not found in resources!");
                return;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            // Group words by their length
            Map<Integer, List<String>> tempWordsByLength = reader.lines()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(word -> word.chars().allMatch(Character::isLetter))
                    .collect(Collectors.groupingBy(String::length));
            
            // Filter out any length categories with fewer than 10 words (too few to play with)
            wordsByLength = tempWordsByLength.entrySet().stream()
                    .filter(entry -> entry.getValue().size() >= 10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            
            // Set current words list to the default word length
            setWordLength(currentWordLength);
            
            if (wordsByLength.isEmpty()) {
                System.err.println(">>>>> ERROR: No valid words found in any length category!");
            }
        } catch (Exception e) {
            System.err.println(">>>>> ERROR loading words.txt: " + e.getMessage());
        }
    }
    
    // Method to change word length and update the target word
    /**
     * Set the word length for the game.
     * @param length - the desired word length
     * @return true if the word length is valid and set successfully, false otherwise
     */
    public boolean setWordLength(int length) {
        if (!wordsByLength.containsKey(length) || wordsByLength.get(length).isEmpty()) {
            System.err.println(">>>>> ERROR: No valid " + length + "-letter words found!");
            return false;
        }
        
        currentWordLength = length;
        currentWordsList = wordsByLength.get(length).stream()
                           .distinct()
                           .collect(Collectors.toList());
        
        // Select a new target word with the new length
        targetWords = currentWordsList.get(random.nextInt(currentWordsList.size())).toUpperCase();
        System.out.println(">>>>> Word selected for Wordle: " + targetWords);
        return true;
    }
    
    // Get available word lengths
    /**
	 * Get a list of available word lengths.
	 * @return a sorted list of available word lengths
	 */
    public List<Integer> getAvailableWordLengths() {
        return new ArrayList<>(wordsByLength.keySet()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get the list of useable words.
     * @return targetWords - the target words for the game
     */
    public String getTargetWords() {
        return targetWords;
    }
    
    /**
	 * Get the list of current words.
	 * @return currentWordsList - the list of words of the current length
	 */
    public List<String> getCurrentWordsList() {
        return currentWordsList;
    }
    
    /**
     * Get the current word length.
     * @return currentWordLength - the length of the current word
     */
    public int getCurrentWordLength() {
        return currentWordLength;
    }

    //// NERDLE METHODS
    // Load equations from file
    /**
     * Load equations from the nerdle_equations.txt file.
     */
    public void loadEquationsList() {
        try {
            InputStream in = getClass().getResourceAsStream("/wordle/Util/nerdle_equations.txt");
            if (in == null) {
                System.err.println(">>>>> ERROR: nerdle_equations.txt not found!");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            equationList = reader.lines()
                    .map(String::trim)
                    .filter(line -> line.length() == 8 && line.matches("^[0-9+\\-*/=]+$"))
                    .collect(Collectors.toList());
            if (equationList.isEmpty()) {
                System.err.println(">>>>> ERROR: No valid equations found in file.");
                return;
            }
            targetEquation = equationList.get(random.nextInt(equationList.size()));
            System.out.println(">>>>> Targetted Equation: " + targetEquation);
        } catch (Exception e) {
            System.err.println(">>>>> ERROR loading nerdle_equations.txt: " + e.getMessage());
        }
    }
    
    /**
     * Get the target equation for the game.
     * @return targetEquation - the target equation for the game
     */
    public static String getTargetEquation() {
        return targetEquation;
    }


    // Validate equation by evaluating left side and comparing with right side
    /**
	 * Validate the equation by checking if the left side equals the right side.
	 * @param input - the equation to validate
	 * @return true if the equation is valid, false otherwise
	 */
    public boolean isValidEquation(String input) {
        if (input == null || !input.contains("="))
            return false;
        String[] parts = input.split("=");
        if (parts.length != 2)
            return false;
        String leftPart = parts[0];
        String rightPart = parts[1];
        try {
            Object result = new javax.script.ScriptEngineManager().getEngineByName("JavaScript").eval(leftPart);
            if (result == null)
                return false;
            int calculatedVal = (int) Math.round(Double.parseDouble(result.toString()));
            int userRightVal = Integer.parseInt(rightPart);
            return calculatedVal == userRightVal;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the word is in target words list
    /**
     * Determine if the word is valid by checking the word list.
     * @param word - the word to validate
     * @return true if the word is valid, false otherwise
     */
    public boolean isValidWord(String word) {
        if (word == null || word.length() != currentWordLength) {
            return false;
        }
        
        String upperCaseWord = word.toUpperCase();
        
        // Check if the word is in the list of valid words for current length
        for (String validWord : currentWordsList) {
            if (validWord.toUpperCase().equals(upperCaseWord)) {
                return true;
            }
        }
        
        return false;
    }
}