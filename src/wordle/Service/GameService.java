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

	public void loadWordsList() {
		try {

			InputStream in = getClass().getResourceAsStream("/wordle/Util/words.txt");
			if (in == null) {
				System.err.println(">>>>> ERROR: words.txt not found in resources!");
				return;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			wordsList = reader.lines().collect(Collectors.toList());

			Random rand = new Random();
			targetWords = wordsList.get(rand.nextInt(wordsList.size())).toUpperCase();

			System.out.println(">>>>> ** <<<<< DEBUG: Target word = " + targetWords); // Remove later
		} catch (Exception e) {
			// System.err.println(">>>>> ERROR: Failed to load words.txt. " +
			// e.getMessage());
		}
	}

	public String getTargetWords() {
		return targetWords;
	}

	public List<String> getWordsList() {
		return wordsList;
	}

}
