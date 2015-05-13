import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class to implement conversion of text files to a single ARFF file
 * 
 * @author Ekal.Golas
 *
 */
public class TextToARFF {
	/**
	 * Set of words
	 */
	private static HashSet<String> words = new HashSet<>();

	/**
	 * Creates ARFF file from dataset
	 * 
	 * @param spam
	 *            Spam dataset
	 * @param ham
	 *            Ham dataset
	 * @param filename
	 *            Name of file to be created
	 * @throws FileNotFoundException
	 */
	private static void createARFF(ArrayList<ArrayList<String>> spam,
			ArrayList<ArrayList<String>> ham, String filename)
			throws FileNotFoundException {
		// Create a file
		PrintWriter writer = new PrintWriter(filename);
		writer.println("@RELATION ham_spam\n");

		// Create and index for features and write those features as attributes
		HashMap<String, Integer> indexes = new HashMap<>();
		int index = 0;
		for (String string : words) {
			writer.println("@ATTRIBUTE t_" + string + " NUMERIC");
			indexes.put(string, index++);
		}

		// Write the data
		writer.println("@ATTRIBUTE class {-1,1}\n");
		writer.println("@DATA");
		for (ArrayList<String> features : ham) {
			// Get the count in a map
			writer.print("{");
			HashMap<String, Integer> count = new HashMap<>();
			Perceptron.map(features, count);

			// Write the data to file in order of indexes
			for (String string : indexes.keySet())
				if (count.containsKey(string))
					writer.print(indexes.get(string) + " " + count.get(string)
							+ ", ");

			writer.println(indexes.size() + " -1}");
		}
		for (ArrayList<String> features : spam) {
			writer.print("{");
			HashMap<String, Integer> count = new HashMap<>();
			Perceptron.map(features, count);

			for (String string : indexes.keySet())
				if (count.containsKey(string))
					writer.print(indexes.get(string) + " " + count.get(string)
							+ ", ");

			writer.println(indexes.size() + " 1}");
		}

		writer.close();
	}

	/**
	 * Main method
	 * 
	 * @param args
	 *            Command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Parse the arguments
		String train_folder = args[0];
		String test_folder = args[1];

		// Parse the features from spam and ham folder
		ArrayList<ArrayList<String>> spam = Parser.parseFolder(train_folder
				+ "\\spam", new HashSet<>(), true);
		ArrayList<ArrayList<String>> ham = Parser.parseFolder(train_folder
				+ "\\ham", new HashSet<>(), true);
		ArrayList<ArrayList<String>> test_spam = Parser.parseFolder(test_folder
				+ "\\spam", new HashSet<>(), true);
		ArrayList<ArrayList<String>> test_ham = Parser.parseFolder(test_folder
				+ "\\ham", new HashSet<>(), true);

		// Create set of words
		for (ArrayList<String> features : spam)
			for (String feature : features)
				words.add(feature);
		for (ArrayList<String> features : ham)
			for (String feature : features)
				words.add(feature);

		// Create ARFF files
		createARFF(spam, ham, "train.arff");
		createARFF(test_spam, test_ham, "test.arff");
	}
}