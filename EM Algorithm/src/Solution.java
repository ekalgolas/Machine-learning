import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse data and apply EM algorithm and produce desired output
 * 
 * @author Ekal.Golas
 *
 */
public class Solution {
	/**
	 * Main method
	 * 
	 * @param args
	 *            Command line arguments
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// Initialize parameters
		String input = args[0];
		boolean var = args[1].equalsIgnoreCase("yes");
		List<Double> data = new ArrayList<>();
		long startTime = System.nanoTime();

		// Read the file and add data
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line = br.readLine();
			while (line != null) {
				data.add(Double.parseDouble(line));
				line = br.readLine();
			}
		}

		// Initialize and train the algorithm
		EM em = new EM(var);
		em.train(data, 3, var);
		long estimatedTime = System.nanoTime() - startTime;

		// Print the parameters
		System.out.print("Mean: ");
		for (Double double1 : em.mean)
			System.out.println(double1);

		System.out.print("\nVariance: ");
		for (Double double1 : em.variance)
			System.out.println(double1);

		System.out.print("\nProbabilities: ");
		for (Double double1 : em.prob)
			System.out.println(double1);

		System.out.print("\nIterations: ");
		System.out.println(em.iterations);

		System.out.println("\nRan in " + (estimatedTime / 1000000.0)
				+ " milliseconds");
	}
}