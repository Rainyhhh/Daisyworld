import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * This class controls the changes of all the patches in 1000 ticks. It contains
 * a map of patches, and at the beginning, according to the user input, randomly
 * grow the white daisies and black daisies. In every tick, track every patch,
 * update the state of each patch. After updating the states of all the patches
 * in one tick, export the data to the file to analyze.
 */
public class Simulator {

	// set the space patches, the ground is a 30x30 map, this is the one to
	// export.
	private Patch[][] patch = new Patch[30][30];

	// this array is to record the data of all the patches before calling the
	// function diffuse.
	private Patch[][] pre_patch = new Patch[30][30];

	Random r = new Random();

	// represents solar luminosity
	private double solar;

	// Prepare to export data to file
	FileWriter writer = null;

	/**
	 * get the population of the daisies and solar luminosity and randomly grow
	 * the daisies in the map, then call print to export the data of the first
	 * tick. solar luminosity has 4 kinds of values, according to the inputs.
	 * 
	 * @param ini_white
	 * @param ini_black
	 * @param solar
	 */
	public Simulator(int ini_white, int ini_black, String solar, char run, int count) {
		// H = High, M = Medium, L = Low, R = Ramp up Ramp down
		// to set the value for solar luminosity
		if (solar.equals("H")) {
			this.solar = 1.4;
		} else if (solar.equals("M")) {
			this.solar = 1.0;
		} else if (solar.equals("L")) {
			this.solar = 0.6;
		} else if (solar.equals("R")) {
			this.solar = 0.8;
		}
		try {
			// create a new file
			writer = new FileWriter("result" + run + count + ".csv");
			// Add title of each column into the file
			writer.append("Solar Luminosity");
			writer.append(',');
			writer.append("White Dasiy" + String.valueOf(ini_white));
			writer.append(',');
			writer.append("Black Daisy" + String.valueOf(ini_black));
			writer.append(',');
			writer.append("Global Temperature");
			writer.append('\n');
			// Set the whole ground with 900 empty patches
			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					// Daisy(0) means empty patch
					pre_patch[i][j] = new Patch(new Daisy(0, 0), this.solar);
				}
			}
			int x, y;
			// Randomly grow the white daisies
			for (int i = 0; i < ini_white; i++) {
				x = r.nextInt(30);
				y = r.nextInt(30);
				// only grow a new daisy when this patch is empty
				while (pre_patch[x][y].getDaisy().getColor() != 0) {
					x = r.nextInt(30);
					y = r.nextInt(30);
				}
				// Daisy(1) is a white daisy
				pre_patch[x][y] = new Patch(new Daisy(1, r.nextInt(25)),
						this.solar);
			}
			// Randomly grow the black daisies
			for (int i = 0; i < ini_black; i++) {
				x = r.nextInt(30);
				y = r.nextInt(30);
				// only grow a new daisy when this patch is empty
				while (pre_patch[x][y].getDaisy().getColor() != 0) {
					x = r.nextInt(30);
					y = r.nextInt(30);
				}
				// Daisy(1) is a black daisy
				pre_patch[x][y] = new Patch(new Daisy(2, r.nextInt(25)),
						this.solar);
			}
			// Calculate the initial temperature of each patch
			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					pre_patch[i][j].cal_temperature();
				}
			}
			// Recalculate the temperature by considering the influence caused
			// by neighbors
			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					patch[i][j] = pre_patch[i][j];
					patch[i][j].setTemperature(diffuse(i, j));
				}
			}
			// Export the results of first tick
			print();
			// If solar luminosity changes during running, call
			// update_R_display.
			// Otherwise, call update_display.
			if (solar.equals("R")) {
				update_R_display();
			} else
				update_display();
			writer.flush();
			// close the file
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this function execute the changes for next 999 ticks. from the top left,
	 * track every patch. if the patch is empty, just update the local
	 * temperature of the patch. if there exists a daisy and the daisy is not a
	 * new one (age = 0), check if reproduceable. if can reproduce, randomly
	 * choose a neighbor. if the neighbor is empty, grow a daisy there in the
	 * same color. if the neighbor is not empty, do nothing. then update the
	 * current local temperature.
	 */
	public void update_display() {
		for (int t = 1; t < 1000; t++) { // loop 1000 times

			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					// update the temperature of each patch
					patch[i][j].cal_temperature();
					// save the results into the pre_patch array
					pre_patch[i][j] = patch[i][j];
				}
			}

			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					// diffuse, recalculate the temperature
					patch[i][j].setTemperature(diffuse(i, j));
				}
			}

			for (int x = 0; x < 30; x++) { // track every patch
				for (int y = 0; y < 30; y++) {
					// check if the patch has a daisy and not a new one in that
					// tick
					if (this.patch[x][y].getDaisy().getColor() != 0
							&& this.patch[x][y].getDaisy().getAge() != 0) {
						// check whether the daisy in the current patch can
						// reproduce
						if (this.patch[x][y].reproduce()) {
							boolean growable = false;
							// track every neighbor, to know if there is a
							// neighbor is empty

							for (int m = x - 1; m <= x + 1; m++) {
								for (int n = y - 1; n <= y + 1; n++) {
									// since the earth is round, the edge of the
									// left is also the neighbor of the edge of
									// the right
									if (m != x && n != y) {
										int m1 = m;
										int n1 = n;
										if (m1 == -1)
											m1 = 29;
										else if (m1 == 30)
											m1 = 0;
										if (n1 == -1)
											n1 = 29;
										else if (n1 == 30)
											n1 = 0;
										// if there is a neighbor is empty
										if (this.patch[m1][n1].getDaisy()
												.getColor() == 0) {
											// then can random choose a place to
											// reproduce
											growable = true;
										}
									}
								}

							}

							if (growable == true) {
								// randomly choose a neighbor until finding an
								// empty neighbor
								int m = r.nextInt(3) + x - 1;
								int n = r.nextInt(3) + y - 1;
								if (m == -1)
									m = 29;
								else if (m == 30)
									m = 0;
								if (n == -1)
									n = 29;
								else if (n == 30)
									n = 0;
								while ((m == x && n == y)
										|| this.patch[m][n].getDaisy()
												.getColor() != 0) {
									m = r.nextInt(3) + x - 1;
									n = r.nextInt(3) + y - 1;
									// as the earth is round
									if (m == -1)
										m = 29;
									else if (m == 30)
										m = 0;
									if (n == -1)
										n = 29;
									else if (n == 30)
										n = 0;
								}
								// after finding an empty neighbor, grow a new
								// one in current species
								this.patch[m][n].setDaisy(new Daisy(
										this.patch[x][y].getDaisy().getColor(),
										0));
							}

						}

					} else if (this.patch[x][y].getDaisy().getColor() != 0)
						// if there is a new daisy, just set the age +1
						this.patch[x][y].getDaisy().setAge(
								this.patch[x][y].getDaisy().getAge() + 1);
				}
			}
			// export_data();
			print();
		}
	}

	/**
	 * The aim of this function is similar to the one above. The difference is
	 * the solar luminosity will change as time goes on. During 200 to 400
	 * ticks, the sl will increase 0.005 for each tick. During 600 to 850 ticks,
	 * the sl will decrease 0.0025 for each tick.
	 */
	public void update_R_display() {
		for (int t = 1; t < 1000; t++) { // loop 1000 times

			// these changes are the same as the netlogo
			if (t > 200 && t <= 400)
				solar += 0.005;
			if (t > 600 && t <= 850)
				solar -= 0.0025;

			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					// since the solar luminosity changes, the patch needs to
					// update
					patch[i][j].setSolar(solar);
					patch[i][j].cal_temperature();
					pre_patch[i][j] = patch[i][j];
				}
			}

			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 30; j++) {
					// diffuse, recalculate the temperature
					patch[i][j].setTemperature(diffuse(i, j));
				}
			}

			for (int x = 0; x < 30; x++) { // track every patch
				for (int y = 0; y < 30; y++) {
					// check if the patch has a daisy and not a new one in that
					// tick
					if (this.patch[x][y].getDaisy().getColor() != 0
							&& this.patch[x][y].getDaisy().getAge() != 0) {
						// check whether the daisy in the current patch can
						// reproduce
						if (this.patch[x][y].reproduce()) {
							boolean growable = false;
							// track every neighbor, to know if there is a
							// neighbor is empty
							for (int m = x - 1; m <= x + 1; m++) {
								for (int n = y - 1; n <= y + 1; n++) {
									// since the earth is round, the edge of the
									// left is also the neighbor of the edge of
									// the right
									if (m != x && n != y) {
										int m1 = m;
										int n1 = n;
										if (m1 == -1)
											m1 = 29;
										else if (m1 == 30)
											m1 = 0;
										if (n1 == -1)
											n1 = 29;
										else if (n1 == 30)
											n1 = 0;
										// if there is a neighbor is empty
										if (this.patch[m1][n1].getDaisy()
												.getColor() == 0) {
											// then can random choose a place to
											// reproduce
											growable = true;
										}
									}
								}

							}
							if (growable == true) {
								// randomly choose a neighbor until finding an
								// empty neighbor
								int m = r.nextInt(3) + x - 1;
								int n = r.nextInt(3) + y - 1;
								if (m == -1)
									m = 29;
								else if (m == 30)
									m = 0;
								if (n == -1)
									n = 29;
								else if (n == 30)
									n = 0;
								while ((m == x && n == y)
										|| this.patch[m][n].getDaisy()
												.getColor() != 0) {
									m = r.nextInt(3) + x - 1;
									n = r.nextInt(3) + y - 1;
									// as the earth is round
									if (m == -1)
										m = 29;
									else if (m == 30)
										m = 0;
									if (n == -1)
										n = 29;
									else if (n == 30)
										n = 0;
								}
								// after finding an empty neighbor, grow a new
								// one in current species
								this.patch[m][n].setDaisy(new Daisy(
										this.patch[x][y].getDaisy().getColor(),
										0));
							}

						}

					} else if (this.patch[x][y].getDaisy().getColor() != 0)
						// if there is a new daisy, just set the age +1
						this.patch[x][y].getDaisy().setAge(
								this.patch[x][y].getDaisy().getAge() + 1);
				}
			}
			print();
		}
	}

	/**
	 * This function is to recalculate the temperature of each patch. Since each
	 * patch can be influenced by all the other neighbors, we have to take this
	 * influence into account. Each patch will offer 50% temperature to the
	 * neighbors and receive 1/16 temperature from each neighbor.
	 * 
	 * @param x
	 *            from current patch
	 * @param y
	 *            from current patch
	 * @return temperature
	 */
	public double diffuse(int x, int y) {
		// each patch will offer 50% temperature to the neighbors
		double temperature = pre_patch[x][y].getTemperature() / 2;
		for (int m = x - 1; m <= x + 1; m++) {
			for (int n = y - 1; n <= y + 1; n++) {
				if (!(m < 0 || m >= 30 || n < 0 || n >= 30 || (m == x && n == y))) {
					temperature += pre_patch[m][n].getTemperature() / 16;
				}
			}
		}
		return temperature;
	}

	/**
	 * This function is to export the data to .csv file. This function will be
	 * called once a tick.
	 * 
	 * @param Luminosity
	 * @param w_daisy
	 * @param b_daisy
	 * @param global_temp
	 */
	public void export_data(double Luminosity, int w_daisy, int b_daisy,
			double global_temp) {
		try {
			writer.append(String.valueOf(Luminosity));
			writer.append(',');
			writer.append(String.valueOf(w_daisy));
			writer.append(',');
			writer.append(String.valueOf(b_daisy));
			writer.append(',');
			writer.append(String.format("%.2f", global_temp));
			writer.append('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function is to calculate the global temperature, population of two
	 * kinds of daisies and print out the data for each tick. To test more
	 * conveniently, we add system print before exporting to the file. Data
	 * includes solar luminosity, white daisy population, black daisy population
	 * and global temperature.
	 */
	public void print() {
		double total_temperature = 0;
		double global_temperature = 0;
		int w_popu = 0;
		int b_popu = 0;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				total_temperature += patch[i][j].getTemperature();
				if (patch[i][j].getDaisy().getColor() == 1)
					w_popu += 1;
				else if (patch[i][j].getDaisy().getColor() == 2)
					b_popu += 1;

			}
		}
		global_temperature = total_temperature / 900;
		//System.out.println(global_temperature + "  " + w_popu + "  " + b_popu);
		export_data(solar, w_popu, b_popu, global_temperature);
	}

}
