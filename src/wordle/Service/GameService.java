// 1. First, let's modify the GameService class to handle variable word lengths

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

public class GameService {
    private String targetWords;
    private Map<Integer, List<String>> wordsByLength;  // Map to store words by their length
    private List<String> currentWordsList;  // Words of current selected length
    private List<String> equationList;
    private static String targetEquation;
    private int currentWordLength = 5;  // Default word length
    private final Random random = new Random();
    
    public GameService() {
        wordsByLength = new HashMap<>();
    }

    //// WORDLE METHODS
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
    public List<Integer> getAvailableWordLengths() {
        return new ArrayList<>(wordsByLength.keySet()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public String getTargetWords() {
        return targetWords;
    }

    public List<String> getCurrentWordsList() {
        return currentWordsList;
    }
    
    public int getCurrentWordLength() {
        return currentWordLength;
    }

    //// NERDLE METHODS
    // Load equations from file
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
    
    public static String getTargetEquation() {
        return targetEquation;
    }


    // Validate equation by evaluating left side and comparing with right side
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