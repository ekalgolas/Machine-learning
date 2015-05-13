import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to implement Naive Bayes classifier
 *
 * @author Ekal.Golas
 *
 */
public class NaiveBayes {
	/**
	 * Map the count of each feature in ham
	 */
	private HashMap<String, Integer> count_ham = new HashMap<>();

	/**
	 * Map the count of each feature in spam
	 */
	private HashMap<String, Integer> count_spam = new HashMap<>();

	/**
	 * Count of number of ham keywords
	 */
	private double hams;

	/**
	 * Count of number of spam keywords
	 */
	private double spams;

	/**
	 * Maps the data to a hashmap of string-integer
	 *
	 * @param data
	 *            Data to be mapped
	 * @param map
	 *            Variable to be mapped into
	 * @return Count of features
	 */
	private double map(ArrayList<ArrayList<String>> data,
			HashMap<String, Integer> map) {
		// Initialize count
		double count = 0.0;

		// Map features in each document
		for (ArrayList<String> features : data)
			for (String feature : features) {
				// Add it to the set of words
				Solution.words.add(feature);
				count++;

				// Map features to counts in map
				if (map.containsKey(feature))
					map.put(feature, map.get(feature) + 1);
				else
					map.put(feature, 2);
			}

		// Return the count
		return count;
	}

	/**
	 * Trains the classifier
	 *
	 * @param ham
	 *            Data containing features in ham
	 * @param spam
	 *            Data containing features in spam
	 */
	public void train(ArrayList<ArrayList<String>> ham,
			ArrayList<ArrayList<String>> spam) {
		// Map features in spam and ham
		hams = map(ham, count_ham);
		spams = map(spam, count_spam);
	}

	/**
	 * Checks if probability of ham occurrence is greater then spam occurrence
	 * 
	 * @param features
	 *            List of features
	 * @param spam_documents
	 *            Number of spam documents
	 * @param ham_documents
	 *            Number of ham documents
	 * @return Result as boolean
	 */
	private boolean check(ArrayList<String> features, int spam_documents,
			int ham_documents) {
		// Calculate parameters
		double numberOfDocuments = spam_documents + ham_documents;
		double prob_spam = Math.log(spam_documents / numberOfDocuments);
		double prob_ham = Math.log(ham_documents / numberOfDocuments);

		// Do for each feature
		for (String feature : features) {
			// Get occurences for spam and ham
			double occur_ham = count_ham.containsKey(feature) ? count_ham
					.get(feature) : 1.0;
			double occur_spam = count_spam.containsKey(feature) ? count_spam
					.get(feature) : 1.0;

			// Compute log likelihood
			prob_ham += Math.log(occur_ham / (hams + Solution.words.size()));
			prob_spam += Math.log(occur_spam / (spams + Solution.words.size()));
		}

		// Return the result
		return prob_ham > prob_spam;
	}

	/**
	 * Calculates the accuracy of a classifier
	 * 
	 * @param test_ham
	 *            Test ham data
	 * @param test_spam
	 *            Test spam data
	 * @param spam_documents
	 *            Number of spam documents
	 * @param ham_documents
	 *            Number of ham documents
	 * @return Accuracy as a percentage
	 */
	public double getAccuracy(ArrayList<ArrayList<String>> test_ham,
			ArrayList<ArrayList<String>> test_spam, int spam_documents,
			int ham_documents) {
		// Initialize accuracy
		double accuracy = 0.0;

		// Check for ham
		for (ArrayList<String> features : test_ham)
			if (check(features, spam_documents, ham_documents))
				accuracy++;

		// Check for spam
		for (ArrayList<String> features : test_spam)
			if (!check(features, spam_documents, ham_documents))
				accuracy++;

		// Return the accuracy
		return (accuracy * 100) / (test_ham.size() + test_spam.size());
	}
}