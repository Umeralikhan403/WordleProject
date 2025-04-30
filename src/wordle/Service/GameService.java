package wordle.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GameService {
	private String targetWords;
	private List<String> wordsList;

	private final Random random = new Random();
	private static final char[] OPERATORS = { '+', '-', '*', '/' };
	private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

	public void loadWordsList() {
		try {
			InputStream in = getClass().getResourceAsStream("/wordle/Util/words.txt");
			if (in == null) {
				System.err.println(">>>>> ERROR: words.txt not found in resources!");
				return;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			//// we need to filter the words i mean select a word that has 5 alphabets
			wordsList = reader.lines().map(String::trim).map(String::toLowerCase)
					.filter(word -> word.length() == 5 && word.chars().allMatch(Character::isLetter)).distinct()
					.collect(Collectors.toList());

			if (wordsList.isEmpty()) {
				System.err.println(">>>>> error:: No 5-letter words found");
				return;
			}

			Random rand = new Random();
			targetWords = wordsList.get(rand.nextInt(wordsList.size())).toUpperCase();

			System.out.println(">>>>> word selected :: " + targetWords);
		} catch (Exception e) {
			System.err.println(">>>>> ERROR: Failed file :: " + e.getMessage());
		}
	}

	public String getTargetWords() {
		return targetWords;
	}

	public List<String> getWordsList() {
		return wordsList;
	}

	//// Nerdle game code starts here
	public String equationGeneration() {
	        while (true) {
	            String left = generateLeftExp();
	            try {
	                Object result = engine.eval(left);
	                if (result instanceof Number) {
	                    int answer = (int) Math.round(Double.parseDouble(result.toString()));
	                    String equation = left + "=" + answer;
	                    if (equation.length() == 8 && equation.matches("^[0-9+\\-*/=]+$")) {
	                        return equation;
	                    }
	                }
	            } catch (Exception ignored) {
	                
	            }
	        }
	    }

	private String generateLeftExp() {
		int length = random.nextInt(3) + 3;
		StringBuilder sb = new StringBuilder();
		boolean lastOperator = false;

		while (sb.length() < length) {
			if (lastOperator) {
				sb.append(random.nextInt(10));
				lastOperator = false;
			} else {
				if (sb.length() == 0 || random.nextBoolean()) {
					sb.append(random.nextInt(10));
				} else {
					char op = OPERATORS[random.nextInt(OPERATORS.length)];
					sb.append(op);
					lastOperator = true;
				}
			}
		}
		return sb.toString().replaceAll("[/*]-", "");
	}

	public boolean isValidEquation(String input) {
		if (!input.contains("="))
			return false;
		String[] parts = input.split("=");
		if (parts.length != 2)
			return false;

		try {
			Object results = engine.eval(parts[0]);
			int eval = (int) Math.round(Double.parseDouble(results.toString()));
			int providedInt = Integer.parseInt(parts[1]);
			return eval == providedInt;
		} catch (Exception e) {
			return false;
		}
	}

}
