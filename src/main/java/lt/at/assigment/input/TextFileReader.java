package lt.at.assigment.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class TextFileReader implements Runnable {

	private final Path path;
	private final ConcurrentHashMap<String, Integer> groupA_G;
	private final ConcurrentHashMap<String, Integer> groupH_N;
	private final ConcurrentHashMap<String, Integer> groupO_U;
	private final ConcurrentHashMap<String, Integer> groupV_Z;

	public TextFileReader(Path path,
			ConcurrentHashMap<String, Integer> groupA_G,
			ConcurrentHashMap<String, Integer> groupH_N,
			ConcurrentHashMap<String, Integer> groupO_U,
			ConcurrentHashMap<String, Integer> groupV_Z) {
		this.path = path;
		this.groupA_G = groupA_G;
		this.groupH_N = groupH_N;
		this.groupO_U = groupO_U;
		this.groupV_Z = groupV_Z;
	}

	@Override
	public void run() {
		readFile();
	}

	private void readFile() {
		if (path.toFile().getName().endsWith(".txt")) {
			try {
				BufferedReader br = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1);
				Stream<String> lines = br.lines().map(str -> str.toLowerCase());
				Object[] objectArray = lines.toArray();
				for (Object obj : objectArray) {
					String line = (String) obj;
					String[] wordArray = line.split("\\W+");
					goThroughWordArray(wordArray);
				}
				lines.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void goThroughWordArray(String[] wordArray) {
		for (String word : wordArray) {
			addWordToGroup(word);
		}
	}

	private void addWordToGroup(String word) {
		char ch = word.charAt(0);
		if (ch >= 'a' && ch <= 'g') {
			addWordToMap(word, groupA_G);
		} else if (ch >= 'h' && ch <= 'n') {
			addWordToMap(word, groupH_N);
		} else if (ch >= 'o' && ch <= 'u') {
			addWordToMap(word, groupO_U);
		} else {
			addWordToMap(word, groupV_Z);
		}
	}

	private void addWordToMap(String word, Map<String, Integer> groupMap) {
		Integer numberOfOccurrences = groupMap.get(word);
		if (numberOfOccurrences != null) {
			groupMap.put(word, ++numberOfOccurrences);
		} else {
			groupMap.put(word, 1);
		}
	}

}
