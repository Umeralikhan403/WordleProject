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
			//// we need to filter the words i mean select a word that has 5 alphabets 
			wordsList = reader.lines()
				.map(String::trim)
				.map(String::toLowerCase)
				.filter(word -> word.length() == 5 && word.chars().allMatch(Character::isLetter))
				.distinct()
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
}
