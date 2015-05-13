import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

/**
 * Class to implement equations 1 and 2
 * 
 * @author Ekal.Golas
 *
 */
public class Recommender {
	public static HashMap<String, HashMap<String, String>> trainUserMap = new HashMap<>();

	public static HashMap<String, HashMap<String, String>> trainMovieMap = new HashMap<>();

	public double getPrediction(String activeUser, String Movie) {
		// Initialize variables
		double prediction = 0.0;
		double weight, weightSum = 0, sum = 0;
		String user;

		for (String string : trainMovieMap.get(Movie).keySet()) {
			user = string.split(",")[0];
			weight = correlation(activeUser, user);
			weightSum += Math.abs(weight);
			sum += weight * (getRating(user, Movie) - getAverageRating(user));
		}

		// Normalize
		sum /= weightSum;

		// Calculate prediction
		prediction = getAverageRating(activeUser) + sum;

		// Return computed prediction
		return prediction;
	}
	private double correlation(String activeUser, String targetUser) {
		// Initialize variables
		double topSum, bottomSumActive, bottomSumTarget, rating1, rating2;
		topSum = bottomSumActive = bottomSumTarget = 0.0;
		double activeAvg = getAverageRating(activeUser);
		double targetAvg = getAverageRating(targetUser);

		ArrayList<Pair<String, String>> pairs = getCommonMovies(activeUser,
				targetUser);
		if (pairs == null)
			return -1.0;

		for (Pair<String, String> pair : pairs) {
			rating1 = Double.parseDouble(pair.getKey()) - activeAvg;
			rating2 = Double.parseDouble(pair.getValue()) - targetAvg;

			topSum += rating1 * rating2;
			bottomSumActive += rating1 * rating1;
			bottomSumTarget += rating2 * rating2;
		}

		// Return the computed value if bottom sums are not 0
		if (bottomSumActive != 0 && bottomSumTarget != 0)
			return topSum / Math.sqrt(bottomSumActive * bottomSumTarget);
		else
			return -1.0;
	}

	private ArrayList<Pair<String, String>> getCommonMovies(String activeUser,
			String targetUser) {
		// Initialize list of pairs
		ArrayList<Pair<String, String>> pairs = new ArrayList<>();

		// Get movies for active and target user
		HashMap<String, String> lefts = trainUserMap.get(activeUser);
		HashMap<String, String> rights = trainUserMap.get(targetUser);

		// Check for null
		if (lefts == null || rights == null)
			return null;

		// Perform join
		for (String string : lefts.keySet()) {
			// Add common movies between active and target user to pairs
			if (rights.containsKey(string))
				pairs.add(new Pair<String, String>(string, rights.get(string)));
		}

		// Return the pairs
		return pairs;
	}

	private double getAverageRating(String user) {
		// Initialize average
		double average = 0.0;

		// Sum all ratings
		HashMap<String, String> movies = trainUserMap.get(user);
		if (movies == null)
			return 0.0;

		for (String string : movies.keySet())
			average += Double.parseDouble(movies.get(string));

		// Return computed average
		return average / movies.size();
	}

	private double getRating(String user, String movie) {
		// Get movies and search in them
		HashMap<String, String> movies = trainUserMap.get(user);
		if (movies == null)
			return 0.0;

		// Get the else if it exists, else 0
		if (movies.containsKey(movie))
			return Double.parseDouble(movies.get(movie));
		else
			return 0.0;
	}
}