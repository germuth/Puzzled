package ca.germuth.puzzled.puzzle;

import java.util.Random;

import ca.germuth.puzzled.openGL.shapes.Shape;

/**
 * Represents a Tile of a puzzle. Each tile has 
 * an RGBA colour where
 * R = red
 * G = green
 * B = blue
 * A = alpha
 * 
 * Each value must be inbetween (inclusive) 0 and 255
 * 
 * For this purposes of this program, every tile object is unique, 
 * so the default object.equals() and object.hashCode() should be 
 * perfect, since they will all hash to different locations.
 * @author Germuth
 */
public class Tile {
	private int red;
	private int green;
	private int blue;
	private int alpha;
	private Shape mShape;

	public Tile(int red, int green, int blue, int alpha){
		assert(red >= 0 && red <= 255);
		assert(green >= 0 && green <= 255);
		assert(blue >= 0 && blue <= 255);
		assert(alpha >= 0 && alpha <= 255);

		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	@Override
	public String toString() {
		return "Tile: " + this.red + ", " + this.green + 
				", " + this.blue;
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


	public Shape getmShape() {
		return mShape;
	}


	public void setmShape(Shape mShape) {
		this.mShape = mShape;
	}
	
}