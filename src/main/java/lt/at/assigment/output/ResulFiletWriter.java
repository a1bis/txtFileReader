package lt.at.assigment.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ResulFiletWriter {

	public static void writeResult(Map<String, Integer> wordMap, String resultDirectory, String filename) {
		File filenameResultDirectory = new File(resultDirectory);
		if (!filenameResultDirectory.exists()) {
			filenameResultDirectory.mkdir();
		}
		Path path = Paths.get(resultDirectory + filename);
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			wordMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(m -> {
				try {
					writer.write(m.getKey() + " : " + m.getValue());
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
