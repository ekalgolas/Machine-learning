import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to implement parsing of data
 *
 * @author Ekal.Golas
 *
 */
public class Parser {
	/**
	 * Parses a spam/ham folder for features
	 *
	 * @param location
	 *            Location of the folder
	 * @param stopwords
	 *            Set of stopwords to be filtered from the data
	 * @param smoothing
	 *            If true, enables feature selection
	 * @return List of lists of features, each list a document
	 * @throws IOException
	 */
	public static ArrayList<ArrayList<String>> parseFolder(String location,
			HashSet<String> stopwords, boolean smoothing) throws IOException {
		// Initialize array list of features
		ArrayList<ArrayList<String>> features = new ArrayList<>();

		// Initialize list of words to be ignored in smoothing
		HashSet<String> ignore = new HashSet<>();
		ignore.add("subject:");
		ignore.add("cc");
		ignore.add("bcc");
		ignore.add("fwd");
		ignore.add("re");

		// Get all files in the folder
		File[] files = new File(location).listFiles();
		for (File file : files) {
			// Initialize list for features in this document
			ArrayList<String> document = new ArrayList<>();

			// Read each file
			BufferedReader reader = new BufferedReader(new FileReader(
					file.getAbsolutePath()));
			String line = null;

			// Get all features for each line of the file
			while ((line = reader.readLine()) != null) {
				// Split document into words by spaces
				String[] words = line.split(" ");

				// Do for each word found
				for (String string : words)
					// Add features filtering out the stop words
					if (!stopwords.contains(string.toLowerCase())) {
						// If smoothing is enabled
						if (smoothing) {
							// Consider only numbers and words
							Pattern p = Pattern.compile("[^a-z0-9 ]",
									Pattern.CASE_INSENSITIVE);
							Matcher m = p.matcher(string);
							boolean b = m.find();

							if (!b && !ignore.contains(string.toLowerCase()))
								document.add(string);
						} else
							// Else, add all the features
							document.add(string);
					}
			}

			// Add the document to the features
			features.add(document);
			reader.close();
		}

		// Return the features
		return features;
	}

	/**
	 * Reads a file to get a set of stopwords
	 *
	 * @param location
	 *            Location of the file
	 * @return Set of stopwords
	 * @throws IOException
	 */
	public static HashSet<String> parseStopwords(String location)
			throws IOException {
		// Initialize a hash set
		HashSet<String> stopwords = new HashSet<>();

		// Read the file
		BufferedReader reader = new BufferedReader(new FileReader(location));
		String line = null;

		// Get all stopwords from each line of the file
		while ((line = reader.readLine()) != null)
			stopwords.add(line);

		reader.close();

		// Return the stopwords
		return stopwords;
	}

	public static void parseTXT(String location,
			HashMap<String, HashMap<String, String>> userMap,
			HashMap<String, HashMap<String, String>> movieMap) {
		// Initialize variables
		BufferedReader br = null;
		String line = "";

		try {
			// Read the first line as header and parse the attribute names from
			// it
			br = new BufferedReader(new FileReader(location));

			// Read each line of the file
			while ((line = br.readLine()) != null) {

				// Use comma as separator
				String[] values = line.split(",");

				// Map the values
				if (!movieMap.containsKey(values[0]))
					movieMap.put(values[0], new HashMap<String, String>());
				if (!userMap.containsKey(values[1]))
					userMap.put(values[1], new HashMap<String, String>());

				movieMap.get(values[0]).put(values[1], values[2]);
				userMap.get(values[1]).put(values[0], values[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Finally, try to close reader
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}