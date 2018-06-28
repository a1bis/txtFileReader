package lt.at.assignment.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lt.at.assigment.input.TextFileReader;
import lt.at.assigment.output.ResulFiletWriter;

public class App {

	private static final String TEXT_FILE_FOLDER = "text_files";
	private static final String RESULTS_FOLDER = "results/";
	private static final String RESULT_FILE_A_TO_G = "a_g.txt";
	private static final String RESULT_FILE_H_TO_N = "h_n.txt";
	private static final String RESULT_FILE_O_TO_U = "o_u.txt";
	private static final String RESULT_FILE_V_TO_Z = "v_z.txt";

	private final ConcurrentHashMap<String, Integer> groupA_G;
	private final ConcurrentHashMap<String, Integer> groupH_N;
	private final ConcurrentHashMap<String, Integer> groupO_U;
	private final ConcurrentHashMap<String, Integer> groupV_Z;

	public App() {
		this.groupA_G = new ConcurrentHashMap<>();
		this.groupH_N = new ConcurrentHashMap<>();
		this.groupO_U = new ConcurrentHashMap<>();
		this.groupV_Z = new ConcurrentHashMap<>();
	}

	public void start() {
		readFiles();
		saveResultsToFile();
	}

	// Collects files from TEXT_FILE_FOLDER directory and populates four maps with words
	private void readFiles() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Path filesDirectory = Paths.get(TEXT_FILE_FOLDER);
		File directory = filesDirectory.toFile();
		if (!directory.exists()) {
			directory.mkdir();
		}
		try (Stream<Path> filesPaths = Files.walk(filesDirectory)) {
			List<Path> files = filesPaths.filter(Files::isRegularFile).collect(Collectors.toList());
			for (Path path : files) {
				executor.execute(new TextFileReader(path, groupA_G, groupH_N, groupO_U, groupV_Z));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void saveResultsToFile() {
		ResulFiletWriter.writeResult(groupA_G, RESULTS_FOLDER, RESULT_FILE_A_TO_G);
		ResulFiletWriter.writeResult(groupH_N, RESULTS_FOLDER, RESULT_FILE_H_TO_N);
		ResulFiletWriter.writeResult(groupO_U, RESULTS_FOLDER, RESULT_FILE_O_TO_U);
		ResulFiletWriter.writeResult(groupV_Z, RESULTS_FOLDER, RESULT_FILE_V_TO_Z);
	}

}
