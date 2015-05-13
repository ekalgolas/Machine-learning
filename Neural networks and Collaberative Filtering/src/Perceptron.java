import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class which implements Perceptron classifier
 * 
 * @author Ekal.Golas
 *
 */
public class Perceptron {
	/**
	 * Map the count of each feature
	 */

	/**
	 * Map that contains string to decimal pairs
	 */
	HashMap<String, Double> weights = new HashMap<>();

	/**
	 * W0 weight in the equation
	 */
	public double W0 = 1.0;

	/**
	 * Default constructor
	 */
	public Perceptron() {
		// Map weights
		Iterator<String> iterator = Solution1.words.iterator();
		while (iterator.hasNext())
			weights.put(iterator.next(), Math.random() * 3 - 1);
	}

	/**
	 * Maps the data to a hashmap of string-integer
	 *
	 * @param data
	 *            Data to be mapped
	 * @param map
	 *            Variable to be mapped into
	 */
	public static void map(ArrayList<String> data, HashMap<String, Integer> map) {
		// Map features in each document
		for (String feature : data) {
			// Map features to counts in map
			if (map.containsKey(feature))
				map.put(feature, map.get(feature) + 1);
			else
				map.put(feature, 1);
		}
	}

	private double classify(ArrayList<String> feature,
			HashMap<String, Integer> count) {
		// Initialize the sum
		double sum = 0.0;

		// Count occurences
		map(feature, count);

		// Return the sum
		for (String string : feature)
			if (weights.containsKey(string))
				sum += weights.get(string) * count.get(string);

		// Classify and return
		return ((W0 + sum) > 0.0 ? 1.0 : -1.0);
	}

	/**
	 * Trains the classifier
	 * 
	 * @param ham
	 *            Ham data
	 * @param spam
	 *            Spam data
	 * @param limit
	 *            Limit to number of iterations
	 * @param eta
	 *            Learning rate
	 */
	public void train(ArrayList<ArrayList<String>> ham,
			ArrayList<ArrayList<String>> spam, int limit, double eta) {
		// Do till we reach the limit
		for (int i = 0; i < limit; i++) {
			// Process features in spam
			for (ArrayList<String> features : spam) {
				// Get classification
				HashMap<String, Integer> count = new HashMap<>();
				double classification = classify(features, count);

				// Update weights
				W0 += eta * (1.0 - classification);
				for (String feature : features)
					weights.put(feature, weights.get(feature) + eta
							* (1.0 - classification) * count.get(feature));
			}

			// Process features in ham
			for (ArrayList<String> features : ham) {
				// Get classification
				HashMap<String, Integer> count = new HashMap<>();
				double classification = classify(features, count);

				// Update weights
				W0 += eta * (-1.0 - classification);
				for (String feature : features)
					weights.put(feature, weights.get(feature) + eta
							* (-1.0 - classification) * count.get(feature));
			}
		}
	}

	/**
	 * Calculates the accuracy of a classifier
	 * 
	 * @param test_ham
	 *            Test ham data
	 * @param test_spam
	 *            Test spam data
	 * @return Accuracy as a percentage
	 */
	public double getAccuracy(ArrayList<ArrayList<String>> test_ham,
			ArrayList<ArrayList<String>> test_spam) {
		// Initialize accuracy
		double accuracy = 0.0;

		// Process test ham data
		for (ArrayList<String> features : test_ham) {
			// Get classification
			HashMap<String, Integer> map = new HashMap<>();
			double classification = classify(features, map);

			// If -1, classification is correct
			if (classification == -1.0)
				accuracy++;
		}

		// Process test spam data
		for (ArrayList<String> features : test_spam) {
			// Get classification
			HashMap<String, Integer> map = new HashMap<>();
			double classification = classify(features, map);

			// If +1, classification is correct
			if (classification == 1.0)
				accuracy++;
		}

		// Return the accuracy
		return (accuracy * 100) / (test_ham.size() + test_spam.size());
	}
}