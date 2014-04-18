package ca.germuth.puzzled.puzzles;
/**
 * Represents an RGBA colour where
 * R = red
 * G = green
 * B = blue
 * A = alpha
 * 
 * Each value must be inbetween (inclusive) 0 and 255
 * @author Germuth
 */
public class Colour {
	private int red;
	private int green;
	private int blue;
	private int alpha;
	
	public Colour(int red, int green, int blue, int alpha){
		assert(red >= 0 && red <= 255);
		assert(green >= 0 && green <= 255);
		assert(blue >= 0 && blue <= 255);
		assert(alpha >= 0 && alpha <= 255);
		
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	
}
