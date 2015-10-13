/**
 * Daisy has two attributes. 
 * color could be 0, 1 or 2.
 * 0 means patch is empty.
 * 1 stands for white daisy, 2 stands for black daisy.
 */
public class Daisy {

	// define the color of a daisy
	private int color;

	// define the age of a daisy
	private int age;

	// initialize a daisy
	public Daisy(int color, int age) {
		this.color = color;
		this.age = age;

	}
	
	/**
	 * sets and gets
	 */
	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return this.color;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return this.age;
	}

}
