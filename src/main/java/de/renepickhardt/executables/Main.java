package de.renepickhardt.executables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("\n\n\nlets go!\n\n");

		generateTestFileFromWikipedia();
	}

	private static void generateTestFileFromWikipedia() {
		BufferedReader br = IOHelper.openReadFile(Config.get().wikiRawFile);
		String line = "";
		try {
			TreeMap<String, Integer> counts = new TreeMap<String, Integer>();
			int cnt = 0;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(" ");
				for (String s : values) {
					if (s.length() <= 3 || s.length() > 10) {
						continue;
					}
					Integer idx = counts.get(s);
					counts.put(s, idx == null ? 1 : idx + 1);
				}
				if (cnt++ % 1000 == 0) {
					IOHelper.log("parsed" + cnt + "wiki articles found "
							+ counts.size() + " different words");
				}
				if (cnt > 50000) {
					break;
				}
			}
			writeToFile(counts, 10);
			writeToFile(counts, 100);
			writeToFile(counts, 1000);
			writeToFile(counts, 10000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeToFile(TreeMap<String, Integer> counts,
			int threshold) throws IOException {
		BufferedWriter bw = IOHelper.openWriteFile(Config.get().inputFile + "."
				+ threshold);
		for (String key : counts.descendingKeySet()) {
			Integer value = counts.get(key);
			if (value > threshold) {
				bw.write(key + "\t" + value + "\n");
			}
		}

	}
}
