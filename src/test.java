import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Yuqing Han 680292
 * @author Xiang Xue 675875
 * 
 * This class is to run the program and generalize the results. Each case will
 * be executed 10 times and the relevant results will be saved into 10 .csv
 * files then combined to one.
 *
 */
public class test {
	public static void main(String[] args) {

		// Medium solar luminosity, 180 white daisies, 0 black daisies
		int ini_white = 180;
		int ini_black = 0;
		String solar = "M";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'A', count);
		}

		// Medium solar luminosity, 0 white daisies, 180 black daisies
		ini_white = 0;
		ini_black = 180;
		solar = "M";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'B', count);
		}

		// Medium solar luminosity, 90 white daisies, 90 black daisies
		ini_white = 90;
		ini_black = 90;
		solar = "M";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'C', count);
		}

		// Medium solar luminosity, 315 white daisies, 315 black daisies
		ini_white = 315;
		ini_black = 315;
		solar = "M";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'D', count);
		}

		// High solar luminosity, 180 white daisies, 0 black daisies
		ini_white = 180;
		ini_black = 0;
		solar = "H";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'E', count);
		}

		// High solar luminosity, 0 white daisies, 180 black daisies
		ini_white = 0;
		ini_black = 180;
		solar = "H";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'F', count);
		}

		// High solar luminosity, 90 white daisies, 90 black daisies
		ini_white = 90;
		ini_black = 90;
		solar = "H";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'G', count);
		}

		// High solar luminosity, 315 white daisies, 315 black daisies
		ini_white = 315;
		ini_black = 315;
		solar = "H";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'H', count);
		}

		// Ramp-up-ramp-down solar luminosity, 180 white daisies, 0 black
		// daisies
		ini_white = 180;
		ini_black = 0;
		solar = "R";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'I', count);
		}

		// Ramp-up-ramp-down solar luminosity, 0 white daisies, 180 black
		// daisies
		ini_white = 0;
		ini_black = 180;
		solar = "R";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'J', count);
		}

		// Ramp-up-ramp-down solar luminosity, 90 white daisies, 90 black
		// daisies
		ini_white = 90;
		ini_black = 90;
		solar = "R";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'K', count);
		}

		// Ramp-up-ramp-down solar luminosity, 315 white daisies, 315 black daisies
		ini_white = 315;
		ini_black = 315;
		solar = "R";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'L', count);
		}

		// Low solar luminosity, 180 white daisies, 0 black daisies
		ini_white = 180;
		ini_black = 0;
		solar = "L";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'M', count);
		}

		// Low solar luminosity, 0 white daisies, 180 black daisies
		ini_white = 0;
		ini_black = 180;
		solar = "L";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'N', count);
		}

		// Low solar luminosity, 90 white daisies, 90 black daisies
		ini_white = 90;
		ini_black = 90;
		solar = "L";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'O', count);
		}

		// Low solar luminosity, 315 white daisies, 315 black daisies
		ini_white = 315;
		ini_black = 315;
		solar = "L";

		for (int count = 0; count < 10; count++) {
			new Simulator(ini_white, ini_black, solar, 'P', count);
		}

		combine();
	}

	/**
	 * This function is to combine the results of one case into one .csv file so
	 * that we can calculate the average value more easily.
	 */
	public static void combine() {
		try {

			for (char run = 'A'; run < 'Q'; run++) {

				BufferedReader br0 = new BufferedReader(new FileReader("result"
						+ run + "0.csv"));
				BufferedReader br1 = new BufferedReader(new FileReader("result"
						+ run + "1.csv"));
				BufferedReader br2 = new BufferedReader(new FileReader("result"
						+ run + "2.csv"));
				BufferedReader br3 = new BufferedReader(new FileReader("result"
						+ run + "3.csv"));
				BufferedReader br4 = new BufferedReader(new FileReader("result"
						+ run + "4.csv"));
				BufferedReader br5 = new BufferedReader(new FileReader("result"
						+ run + "5.csv"));
				BufferedReader br6 = new BufferedReader(new FileReader("result"
						+ run + "6.csv"));
				BufferedReader br7 = new BufferedReader(new FileReader("result"
						+ run + "7.csv"));
				BufferedReader br8 = new BufferedReader(new FileReader("result"
						+ run + "8.csv"));
				BufferedReader br9 = new BufferedReader(new FileReader("result"
						+ run + "9.csv"));

				FileWriter writer = new FileWriter("combined" + run + ".csv");

				while (true) {

					String part0 = br0.readLine();
					String part1 = br1.readLine();
					String part2 = br2.readLine();
					String part3 = br3.readLine();
					String part4 = br4.readLine();
					String part5 = br5.readLine();
					String part6 = br6.readLine();
					String part7 = br7.readLine();
					String part8 = br8.readLine();
					String part9 = br9.readLine();

					if (part0 == null)
						break;

					// System.out.println(partOne + "," + partTwo);

					writer.append(part0);
					writer.append(',');
					writer.append(part1);
					writer.append(',');
					writer.append(part2);
					writer.append(',');
					writer.append(part3);
					writer.append(',');
					writer.append(part4);
					writer.append(',');
					writer.append(part5);
					writer.append(',');
					writer.append(part6);
					writer.append(',');
					writer.append(part7);
					writer.append(',');
					writer.append(part8);
					writer.append(',');
					writer.append(part9);

					writer.append('\n');

				}

				writer.flush();
				writer.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
