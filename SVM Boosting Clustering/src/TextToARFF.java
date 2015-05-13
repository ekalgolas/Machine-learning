import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class to implement conversion of text file to ARFF file
 * 
 * @author Ekal.Golas
 *
 */
public class TextToARFF {
	/**
	 * Creates a ARFF file
	 * 
	 * @param input
	 *            input text file in LIBSVM format
	 * @param output
	 *            output ARFF file
	 * @throws IOException
	 */
	private static void createARFF(String input, String output)
			throws IOException {
		// Create a file
		PrintWriter writer = new PrintWriter(output);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = null;
		writer.println("@RELATION promoter\n");

		// Create attributes
		for (int i = 0; i < 57; i++)
			writer.println("@ATTRIBUTE t_" + i + " NUMERIC");

		// Write the data
		writer.println("@ATTRIBUTE class {0,1}\n");
		writer.println("@DATA");

		// Get all features for each line of the file
		while ((line = reader.readLine()) != null) {
			String[] attributes = line.split(" ");
			writer.print("{");
			for (int i = 1; i < attributes.length; i++) {
				String[] values = attributes[i].split(":");
				writer.print(values[0] + " " + values[1] + ", ");
			}

			writer.print((attributes.length - 1) + " " + attributes[0]);
			writer.println("}");
		}

		reader.close();
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
		String train_file = args[0];
		String test_file = args[1];

		// Create ARFF files
		createARFF(train_file, train_file.replace(".new", ".arff"));
		createARFF(test_file, test_file.replace(".new", ".arff"));
	}
}