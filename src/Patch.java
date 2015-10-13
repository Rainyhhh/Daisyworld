import java.util.Random;

/**
 * One patch record the change of its daisy in every tick. Each patch represents
 * one piece of ground. Each patch has its own local temperature decided by the
 * daisy and solar. Each patch may contain one daisy or 0.
 */
public class Patch {

	// Set albedo of white daisies
	private final double W_ALBEDO = 0.75;

	// Set albedo of black daisies
	private final double B_ALBEDO = 0.25;

	// Set albedo of surface
	private final double SUR_ALBEDO = 0.5;

	// Set solar luminosity
	private double solar;

	// Set max age of a daisy
	private final double MAX_AGE = 25;

	// Initialize local temperature of current patch
	private double local_temperature = 0;

	// Set empty for current patch
	private Daisy daisy;

	/**
	 * when grow a new daisy in the current patch, call this function. 
	 * This behavior usually will only be called in the first tick.
	 */
	public Patch(Daisy daisy, double solar) {
		this.daisy = daisy;
		this.solar = solar;
	}

	/**
	 * sets and gets
	 */
	public void setDaisy(Daisy daisy) {
		this.daisy = daisy;
	}

	public Daisy getDaisy() {
		return this.daisy;
	}

	public void setTemperature(double temperature) {
		this.local_temperature = temperature;
	}

	public double getTemperature() {
		return this.local_temperature;
	}
	
	public void setSolar(double solar) {
		this.solar = solar;
	}
	
	public double getSolar() {
		return this.solar;
	}

	/**
	 * this function is to calculate the local temperature in each tick.
	 * temperature is affected by the pre-temperature and local heating.
	 * local-heating is calculated as logarithmic function of solar-luminosity.
	 * if this patch is empty, then absorbed luminosity is affected by albedo of
	 * surface. if this patch has a daisy, then absorbed luminosity is affected
	 * by albedo of the daisy.
	 */
	public double cal_temperature() {
		double absorbed_luminosity = 0;
		double local_heating = 0;
		if (this.daisy.getColor() == 0) {
			// the percentage of absorbed energy is calculated (1 -
			// albedo-of-surface) and then multiplied by the
			// solar-luminosity
			absorbed_luminosity = (1 - SUR_ALBEDO) * solar;
		} else if (daisy.getColor() == 1) {
			// the percentage of absorbed energy is calculated (1 -
			// albedo)
			// and then multiplied by the solar-luminosity
			absorbed_luminosity = (1 - W_ALBEDO) * solar;
		} else if (daisy.getColor() == 2) {
			// the percentage of absorbed energy is calculated (1 -
			// albedo)
			// and then multiplied by the solar-luminosity
			absorbed_luminosity = (1 - B_ALBEDO) * solar;
		}
		if (absorbed_luminosity > 0) {
			local_heating = 72 * (Math.log(absorbed_luminosity)) + 80;
		} else
			local_heating = 80;
		// set the temperature at this patch to be the average of the
		// current
		// temperature and the local-heating effect
		local_temperature = (local_temperature + local_heating) / 2;
		return local_temperature;
	}

	/**
	 * this function is to check if this daisy can reproduce a new one and
	 * survive. in this tick first check if the daisy reaches max age, if
	 * yes, let it die. if not, calculate seed_threshhold, then decide if
	 * reproduceable
	 * 
	 * @return true or false
	 */
	public boolean reproduce() {
		double seed_threshhold = 0;
		Random r = new Random();
		this.daisy.setAge(this.daisy.getAge() + 1);
		if (daisy.getAge() < MAX_AGE) {
			// seed-threshold = ((0.1457 * temperature) - (0.0032 *
			// (temperature
			// ^ 2)) - (0.6443))
			seed_threshhold = 0.1457 * local_temperature - 0.0032 * Math.pow(local_temperature, 2) - 0.6443;
			if (r.nextDouble() < seed_threshhold) {
				return true;
			}
		}
		// set the patch empty
		else
			this.daisy = new Daisy(0, 0);
		return false;
	}
}
