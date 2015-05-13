import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class to implement program of spam filters
 *
 * @author Ekal.Golas
 *
 */
public class Solution {
	/**
	 * Initialize hash set for total unique words
	 */
	public static HashSet<String> words = new HashSet<>();

	/**
	 * Parses the arguments and trains the classifiers
	 *
	 * @param stopwords
	 *            Set of stopwords
	 * @param args
	 *            Program arguments
	 * @param smoothing
	 *            If true, enables feature selection
	 * @return array of decimals, denothing naive bayes and logisitc regression
	 *         accuracies
	 * @throws IOException
	 */
	public static double[] classify(HashSet<String> stopwords, String[] args,
			boolean smoothing) throws IOException {
		// Initialize array to store accuracies
		double[] accuracies = new double[2];

		// Parse arguments
		double lambda = Double.parseDouble(args[0]);
		double eta = Double.parseDouble(args[1]);
		int limitIterations = Integer.parseInt(args[2]);
		String train_folder = args[3];
		String test_folder = args[4];

		// Parse the features from spam and ham folder
		ArrayList<ArrayList<String>> spam = Parser.parseFolder(train_folder
				+ "\\spam", stopwords, smoothing);
		ArrayList<ArrayList<String>> ham = Parser.parseFolder(train_folder
				+ "\\ham", stopwords, smoothing);
		ArrayList<ArrayList<String>> test_spam = Parser.parseFolder(test_folder
				+ "\\spam", stopwords, smoothing);
		ArrayList<ArrayList<String>> test_ham = Parser.parseFolder(test_folder
				+ "\\ham", stopwords, smoothing);

		// Train the classifiers
		NaiveBayes naiveBayes = new NaiveBayes();
		naiveBayes.train(ham, spam);

		LogisticRegression logisticRegression = new LogisticRegression();
		logisticRegression.train(ham, spam, lambda, limitIterations, eta);

		// Evaluate the classifiers
		accuracies[0] = naiveBayes.getAccuracy(test_ham, test_spam,
				spam.size(), ham.size());
		accuracies[1] = logisticRegression.getAccuracy(test_ham, test_spam);

		// Return the accuracies
		return accuracies;
	}

	/**
	 * Main function
	 *
	 * @param args
	 *            Command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Train classifiers without any filtering in features
		double[] accuracies = classify(new HashSet<>(), args, false);

		// Print the results
		System.out.println("Accuracy with Naive Bayes classifier is: "
				+ accuracies[0] + "%");
		System.out
				.println("\nAccuracy with Logistic Regression classifier is: "
						+ accuracies[1] + "%");

		// Train classifiers with smoothing enabled
		accuracies = classify(new HashSet<>(), args, true);

		// Print the results
		System.out
				.println("\nAccuracy with Naive Bayes classifier after applying feature selection is: "
						+ accuracies[0] + "%");
		System.out
				.println("\nAccuracy with Logistic Regression classifier after applying feature selection is: "
						+ accuracies[1] + "%");

		// Get stop words
		String stopwords_path = args[5];
		HashSet<String> stopwords = Parser.parseStopwords(stopwords_path);

		// Parse the features from spam and ham folder with stop words
		accuracies = classify(stopwords, args, false);

		// Print the results
		System.out
				.println("\nAccuracy with Naive Bayes classifier after filtering out stopwords is: "
						+ accuracies[0] + "%");
		System.out
				.println("\nAccuracy with Logistic Regression classifier after filtering out stopwords is: "
						+ accuracies[1] + "%");

		// Parse the features from spam and ham folder with stop words and
		// smoothing
		accuracies = classify(stopwords, args, true);

		// Print the results
		System.out
				.println("\nAccuracy with Naive Bayes classifier after applying feature selection and filtering out stopwords is: "
						+ accuracies[0] + "%");
		System.out
				.println("\nAccuracy with Logistic Regression classifier after applying feature selection and filtering out stopwords is: "
						+ accuracies[1] + "%");

	}
}