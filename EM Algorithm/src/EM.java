import java.util.List;

/**
 * Class to represent EM algorithm operations
 * 
 * @author Ekal.Golas
 *
 */
public class EM {
	/**
	 * List of means
	 */
	double[] mean;

	/**
	 * List of variances
	 */
	double[] variance;

	/**
	 * List of probabilities
	 */
	double[] prob;

	/**
	 * Number of iterations
	 */
	int iterations = 0;

	/**
	 * Constructor
	 * 
	 * @param constVar
	 *            Is variance constant?
	 */
	public EM(boolean constVar) {
		// Set variances to 1.0 if constant
		variance = new double[3];
		if (!constVar) {
			variance[0] = 0.5;
			variance[1] = 0.5;
			variance[2] = 0.5;
		} else {
			variance[0] = 1.0;
			variance[1] = 1.0;
			variance[2] = 1.0;
		}

		mean = new double[3];
		mean[0] = 5;
		mean[1] = 15;
		mean[2] = 25;

		prob = new double[3];
		prob[0] = 0.25;
		prob[1] = 0.5;
		prob[2] = 0.25;
	}

	/**
	 * Compute univariate gaussian
	 * 
	 * @param meanID
	 *            index for mean list
	 * @param varID
	 *            index for variance list
	 * @param dataID
	 *            index for data list
	 * @param data
	 *            the data
	 * @return result as a decimal
	 */
	public double gaussian(int meanID, int varID, int dataID, List<Double> data) {
		double diff = data.get(dataID) - mean[meanID];
		double exp = Math.exp((diff * diff) / (-2 * variance[varID]));
		double result = exp / Math.sqrt(2 * Math.PI * variance[varID]);

		if (Math.log(result) < -100)
			return 0;
		else
			return result;
	}

	/**
	 * Checks if convergence was reached
	 * 
	 * @param previous
	 *            Previous list
	 * @param current
	 *            List in this iteration
	 * @return True if converged, false otherwise
	 */
	public boolean checkConvergence(double[] previous, double[] current) {
		double diff = 0.0;
		for (int i = 0; i < previous.length; i++) {
			diff += Math.abs(previous[i] - current[i]);

			// If we cannot ignore the difference, return false
			if (Math.log(diff) > -6)
				return false;
		}

		// If we reach here, difference can be ignored
		return true;
	}

	/**
	 * Normalize the data by its magnitude
	 * 
	 * @param data
	 *            The data
	 */
	public void normalize(double[] data) {
		// Calculate magnitude
		double mag = 0.0;
		for (Double double1 : data)
			mag += (double1 * double1);

		// Normalize
		mag = Math.sqrt(mag);
		for (int i = 0; i < data.length; i++) {
			if (mag == 0)
				data[i] = 0.0;
			else
				data[i] /= mag;
		}
	}

	public void train(List<Double> data, int numClusters, boolean var) {
		// Initialize parameters
		double[] previous = new double[numClusters];

		// Do until convergence
		do {
			// Copy to previous
			for (int i = 0; i < prob.length; i++)
				previous[i] = prob[i];

			// Update probabilities
			double[][] listProb = new double[data.size()][numClusters];
			for (int i = 0; i < numClusters; i++)
				for (int j = 0; j < data.size(); j++)
					listProb[j][i] = gaussian(i, i, j, data) * previous[i];

			for (double[] element : listProb)
				normalize(element);

			// Compute sum for probabilities
			double[] sum = new double[numClusters];
			for (int i = 0; i < numClusters; i++)
				for (int j = 0; j < data.size(); j++)
					sum[i] += listProb[j][i];

			// Update means
			for (int i = 0; i < numClusters; i++) {
				double m = 0.0;
				for (int j = 0; j < data.size(); j++)
					m += listProb[j][i] * data.get(j);

				mean[i] = m / sum[i];
			}

			// Get variance if its not constant
			if (!var) {
				for (int i = 0; i < numClusters; i++) {
					double m = 0.0;
					for (int j = 0; j < data.size(); j++) {
						double diff = data.get(j) - mean[i];
						m += listProb[j][i] * diff * diff;
					}

					variance[i] = m / sum[i];
				}
			}

			// Compute probabilities
			for (int i = 0; i < numClusters; i++)
				prob[i] = sum[i] / data.size();

			normalize(prob);
			iterations++;
		} while (!checkConvergence(previous, prob));
	}
}