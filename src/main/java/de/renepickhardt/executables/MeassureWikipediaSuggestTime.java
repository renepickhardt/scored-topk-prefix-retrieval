package de.renepickhardt.executables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.renepickhardt.utils.IOHelper;

public class MeassureWikipediaSuggestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		doWikiRequest("N");
		Thread.sleep((long) (1000 + 500 * Math.random()));
		doWikiRequest("No");
		Thread.sleep((long) (1000 + 500 * Math.random()));
		doWikiRequest("Notr");
		Thread.sleep((long) (1000 + 500 * Math.random()));
		doWikiRequest("Notru");
		Thread.sleep((long) (1000 + 500 * Math.random()));
		doWikiRequest("Zentra");
		Thread.sleep((long) (1000 + 500 * Math.random()));
		doWikiRequest("Versic");
	}

	private static void doWikiRequest(String prefix) {
		long start = System.nanoTime();
		URL url = prepareUrl(prefix);
		doRequest(url);
		long end = System.nanoTime();
		IOHelper.log("WIKIREQUEST: for " + prefix + " took: " + (end - start)
				/ 1000 + " micro seconds");

	}

	private static void doRequest(URL url) {
		URLConnection yc;
		try {
			yc = url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while (in.readLine() != null) {
				// System.out.println(inputLine);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static URL prepareUrl(String prefix) {
		try {
			return new URL(
					"http://en.wikipedia.org/w/api.php?format=json&action=opensearch&search="
							+ prefix + "&namespace=0&suggest=");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
