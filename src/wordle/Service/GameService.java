package wordle.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameService {

    private String targetWords;
    private List<String> wordsList;
    private List<String> equationList;
    private String targetEquation;

    private final Random random = new Random();

    // ------------------- WORDLE METHODS ---------------------------

    public void loadWordsList() {
        try {
            InputStream in = getClass().getResourceAsStream("/wordle/Util/words.txt");
            if (in == null) {
                System.err.println(">>>>> ERROR: words.txt not found in resources!");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            wordsList = reader.lines()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(word -> word.length() == 5 && word.chars().allMatch(Character::isLetter))
                    .distinct()
                    .collect(Collectors.toList());

            if (wordsList.isEmpty()) {
                System.err.println(">>>>> ERROR: No valid 5-letter words found!");
                return;
            }

            targetWords = wordsList.get(random.nextInt(wordsList.size())).toUpperCase();
            System.out.println(">>>>> Word selected for Wordle: " + targetWords);

        } catch (Exception e) {
            System.err.println(">>>>> ERROR loading words.txt: " + e.getMessage());
        }
    }

    public String getTargetWords() {
        return targetWords;
    }

    public List<String> getWordsList() {
        return wordsList;
    }

    // ------------------- NERDLE METHODS ---------------------------

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

    public String getTargetEquation() {
        return targetEquation;
    }

    // Validate equation by evaluating left side and comparing with right side (Correct validation)
    public boolean isValidEquation(String input) {

        if (input == null || !input.contains("=")) return false;

        String[] parts = input.split("=");

        if (parts.length != 2) return false;

        String leftPart = parts[0];
        String rightPart = parts[1];

        try {
            Object result = new javax.script.ScriptEngineManager().getEngineByName("JavaScript").eval(leftPart);

            if (result == null) return false;

            int calculatedValue = (int) Math.round(Double.parseDouble(result.toString()));
            int userRightValue = Integer.parseInt(rightPart);

            return calculatedValue == userRightValue;

        } catch (Exception e) {
            return false;
        }
    }
}
