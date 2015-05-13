import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to implement Collaberative filtering
 * 
 * @author Ekal.Golas
 *
 */
public class Solution2 {
	/**
	 * Main method
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		// Parse the arguments
		String train = args[0];
		String test = args[1];

		// Parse the files
		Parser.parseTXT(train, Recommender.trainUserMap,
				Recommender.trainMovieMap);

		double[] errors = getAccuracy(test);
		System.out.println("Mean Absolute Error: " + errors[0]);
		System.out.println("Root Mean Squared Error: " + errors[1]);
	}

	@SuppressWarnings("finally")
	private static double[] getAccuracy(String testLocation) {
		// Initialize variables
		double[] errors = new double[2];
		BufferedReader br = null;
		String line = "";
		Recommender recommender = new Recommender();
		double count = 0.0;

		try {
			// Read the file
			br = new BufferedReader(new FileReader(testLocation));

			// Read each line of the file
			while ((line = br.readLine()) != null) {

				// Use comma as separator
				String[] values = line.split(",");

				// Get prediction
				double prediction = recommender.getPrediction(values[1],
						values[0]);

				// Calculate mean error
				if (prediction != -1.0) {
					errors[0] += Math.abs(Double.parseDouble(values[2])
							- prediction);
					errors[1] += Math.pow(Double.parseDouble(values[2])
							- prediction, 2);
				}

				// Increment count
				count++;

				if (count % 100 == 0) {
					System.out.println("\nData for " + count + " instances:");
					System.out.println("Mean Absolute Error: " + errors[0]
							/ count);
					System.out.println("Root Mean Squared Error: " + errors[1]
							/ count);
				}
			}

			// Get mean
			errors[0] /= count;
			errors[1] = Math.sqrt(errors[1]) / count;
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

			// Return errors
			return errors;
		}
	}
}